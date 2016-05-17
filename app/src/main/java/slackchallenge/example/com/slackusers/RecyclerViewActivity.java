package slackchallenge.example.com.slackusers;

/**
 * Created by Sahil on 5/14/2016.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends Activity implements SlackAPICall.AsyncResponse {

    private List<Person> persons;
    private RecyclerView rv;
    SlackAPICall s = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_recycler_view);

        rv = (RecyclerView)findViewById(R.id.recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        //initializeAdapter();
    }

    // Call the API and get the data
    private void initializeData(){
        persons = new ArrayList<>();
        Log.e("JSON_TRIAL", "BEFORE CALL");

        s = new SlackAPICall(this);
        s.execute();

    }


    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(persons, this);
        rv.setAdapter(adapter);
    }

    @Override
    public void processFinish() {

        Log.e("JSON_TRIAL", "AFTER CALL");
        for(SlackAPICall.Members m : s.getMembers()) {
            Person p = new Person( m.name,
                                   m.profile.title,
                                   m.profile.email,
                                   m.profile.image,
                                   m.profile.phone,
                                   m.id,
                                   m.userName,
                                   m.color);
            persons.add(p);
            Log.e("JSON_TRIAL", "Added new person");
        }

        initializeAdapter();
    }
}


     /*SlackAPICall s = new SlackAPICall(new SlackAPICall.AsyncResponse(){
            @Override
            public void processFinish(Void output) {

            }
        }).execute();*/

//new SlackAPICall().execute();