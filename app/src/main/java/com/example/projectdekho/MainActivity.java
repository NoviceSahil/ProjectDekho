package com.example.projectdekho;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

    // creating variables for fab, firebase database,
    // progress bar, list, adapter,firebase auth,
    // recycler view and relative layout.

    private EditText editTextProduct;
    private EditText editTextPrice;

    private DatabaseReference myRef;
    private String ValueDatabase;
    private String refinedData;
    private String search;
    private ListView listView;

    private SearchView searchView;
    private TextView textViewSearch;
    private FloatingActionButton addProjectFAB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView courseRV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<ProjectRvModal> ProjectRvModalArrayList;
    private ProjectRvAdapter ProjectRvAdapter;
    private RelativeLayout homeRL;
    private CollationElementIterator CoursePriceTV;
    private String searchValue;
//    private BreakIterator CategoryTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initializing all our variables.

        courseRV = findViewById(R.id.idRVCourses);
        homeRL = findViewById(R.id.idRLBSheet);
        loadingPB = findViewById(R.id.idPBLoading);
        addProjectFAB = findViewById(R.id.idFABAddCourse);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        ProjectRvModalArrayList = new ArrayList<>();
        // on below line we are getting database reference.
        databaseReference = firebaseDatabase.getReference("Projects");
        // on below line adding a click listener for our floating action button.
        addProjectFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity for adding a course.
                Intent i = new Intent(MainActivity.this, AddProjectActivity.class);
                startActivity(i);
            }
        });
        // on below line initializing our adapter class.
        ProjectRvAdapter = new ProjectRvAdapter(ProjectRvModalArrayList, this, this::onCourseClick);
        // setting layout malinger to recycler view on below line.
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        // setting adapter to recycler view on below line.
        courseRV.setAdapter(ProjectRvAdapter);

        // on below line calling a method to fetch courses from database.
        getProjects();
    }

    private void getProjects() {
        // on below line clearing our list.
        ProjectRvModalArrayList.clear();
        // on below line we are calling add child event listener method to read the data.
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // on below line we are hiding our progress bar.
                loadingPB.setVisibility(View.GONE);
                // adding snapshot to our array list on below line.
                ProjectRvModalArrayList.add(snapshot.getValue(ProjectRvModal.class));

                // notifying our adapter that data has changed.
                ProjectRvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
                loadingPB.setVisibility(View.GONE);
                ProjectRvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // notifying our adapter when child is removed.
                ProjectRvAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // notifying our adapter when child is moved.
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
        // calling a method to display a bottom sheet on below line.
        displayBottomSheet(ProjectRvModalArrayList.get(position));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // adding a click listener for option selected on below line.
        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:
                // displaying a toast message on user logged out inside on click.
                Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();
                // on below line we are signing out our user.
                mAuth.signOut();
                // on below line we are opening our login activity.
                Intent i = new Intent(MainActivity.this, SignIn.class);
                startActivity(i);
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void processSearch(String s) {

        databaseReference.orderByChild("category").startAt(s).endAt(s+"\uf8ff").addChildEventListener(new ChildEventListener() {
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
        // on below line we are inflating our menu
        // file for displaying our menu options.
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
                    Toast.makeText(MainActivity.this, "in newtext", Toast.LENGTH_SHORT).show();
                    getProjects();
                } else {
                    Toast.makeText(MainActivity.this, searchValue, Toast.LENGTH_SHORT).show();
                }
//                processSearch(newText);
                return false;
            }

        });

        return true;
    }

    private void displayBottomSheet(ProjectRvModal modal) {
        // on below line we are creating our bottom sheet dialog.
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        // on below line we are inflating our layout file for our bottom sheet.
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, homeRL);
        // setting content view for bottom sheet on below line.
        bottomSheetTeachersDialog.setContentView(layout);
        // on below line we are setting a cancelable
        bottomSheetTeachersDialog.setCancelable(false);
        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true);
        // calling a method to display our bottom sheet.
        bottomSheetTeachersDialog.show();
        // on below line we are creating variables for
        // our text view and image view inside bottom sheet
        // and initialing them with their ids.
        TextView ProjectNameTV = layout.findViewById(R.id.idTVCourseName);
        TextView ProjectDescTV = layout.findViewById(R.id.idTVCourseDesc);
        TextView suitedForTV = layout.findViewById(R.id.idTVSuitedFor);
        TextView CategoryTV = layout.findViewById(R.id.idTVCoursePrice);
        TextView batch = layout.findViewById(R.id.idTVBatch);
        ImageView courseIV = layout.findViewById(R.id.idIVCourse);
        // on below line we are setting data to different views on below line.
        ProjectNameTV.setText(modal.getprojectName());
        ProjectDescTV.setText(modal.getProjectDescription());
        suitedForTV.setText("Suited for " + modal.getBranch());
        CategoryTV.setText( modal.getCategory());
        batch.setText( "Batch Year " +modal.getBatch());
        Picasso.get().load(modal.getprojectImg()).into(courseIV);
        Button viewBtn = layout.findViewById(R.id.idBtnVIewDetails);
        Button editBtn = layout.findViewById(R.id.idBtnEditCourse);

        // adding on click listener for our edit button.
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are opening our EditCourseActivity on below line.
                Intent i = new Intent(MainActivity.this, EditProjectActivity.class);
                // on below line we are passing our course modal
                i.putExtra("project", modal);
                startActivity(i);
            }
        });
        // adding click listener for our view button on below line.
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are navigating to browser
                // for displaying course details from its url
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(modal.getprojectLink()));
                startActivity(i);
            }
        });
    }
}
