package com.example.queuedisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.queuedisplay.database.DataSource;
import com.example.queuedisplay.model.Ticket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Message extends AppCompatActivity {

    private String ticketId;
    private String userId;
    private TextView messageTV;
    private RequestQueue mQueue;
    private final String companyId = "496477151"; // This is the company Id that is hardcoded for the demo
    DataSource db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        db = new DataSource(this);
        Intent mIntent = getIntent();
        userId = mIntent.getStringExtra("userId");
        ticketId = mIntent.getStringExtra("ticketId");
        messageTV = findViewById(R.id.fullMessageTV);
        mQueue = Volley.newRequestQueue(this);

        Timer t = new Timer();
        t.scheduleAtFixedRate(
                new TimerTask()
                {
                    public void run()
                    {

                            getNotifications();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMessage();
                            }
                        });
                    }
                },
                0,      // run first occurrence after 2 seconds
                5000); // run every minute






    }

    private void getNotifications() {
        // Get all tickets stored in the local database
            // Ask the server for notifications for the current ticket
            System.out.println("Processing the ticket: " + ticketId);

            // this is the url that will hit the sever backend asking for the list of tickets for the specified userId
            String url = "http://cyoq-web.herokuapp.com/api/user/" + userId + "/company/" + companyId + "/ticket/" + ticketId + "/notification";

            // copy the userId to a final variable so it can be used in the JsonObjectRequest scope
            final String finalUserId = userId;
            // copy the ticketId to a final variable so it can be used in the JsonObjectRequest scope
            final String finalTicketId = ticketId;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("HTTPRequest", "Asking for notifications for the ticket: " + finalTicketId);
                            try {

                                db.open();
                                JSONArray jsonArray = response.getJSONArray("response");

                                    JSONObject ticketObject = jsonArray.getJSONObject(Integer.parseInt(ticketId));

                                    // store the notification info from the Json object
                                    String ticketId = ticketObject.getString("ticketId");
                                    String status = ticketObject.getString("status");
                                    String createdDate = ticketObject.getString("createdDate");
                                    String message = ticketObject.getString("message");

                                    // print the ticket details in the log console
                                    Log.d("HTTPRequest", ticketId + ", " + status + ", " + createdDate + "\n\n");

                                    // use the dbHelper to insert the current ticket details into the database
                                    db.updateTicket(finalUserId, ticketId, status, message);



                                db.close();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // if the response was an error, then there are no notifications. Nothing to do
                }
            });

            mQueue.add(request);

        }

        private  String showMessage()
        {

            db.open();
            Cursor c = db.getMessage(ticketId);
            if(c.moveToFirst())
            {

                String message = c.getString(0);

                messageTV.setText(message);
                return  message;
            }

            db.close();
            return "No message has been added yet.";
        }
    }
