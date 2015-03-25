package epitech.com.epiandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Billy CHEN on 29/01/2015.
 */
public class Planning extends Fragment
{
    LinearLayout                  linlaHeaderProgress;

    public class previous implements View.OnClickListener {

       View         contextView;
       Bundle       bundle;
       Calendar     date;

       public previous(View v, Bundle b, Calendar currentDate)
       {
           contextView = v;
           bundle = b;
           date = currentDate;
       }
        @Override
        public void onClick(View v)
        {
            List<String> listDay;

            try
            {
                listDay = new ArrayList<String>();
                linlaHeaderProgress.setVisibility(View.VISIBLE);
                getDateBoundary(-13, date, listDay);
                displayPlanning(bundle, contextView, listDay);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    public class next implements View.OnClickListener {

        View         contextView;
        Bundle       bundle;
        Calendar     date;

        public next(View v, Bundle b, Calendar currentDate)
        {
            contextView = v;
            bundle = b;
            date = currentDate;
        }
        @Override
        public void onClick(View v)
        {
            List<String> listDay;

            try
            {
                listDay = new ArrayList<String>();
                linlaHeaderProgress.setVisibility(View.VISIBLE);
                getDateBoundary(1, date, listDay);
                displayPlanning(bundle, contextView, listDay);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    public void displayPlanning(Bundle bundle, final View view, List<String> listDay)
    {
        AsyncHttpClient req;

        req = new AsyncHttpClient();

        req.get("https://epitech-api.herokuapp.com/planning?token=" + bundle.get("token") + "&start=" + listDay.get(0) + "&end=" + listDay.get(1)
                , new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONArray response)
            {
                List<HashMap<String, String>>       listActivities;
                HashMap<String, String>             element;
                JSONObject                          obj;
                SimpleAdapter                         adapter;
                ListView                            planning;
                LinearLayout                        noActivity;

                try
                {
                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                    listActivities = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i < response.length(); i++)
                    {
                        obj = response.getJSONObject(i);
                        if (ListPlanning.isRegistered(obj) != false)
                        {
                            element = new HashMap<String, String>();
                            element = ListPlanning.getStudentActivities(obj);
                            listActivities.add(element);
                        }
                    }
                    adapter = new SimpleAdapter(getActivity(), listActivities, R.layout.listview_planning,
                            new String[] {"acti_title", "titlemodule", "start",
                                    "end", "room"},
                            new int[] {R.id.activityName,
                                    R.id.moduleName,
                                    R.id.startActivity,
                                    R.id.endActivity,
                                    R.id.roomActivity});
                    planning = (ListView) view.findViewById(R.id.planningView);
                    planning.setAdapter(adapter);
                    noActivity = (LinearLayout) view.findViewById(R.id.noActivityLayout);
                    if (listActivities.size() == 0)
                    {
                        noActivity.setVisibility(view.VISIBLE);
                    }
                    else
                    {
                        noActivity.setVisibility(view.GONE);
                    }
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
    }

    public void getDateBoundary(int addDay, Calendar date, List<String> listDay)
    {
        String dateString;

        listDay.clear();
        date.add(Calendar.DATE, addDay);
        for (int i = 0; i < 2; i++) {
            dateString = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
            listDay.add(dateString);
            if (i != 1)
                date.add(Calendar.DATE, 6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.planning, container, false);
        Calendar date = Calendar.getInstance();
        List<String>            listDay;
        Date                    format;
        String                  dateString;
        AsyncHttpClient         req;
        Bundle                  bundle;
        Button                  previousButton;
        Button                  nextButton;

        listDay = new ArrayList<String>();
        getDateBoundary(0, date, listDay);
        bundle = getArguments();
        displayPlanning(bundle, view, listDay);
        previousButton = (Button) view.findViewById(R.id.previousWeek);
        nextButton = (Button) view.findViewById(R.id.nextWeek);
        linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
        previousButton.setOnClickListener(new previous(view, bundle, date));
        nextButton.setOnClickListener(new next(view, bundle, date));
        return (view);
    }
}
