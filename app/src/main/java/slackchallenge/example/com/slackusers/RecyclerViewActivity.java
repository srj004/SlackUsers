package slackchallenge.example.com.slackusers;

/**
 * Launch of application, sets Recycler View
 */
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends Activity implements SlackAPICall.AsyncResponse {

    private final String PREF_KEY = "jsondata";
    public SharedPreferences sharedPreferences;
    public  SharedPreferences.Editor sharedPreferencesEditor;

    private List<Person> persons;
    private RecyclerView rv;
    private SlackAPICall slackAPICall = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_recycler_view);

        // Set up references to SharedPreferences
        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();

        // Set up Recycler View
        rv = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true); // All the views are the same size

        // Get Data (calls the network, or uses SharedPreferences)
        initializeData();
    }

     private void initializeData() {
         //Log.e("Cold Start", "Start");
         persons = new ArrayList<>();
         slackAPICall = new SlackAPICall(this);

         String jsonStr = sharedPreferences.getString(PREF_KEY, "EMPTY");
         //Log.e("Cold Start", "JSONStr: " + jsonStr);
         if (!jsonStr.equals("EMPTY")) {
             //Log.e("Cold Start", "FOUND");
             gsonCallAndPostProcessing(jsonStr);
         } else {
             //Log.e("Cold Start", "NETWORK");
             slackAPICall.execute();
         }
     }

    private void gsonCallAndPostProcessing(String jsonStr) {
        Gson gson = new Gson();
        SlackAPICall.Response r = gson.fromJson(jsonStr, SlackAPICall.Response.class);
        slackAPICall.setResponseObject(r);
        processFinish();
    }

    private void setSharedPreferenceData(String data) {
        sharedPreferencesEditor.putString(PREF_KEY, data);
        sharedPreferencesEditor.apply();
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(persons, this);
        rv.setAdapter(adapter);
    }

    @Override
    public void processFinish() {

        //Log.e("Cold Start", "Setting String to: " + slackAPICall.getResponseJSONString());
        // Set the shared preferences for cold start
        String responseStr = slackAPICall.getResponseJSONString();
        if(responseStr != null) {
            setSharedPreferenceData(responseStr);
        }

        for (SlackAPICall.Members m : slackAPICall.getMembers()) {
            Person p = new Person(m.name,
                    m.profile.title,
                    m.profile.email,
                    m.profile.image,
                    m.profile.phone,
                    m.id,
                    m.userName,
                    m.color,
                    m.isAdmin);
            // Add to the user list
            persons.add(p);
        }

        initializeAdapter();
    }
}