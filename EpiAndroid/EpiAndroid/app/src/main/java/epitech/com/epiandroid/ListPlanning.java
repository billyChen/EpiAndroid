package epitech.com.epiandroid;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Billy CHEN on 30/01/2015.
 */
final public class ListPlanning
{
    public static boolean isRegistered(JSONObject response)
    {
        try
        {
            if (response.getString("event_registered").equals("registered"))
                return true;
            else
                return false;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static HashMap<String, String> getStudentActivities(JSONObject response)
    {
        HashMap<String, String>     element;

        element = new HashMap<String, String>();

        try
        {
            element.put("start", response.getString("start"));
            element.put("end", response.getString("end"));
            if (!response.has("calendar_type")) {
                element.put("acti_title", response.getString("acti_title"));
                element.put("titlemodule", response.getString("titlemodule"));
                element.put("room", response.getJSONObject("room").getString("code"));
            }
            else
            {
                element.put("acti_title", response.getString("title"));
                element.put("room", response.getJSONObject("maker").getString("title"));
                element.put("titlemodule", "Susie");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return element;
    }
}
