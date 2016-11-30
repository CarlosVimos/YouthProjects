package com.vimos.karol.youthprojects;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vimos.karol.youthprojects.Adapter.Project;

import java.util.ArrayList;
import java.util.List;

public class ChooseProject extends AppCompatActivity {

    private List<Project> myProjects = new ArrayList<Project>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_project);

        populateProjectList();
        populateListView();
        registerClickCallBack();

    }

    private void populateProjectList() {

        myProjects.add(new Project("EVS", "", R.drawable.list_ee));
        myProjects.add(new Project("Conferences","", R.drawable.list_cc));
        myProjects.add(new Project("Erasmus+","", R.drawable.list_e1w));
        myProjects.add(new Project("Internships","", R.drawable.list_ii));
        myProjects.add(new Project("Others", "", R.drawable.list_oo));
        myProjects.add(new Project("", "YouthProjects", R.color.othersColor));

    }

    private void populateListView(){

        ArrayAdapter<Project> adapter= new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listView1);
        list.setAdapter(adapter);

    }

    public void lastMinuteClick(View view) {

        startActivity(new Intent(ChooseProject.this, LastMinuteActivity.class));
    }

    private class MyListAdapter extends ArrayAdapter<Project>{
        public MyListAdapter(){
            super(ChooseProject.this, R.layout.list_row, myProjects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_row, parent, false);
            }

            // find the project to work with.
            Project currentProject = myProjects.get(position);

            // fill the view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(currentProject.getIconID());

            // Text
            TextView text = (TextView) itemView.findViewById(R.id.textView);
            text.setText(currentProject.getText());


            //set Others text
            TextView othersText = (TextView) itemView.findViewById(R.id.textOthers);
            othersText.setText(currentProject.getOthers());


           return itemView;
        }

    }

    private void registerClickCallBack() {

        ListView listView = (ListView) findViewById(R.id.listView1);
        assert listView != null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Project clickedProject = myProjects.get(position);

                switch (position) {
                    case 0:
                        Intent intent = new Intent(ChooseProject.this, EVS_Activity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(ChooseProject.this, Conferences_Activity.class);
                        startActivity(intent1);
                        break;

                    case 2:
                        Intent intent2 = new Intent(ChooseProject.this, Erasmus_Activity.class);
                        startActivity(intent2);
                        break;

                    case 3:
                        Intent intent3 = new Intent(ChooseProject.this, Internships_Activity.class);
                        startActivity(intent3);
                        break;

                    case 4:
                        Intent intent4 = new Intent(ChooseProject.this, OthersActivity.class);
                        startActivity(intent4);
                        break;

                    case 5:
                        Intent intent5 = new Intent(ChooseProject.this, YouthProjectsActivity.class);
                        startActivity(intent5);
                        break;

                }
            }
        });

    }






}
