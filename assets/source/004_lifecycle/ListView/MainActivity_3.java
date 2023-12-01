package edu.mci.lifecycler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Properties
    private ArrayAdapter<String> listAdapterEvents;
    private ArrayList<String> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set activity content to layout defined in 'activity_main.xml'
        setContentView(R.layout.activity_main);

        // Create local variable and link ListView widget
        ListView listViewEvents = findViewById(R.id.listViewEvents);

        // Create list data storage (String)
        listData = new ArrayList<>();
        // Create ArrayAdapter to displays a list of texts (String)
        listAdapterEvents = new ArrayAdapter<>(this, R.layout.simple_list_item, listData);
        // Set adapter to be used by list view
        listViewEvents.setAdapter(listAdapterEvents);

        // Fill data list with dummy events
        for(int i = 0; i < 10; i++) {
            listData.add(String.format("Event #%d", i));
        }
        // clear list again
        listData.clear();

        // notify the adapter that the underlying data has changed
        listAdapterEvents.notifyDataSetChanged();

        // Create local variable and link FAB widget
        FloatingActionButton fabDestroy = findViewById(R.id.fabDestroy);
        // Install click listener
        fabDestroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("OnLifeCycle", "Finish it!!!!!1");
                MainActivity.this.finish(); // exits the activity
            }
        });

        // Log event and add to list
        Log.d("OnLifeCycle", "onCreate()");
        addListItem("onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Log event and add to list
        Log.d("OnLifeCycle", "onStart()");
        addListItem("onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Log event and add to list
        Log.d("OnLifeCycle", "onResume()");
        addListItem("onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Log event and add to list
        Log.d("OnLifeCycle", "onPause()");
        addListItem("onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Log event and add to list
        Log.d("OnLifeCycle", "onStop()");
        addListItem("onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Log event and add to list
        Log.d("OnLifeCycle", "onDestroy()");
        addListItem("onDestroy()");
    }

    private void addListItem(String name) {
        // create formatted timestamp of current time
        String timeNow = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        // combine timestamp and parameter
        listData.add(String.format("[%s] %s", timeNow, name));

        // notify the adapter that the underlying data has changed
        listAdapterEvents.notifyDataSetChanged();
    }
}