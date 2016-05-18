package slackchallenge.example.com.slackusers;

//import org.apache.http.HttpResponse;
// These are two Lirbaries I need to use:
// api client auth
// name.monkey

import android.os.AsyncTask;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
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
 * SlackAPICall in charge of network call to get user list
 */
public class SlackAPICall extends AsyncTask<Void, Void, Void> {

    private static final String baseUserAPIUrl = "https://slack.com/api/users.list";
    private static final String authToken = "token=xoxp-5048173296-5048487710-19045732087-b5427e3b46";

    private String responseStr = null;
    private Response httpResponse = null;

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

        // Build the RESTful URL for HTTP Request
        StringBuilder builder = new StringBuilder(baseUserAPIUrl)
                .append("?")
                .append(authToken);

        com.google.api.client.http.HttpResponse response = null;
        String res = null;
        try {
            response = run(builder.toString());
            res = response.parseAsString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        // Call server and get json data
        String jsonResponse = getUserDataJSONThroughHttp();

        Gson gson = new Gson();
        httpResponse = gson.fromJson(jsonResponse, Response.class);
        responseStr = gson.toJson(httpResponse);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        delegate.processFinish();
    }

    public List<Members> getMembers() {
        return httpResponse.getMembers();
    }

    public String getResponseJSONString() {
        return responseStr;
    }

    public void setResponseObject(Response r) {
        httpResponse = r;
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

        @SerializedName("is_admin")
        Boolean isAdmin;
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