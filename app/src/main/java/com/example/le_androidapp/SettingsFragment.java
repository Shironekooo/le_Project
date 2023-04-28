package com.example.le_androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SettingsFragment extends Fragment {

    Switch phoneVibrateSwitch;

    Button exportDownload;
    Button supportDevs;

    SharedPreferences sp;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        // Setting to turn phone vibration on or off
        phoneVibrateSwitch = (Switch) view.findViewById(R.id.setting_switch2);
        phoneVibrateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp = getActivity().getSharedPreferences("sharedData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if(phoneVibrateSwitch.isChecked()) {
                    editor.putInt("phoneVibrate", 1);
                    v.vibrate(250);
                    //Toast.makeText(getActivity(), "Phone Vibration Turned On", Toast.LENGTH_SHORT).show();
                }
                else {
                    editor.putInt("phoneVibrate", 2);
                    //Toast.makeText(getActivity(), "Phone Vibration Turned Off", Toast.LENGTH_SHORT).show();
                }
                editor.commit();
            }
        });

        exportDownload = (Button) view.findViewById(R.id.export_button);
        exportDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePDF();
            }
        });

        supportDevs = (Button) view.findViewById(R.id.dev_support);
        supportDevs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "The Devs Need Money", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.patreon.com"));
                startActivity(intent);
            }
        });
        return view;
    }

    // TODO
    private void generatePDF() {
        String fileName = "PosturePerfectData.pdf";
        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

        try {
            // Initialize PDF writer
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Initialize PDF document
            Document document = new Document(pdfDoc);

            // Sample text output for PDF
            // TODO edit to output data later
            String defaultText = "SamplePDF";
            Paragraph paragraph1 = new Paragraph(defaultText);

            // Adding to PDF
            document.add(paragraph1);

            // Close document
            document.close();

            Toast.makeText(getActivity(), "PDF exported to Downloads Folder", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "PDF generation failed", Toast.LENGTH_SHORT).show();
        }
    }
}