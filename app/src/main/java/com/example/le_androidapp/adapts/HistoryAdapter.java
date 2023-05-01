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
import com.example.le_androidapp.tables.History;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private List<History> historyList;

    public HistoryAdapter(OverviewFragment overviewFragment, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History historyItem = historyList.get(position);

        holder.historyId.setText(String.valueOf(historyItem.historyId));
        holder.totalEvent.setText(String.valueOf(historyItem.totalEvent));
        holder.totalTime.setText(String.valueOf(historyItem.totalTime));
        holder.usedMode.setText(historyItem.usedMode);

        holder.historyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OverviewFragment.class);
                intent.putExtra("ID", historyList.get(holder.getAdapterPosition()).getHistoryId());
                intent.putExtra("Total Event", historyList.get(holder.getAdapterPosition()).getTotalEvent());
                intent.putExtra("Total Time", historyList.get(holder.getAdapterPosition()).getTotalTime());
                intent.putExtra("Mode", historyList.get(holder.getAdapterPosition()).getUsedMode());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView historyId,totalEvent, totalTime, usedMode;
        CardView historyCard;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            historyId = itemView.findViewById(R.id.history_id);
            totalEvent = itemView.findViewById(R.id.total_event);
            totalTime = itemView.findViewById(R.id.total_time);
            usedMode = itemView.findViewById(R.id.used_mode);
            historyCard = itemView.findViewById(R.id.historyCard);

        }
    }
}

