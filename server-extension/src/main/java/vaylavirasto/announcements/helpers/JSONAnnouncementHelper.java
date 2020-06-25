package vaylavirasto.announcements.helpers;

import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import vaylavirasto.announcements.Announcement;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.JSONHelper;

public class JSONAnnouncementHelper {

	private static final Logger log = LogFactory
			.getLogger(JSONAnnouncementHelper.class);

	public static final JSONObject createAnnouncementsJSONOutput(
			List<Announcement> announcements) {
		JSONArray outputArray = new JSONArray();

		for (Announcement a : announcements) {
			JSONObject ob = new JSONObject();
			JSONHelper.putValue(ob, "id", a.getId());
			JSONHelper.putValue(ob, "title", a.getTitle());
			JSONHelper.putValue(ob, "content", a.getContent());
			JSONHelper.putValue(ob, "beginDate", new SimpleDateFormat(
					"yyyy-MM-dd").format(a.getBeginDate()));
			JSONHelper.putValue(ob, "endDate", new SimpleDateFormat(
				"yyyy-MM-dd").format(a.getEndDate()));
			JSONHelper.putValue(ob, "active", a.getActive());

			outputArray.put(ob);
		}

		JSONObject outputObject = new JSONObject();
		JSONHelper.putValue(outputObject, "announcements", outputArray);

		return outputObject;
	}
}
