package com.example.projectdekho;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.CollationElementIterator;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProjectRvAdapter.CourseClickInterface {

    private SearchView searchView;
    private TextView textViewSearch;
    private FloatingActionButton addProjectFAB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView projectRV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<ProjectRvModal> ProjectRvModalArrayList;
    private ProjectRvAdapter ProjectRvAdapter;
    private RelativeLayout homeRL;
    private CollationElementIterator CategoryTV;
    private String searchValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectRV = findViewById(R.id.idRVProjects);
        homeRL = findViewById(R.id.idRLBSheet);
        loadingPB = findViewById(R.id.idPBLoading);
        addProjectFAB = findViewById(R.id.idFABAddProject);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        ProjectRvModalArrayList = new ArrayList<>();

        databaseReference = firebaseDatabase.getReference("Projects");

        addProjectFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, AddProjectActivity.class);
                startActivity(i);
            }
        });

        ProjectRvAdapter = new ProjectRvAdapter(ProjectRvModalArrayList, this, this::onCourseClick);

        projectRV.setLayoutManager(new LinearLayoutManager(this));

        projectRV.setAdapter(ProjectRvAdapter);

        getProjects();
    }

    private void getProjects() {

        ProjectRvModalArrayList.clear();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                loadingPB.setVisibility(View.GONE);

                ProjectRvModalArrayList.add(snapshot.getValue(ProjectRvModal.class));


                ProjectRvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                loadingPB.setVisibility(View.GONE);
                ProjectRvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                ProjectRvAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ProjectRvAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onCourseClick(int position) {

        displayBottomSheet(ProjectRvModalArrayList.get(position));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:

                Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();

                mAuth.signOut();

                Intent i = new Intent(MainActivity.this, SignIn.class);
                startActivity(i);
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void processSearch(String s) {

        databaseReference.orderByChild("projectName").startAt(s).endAt(s+"\uf8ff").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ProjectRvModalArrayList.add(snapshot.getValue(ProjectRvModal.class));
                ProjectRvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                processSearch(query);
                ProjectRvModalArrayList.clear();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchValue = newText;
                if(searchValue.equals("")) {
                    getProjects();
                } else {
                    Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                    processSearch(searchValue);
                    ProjectRvModalArrayList.clear();
                }
                return false;
            }
        });

        return true;
    }

    private void displayBottomSheet(ProjectRvModal modal) {

        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, homeRL);
        bottomSheetTeachersDialog.setContentView(layout);
        bottomSheetTeachersDialog.setCancelable(false);
        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true);

        bottomSheetTeachersDialog.show();

        TextView ProjectNameTV = layout.findViewById(R.id.idTVProjectName);
        TextView ProjectDescTV = layout.findViewById(R.id.idTVProjectDesc);
        TextView suitedForTV = layout.findViewById(R.id.idTVBranch1);
        TextView CategoryTV = layout.findViewById(R.id.idTVCategory1);
        TextView batch = layout.findViewById(R.id.idTVBatch);
        ImageView projectIV = layout.findViewById(R.id.idIVImage);

        ProjectNameTV.setText(modal.getprojectName());
        ProjectDescTV.setText(modal.getProjectDescription());
        suitedForTV.setText("Suited for " + modal.getBranch());
        CategoryTV.setText( modal.getCategory());
        batch.setText( "Batch Year " +modal.getBatch());
        Picasso.get().load(modal.getprojectImg()).into(projectIV);
        Button viewBtn = layout.findViewById(R.id.idBtnVIewDetails);
        Button editBtn = layout.findViewById(R.id.idBtnEditCourse);


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, EditProjectActivity.class);

                i.putExtra("project", modal);
                startActivity(i);
            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(modal.getprojectLink()));
                startActivity(i);
            }
        });
    }
}
