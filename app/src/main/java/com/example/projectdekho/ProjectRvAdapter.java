package com.example.projectdekho;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProjectRvAdapter extends RecyclerView.Adapter<ProjectRvAdapter.ViewHolder> {

    private ArrayList<ProjectRvModal> ProjectRvModalArrayList;
    private Context context;
    private ProjectClickInterface projectClickInterface;
    int lastPos = -1;

    public ProjectRvAdapter(ArrayList<ProjectRvModal> ProjectRvModalArrayList, Context context,ProjectClickInterface projectClickInterface) {
        this.ProjectRvModalArrayList = ProjectRvModalArrayList;
        this.context = context;
        this.projectClickInterface = projectClickInterface;
    }

    @NonNull
    @Override
    public ProjectRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.project_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectRvAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ProjectRvModal ProjectRvModal = ProjectRvModalArrayList.get(position);
        holder.projectNameTV.setText(ProjectRvModal.getprojectName());
        holder.categoryTV.setText(ProjectRvModal.getCategory());
        Picasso.get().load(ProjectRvModal.getprojectImg()).into(holder.projectIV);

        setAnimation(holder.itemView, position);
        holder.projectIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectClickInterface.onProjectClick(position);
            }
        });
    }

    private void setAnimation(View itemView, int position) {
        if (position > lastPos) {

            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }

    @Override
    public int getItemCount() {
        return ProjectRvModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView projectIV;
        private TextView projectNameTV;
        private TextView categoryTV;

    public ViewHolder(@NonNull View itemView) {
            super(itemView);

            projectIV = itemView.findViewById(R.id.idIVProject);
            projectNameTV = itemView.findViewById(R.id.idTVProjectNameH);
            categoryTV = itemView.findViewById(R.id.idTVCategory);

        }
    }

    public interface ProjectClickInterface {
        void onProjectClick(int position);
    }
}

