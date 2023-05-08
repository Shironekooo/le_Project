package com.example.le_androidapp.adapts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.le_androidapp.OverviewFragment;
import com.example.le_androidapp.R;
import com.example.le_androidapp.tables.ReadData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class ReadAdapter extends FirebaseRecyclerAdapter<ReadData, ReadViewHolder> {

    private final Context context;

    public ReadAdapter(@NonNull FirebaseRecyclerOptions<ReadData> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ReadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        return new ReadViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReadViewHolder holder, int position, @NonNull ReadData model) {
        holder.totalEvent.setText(String.valueOf(model.getTotalEvent()));
        holder.totalTime.setText(String.valueOf(model.getTotalTime()));
        holder.dataCard.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), OverviewFragment.class);
            intent.putExtra("Total Event", model.getTotalEvent());
            intent.putExtra("Total Time", model.getTotalTime());
            holder.itemView.getContext().startActivity(intent);
        });
    }



    /*@Override
    public int getItemCount() {
        return readDataList.size();
    }*/
}



