package com.vimos.karol.youthprojects;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Details_Project_Activity extends AppCompatActivity {

    private TextView detailsTitle;
    private TextView detailsPlace;
    private TextView detailsDate;
    private TextView detailsDeadline;
    private TextView detailsWebsite;
    private Button shareButton;
    private ImageButton imageButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details__project_);



        detailsTitle = (TextView) findViewById(R.id.detsTitle);
        detailsPlace = (TextView) findViewById(R.id.detsPlace);
        detailsDate = (TextView) findViewById(R.id.detsDate);
        detailsDeadline = (TextView) findViewById(R.id.detsDeadline);
        detailsWebsite = (TextView) findViewById(R.id.detsWebsite);
        shareButton = (Button) findViewById(R.id.detsShareButton);
        imageButton = (ImageButton) findViewById(R.id.imageButton);



        Intent intent = this.getIntent();
        detailsTitle.setText(intent.getStringExtra(Constants.KEY_LIST_TITLE));
        detailsPlace.setText(intent.getStringExtra(Constants.KEY_LIST_PLACE));
        detailsDate.setText(intent.getStringExtra(Constants.KEY_LIST_DATE));
        detailsDeadline.setText(intent.getStringExtra(Constants.KEY_LIST_DEADLINE));
        detailsWebsite.setText(intent.getStringExtra(Constants.KEY_LIST_WEBSITE));



        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareProject();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                String url = detailsWebsite.getText().toString();
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setData(Uri.parse(url));
                startActivity(intent1);

            }
        });



    }

    public void shareProject(){

        StringBuilder dataString = new StringBuilder();

        String title = detailsTitle.getText().toString();
        String place = detailsPlace.getText().toString();
        String date = detailsDate.getText().toString();
        String deadline = detailsDeadline.getText().toString();
        String website = detailsWebsite.getText().toString();

        dataString.append(" Title: " + title + "\n\n");
        dataString.append(" Place: " + place + "\n\n");
        dataString.append(" Date: " + date + "\n\n");
        dataString.append(" Application form: " + website + "\n\n");
        dataString.append(" Deadline: " + deadline + "\n");


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, title);
        //i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
        i.putExtra(Intent.EXTRA_TEXT, dataString.toString());

        try{

            startActivity(Intent.createChooser(i, "Send mail..."));

        }catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Please install email client before sending",
                    Toast.LENGTH_LONG).show();
        }
    }
}
