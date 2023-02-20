package com.example.le_androidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class UserAdapter extends ArrayAdapter<AppUser> {
    public UserAdapter(Context context, List<AppUser> appUserList)
    {
        super(context, 0, appUserList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        AppUser appUser = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_cell, parent, false);

        TextView name = convertView.findViewById(R.id.cellName);
        TextView count = convertView.findViewById(R.id.cellCount);

        name.setText(appUser.getUsername());
        count.setText(String.valueOf(appUser.getBadCount()));

        return convertView;
    }
}
