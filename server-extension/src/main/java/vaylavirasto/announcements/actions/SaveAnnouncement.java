package vaylavirasto.announcements.actions;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.ResponseHelper;
import fi.nls.oskari.control.RestActionHandler;
import vaylavirasto.announcements.helpers.AnnouncementsDBHelper;
import org.json.JSONException;
import org.json.JSONObject;

@OskariActionRoute("SaveAnnouncement")
public class SaveAnnouncement extends RestActionHandler{
    private static Logger LOG = LogFactory.getLogger(SaveAnnouncement.class);

    @Override
    public void handlePost(ActionParameters params) throws ActionException {

        try {
            JSONObject result = AnnouncementsDBHelper.saveAnnouncement(params);
            ResponseHelper.writeResponse(params, result);
        } catch (JSONException e) {
            LOG.error("Error saving announcement", e);
            throw new ActionException("Cannot save announcement");
        }
    }
}
