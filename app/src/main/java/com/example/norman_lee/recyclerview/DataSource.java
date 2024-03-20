package com.example.norman_lee.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

public interface DataSource {

    void addData(String name, String path);

    void addData(String name, Bitmap bitmapImage, Context context);
    void removeData(int position);

    String getName(int i);

    String getPath(int i);
    void putImageOnImageView(int i, ImageView imageView);

    void putNameOnTextView(int i, TextView textView);

    int getSize();
}
