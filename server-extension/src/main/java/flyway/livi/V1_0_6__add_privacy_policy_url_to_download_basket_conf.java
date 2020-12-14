package flyway.livi;

import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.JSONHelper;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Adds privacyPolicyUrl to download-basket conf
 */
public class V1_0_6__add_privacy_policy_url_to_download_basket_conf extends BaseJavaMigration {
    private static final Logger LOG = LogFactory.getLogger(V1_0_6__add_privacy_policy_url_to_download_basket_conf.class);

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        
        final ArrayList<Bundle> downloadBasketBundles = getDownloadBasketBundles(connection);
        for(Bundle bundle: downloadBasketBundles) {
            if(!modifyConfig(bundle)) {
                continue;
            }
            // update view back to db
            updateBundleInView(connection, bundle);
        }

    }

    public static Bundle updateBundleInView(Connection connection, Bundle bundle)
            throws SQLException {
        final String sql = "UPDATE oskari_appsetup_bundles SET " +
                "config=? " +
                " WHERE bundle_id=? " +
                " AND appsetup_id=?";

        try (final PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setString(1, bundle.config);
            statement.setLong(2, bundle.bundleId);
            statement.setLong(3, bundle.viewId);
            statement.execute();
        }
        return null;
    }

    private boolean modifyConfig(Bundle bundle) throws Exception {
        JSONObject config = JSONHelper.createJSONObject(bundle.config);
        if(config == null) {
            LOG.warn("Couldn't get config JSON for view:", bundle.viewId);
            return false;
        }
        config.remove("privacyPolicyUrl");
        config.put("privacyPolicyUrl", "https://vayla.fi/yhteystiedot/tietosuoja");
        bundle.config = config.toString(2);
        return true;
    }

    private ArrayList<Bundle> getDownloadBasketBundles(Connection connection) throws Exception {
        ArrayList<Bundle> ids = new ArrayList<>();
        final String sql = "SELECT appsetup_id, bundle_id, config FROM oskari_appsetup_bundles " +
                "WHERE bundle_id = (SELECT id FROM oskari_bundle WHERE name = 'download-basket')";
        try (final PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while(rs.next()) {
                Bundle b = new Bundle();
                b.viewId = rs.getLong("appsetup_id");
                b.bundleId = rs.getLong("bundle_id");
                b.config = rs.getString("config");
                ids.add(b);
            }
        }
        return ids;
    }

}


class Bundle {
    long viewId;
    long bundleId;
    String config;
}