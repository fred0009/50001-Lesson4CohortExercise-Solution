package com.example.norman_lee.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LocalStorage implements DataSource {


    private ArrayList<CardData> dataArrayList;

    LocalStorage(){
        dataArrayList = new ArrayList<>();
    }

    public void addData(String name, String path){
        CardData c = new CardData(name, path);
        dataArrayList.add(c);
    }

    public void addData(String name, Bitmap bitmapImage, Context context){
        String path = Utils.saveToInternalStorage(bitmapImage, name, context);
        CardData c = new CardData(name, path);
        dataArrayList.add(c);
    }

    public void removeData(int position){
        dataArrayList.remove(position);
    }

    public String getName(int i){
        return dataArrayList.get(i).getName();
    }

    public String getPath(int i){
        return dataArrayList.get(i).getPath();
    }

    private Bitmap getImage(int i){
        String name = dataArrayList.get(i).getName();
        String path = dataArrayList.get(i).getPath();
        return Utils.loadImageFromStorage(path, name);
    }
    @Override
    public void putImageOnImageView(int i, ImageView imageView){
        imageView.setImageBitmap( getImage(i));
    }
    @Override
    public void putNameOnTextView( int i, TextView textView){
        textView.setText( getName(i));
    }

    public int getSize(){
        return dataArrayList.size();
    }

    private static class CardData{

        private String name;
        private String path;

        private CardData( String name, String path){
            this.name = name;
            this.path = path;
        }


        private String getName() {
            return name;
        }


        private String getPath(){
            return path;
        }
    }
}
