package vaylavirasto.announcements.helpers;

import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.control.ActionParamsException;
import vaylavirasto.announcements.domain.AnnouncementParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Search helper
 */
public class AnnouncementsParser {
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_BEGIN_DATE = "begin_date";
    public static final String KEY_END_DATE = "end_date";
    public static final String KEY_ACTIVE = "active";

    /**
     * Parse search params from json
     * @param params
     * @return
     * @throws JSONException
     * @throws ActionParamsException
     */
    public static List<AnnouncementParams> parseAnnouncement (ActionParameters params) throws JSONException, ActionParamsException {
        JSONObject jsonParams =  params.getHttpParamAsJSON("params");

        List<AnnouncementParams> AnnouncementParams = new ArrayList<>();

        if (jsonParams.has(KEY_ID)) {
            AnnouncementParams id = new AnnouncementParams(KEY_ID, null, jsonParams.getInt(KEY_ID));
            id.setNeedCastVarchar(true);
            AnnouncementParams.add(id);
        }

        if (jsonParams.has(KEY_TITLE)) {
            AnnouncementParams title = new AnnouncementParams(KEY_TITLE, null, jsonParams.getString(KEY_TITLE));
            title.setNeedCastVarchar(true);
            AnnouncementParams.add(title);
        }

        if (jsonParams.has(KEY_CONTENT)) {
            AnnouncementParams content = new AnnouncementParams(KEY_CONTENT, null, jsonParams.getString(KEY_CONTENT));
            content.setNeedCastVarchar(true);
            AnnouncementParams.add(content);
        }
        
        if (jsonParams.has(KEY_BEGIN_DATE)) {
            AnnouncementParams beginDate = new AnnouncementParams(KEY_BEGIN_DATE, null, jsonParams.getString(KEY_BEGIN_DATE));
            beginDate.setNeedCastVarchar(true);
            AnnouncementParams.add(beginDate);
        }
        
        if (jsonParams.has(KEY_END_DATE)) {
            AnnouncementParams endDate = new AnnouncementParams(KEY_END_DATE, null, jsonParams.getString(KEY_END_DATE));
            endDate.setNeedCastVarchar(true);
            AnnouncementParams.add(endDate);
        }

        if (jsonParams.has(KEY_ACTIVE)) {
            AnnouncementParams active = new AnnouncementParams(KEY_ACTIVE, null, jsonParams.getString(KEY_ACTIVE));
            active.setNeedCastVarchar(true);
            AnnouncementParams.add(active);
        }

        return AnnouncementParams;

    }

    /**
     * Genereates search where clause
     * @param searchParams
     * @return
     */

     /*
    public static String getSearchWhere(final List<AnnouncementParams> searchParams){
        StringBuilder sb = new StringBuilder();
        if(searchParams.size() == 0) {
            return sb.toString();
        }

        sb.append("WHERE ");
        for (AnnouncementParams searchParam : searchParams) {
            if (searchParam.getValue() instanceof Integer) {
                sb.append(searchParam.getId() + "=? AND ");
            } else if (searchParam.getValue() instanceof Long) {
                sb.append(searchParam.getId() + "=? AND ");
            } else if (searchParam.getValue() instanceof String) {
                if(searchParam.isNeedCastVarchar()) {
                    sb.append(searchParam.getId() + "::VARCHAR =? AND ");
                } else {
                    sb.append(searchParam.getId() + "=? AND ");
                }
            }
        }
        String where = sb.toString();
        where = where.substring(0, where.length()-4);

        return where;
    }
    */
}