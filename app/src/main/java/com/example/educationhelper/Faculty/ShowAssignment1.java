package com.example.educationhelper.Faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class ShowAssignment1 extends AppCompatActivity {
    String channelCode, username;
    DatabaseReference reference;
    ListView mListView;
    ArrayList<String> myArrayList = new ArrayList<>();
    ArrayList<String> myArrayListCode = new ArrayList<>();
    ArrayList<String> myArrayListURL = new ArrayList<>();
    long channelCountInt = 0;
    private final String log = "RESULT DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_assignment1);

        username = new CurrentUser(ShowAssignment1.this).getUsername();
        channelCode = getIntent().getStringExtra("channelCode");
        reference = FirebaseDatabase.getInstance().getReference("credentials").child(username).child("channel")
                .child(channelCode).child("assignments");

        mListView= findViewById(R.id.listview);

        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(ShowAssignment1.this, android.R.layout.simple_list_item_1, myArrayList);
        mListView.setAdapter(myArrayAdapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.i(log, String.valueOf(dataSnapshot.getValue()));

                Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                String value = data.get("assignmentName").toString();
                String value2 = data.get("assignmentCount").toString();
                String value3 = data.get("defaultAssignment").toString();

                myArrayList.add(value);
                myArrayListCode.add(value2);
                myArrayListURL.add(value3);
                myArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String assignmentNameStaff = myArrayList.get(position).toString();
                String assignmentCount = myArrayListCode.get(position).toString();
                String URL = myArrayListURL.get(position).toString();
                Intent i = new Intent(ShowAssignment1.this, ShowAssignment2.class);
                i.putExtra("channelCode",channelCode);
                i.putExtra("assignmentCount",assignmentCount);
                i.putExtra("URL",URL);
                i.putExtra("assignmentNameStaff",assignmentNameStaff);

                startActivity(i);
                finish();
            }
        });

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        Intent i = new Intent(ShowAssignment1.this, AssignmentLandingPage.class);
        i.putExtra("channelCode",channelCode);
        startActivity(i);
        finish();
    }
}