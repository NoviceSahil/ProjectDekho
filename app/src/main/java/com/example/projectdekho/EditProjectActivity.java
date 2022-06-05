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

    private TextInputEditText projectNameEdt, projectDescEdt,   bestSuitedEdt , batchEdt, categoryEdt , projectImgEdt, projectLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProjectRvModal projectRVModal;
    private ProgressBar loadingPB;

    private String projectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_project);

        Button addProjectBtn = findViewById(R.id.idBtnUpdateProjectEdit);
        projectNameEdt = findViewById(R.id.idEdtProjectNameEdit);
        projectDescEdt = findViewById(R.id.idEdtProjectsDetailsEdit);
        categoryEdt  = findViewById(R.id.idEdtCategoryEdit);
        batchEdt = findViewById(R.id.idEdtBatchYearEdit);
        bestSuitedEdt = findViewById(R.id.idEdtBranchEdit);
        projectImgEdt = findViewById(R.id.idEdtProjectImageLinkEdit);
        projectLinkEdt = findViewById(R.id.idEdtProjectLinkEdit);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();

        projectRVModal = getIntent().getParcelableExtra("project");
        Button deleteProjectBtn = findViewById(R.id.idBtnDeleteProjectEdit);

        if (projectRVModal != null) {

            projectNameEdt.setText(projectRVModal.getprojectName());
            bestSuitedEdt.setText(projectRVModal.getBranch());
            categoryEdt.setText(projectRVModal.getCategory());
            batchEdt.setText(projectRVModal.getBatch());
            projectImgEdt.setText(projectRVModal.getprojectImg());
            projectLinkEdt.setText(projectRVModal.getprojectLink());
            projectDescEdt.setText(projectRVModal.getProjectDescription());
             projectID = projectRVModal.getProjectId();
        }

        databaseReference = firebaseDatabase.getReference("Projects").child(projectID);

        addProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingPB.setVisibility(View.VISIBLE);

                String projectName = projectNameEdt.getText().toString();
                String projectDesc = projectDescEdt.getText().toString();
                String category =    categoryEdt.getText().toString();
                String batch     =      batchEdt.getText().toString();
                String bestSuited = bestSuitedEdt.getText().toString();
                String projectImg = projectImgEdt.getText().toString();
                String projectLink = projectLinkEdt.getText().toString();

                Map<String, Object> map = new HashMap<>();
                map.put("projectName", projectName);
                map.put("projectDescription", projectDesc);
                map.put("category", category);
                map.put("bestSuitedFor", bestSuited);
                map.put("batch",batch);
                map.put("projectImg", projectImg);
                map.put("projectLink", projectLink);
                map.put("projectId", projectID);


                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        databaseReference.updateChildren(map);
                        Toast.makeText(EditProjectActivity.this, "Project Updated..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditProjectActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditProjectActivity.this, "Fail to update project..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        deleteProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse();
            }
        });
    }

    private void deleteCourse() {
        databaseReference.removeValue();
        Toast.makeText(this, "Project Deleted..", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditProjectActivity.this, MainActivity.class));
    }
}

