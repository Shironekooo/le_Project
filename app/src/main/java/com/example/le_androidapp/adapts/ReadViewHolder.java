package com.example.le_androidapp.adapts;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.le_androidapp.R;
import com.example.le_androidapp.tables.ReadData;

public class ReadViewHolder extends RecyclerView.ViewHolder {
    TextView totalEvent, totalTime;
    CardView dataCard;

    public ReadViewHolder(@NonNull View itemView) {
        super(itemView);

        totalEvent = itemView.findViewById(R.id.total_event);
        totalTime = itemView.findViewById(R.id.total_time);
        dataCard = itemView.findViewById(R.id.dataCard);
    }

    public void bind(ReadData model) {
        totalEvent.setText(String.valueOf(model.getTotalEvent()));
        totalTime.setText(String.valueOf(model.getTotalTime()));
    }
}
