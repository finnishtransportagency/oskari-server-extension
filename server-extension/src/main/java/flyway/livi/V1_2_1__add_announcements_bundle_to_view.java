package flyway.announcements;

import fi.nls.oskari.domain.map.view.View;
import fi.nls.oskari.map.view.ViewService;
import fi.nls.oskari.map.view.AppSetupServiceMybatisImpl;
import org.oskari.helpers.AppSetupHelper;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.util.List;

public class V1_2_1__add_announcements_bundle_to_view extends BaseJavaMigration {
	
	private static final ViewService VIEW_SERVICE = new AppSetupServiceMybatisImpl();
    private static final  String BUNDLE_ID = "announcements";

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        List<View> views = VIEW_SERVICE.getViewsForUser(-1);
        for (View v : views) {
            if (v.isDefault()) {
                if (AppSetupHelper.appContainsBundle(connection, v.getId(), BUNDLE_ID)) {
                    continue;
                }
                AppSetupHelper.addBundleToApp(connection, v.getId(), BUNDLE_ID);
            }
        }
    }
}
