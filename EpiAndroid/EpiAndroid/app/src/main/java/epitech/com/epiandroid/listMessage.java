package epitech.com.epiandroid;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Billy CHEN on 27/01/2015.
 */
final public class listMessage{

    public static HashMap<String, String> parseJSONObject(JSONObject object)
    {
        HashMap<String, String>     element;

        element = new HashMap<String, String>();

        try
        {
            element.put("title", object.getString("title"));
            element.put("content", object.getString("content"));
            element.put("date", object.getString("date"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (element);
    }

}
