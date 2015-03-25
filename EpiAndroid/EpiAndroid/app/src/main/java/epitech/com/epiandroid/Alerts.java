package epitech.com.epiandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Billy CHEN on 01/02/2015.
 */
public class Alerts extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.alerts, container, false);
        AsyncHttpClient             req;
        Bundle                      save;

        save = getArguments();
        req = new AsyncHttpClient();
        req.get("https://epitech-api.herokuapp.com/alerts?token=" + save.getString("token"), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] header, JSONArray response)
            {
                List<HashMap<String, String>>           list;
                HashMap<String, String>                 element;
                JSONObject                              obj;
                SimpleAdapter                           adapter;
                ListView                                alerts;
                try
                {
                    list = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i < response.length(); i++)
                    {
                        obj = response.optJSONObject(i);
                        element = new HashMap<String, String>();

                        element.put("title", obj.getString("title"));
                        list.add(element);
                    }
                    alerts = (ListView) view.findViewById(R.id.alertsListView);
                    adapter = new SimpleAdapter(getActivity(), list, R.layout.listview_alerts, new String [] {"title"}, new int [] {R.id.titleAlerts});
                    alerts.setAdapter(adapter);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject response) {
                try
                {
                    Toast.makeText(getActivity(), "An error occured", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                }
            }
        });


        return (view);
    }


}
