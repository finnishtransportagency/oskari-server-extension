package flyway.livi;

import org.oskari.helpers.AppSetupHelper;
import fi.nls.oskari.util.PropertyUtil;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.util.List;

/**
 * Migration is skipped by default!
 *
 * Add "flyway.sample.1_0_12.skip=false" in oskari-ext.properties to add
 * download-basket for all default and user views.
 *
 */
public class V1_0_4__add_download_basket_to_views extends BaseJavaMigration {

    private static final String BUNDLE_ID = "download-basket";

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        if (PropertyUtil.getOptional("flyway.livi.1_0_4.skip", true)) {
            return;
        }

        final List<Long> views = AppSetupHelper.getSetupsForUserAndDefaultType(connection);
        for (Long viewId : views) {
            if (AppSetupHelper.appContainsBundle(connection, viewId, BUNDLE_ID)) {
                continue;
            }
            AppSetupHelper.addBundleToApp(connection, viewId, BUNDLE_ID);
        }
    }
}
