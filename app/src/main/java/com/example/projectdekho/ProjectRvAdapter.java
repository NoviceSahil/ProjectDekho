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
    // creating variables for our list, context, interface and position.
    private ArrayList<ProjectRvModal> ProjectRvModalArrayList;
    private Context context;
    private CourseClickInterface courseClickInterface;
    int lastPos = -1;

    // creating a constructor.
    public ProjectRvAdapter(ArrayList<ProjectRvModal> ProjectRvModalArrayList, Context context, CourseClickInterface courseClickInterface) {
        this.ProjectRvModalArrayList = ProjectRvModalArrayList;
        this.context = context;
        this.courseClickInterface = courseClickInterface;
    }

    @NonNull
    @Override
    public ProjectRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.project_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectRvAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // setting data to our recycler view item on below line.
        ProjectRvModal ProjectRvModal = ProjectRvModalArrayList.get(position);
        holder.courseTV.setText(ProjectRvModal.getprojectName());
        holder.coursePriceTV.setText(ProjectRvModal.getCategory());
        Picasso.get().load(ProjectRvModal.getprojectImg()).into(holder.courseIV);
        // adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position);
        holder.courseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseClickInterface.onCourseClick(position);
            }
        });
    }

    private void setAnimation(View itemView, int position) {
        if (position > lastPos) {
            // on below line we are setting animation.
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
        // creating variable for our image view and text view on below line.
        private ImageView courseIV;
        private TextView courseTV;
        private TextView coursePriceTV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing all our variables on below line.
            courseIV = itemView.findViewById(R.id.idIVCourse);
            courseTV = itemView.findViewById(R.id.idTVCOurseName);
            coursePriceTV = itemView.findViewById(R.id.idTVCoursePrice);

        }
    }

    // creating a interface for on click
    public interface CourseClickInterface {
        void onCourseClick(int position);
    }
}

