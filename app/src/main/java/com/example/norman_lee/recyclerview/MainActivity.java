package com.example.norman_lee.recyclerview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CardAdapter cardAdapter;
    ImageView imageViewAdded;
    ArrayList<CardModel> dataSrc = new ArrayList<>();

    final String KEY_DATA_PATH = "SHARED_PREF_DATA_PATH";
    final String KEY_DATA_NAME = "SHARED_PREF_DATA_NAME";
    final String LOGCAT = "POKEDEX";
    final String PREF_FILE = "mainsharedpref";

    SharedPreferences mPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO 11.1 Get references to the widgets
        recyclerView = findViewById(R.id.cardRecycleView);
        imageViewAdded = findViewById(R.id.imageViewAdded);

        //TODO 12.7 Load the Json string from shared Preferences
        //TODO 12.8 Initialize your dataSource object with the Json string
        mPreferences = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPreferences.getString(KEY_DATA_PATH, "");
        String jsonNames = mPreferences.getString(KEY_DATA_NAME, "");

        //TODO 11.2 Initialize your dataSource object with drawables resource
        if( json.equals("") ){
            Log.d(LOGCAT, "onCreate: empty data... initiating some images from drawable resource");
            dataSrc.add( Utils.convertDrawableToCardModel(this, R.drawable.pikachu));
            dataSrc.add( Utils.convertDrawableToCardModel(this, R.drawable.psyduck));
            dataSrc.add( Utils.convertDrawableToCardModel(this, R.drawable.squirtle));
            dataSrc.add( Utils.convertDrawableToCardModel(this, R.drawable.spearow));
            dataSrc.add( Utils.convertDrawableToCardModel(this, R.drawable.bulbasaur));
        }else{
            Log.d(LOGCAT, "onCreate: data exist " + json);
            ArrayList<String> filePaths = gson.fromJson(json, ArrayList.class);
            ArrayList<String> names = gson.fromJson(jsonNames, ArrayList.class);
            for (int i=0;i<filePaths.size();i++) {
                String name = names.get(i);
                Bitmap img = Utils.loadImageFromStorage(filePaths.get(i),name);
                dataSrc.add(new CardModel(name, img));
            }
            Log.d(LOGCAT, "onCreate: data loaded " + json);
        }

        //TODO 11.3 --> Go to CardAdapter
        //TODO 11.8 Complete the necessary code to initialize your RecyclerView
        cardAdapter = new CardAdapter(dataSrc);
        recyclerView.setAdapter(cardAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));



        //TODO 12.1 Set up an Explicit Intent to DataEntry Activity with startActivityForResult (no coding)
        //TODO 12.5a Set up an Activity Launcher to process the data returned
        final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Result from DataEntryActivity is obtained
                        // Get the data and insert it into datasource
                        Bundle b = result.getData().getExtras();
                        String name = b.getString(DataEntryActivity.KEY_NAME);
                        String path = b.getString(DataEntryActivity.KEY_PATH);
                        Bitmap img = Utils.loadImageFromStorage(path, name);
                        dataSrc.add(new CardModel(name, img));
                        cardAdapter.notifyItemInserted(dataSrc.size()-1);

                        // -- Displays the selected image at the big ? widget --
                        imageViewAdded.setImageBitmap(img);
                    }
                }

        );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DataEntryActivity.class);
                launcher.launch(intent);
            }
        });

        //TODO 12.9 [OPTIONAL] Add code to delete a RecyclerView item upon swiping. See notes for the code.
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                //code to delete the view goes here
                CardAdapter.CardViewHolder cardVH = (CardAdapter.CardViewHolder) viewHolder;
                int position = cardVH.getBindingAdapterPosition();
                dataSrc.remove(position);
                cardAdapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper
                = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    //TODO 12.6 Complete onPause to store the DataSource in SharedPreferences as a JSON string
    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor prefsEditor = mPreferences.edit();
        Gson gson = new Gson();
        ArrayList<String> filePaths = new ArrayList<>();
        ArrayList<String> imgNames = new ArrayList<>();
        for (CardModel cm: dataSrc) {
            String path = Utils.saveToInternalStorage(cm.getImg(), cm.getName(), this);
            filePaths.add(path);
            imgNames.add(cm.getName());
        }
        String jsonImgs = gson.toJson( filePaths );
        String jsonNames = gson.toJson( imgNames );
        prefsEditor.putString(KEY_DATA_PATH, jsonImgs);
        prefsEditor.putString(KEY_DATA_NAME, jsonNames);
        prefsEditor.apply();
    }
}
