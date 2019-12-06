package com.example.queuedisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.List;

public class ListQueues extends AppCompatActivity {
    private DataSource db;
    private ListView ticketsLV;
    private List<String> ticketList;
    private List<String> ticketIds;
    private RequestQueue mQueue;
    private String ticketIdParam;
    private String userId;
    private ArrayAdapter<String> itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_queues);

        // create a DataSource object to interact with the database
        db = new DataSource(this);

        // Used to parse the JSON object returned by the server
        mQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userInput"); // get user id from input
        //view
        ticketsLV = findViewById(R.id.ticketsListView);
        //source
        ticketList = new ArrayList<>();
        ticketIds = new ArrayList<>();


        //Populate the source

        populateListView();

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,ticketList);
        ticketsLV.setAdapter(itemsAdapter);

        ticketsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {



                //Display message for the ticketId
                Intent messageIntent = new Intent(ListQueues.this, Message.class);
                messageIntent.putExtra("ticketId",ticketIds.get(i));
                messageIntent.putExtra("userId", userId);
                startActivity(messageIntent);
            }
        });
    }



    private void populateListView()
    {
        Ticket ticket;
        String email, ticketId, status, dateCreated;
        db.open();
        Cursor c = db.getAllTickets(userId);
        if (c.moveToFirst())
        {
            do {
                email = c.getString(0);
                ticketId = c.getString(2);
                status = c.getString(1);
                dateCreated = c.getString(4);
                ticket = new Ticket(email,ticketId,status,"",dateCreated);
                ticketList.add(ticket.toString());
                ticketIds.add(ticketId);
            } while (c.moveToNext());
        }

        db.close();
    }


}
