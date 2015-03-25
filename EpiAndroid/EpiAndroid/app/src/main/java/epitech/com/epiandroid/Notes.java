package epitech.com.epiandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
public class Notes extends Fragment {

            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
            {
                final View view = inflater.inflate(R.layout.note, container, false);
                AsyncHttpClient         req;
                AsyncHttpClient         reqInfos;
                Bundle                  save;

                save = getArguments();

                req = new AsyncHttpClient();
                req.get("https://epitech-api.herokuapp.com/marks?token=" + save.getString("token"), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] header, JSONObject response)
                        {
                            List<HashMap<String, String>>           listMarks;
                            JSONObject                              obj;
                            HashMap<String, String>                 element;
                            SimpleAdapter                           adapter;
                            ListView                                marks;
                            JSONArray                               marksArray;
                            final LinearLayout linlaHeaderProgress;

                            linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);

                            try
                            {
                                marksArray = response.getJSONArray("notes");
                                linlaHeaderProgress.setVisibility(View.VISIBLE);

                                listMarks = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < marksArray.length(); i++)
                                {
                                    try
                                    {
                                        obj = marksArray.getJSONObject(i);
                                        element = new HashMap<String, String>();

                                        element.put("title", obj.getString("title"));
                                        element.put("titleModule", obj.getString("titlemodule"));
                                        element.put("note", obj.getString("final_note"));
                                        element.put("date", obj.getString("date"));
                                        listMarks.add(element);

                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter = new SimpleAdapter(getActivity(), listMarks, R.layout.listview_marks,
                                                        new String[] {"title", "titleModule", "note", "date"},
                                                        new int[] { R.id.titleMarks,
                                                                    R.id.titleModuleMarks,
                                                                    R.id.noteMarks,
                                                                    R.id.dateMarks});
                            marks = (ListView) view.findViewById(R.id.marksListView);
                            marks.setAdapter(adapter);
                                linlaHeaderProgress.setVisibility(View.GONE);

                            } catch (JSONException e) {
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
                                e.printStackTrace();
                            }
                        }
                });

                return (view);
            }


}
