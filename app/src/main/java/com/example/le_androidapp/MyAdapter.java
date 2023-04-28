package com.example.le_androidapp;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<UserClass> userList;

    public MyAdapter(Context context, List<UserClass> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(userList.get(position).getDataImage()).into(holder.recImg);
        holder.recFirst.setText(userList.get(position).getFirstName());
        holder.recLast.setText(userList.get(position).getLastName());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("Image", userList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("First Name", userList.get(holder.getAdapterPosition()).getFirstName());
                intent.putExtra("Last Name", userList.get(holder.getAdapterPosition()).getLastName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImg;
    TextView recFirst, recLast;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView){
        super(itemView);

        recImg = itemView.findViewById(R.id.recImg);
        recFirst = itemView.findViewById(R.id.recFirst);
        recLast = itemView.findViewById(R.id.recLast);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
