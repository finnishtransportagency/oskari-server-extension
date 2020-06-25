package vaylavirasto.announcements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import fi.nls.oskari.annotation.OskariActionRoute;
import vaylavirasto.announcements.service.AnnouncementsDbService;
import vaylavirasto.announcements.service.AnnouncementsDbServiceIbatisImpl;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionHandler;
import fi.nls.oskari.control.ActionParameters;
import vaylavirasto.announcements.Announcement;
import vaylavirasto.announcements.helpers.JSONAnnouncementHelper;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.ResponseHelper;

@OskariActionRoute("GetAnnouncements")
public class GetAnnouncementsHandler extends ActionHandler {

	private static final Logger log = LogFactory
			.getLogger(GetAnnouncementsHandler.class);
	private static final AnnouncementsDbService announcementsService = new AnnouncementsDbServiceIbatisImpl();

	@Override
	public void handleAction(ActionParameters params) throws ActionException {
		List<Announcement> announcements;
		Date beginDate;
		Date endDate;

		try {

			SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
			beginDate = sdfBegin.parse(sdfBegin.format(new Date()));

			SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
			endDate = sdfEnd.parse(sdfEnd.format(new Date()));
			
		} catch (ParseException e1) {
			throw new ActionException("Error during date parsing");
		}

		try {
			announcements = announcementsService
					.getAnnouncements(beginDate, endDate);
		} catch (Exception e) {
			throw new ActionException(
					"Error during selecting required data from database");
		}

		try {
			JSONObject main = JSONAnnouncementHelper.createAnnouncementsJSONOutput(announcements);
			ResponseHelper.writeResponse(params, main);
		} catch (Exception e) {
			throw new ActionException(
					"Error during creating JSON announcements object");
			}
	}

}
