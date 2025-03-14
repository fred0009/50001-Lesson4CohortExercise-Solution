package com.example.norman_lee.recyclerview;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    static String LOGCAT = "Pokemon";

//     Code adapted from https://stackoverflow.com/questions/15662258/how-to-save-a-bitmap-on-internal-storage
    static String saveToInternalStorage(Bitmap bitmapImage, String name, Context context){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath=new File(directory,name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(LOGCAT, "saveToInternalStorage: " + directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }

    static Bitmap loadImageFromStorage(String path, String name)
    {
        Bitmap b = null;
        try {
            File f=new File(path, name);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

    public static CardModel convertDrawableToCardModel(Context context, int drawable) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
        String imageName = context.getResources().getResourceEntryName(drawable);
        return new CardModel(imageName, bitmap);
    }
}
