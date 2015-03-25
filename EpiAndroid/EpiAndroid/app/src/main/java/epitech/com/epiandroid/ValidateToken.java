package epitech.com.epiandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Billy CHEN on 01/02/2015.
 */
public class ValidateToken extends Fragment {

    public class        sendToken implements View.OnClickListener {
        View            vContext;
        String          token;
        EditText        text;

        public sendToken(View view, String t)
        {
            vContext = view;
            token = t;
        }

        @Override
        public void onClick(View v) {

            ViewGroup group = (ViewGroup) vContext.findViewById(R.id.activiteListview);
            AsyncHttpClient         req;
            RequestParams           param;

            for (int i = 0, count = group.getChildCount(); i < count; i++)
            {
                View view = group.getChildAt(i);
                for (int x = 0, countChild = ((ViewGroup)view).getChildCount(); x < countChild; x++)
                {
                    View viewChild = ((ViewGroup)view).getChildAt(x);
                    if (viewChild instanceof EditText)
                    {
                       if (((EditText) viewChild).length() > 0) {
                           String tokenLink;
                           final HashMap<String, String> tokenArgs;

                           tokenArgs = new HashMap<String, String>();
                           if (((ViewGroup) view).getChildAt(x - 1) instanceof TextView) {
                               View t = ((ViewGroup) view).getChildAt(x - 1);
                               tokenLink = ((TextView) t).getText().toString();
                               parseTokenLink(tokenArgs, tokenLink);
                               tokenArgs.put("tokenvalidationcode",((EditText) viewChild).getText().toString());
                               param = new RequestParams();

                               param.add("token", token);
                               param.add("scolaryear", tokenArgs.get("scolaryear"));
                               param.add("codemodule", tokenArgs.get("codemodule"));
                               param.add("codeinstance", tokenArgs.get("codeinstance"));
                               param.add("codeacti", tokenArgs.get("codeacti"));
                               param.add("codeevent", tokenArgs.get("codeevent"));
                               param.add("tokenvalidationcode", tokenArgs.get("tokenvalidationcode"));

                               req = new AsyncHttpClient();
                               req.post("https://epitech-api.herokuapp.com/token", param, new JsonHttpResponseHandler() {

                                   @Override
                                   public void onSuccess(int statusCode, Header[] header, JSONObject response) {
                                       try {
                                           if (!response.has("error")) {
                                               Toast.makeText(getActivity(), "Token validate", Toast.LENGTH_SHORT).show();

                                           } else {
                                               Toast.makeText(getActivity(), "token: " + tokenArgs.get("codemodule") + " : " + response.getString("error"), Toast.LENGTH_SHORT).show();
                                           }
                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                   }

                                   @Override
                                   public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject response) {
                                       try {
                                           Toast.makeText(getActivity(), "An error occured", Toast.LENGTH_SHORT).show();
                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                   }

                               });
                           }
                       }
                    }
                }
            }
        }
    }

    public void parseTokenLink(HashMap<String, String> element, String tokenLink)
    {
        List<String> list;

        list = Arrays.asList(tokenLink.split("/"));
        element.put("scolaryear", list.get(2));
        element.put("codemodule", list.get(3));
        element.put("codeinstance", list.get(4));
        element.put("codeacti", list.get(5));
        element.put("codeevent", list.get(6));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.validate_token, container, false);
        AsyncHttpClient     req;
        final Bundle        saved;
        RequestParams       param;

        req = new AsyncHttpClient();
        saved = getArguments();
        param = new RequestParams();

        param.put("token", saved.getString("token"));
        req.post("https://epitech-api.herokuapp.com/infos", param, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject response)
            {
                String                  link;
                JSONArray               activite;
                JSONObject              activiteObject;
                HashMap<String, String> element;
                List<HashMap<String, String>>   listActivites;
                SimpleAdapter                   adapter;
                ListView                        tokenListView;
                Button                          buttonValidation;

                try
                {
                    listActivites = new ArrayList<HashMap<String, String>>();
                    activite = response.getJSONObject("board").getJSONArray("activites");
                    for (int i = 0; i < activite.length(); i++)
                    {
                        activiteObject = activite.optJSONObject(i);

                        if (!activiteObject.getString("token").equals("null"))
                        {
                            element = new HashMap<String, String>();
                            element.put("title", activiteObject.getString("title"));
                            element.put("tokenLink", activiteObject.getString("token_link"));
                            listActivites.add(element);
                        }
                    }
                    if (listActivites.size() == 0)
                    {
                        TextView noToken;

                        noToken = (TextView) view.findViewById(R.id.noToken);
                        noToken.setText("Pas de token à saisir");
                    }
                    adapter = new SimpleAdapter(getActivity(), listActivites, R.layout.listview_token, new String[] {"title", "tokenLink"},
                                                new int[] {R.id.titleToken, R.id.tokenLink});
                    tokenListView = (ListView) view.findViewById(R.id.activiteListview);
                    tokenListView.setAdapter(adapter);
                    buttonValidation = (Button) view.findViewById(R.id.validateToken);
                    buttonValidation.setOnClickListener(new sendToken(view, saved.getString("token")));
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
