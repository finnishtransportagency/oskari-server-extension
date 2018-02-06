package flyway.livi;

import fi.nls.oskari.domain.map.OskariLayer;
import fi.nls.oskari.domain.map.view.Bundle;
import fi.nls.oskari.domain.map.view.View;
import fi.nls.oskari.map.layer.OskariLayerService;
import fi.nls.oskari.map.layer.OskariLayerServiceIbatisImpl;
import fi.nls.oskari.map.view.ViewService;
import fi.nls.oskari.map.view.ViewServiceIbatisImpl;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import fi.nls.oskari.util.FlywayHelper;
import fi.nls.oskari.util.PropertyUtil;


import java.sql.Connection;
import java.util.List;

/**
 * Migration is skipped by default!
 *
 * Add "flyway.sample.1_0_12.skip=false" in oskari-ext.properties to add
 * download-basket for all default and user views.
 *
 */
public class V1_0_6__add_download_basket_to_views implements JdbcMigration {

    private static final String BUNDLE_ID = "download-basket";

    public void migrate(Connection connection) throws Exception {
        if (PropertyUtil.getOptional("flyway.sample.1_0_12.skip", true)) {
            return;
        }

        final List<Long> views = FlywayHelper.getUserAndDefaultViewIds(connection);
        for (Long viewId : views) {
            if (FlywayHelper.viewContainsBundle(connection, BUNDLE_ID, viewId)) {
                continue;
            }
            FlywayHelper.addBundleWithDefaults(connection, viewId, BUNDLE_ID);
        }
    }
}
