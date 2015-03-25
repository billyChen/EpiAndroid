package epitech.com.epiandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Billy CHEN on 24/01/2015.
 */
public class ConnexionPage extends Fragment
{

    private Button connect_button;
    private EditText editLogin;
    private EditText editPassword;
    private AsyncHttpClient req;
    private String token;
    private FragmentActivity myContext;
    private dataPasser     mCallbacks;

    public interface dataPasser
    {
        public void dataPass(HashMap<String, String> data);
    }

    @Override
    public void onAttach(Activity activity)
    {
        myContext = (FragmentActivity) activity;
        mCallbacks = (dataPasser) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
         final View view = inflater.inflate(R.layout.connexion,
                container, false);

        connect_button = (Button) view.findViewById(R.id.connexion);
        connect_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                final LinearLayout            linlaHeaderProgress;

                linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);

                linlaHeaderProgress.setVisibility(View.VISIBLE);
                editLogin = (EditText) getView().findViewById(R.id.editLogin);
                editPassword = (EditText) getView().findViewById(R.id.editPassword);
                RequestParams params = new RequestParams();

                params.put("login", editLogin.getText());
                params.put("password", editPassword.getText());
                req = new AsyncHttpClient();
                req.post("https://epitech-api.herokuapp.com/login", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            Bundle bundle = new Bundle();
                            Fragment fragment = new Accueil();
                            FragmentTransaction transaction = myContext.getSupportFragmentManager().beginTransaction();
                            HashMap<String, String> data = new HashMap<String, String>();
                            token = response.getString("token");
                            bundle.putString("token", token);
                            bundle.putString("login", editLogin.getText().toString());
                            bundle.putString("password", editPassword.getText().toString());
                            Toast.makeText(getActivity(), "Connection reussie", Toast.LENGTH_SHORT).show();

                            data.put("login", editLogin.getText().toString());
                            data.put("password", editPassword.getText().toString());
                            data.put("token", token);
                            mCallbacks.dataPass(data);
                            fragment.setArguments(bundle);
                            transaction.replace(R.id.container, fragment);
                            transaction.commit();
                            linlaHeaderProgress.setVisibility(View.GONE);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] h, JSONArray response) {
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject response) {
                        JSONObject array;
                        TextView    error;

                        array = new JSONObject();
                        try {
                            array = response.getJSONObject("error");
                            error = (TextView) view.findViewById(R.id.errorConnexion);
                            error.setText("Failed to connect. Please try to reconnect.");
                            linlaHeaderProgress.setVisibility(View.GONE);
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            linlaHeaderProgress.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        return view;
    }
}
