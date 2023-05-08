package com.example.le_androidapp.adapts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.le_androidapp.OverviewFragment;
import com.example.le_androidapp.R;
import com.example.le_androidapp.tables.ReadData;

import java.util.List;

public class ReadAdapter extends RecyclerView.Adapter<ReadViewHolder> {

    private final Fragment historyFragment;
    private final List<ReadData> readDataList;
    private LayoutInflater inflater;

    public ReadAdapter(Fragment historyFragment, List<ReadData> readDataList) {
        this.historyFragment = historyFragment;
        this.readDataList = readDataList;
    }

    @NonNull
    @Override
    public ReadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        return new ReadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadViewHolder holder, int position) {
        ReadData dataItem = readDataList.get(position);
        holder.totalEvent.setText(String.valueOf(dataItem.totalEvent));
        holder.totalTime.setText(String.valueOf(dataItem.totalTime));
        holder.dataCard.setOnClickListener(v -> {
            Intent intent = new Intent(historyFragment.requireContext(), OverviewFragment.class);
            intent.putExtra("Total Event", readDataList.get(holder.getAdapterPosition()).getTotalEvent());
            intent.putExtra("Total Time", readDataList.get(holder.getAdapterPosition()).getTotalTime());
            historyFragment.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return readDataList.size();
    }
}
class ReadViewHolder extends RecyclerView.ViewHolder {
    TextView totalEvent, totalTime;
    CardView dataCard;

    public ReadViewHolder(@NonNull View itemView) {
        super(itemView);


        totalEvent = itemView.findViewById(R.id.total_event);
        totalTime = itemView.findViewById(R.id.total_time);
        dataCard = itemView.findViewById(R.id.dataCard);
    }
}


