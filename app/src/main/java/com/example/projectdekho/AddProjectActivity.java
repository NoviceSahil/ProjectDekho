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

    // creating variables for our button, edit text,
    // firebase database, database reference, progress bar.
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
        // initializing all our variables.
        addCourseBtn = findViewById(R.id.idBtnAddCourse);
        projectNameEdt = findViewById(R.id.idEdtCourseName);
        projectDescEdt = findViewById(R.id.idEdtCourseDescription);
        branch = findViewById(R.id.idEdtCoursePrice);
        batchEdt = findViewById(R.id.idEdtBatch);
        bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
        projectImgEdt = findViewById(R.id.idEdtCourseImageLink);
        projectLinkEdt = findViewById(R.id.idEdtCourseLink);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1", "Project Added", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference("Projects");
        // adding click listener for our add project button.
        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                // getting data from our edit text.
                String projectName = projectNameEdt.getText().toString();
                String projectDesc = projectDescEdt.getText().toString();
                String bestSuited = branch.getText().toString();
                String batch = batchEdt.getText().toString();
                String  category = bestSuitedEdt.getText().toString();
                String projectImg = projectImgEdt.getText().toString();
                String projectLink = projectLinkEdt.getText().toString();
                projectID = projectName;
                // on below line we are passing all data to our modal class.
                ProjectRvModal projectRVModal = new ProjectRvModal(projectID, projectName, projectDesc, bestSuited ,category, batch , projectImg, projectLink);
                // on below line we are calling a add value event
                // to pass data to firebase database.
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // on below line we are setting data in our firebase database.
                        if (TextUtils.isEmpty(projectName) && TextUtils.isEmpty(projectLink) && TextUtils.isEmpty(projectDesc) &&TextUtils.isEmpty(bestSuited) &&TextUtils.isEmpty(category) &&TextUtils.isEmpty(batch) &&TextUtils.isEmpty(projectImg)  ) {
                            Toast.makeText(AddProjectActivity.this, "Please fill all the Fields", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child(projectID).setValue(projectRVModal);

                            // displaying a toast message.
                            Toast.makeText(AddProjectActivity.this, "project Added..", Toast.LENGTH_SHORT).show();
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(AddProjectActivity.this, "New Project Added");
                            builder.setContentTitle("Project Added ");
                            builder.setContentText("Hey Buddy, Your project is added");
                            builder.setSmallIcon(R.drawable.ic_logosvg);
                            builder.setAutoCancel(true);
                            builder.setChannelId("1");


                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AddProjectActivity.this);
                            managerCompat.notify(1, builder.build());
                            // starting a main activity.
                            startActivity(new Intent(AddProjectActivity.this, MainActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // displaying a failure message on below line.
                        Toast.makeText(AddProjectActivity.this, "Fail to add project..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}}
