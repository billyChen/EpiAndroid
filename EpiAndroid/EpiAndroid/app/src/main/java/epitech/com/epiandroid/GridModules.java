package epitech.com.epiandroid;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Billy CHEN on 29/01/2015.
 */
final public class GridModules {
    public static HashMap<String, String> getTitle(JSONObject object)
    {
        HashMap<String, String>     element;

        element = new HashMap<String, String>();

        try
        {
            element.put("main", object.getString("title"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return (element);

    }

    public static HashMap<String, String> getGrade(JSONObject object)
    {
        HashMap<String, String>     element;

        element = new HashMap<String, String>();

        try
        {
            element.put("main", object.getString("grade"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (element);

    }
    public static HashMap<String, String> getCredits(JSONObject object)
    {
        HashMap<String, String>     element;

        element = new HashMap<String, String>();

        try
        {
            element.put("main", object.getString("credits"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (element);

    }
}
