package slackchallenge.example.com.slackusers;

//import org.apache.http.HttpResponse;
// These are two Lirbaries I need to use:
// api client auth
// name.monkey


import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sahil on 5/14/2016.
 */
public class SlackAPICall extends AsyncTask<Void, Void, Void> {

    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private boolean debug = false;
    public AsyncResponse delegate = null;

    public SlackAPICall(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private com.google.api.client.http.HttpResponse run(String url) throws IOException {
        if (debug) System.out.println("Fetching: " + url);
        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest httpRequest) throws IOException {
                        httpRequest.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });

        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(url));
        return request.execute();
    }


    private String getUserDataJSONThroughHttp() {
        String apiUrl = "https://slack.com/api/users.list";
        String token = "token=xoxp-5048173296-5048487710-19045732087-b5427e3b46";
        StringBuilder builder = new StringBuilder(apiUrl)
                .append("?")
                .append(token);

        com.google.api.client.http.HttpResponse response = null;
        String res = null;
        try {

            response = run(builder.toString());
            res = response.parseAsString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;  //.parseAs(UsersInfoResponse.class);
    }

    String responseStr = null;

    String jsonData =
            "{" + "\"ok\":true," +
                    "\"members\" :" +
                "[{ " +
                    "\"id\":\"1\"," +
                    "\"team_id\":\"2\"," +
                    "\"name\":\"amy\"," +
                    "\"deleted\":false," +
                    "\"status\":null, " +
                    "\"color\":\"684b6c\"," +
                    "\"real_name\":\"Amy Adams\"," +
                    "\"tz\":\"America Los_Angeles\"," +
                    "\"tz_label\":\"Pacific Daylight Time\"," +
                    "\"profile\":{" +
                        "\"title\":\"Marketing\"," +
                        "\"phone\":\"415-5559463\"," +
                        "\"real_name\":\"Amy Adams\"," +
                        "\"real_name_normalized\":\"Amy Adams\","+
                        "\"email\":\"steve+ae1_7@slack-corp.com\"" +
                    "}," +
                    "\"is_admin\":false" +
                "}]}";

    Response response = null;

    public List<Members> getMembers() {
        return response.getMembers();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        String jsonResponse = getUserDataJSONThroughHttp(); //jsonData;

        Log.e("JSON_TRIAL STRING", jsonResponse);

        Gson gson = new Gson();
        response = gson.fromJson(jsonResponse, Response.class);
        responseStr = gson.toJson(response);

        for(Members m: response.getMembers()) {
            Log.e("JSON_TRIAL THIS", m.name);
        }
        Log.e("JSON_TRIAL", responseStr);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.e("JSON_TRIAL", "HERE ON_POST");
        delegate.processFinish();
    }

    public interface AsyncResponse {
        void processFinish();
    }

/*
Other paramaters: is_owner, is_admin, deleted (false, true)
 */

    public class Response
    {
        @SerializedName("members")
        List<Members> members;

        public List<Members> getMembers() {
            return members;
        }
    }

    class Members
    {
        @SerializedName("id")
        String id;

        @SerializedName("real_name")
        String name;

        @SerializedName("name")
        String userName;

        @SerializedName("color")
        String color;

        @SerializedName("profile")
        Profile profile;
    }

    class Profile
    {
        @SerializedName("real_name_normalized")
        String real_name;

        @SerializedName("title")
        String title;

        @SerializedName("email")
        String email;

        @SerializedName("phone")
        String phone;

        @SerializedName("image_192")
        String image;
    }

}