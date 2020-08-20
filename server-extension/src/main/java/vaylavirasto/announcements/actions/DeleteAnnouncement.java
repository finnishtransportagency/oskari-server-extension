package vaylavirasto.announcements.actions;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.ResponseHelper;
import vaylavirasto.announcements.helpers.AnnouncementsDBHelper;
import org.json.JSONException;
import org.json.JSONObject;

@OskariActionRoute("DeleteAnnouncement")
public class DeleteAnnouncement extends AnnouncementsRestActionHandler {
    private static Logger LOG = LogFactory.getLogger(DeleteAnnouncement.class);

    @Override
    public void handlePost(ActionParameters params) throws ActionException {
        requireLiviConfigured();

        try {
            System.out.println("==========================");
        System.out.println("==============DELETEANN============");
        System.out.println("==========================");
            JSONObject result = AnnouncementsDBHelper.deleteAnnouncement(params);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(result);
            ResponseHelper.writeResponse(params, result);
        } catch (JSONException e) {
            LOG.error("Error with deleting announcement", e);
            throw new ActionException("Cannot delete announcement");
        }
    }
}
