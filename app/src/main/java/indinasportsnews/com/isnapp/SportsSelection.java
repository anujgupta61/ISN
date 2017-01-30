package indinasportsnews.com.isnapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import static indinasportsnews.com.isnapp.R.id.toolbar;

public class SportsSelection extends AppCompatActivity {

    private GridView gridView;
    private View btnGo;
    private ArrayList<String> selectedStrings;
    private static final String[] numbers = new String[] {
            "Football" , "Table Tennis" , "Golf" , "Hockey", "Cricket", "Chess", "Boxing", "Tennis", "Basketball", "Racing", "Shooting",
            "Wrestling", "Athletics", "Snooker/Billiards", "Badminton", "Blog" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sports_selection) ;
        Toolbar myToolbar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(myToolbar);
        try {
            getSupportActionBar().setTitle("Choose your interest");
        } catch(Exception ex) {

        }
        gridView = (GridView) findViewById(R.id.gridview);
        btnGo = findViewById(R.id.continue_button);

        selectedStrings = new ArrayList<>();

        final GridAdapter adapter = new GridAdapter(numbers , this);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int selectedIndex = adapter.selectedPositions.indexOf(position);
                if (selectedIndex > -1) {
                    adapter.selectedPositions.remove(selectedIndex);
                    ((GridItemView) v).display(false);
                    selectedStrings.remove((String) parent.getItemAtPosition(position));
                } else {
                    adapter.selectedPositions.add(position);
                    ((GridItemView) v).display(true);
                    selectedStrings.add((String) parent.getItemAtPosition(position));
                }
            }
        });

        //set listener for Button event
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreferences . setPrefSportsList(getApplicationContext() , selectedStrings);
                Intent intent = new Intent(SportsSelection.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

}
