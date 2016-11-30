package com.vimos.karol.youthprojects.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vimos.karol.youthprojects.Model.YouthProject;
import com.vimos.karol.youthprojects.OnItemClickListener;
import com.vimos.karol.youthprojects.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karol on 2016-08-21.
 */
public class OthersAdapter extends RecyclerView.Adapter<OthersAdapter.MyViewHolder> {

    private List<YouthProject> youthProjectList;
    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView title;
        TextView place;
        TextView date;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            title = (TextView) view.findViewById(R.id.list_others_title);
            place = (TextView) view.findViewById(R.id.list_others_place);
            date = (TextView) view.findViewById(R.id.list_others_date);
        }
    }

    public OthersAdapter(OnItemClickListener listener) {
        this.youthProjectList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_others, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final YouthProject youthProject = youthProjectList.get(position);
        holder.title.setText(youthProject.getTitle());
        holder.place.setText(youthProject.getPlace());
        holder.date.setText(youthProject.getDate());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(youthProject);
            }
        });
    }

    public void add(YouthProject youthProject) {
        youthProjectList.add(youthProject);
    }

    @Override
    public int getItemCount() {
        return youthProjectList.size();
    }
}