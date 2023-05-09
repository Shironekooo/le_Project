package com.example.le_androidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;


// TODO change to be able to show history or actionable data not raw data

@AndroidEntryPoint
public class OverviewFragment extends Fragment {

    Button dbBtn;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        dbBtn = view.findViewById(R.id.dbBtn);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("NOTICE");
        builder.setMessage("By proceeding, you hereby adhere to the Data Privacy Act of 2012 and swear, under oath that you are a medical"+
                " health professional who is allowed to access patient records.");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://appdb-db0d2-default-rtdb.asia-southeast1.firebasedatabase.app/"));
                startActivity(intent);
            }
        });

        builder.create();


        dbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });



        return view;
    }
}