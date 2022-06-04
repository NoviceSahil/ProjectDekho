package com.example.projectdekho;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProjectActivity extends AppCompatActivity {

    // creating variables for our edit text, firebase database,
    // database reference, course rv modal,progress bar.
    private TextInputEditText projectNameEdt, projectDescEdt,   bestSuitedEdt , batchEdt, categoryEdt , projectImgEdt, projectLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProjectRvModal projectRVModal;
    private ProgressBar loadingPB;
    // creating a string for our course id.
    private String projectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_project);
        // initializing all our variables on below line.
        Button addCourseBtn = findViewById(R.id.idBtnAddCourse);
        projectNameEdt = findViewById(R.id.idEdtCourseName);
        projectDescEdt = findViewById(R.id.idEdtCourseDescription);
        categoryEdt  = findViewById(R.id.idEdtCoursePrice);
        batchEdt = findViewById(R.id.idEdtBatch);
        bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
        projectImgEdt = findViewById(R.id.idEdtCourseImageLink);
        projectLinkEdt = findViewById(R.id.idEdtCourseLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line we are getting our modal class on which we have passed.
        projectRVModal = getIntent().getParcelableExtra("project");
        Button deleteCourseBtn = findViewById(R.id.idBtnDeleteCourse);

        if (projectRVModal != null) {
            // on below line we are setting data to our edit text from our modal class.
            projectNameEdt.setText(projectRVModal.getprojectName());
            bestSuitedEdt.setText(projectRVModal.getBranch());
            categoryEdt.setText(projectRVModal.getCategory());
            batchEdt.setText(projectRVModal.getBatch());
            projectImgEdt.setText(projectRVModal.getprojectImg());
            projectLinkEdt.setText(projectRVModal.getprojectLink());
            projectDescEdt.setText(projectRVModal.getProjectDescription());
             projectID = projectRVModal.getProjectId();
        }

        // on below line we are initialing our database reference and we are adding a child as our course id.
//        Toast.makeText(this, projectID, Toast.LENGTH_SHORT).show();
        databaseReference = firebaseDatabase.getReference("Projects").child(projectID);
        // on below line we are adding click listener for our add course button.
        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are making our progress bar as visible.
                loadingPB.setVisibility(View.VISIBLE);
                // on below line we are getting data from our edit text.
                String projectName = projectNameEdt.getText().toString();
                String projectDesc = projectDescEdt.getText().toString();
                String category =    categoryEdt.getText().toString();
                String batch     =      batchEdt.getText().toString();
                String bestSuited = bestSuitedEdt.getText().toString();
                String projectImg = projectImgEdt.getText().toString();
                String projectLink = projectLinkEdt.getText().toString();
                // on below line we are creating a map for
                // passing a data using key and value pair.
                Map<String, Object> map = new HashMap<>();
                map.put("projectName", projectName);
                map.put("projectDescription", projectDesc);
                map.put("category", category);
                map.put("bestSuitedFor", bestSuited);
                map.put("batch",batch);
                map.put("projectImg", projectImg);
                map.put("projectLink", projectLink);
                map.put("projectId", projectID);

                // on below line we are calling a database reference on
                // add value event listener and on data change method
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // making progress bar visibility as gone.
                        loadingPB.setVisibility(View.GONE);
                        // adding a map to our database.
                        databaseReference.updateChildren(map);
                        // on below line we are displaying a toast message.
                        Toast.makeText(EditProjectActivity.this, "Project Updated..", Toast.LENGTH_SHORT).show();
                        // opening a new activity after updating our coarse.
                        startActivity(new Intent(EditProjectActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // displaying a failure message on toast.
                        Toast.makeText(EditProjectActivity.this, "Fail to update project..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // adding a click listener for our delete course button.
        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to delete a course.
                deleteCourse();
            }
        });

    }

    private void deleteCourse() {
        // on below line calling a method to delete the course.
        databaseReference.removeValue();
        // displaying a toast message on below line.
        Toast.makeText(this, "Course Deleted..", Toast.LENGTH_SHORT).show();
        // opening a main activity on below line.
        startActivity(new Intent(EditProjectActivity.this, MainActivity.class));
    }
}

