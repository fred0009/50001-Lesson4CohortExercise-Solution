package com.example.norman_lee.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

public class DataEntryActivity extends AppCompatActivity {

    EditText editTextNameEntry;
    Button buttonSelectImage;
    Button buttonOK;
    ImageView imageViewSelected;
    Bitmap bitmap;
    final static String KEY_PATH = "Image";
    final static String KEY_NAME = "Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        editTextNameEntry = findViewById(R.id.editTextNameEntry);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        imageViewSelected = findViewById(R.id.imageViewSelected);
        buttonOK = findViewById(R.id.buttonOK);

        //TODO 12.3a Set up a launcher to process the result of the selection
        final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // YOU SAY WHAT HAPPENS WHEN THE IMAGE IS SELECTED
                        // --> A URI to the image is returned
                        if( result.getResultCode() == Activity.RESULT_OK
                                && result.getData() != null ){
                            Intent intent = result.getData();
                            Uri photoUri = intent.getData();
                            imageViewSelected.setImageURI(photoUri);

                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(
                                        DataEntryActivity.this.getContentResolver(),
                                        photoUri
                                );
                            } catch (IOException e) {
                                e.printStackTrace(); // Write a toast if you want
                            }
                        }
                    }
                }

        );

        //TODO 12.2 Set up an implicit intent to the image gallery (standard code)
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);
            }
        });

        //TODO 12.4 When the OK button is clicked, set up an intent to go back to MainActivity
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* First,
                save the bitmap to app's internal storage,
                and get the path of its location */
                String name = editTextNameEntry.getText().toString();
                String path = Utils.saveToInternalStorage( bitmap, name, DataEntryActivity.this);

                /* Recall that DataEntry was invoked by an intent from MainActivity that expected a result
                ie. MainActivity launched DataEntry for the user to enter a name and get an image,
                and MainActivity wants the information back

                Next, for DataEntry to return the result,
                1. instantiate an Intent object
                2. use putExtra() to attach the information you want to return
                3. call setResult() followed by finish()
                 */
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_NAME, name);
                resultIntent.putExtra(KEY_PATH, path);
                setResult( Activity.RESULT_OK, resultIntent);
                finish();

            }
        });

        //TODO 12.5 --> Go back to MainActivity


    }
}
