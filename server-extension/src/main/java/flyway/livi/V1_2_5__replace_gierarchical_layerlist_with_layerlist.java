package flyway.livi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oskari.helpers.BundleHelper;
import fi.nls.oskari.domain.map.view.Bundle;
import fi.nls.oskari.domain.map.view.View;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import org.oskari.helpers.AppSetupHelper;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import fi.nls.oskari.map.view.ViewService;
import fi.nls.oskari.map.view.AppSetupServiceMybatisImpl;


public class V1_2_5__replace_gierarchical_layerlist_with_layerlist extends BaseJavaMigration {
    private static final Logger LOG = LogFactory.getLogger(V1_2_5__replace_gierarchical_layerlist_with_layerlist.class);

    private static final String BUNDLE_HIERARCHICAL_LAYERLIST = "hierarchical-layerlist";
    private static final String BUNDLE_LAYERLIST = "layerlist";

    private int updatedViewCount = 0;
    private ViewService service = null;

    public void migrate(Context context) throws Exception {
        
        Connection connection = context.getConnection();
        service =  new AppSetupServiceMybatisImpl();
        try {
            updateViews(connection);
        }
        finally {
            LOG.info("Updated views:", updatedViewCount);
            service = null;
        }
    }

    private void updateViews(Connection conn)
            throws Exception {
        List<View> list = getOutdatedViews(conn);
        LOG.info("Got", list.size(), "outdated views");
        for(View view : list) {
            addHierarchicalLayerListBundle(conn, view.getId());
            updatedViewCount++;
        }
    }

    private List<View> getOutdatedViews(Connection conn) throws SQLException {

        List<View> list = new ArrayList<>();
        final String sql = "SELECT id FROM oskari_appsetup " +
                "WHERE (type = 'USER' OR type = 'DEFAULT') AND " +
                "id IN (" +
                "SELECT distinct appsetup_id FROM oskari_appsetup_bundles WHERE bundle_id IN (" +
                "SELECT id FROM oskari_bundle WHERE name='hierarchical-layerlist'" +
                "));";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    View view = new View();
                    view.setId(rs.getLong("id"));
                    list.add(view);
                }
            }
        }
        return list;
    }


    public void addHierarchicalLayerListBundle(Connection conn, final long viewId) throws SQLException {
        Bundle layerselectorBundle = BundleHelper.getRegisteredBundle(conn, BUNDLE_HIERARCHICAL_LAYERLIST);
        if( layerselectorBundle == null) {
            // not even registered so migration not needed
            return;
        }
        Bundle newBundle = BundleHelper.getRegisteredBundle(conn, BUNDLE_LAYERLIST);
        if(newBundle == null) {
            throw new RuntimeException("Bundle not registered: " + BUNDLE_LAYERLIST);
        }

        // update hierarchical-layerlist bundle to layerlist
        replaceLayerselectorBundleToHierarchicalLayerlist(conn, viewId, layerselectorBundle, newBundle);

    }

    public void replaceLayerselectorBundleToHierarchicalLayerlist(Connection conn, final long viewId, final Bundle oldBundle, final Bundle newBundle) throws SQLException {
        final String sql = "UPDATE oskari_appsetup_bundles " +
                "SET " +
                "    bundle_id=?, " +
                "    bundleinstance=?" +
                "WHERE bundle_id = ? and appsetup_id=?";
        
        try (PreparedStatement statement =
                     conn.prepareStatement(sql)){
            statement.setLong(1, newBundle.getBundleId());
            statement.setString(2, newBundle.getName());
            statement.setLong(3, oldBundle.getBundleId());
            statement.setLong(4, viewId);
            statement.execute();
        }
    }
}
