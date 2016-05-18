# SlackUsers

Slack Coding Challenge
README
By Sahil Jain


Application Features:
List of card views displaying the summary of the user
Name, Title, and UserName and UserID


Upon clicking a summary card view, we are presented with a dialog showing the detail view of the user.
Name, Title, UserName and UserID, email, and phone number (clickable)


Clickable Phone number
Upon clicking the phone number on the detail view, we are redirected to the phone pad where the user's number is already inserted


Admin Account Identification
For any admin users that are on the list, an admin icon is displayed on the summary card view and the detailed cardview

Color of each card comes from the json data (assumed to be the favorite color of each user)

________________________________________________________________________
Class Structure, Flow and Description:
Diagram of my Class Structure and flow:


Class Description:
RecyclerViewActivity
Created on application launch
Sets up the Recyclerview for displaying the initial card views
Implements SlackAPICall.AsyncResponse
This is an interface to handle the callback from the Async API call
RVAdapter and PersonViewHolder
Implements the adapter for the RecyclerViewActivity
Main purpose is to map data to view holder (card views)
PersonViewHolder
Holds references to the children's views  in the parent cardview
There is a front card view (cv) and a detailed card view (cv_d)
SlackAPICall
Extends AsyncTask specifically for HTTP requests
Implements doInBackground method to request and receive the JSON user list data
Implements onPostExecute method to post process http response
Classes: Response, Members, Profile
POJO using reflection to extract data from JSON response
Person
POJO representing each member (person) in the team
We have instance variables to describe the person:
String name;
String email;
String title;
String photoURL;
String phoneNumber;
String userId;
String userName;
String colorCode;
Boolean isAdmin;

________________________________________________________________________

Libraries Used:
Gson - https://github.com/google/gson
Class usage: SlackAPICall, RecyclerViewActivity
For converting JSON string to JSON object with Serialization
SharedPreferences
Class usage: RecyclerViewActivity
For persistent storage of JSON received from HTTP network API call
Google APIs Client Libraries - https://developers.google.com/api-client-library/java/
Class useage: SlackAPICall
For HTTP request builder (HttpRequestFactory, HttpRequest) and call (request.execute();)
HttpTransport HTTP_TRANSPORT
JsonFactory JSON_FACTORY

