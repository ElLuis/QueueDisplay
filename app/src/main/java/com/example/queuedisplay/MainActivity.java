package com.example.queuedisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.queuedisplay.database.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private DataSource db;
    private TextView mTextViewResult;
    private EditText userInput;
    private RequestQueue mQueue;
    private String userId;
    //private String userId = "esteban.arrangoiz@gmail.com"; // This is the user id that will be used to request the queues on the server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create a DataSource object to interact with the database
        db = new DataSource(this);
        db.open();
        db.deleteAllTickets();
        db.close();
        // Text View element to display the information on the activity
       // mTextViewResult = findViewById(R.id.text_view_result);

        //User's input
        userInput = findViewById(R.id.userId);

        // Used to parse the JSON object returned by the server
        mQueue = Volley.newRequestQueue(this);

    }

    //Login event - sends user to view the list of queues
    public void login_OnClick(View v)
    {
         userId = userInput.getText().toString();
        String url = "http://cyoq-web.herokuapp.com/api/user/" + userId + "/queue/get/496477151";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("response");
                            // delete all tickets before inserting the received ones
                            db.open();
                            /*db.deleteAllTickets();*/

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ticketObject = jsonArray.getJSONObject(i);

                                // store the ticket details from the Json object
                                String ticketId = ticketObject.getString("ticketId");
                                String status = ticketObject.getString("status");
                                String createdDate = ticketObject.getString("createdDate");
                                String message = "";
                                //Ticket ticket = new Ticket(finalUserId,ticketId,status,message,createdDate); //Object to pass to List as string
                                // print the ticket details in the TextView (just for testing purposes)
                                // mTextViewResult.append(ticketId + ", " + status + ", " + createdDate + "\n\n");
                                // ticketList.add(ticketId);
                                // use the dbHelper to insert the current ticket details into the database
                                db.insertTicket(userId, ticketId, status, message, createdDate); //No created date parameter

                                Intent intent = new Intent(MainActivity.this, ListQueues.class);
                                intent.putExtra("userInput", userId); //Pass user input
                                startActivity(intent);
                            }
                            db.close();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // if the response was an error ("Unauthorised User"), print the following message in the TextView
               userInput.setError("This user doesn't have active tickets on the server, please try again.");
            }

        });
        mQueue.add(request);
}
}
