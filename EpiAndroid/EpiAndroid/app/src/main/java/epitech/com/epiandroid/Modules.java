package epitech.com.epiandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Billy CHEN on 28/01/2015.
 */
public class Modules extends Fragment
{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.modules, container, false);
        AsyncHttpClient req;
        String          token;
        Bundle          saved;

        saved = getArguments();
        token = saved.getString("token");
        req = new AsyncHttpClient();
        req.get("https://epitech-api.herokuapp.com/modules?token=" + token, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                final LinearLayout              linlaHeaderProgress;
                GridView                        grid;
                HashMap<String, String>         element;
                List<HashMap<String, String>>   list;
                JSONArray                       modules;
                ListAdapter                     adapter;

                try
                {
                    linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                    modules = response.getJSONArray("modules");
                    grid = (GridView)view.findViewById(R.id.mainGrid);
                    list = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i < modules.length(); i++)
                    {
                        element = new HashMap<String, String>();
                        if (modules.optJSONObject(i) != null)
                        {
                            element = GridModules.getTitle(modules.optJSONObject(i));
                            list.add(element);
                            element = GridModules.getGrade(modules.optJSONObject(i));
                            list.add(element);
                            element = GridModules.getCredits(modules.optJSONObject(i));
                            list.add(element);
                        }
                    }
                    adapter = new SimpleAdapter(getActivity(), list,R.layout.gridview_layout,
                                                                    new String[] {"main"},
                                                                    new int[] { R.id.title}
                    );
                    grid.setAdapter(adapter);
                    linlaHeaderProgress.setVisibility(View.GONE);
                }
                catch (Exception e) {
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
