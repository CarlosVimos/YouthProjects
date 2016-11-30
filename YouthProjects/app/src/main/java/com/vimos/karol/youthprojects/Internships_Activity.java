package com.vimos.karol.youthprojects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vimos.karol.youthprojects.Adapter.InternshipsAdapter;
import com.vimos.karol.youthprojects.Model.YouthProject;

public class Internships_Activity extends AppCompatActivity implements OnItemClickListener{

    private InternshipsAdapter adapter;
    private RecyclerView listView;
    private DatabaseReference reference;

    @Override
    public void onItemClick(YouthProject selectedList) {
        if (selectedList != null) {
            Intent intent = new Intent(this, Details_Project_Activity.class);

            intent.putExtra(Constants.KEY_LIST_TITLE, selectedList.getTitle());
            intent.putExtra(Constants.KEY_LIST_PLACE, selectedList.getPlace());
            intent.putExtra(Constants.KEY_LIST_DATE, selectedList.getDate());
            intent.putExtra(Constants.KEY_LIST_DEADLINE, selectedList.getDeadline());
            intent.putExtra(Constants.KEY_LIST_WEBSITE, selectedList.getWebsite());
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internships_);

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);


        listView = (RecyclerView) findViewById(R.id.list_internships);
        adapter = new InternshipsAdapter(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("internships");

        reference.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                YouthProject msg = dataSnapshot.getValue(YouthProject.class);
                adapter.add(msg);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
