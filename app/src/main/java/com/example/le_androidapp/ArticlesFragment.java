package com.example.le_androidapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class ArticlesFragment extends Fragment {

    Button article1Button;
    Button article2Button;
    Button article3Button;
    Button article4Button;
    Button emergencyHotlinesButton;
    Button triviaButton;

    public ArticlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles, container, false);

        article1Button = (Button) view.findViewById(R.id.article1);
        article1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.usa.edu/blog/how-to-improve-posture/"));
                startActivity(intent);
            }
        });

        article2Button = (Button) view.findViewById(R.id.article2);
        article2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.health.harvard.edu/staying-healthy/is-it-too-late-to-save-your-posture"));
                startActivity(intent);
            }
        });

        article3Button = (Button) view.findViewById(R.id.article3);
        article3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://my.clevelandclinic.org/health/treatments/21033-chiropractic-adjustment"));
                startActivity(intent);
            }
        });

        article4Button = (Button) view.findViewById(R.id.article4);
        article4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.medicalnewstoday.com/articles/160645"));
                startActivity(intent);
            }
        });

        emergencyHotlinesButton = (Button) view.findViewById(R.id.emergency);
        emergencyHotlinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImageDialogueActivity.class);
                startActivity(intent);
            }
        });

        triviaButton = (Button) view.findViewById(R.id.trivia);
        triviaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://alignedmodernhealth.com/8-facts-know-posture/"));
                startActivity(intent);
            }
        });

        return view;
    }
}