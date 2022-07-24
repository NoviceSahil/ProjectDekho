package com.example.projectdekho;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddProjectActivity extends AppCompatActivity {

    private Button addCourseBtn;
    private TextInputEditText projectNameEdt, projectDescEdt,bestSuitedEdt , branch, projectImgEdt, projectLinkEdt , batchEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar loadingPB;
    private String projectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addprojectdetails);

        addCourseBtn = findViewById(R.id.idBtnAddProject);
        projectNameEdt = findViewById(R.id.idEdtProjectNameAdd);
        projectDescEdt = findViewById(R.id.idEdtProjectDetailsAdd);
        branch = findViewById(R.id.idEdtBranchAdd);
        batchEdt = findViewById(R.id.idEdtBatchYearAdd);
        bestSuitedEdt = findViewById(R.id.idEdtCategoryAdd);
        projectImgEdt = findViewById(R.id.idEdtProjectImageLinkAdd);
        projectLinkEdt = findViewById(R.id.idEdtProjectLinkAdd);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1", "Project Added", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            }

        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("Projects");

        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingPB.setVisibility(View.VISIBLE);

                String projectName = projectNameEdt.getText().toString();
                String projectDesc = projectDescEdt.getText().toString();
                String bestSuited = branch.getText().toString();
                String batch = batchEdt.getText().toString();
                String category = bestSuitedEdt.getText().toString();
                String projectImg = projectImgEdt.getText().toString();
                String projectLink = projectLinkEdt.getText().toString();
                projectID = projectName;

                ProjectRvModal projectRVModal = new ProjectRvModal(projectID, projectName, projectDesc, bestSuited ,category, batch , projectImg, projectLink);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (TextUtils.isEmpty(projectName) && TextUtils.isEmpty(projectLink) && TextUtils.isEmpty(projectDesc) &&TextUtils.isEmpty(bestSuited) &&TextUtils.isEmpty(category) &&TextUtils.isEmpty(batch) &&TextUtils.isEmpty(projectImg)  ) {
                            Toast.makeText(AddProjectActivity.this, "Please fill all the Fields", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child(projectID).setValue(projectRVModal);

                            Toast.makeText(AddProjectActivity.this, "Project Added !", Toast.LENGTH_SHORT).show();

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(AddProjectActivity.this, "New Project Added");
                            builder.setContentTitle("Added");
                            builder.setContentText("Hey Buddy, Your project is added");
                            builder.setSmallIcon(R.drawable.ic_logosvg);
                            builder.setAutoCancel(true);
                            builder.setChannelId("1");


                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AddProjectActivity.this);
                            managerCompat.notify(1, builder.build());

                            startActivity(new Intent(AddProjectActivity.this, MainActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(AddProjectActivity.this, "Fail to add project..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
