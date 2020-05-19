package flyway.livi;

import fi.nls.oskari.domain.map.view.View;
import fi.nls.oskari.map.view.ViewService;
import fi.nls.oskari.map.view.AppSetupServiceMybatisImpl;
import fi.nls.oskari.util.FlywayHelper;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.util.List;

public class V1_2_1__add_announcements_bundle_to_view implements JdbcMigration {
	
	private static final ViewService VIEW_SERVICE = new AppSetupServiceMybatisImpl();
    private static final  String BUNDLE_ID = "announcements";

    public void migrate(Connection connection) throws Exception {
        List<View> views = VIEW_SERVICE.getViewsForUser(-1);
        for (View v : views) {
            if (v.isDefault()) {
                if (FlywayHelper.viewContainsBundle(connection, BUNDLE_ID, v.getId())) {
                    continue;
                }
                FlywayHelper.addBundleWithDefaults(connection, v.getId(), BUNDLE_ID);
            }
        }
    }
}
