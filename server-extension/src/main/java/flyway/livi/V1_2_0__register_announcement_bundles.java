package flyway.livi;

import java.sql.Connection;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import fi.nls.oskari.db.BundleHelper;
import fi.nls.oskari.domain.map.view.Bundle;

public class V1_2_0__register_announcement_bundles implements JdbcMigration {

	@Override
	public void migrate(Connection connection) throws Exception {
		
		// BundleHelper checks if these bundles are already registered
        Bundle bundle = new Bundle();
        bundle.setName("announcements");
        BundleHelper.registerBundle(bundle, connection);

        Bundle admin = new Bundle();
        admin.setName("admin-announcements");
        BundleHelper.registerBundle(admin, connection);
	}
}
