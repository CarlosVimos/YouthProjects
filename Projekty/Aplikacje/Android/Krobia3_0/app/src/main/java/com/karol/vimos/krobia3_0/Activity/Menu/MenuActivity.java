package com.karol.vimos.krobia3_0.Activity.Menu;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.karol.vimos.krobia3_0.AppInfoActivity;
import com.karol.vimos.krobia3_0.BusinessActivity;
import com.karol.vimos.krobia3_0.EntertainmentActivity;
import com.karol.vimos.krobia3_0.Model.NewsDayModel;
import com.karol.vimos.krobia3_0.NewsDayActivity;
import com.karol.vimos.krobia3_0.R;
import com.karol.vimos.krobia3_0.Utils.Constants;

import es.dmoral.toasty.Toasty;

public class MenuActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private static final String TAG = "MyFirebaseIIDService";

    private AlphaAnimation alphaAnimation = new AlphaAnimation(1F,0.5F);
    Button bipButton, aboutAppButton, infoButton, sportButton, cinemaButton, quizButton, newsButton;
    TextView menuNewsTV, shortInfoTV;
    private Firebase firebase;
    private String news, info;
    private RelativeLayout relativeLayoutMenu;
    Activity activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

//        infoButton = (Button) findViewById(R.id.btnInfoId);

        Toasty.success(this, "Połączono z Internetem", Toast.LENGTH_LONG, true).show();

        cinemaButton = (Button) findViewById(R.id.btnCinemaId);
        quizButton = (Button) findViewById(R.id.btnQuizId);

        menuNewsTV = (TextView) findViewById(R.id.tvInfoTitle);
        shortInfoTV = (TextView) findViewById(R.id.tvInfoShort);

        Firebase.setAndroidContext(getApplicationContext());
        firebase = new Firebase("https://krobia30.firebaseio.com/news_day");

        relativeLayoutMenu = (RelativeLayout) findViewById(R.id.relativeLayoutMenu);

        relativeLayoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), NewsDayActivity.class);
                //intent.putExtra(Constants.KEY_PHOTO_NEWS_DAY, newsDayModel.getPhotoNewsDay());
                startActivity(intent);



            }
        });

//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("erasmus");
//
//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
//
//                NewsDayModel menuNewsModel = dataSnapshot.getValue(NewsDayModel.class);
//                news = menuNewsModel.getTitle();
//                menuNewsTV.setText(news);
//            }
//
//            @Override
//            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });





       // aboutAppButton = (Button) findViewById(R.id.btnAplikacjaId);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                NewsDayModel newsDayModel = dataSnapshot.getValue(NewsDayModel.class);

                news = newsDayModel.getTitleNewsDay();
                info = newsDayModel.getDescriptionShortNewsDay();

                menuNewsTV.setText(info);
                shortInfoTV.setText(news);




            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        Typeface typeface = Typeface.createFromAsset(getAssets(), "helvetica.ttf"); // bez polskich znakow
//        aboutAppButton.setTypeface(typeface);
   //     bipButton.setTypeface(typeface);
//        infoButton.setTypeface(typeface);
//        sportButton.setTypeface(typeface);
        cinemaButton.setTypeface(typeface);
//        quizButton.setTypeface(typeface);
        //servicesButton.setTypeface(typeface);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.e(TAG, "Refreshed token: " + refreshedToken);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("erasmus");




    }




    public void toNewsOnClickButton(View view) {

        view.startAnimation(alphaAnimation);
        startActivity(new Intent(MenuActivity.this, NewsActivity.class));


    }


    public void toSportOnclickButton(View view) {

        view.startAnimation(alphaAnimation);
        startActivity(new Intent(MenuActivity.this, SportActivity.class));


    }

    public void toInfoClickButton(View view) {

        view.startAnimation(alphaAnimation);

    }


    public void toAppInfoOnClickButton(View view) {

        view.startAnimation(alphaAnimation);
        startActivity(new Intent(MenuActivity.this, AppInfoActivity.class));

    }

    public void toBusinessOnClickButton(View view) {

        view.startAnimation(alphaAnimation);
        startActivity(new Intent(MenuActivity.this, BusinessActivity.class));

    }

    public void toEntertainmentActivityOnClick(View view) {

        view.startAnimation(alphaAnimation);
        startActivity(new Intent(MenuActivity.this, EntertainmentActivity.class));
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
