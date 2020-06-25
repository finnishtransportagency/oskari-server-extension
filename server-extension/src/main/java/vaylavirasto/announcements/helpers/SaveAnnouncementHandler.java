package vaylavirasto.announcements.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import fi.nls.oskari.annotation.OskariActionRoute;
import vaylavirasto.announcements.service.AnnouncementsDbService;
import vaylavirasto.announcements.service.AnnouncementsDbServiceIbatisImpl;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionHandler;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.domain.User;
import vaylavirasto.announcements.Announcement;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.service.ServiceException;
import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.util.ResponseHelper;

@OskariActionRoute("SaveAnnouncement")
public class SaveAnnouncementHandler extends ActionHandler{
	
	private static final Logger log = LogFactory
			.getLogger(SaveAnnouncementHandler.class);
	private static final AnnouncementsDbService announcementService = new AnnouncementsDbServiceIbatisImpl();
	
	private static final String ANNOUNCEMENT_ID = "id";
	private static final String ANNOUNCEMENT_TITLE = "title";
	private static final String ANNOUNCEMENT_CONTENT = "content";
	private static final String ANNOUNCEMENT_BEGIN_DATE = "beginDate";
	private static final String ANNOUNCEMENT_END_DATE = "endDate";
	private static final String ANNOUNCEMENT_ACTIVE = "active";
	
	@Override
	public void handleAction(ActionParameters params) throws ActionException {
		User user = params.getUser();
		String content;
		Long insertId = null;
		JSONObject response = new JSONObject();
		
		if (!user.isGuest()) {
			
			String idParam = params.getHttpParam(ANNOUNCEMENT_ID);
			String titleParam = params.getHttpParam(ANNOUNCEMENT_TITLE);
			String contentParam = params.getHttpParam(ANNOUNCEMENT_CONTENT);
			String beginDateParam = params.getHttpParam(ANNOUNCEMENT_BEGIN_DATE);
			String endDateParam = params.getHttpParam(ANNOUNCEMENT_END_DATE);
			String activeParam = params.getHttpParam(ANNOUNCEMENT_ACTIVE);

			long id = 0;
			if (idParam != null && !idParam.isEmpty()) {
				id = Long.parseLong(idParam);
			}

			Boolean active = false;
			if (activeParam != null && !activeParam.isEmpty()) {
				active = Boolean.parseBoolean(activeParam);
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date dtBeginDate;
			Date dtEndDate;
			try {
				dtBeginDate = formatter.parse(beginDateParam);
				dtEndDate = formatter.parse(endDateParam);
			} catch (ParseException e1) {
				throw new ActionException("Error during date parsing");
			}
			
			Announcement a = new Announcement();
			a.setId(id);
			a.setTitle(titleParam);
			a.setContent(contentParam);
			a.setBeginDate(dtBeginDate);
			a.setEndDate(dtEndDate);
			a.setActive(active);
			
			if (a.getId() == 0) {
				try {
					insertId = announcementService.insertAnnouncement(a);
					response.put("success", "Announcement has been created: id = " + insertId);			
					response.put("id", insertId);
				} catch (JSONException e) {
					throw new ActionException("Error during generating response", e);
				} catch (ServiceException e) {
					throw new ActionException("Error during saving the announcement to database", e);
				}

			} else {
				try {
					//delete old announcement and add new one (with new id)
					//it's because the announcement should be shown after edition, even if user selects option to not show this message anymore  
					announcementService.deleteAnnouncement(a.getId());
					announcementService.insertAnnouncement(a);
					response.put("success", "Announcement has been updated");
				} catch (JSONException e) {
					throw new ActionException("Error during generating response", e);	
				} catch (ServiceException e) {
					throw new ActionException("Error during saving the announcement to database", e);
				}

			}

			
		} else {
			try {
				response.put("error", "There is no logged user.");
			} catch (JSONException e) {
				throw new ActionException("Error during generating response", e);
			}
		}
		
		ResponseHelper.writeResponse(params, response);
	}

}