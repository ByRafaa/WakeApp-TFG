package com.example.wakeupapplication.vistas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wakeupapplication.R;

public class InfoFragment extends Fragment {

    TextView info;

    Button download;


    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate( R.layout.fragment_info, container, false );

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.info_menu));

        info = rootView.findViewById(R.id.tvInfo);
        download = rootView.findViewById(R.id.bDownloadManual);

        info.setText(getString(R.string.info_view));

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://drive.google.com/file/d/1Ftjrf9Z1YcR3mjLUURH-5Fuat1cV-Vls/view?usp=sharing");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
}