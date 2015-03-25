package epitech.com.epiandroid;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Billy CHEN on 25/01/2015.
 */
public class Accueil extends Fragment
{
    AsyncHttpClient req;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View                                  view = inflater.inflate(R.layout.accueil, container, false);
        RequestParams                               param;
        final Bundle                                saved;

        saved = getArguments();
        param = new RequestParams();
        req = new AsyncHttpClient();
        param.put("token", saved.getString("token"));
        req.post("https://epitech-api.herokuapp.com/infos", param, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject response)
            {
                try
                {
                    final LinearLayout              linlaHeaderProgress;
                    AsyncHttpClient                 imgReq;
                    RequestParams                   imgParams;
                    String                          logtime;
                    String                          userFullname;
                    String                          userSemester;
                    TextView                        logtimeView;
                    TextView                        userFullnameView;
                    TextView                        userSemesterView;

                    ListView                        historyList = null;
                    JSONArray                       history;
                    List<HashMap<String, String>>   list;
                    HashMap<String, String>         element;
                    ListAdapter                     adapter;

                    linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                    list = new ArrayList<HashMap<String, String>>();
                    imgReq = new AsyncHttpClient();
                    logtime = response.getJSONObject("current").getString("active_log");
                    userFullname = response.getJSONObject("infos").getString("title");
                    userSemester = response.getJSONObject("infos").getString("semester");
                    logtimeView = (TextView) view.findViewById(R.id.logtime);
                    userFullnameView = (TextView) view.findViewById(R.id.userFullname);
                    userSemesterView = (TextView) view.findViewById(R.id.userSemester);
                    logtimeView.setText(logtime);
                    userFullnameView.setText(userFullname);
                    userSemesterView.setText("Semestre " + userSemester);
                    imgReq.get("https://epitech-api.herokuapp.com/photo?token=" + saved.getString("token") + "&login=" + saved.getString("login"), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] header, JSONObject response) {
                            try
                            {
                                ImageLoader img;
                                ImageView profil_picture;
                                String urlImg = response.getString("url");

                                img = ImageLoader.getInstance();
                                profil_picture = (ImageView) view.findViewById(R.id.profilPicture);
                                img.displayImage(urlImg, profil_picture);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                    historyList = (ListView) view.findViewById(R.id.history_list);
                    history = response.getJSONArray("history");
                    for (int i = 0; i < history.length(); i++)
                    {
                        element = new HashMap<String, String>();
                        if (history.optJSONObject(i) != null)
                        {
                            element = listMessage.parseJSONObject(history.optJSONObject(i));
                            list.add(element);
                        }
                    }
                    adapter = new SimpleAdapter(getActivity(), list, android.R.layout.simple_list_item_2, new String[]{"title", "content", "date"}, new int[] {android.R.id.text1, android.R.id.text2, android.R.id.text2});
                    historyList.setAdapter(adapter);
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
