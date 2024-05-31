package edu.mci.lifecycler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

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

        // notify the adapter that the underlying data has changed
        listAdapterEvents.notifyDataSetChanged();
    }
}