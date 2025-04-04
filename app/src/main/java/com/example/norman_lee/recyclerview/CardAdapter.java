package com.example.norman_lee.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{

    ArrayList<CardModel> dataSource;

    //TODO 11.3 Complete the constructor to initialize the DataSource instance variable
    CardAdapter(ArrayList<CardModel> dataSource){
        this.dataSource = dataSource;
    }


    //TODO 11.5 the layout of each Card is inflated and used to instantiate CharaViewHolder (no coding)
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card,
                viewGroup, false);
        return new CardViewHolder(itemView);
    }

    //TODO 11.6 the data at position i is extracted and placed on the i-th card
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder cardViewHolder, int i) {
        cardViewHolder.imageViewChara.setImageBitmap(dataSource.get(i).getImg());
        cardViewHolder.textViewName.setText(dataSource.get(i).getName());
    }

    //TODO 11.7 the total number of data points must be returned here
    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    //TODO 11.4 complete the constructor to initialize the instance variables by using findViewbyId
    static class CardViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewChara;
        TextView textViewName;

        CardViewHolder(View view){
            super(view);
            imageViewChara = view.findViewById(R.id.cardViewImage);
            textViewName = view.findViewById(R.id.cardViewTextName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "I am "+textViewName.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
