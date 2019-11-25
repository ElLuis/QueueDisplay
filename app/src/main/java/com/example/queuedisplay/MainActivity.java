package com.example.queuedisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewResult;
    private RequestQueue mQueue;
    private String userId = "arjunsk92@gmail.com"; // This is the user id that will be used to request the queues on the server
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create a DBHelper object to interact with the database
        dbHelper = new DBHelper(this);
        dbHelper.deleteAllTickets();

        // Text View element to display the information on the activity
        mTextViewResult = findViewById(R.id.text_view_result);

        // This button triggers the process of getting data from the server and insert it in the DB
        Button buttonFetch = findViewById(R.id.button_fetch);

        // Used to parse the JSON object returned by the server
        mQueue = Volley.newRequestQueue(this);

        buttonFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQueues(userId);
            }
        });
    }

    private void getQueues(String userId) {

        if (userId.length() == 0)
            userId = "arjunsk92@gmail.com"; // in case the user id was not specified, use a default one for testing

        // this is the url that will hit the sever backend asking for the list of tickets for the specified userId
        String url = "http://cyoq-web.herokuapp.com/api/user/" + userId + "/queue/get/496477151";

        // copy the userId to a final variable so it can be used in the JsonObjectRequest scope
        final String finalUserId = userId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mTextViewResult.setText("Registered queues for " + finalUserId + ":\n\n");
                        try {
                            JSONArray jsonArray = response.getJSONArray("response");

                            // delete all tickets before inserting the received ones
                            dbHelper.deleteAllTickets();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ticketObject = jsonArray.getJSONObject(i);

                                // store the ticket details from the Json object
                                String ticketId = ticketObject.getString("ticketId");
                                String status = ticketObject.getString("status");
                                String createdDate = ticketObject.getString("createdDate");
                                String message = "";

                                // print the ticket details in the TextView (just for testing purposes)
                                mTextViewResult.append(ticketId + ", " + status + ", " + createdDate + "\n\n");

                                // use the dbHelper to insert the current ticket details into the database
                                dbHelper.insertTicket(finalUserId, ticketId, status, message, createdDate);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // if the response was an error ("Unauthorised User"), print the following message in the TextView
                mTextViewResult.setText("The user " + finalUserId + " doesn't have active tickets on the server.");
            }
        });

        mQueue.add(request);
    }
}
