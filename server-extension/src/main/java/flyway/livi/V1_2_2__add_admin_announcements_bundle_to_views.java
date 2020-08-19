package flyway.livi;

import fi.nls.oskari.map.view.ViewService;
import fi.nls.oskari.map.view.AppSetupServiceMybatisImpl;
import fi.nls.oskari.util.FlywayHelper;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;

public class V1_2_2__add_admin_announcements_bundle_to_views implements JdbcMigration {

    private static final ViewService VIEW_SERVICE = new AppSetupServiceMybatisImpl();
    private static final  String BUNDLE = "admin-announcements";
    private static final String ROLE = "Admin";

    public void migrate(Connection connection) throws Exception {
        long viewId = VIEW_SERVICE.getDefaultViewIdForRole(ROLE);
        if(FlywayHelper.getBundleFromView(connection, BUNDLE, viewId) == null) {
            FlywayHelper.addBundleWithDefaults(connection, viewId, BUNDLE);
        }
    }
}