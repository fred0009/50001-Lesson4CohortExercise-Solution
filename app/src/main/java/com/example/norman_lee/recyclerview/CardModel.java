package com.example.norman_lee.recyclerview;

import android.graphics.Bitmap;

public class CardModel {

        private final String name;
        private final Bitmap img;

        public CardModel(String name, Bitmap img) {
            this.name = name;
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public Bitmap getImg() {
            return img;
        }
}