package com.example.wakeupapplication.vistas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeupapplication.R;

public class ContactFragment extends Fragment {

    TextView mail,subject,body;
    ImageButton send;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate( R.layout.fragment_contact, container, false );

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.contact_menu));

        mail = rootView.findViewById(R.id.userMail);
        subject = rootView.findViewById(R.id.userSubject);
        body = rootView.findViewById(R.id.userBody);
        send = rootView.findViewById(R.id.sendMailButton);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mail.getText().toString().isEmpty() || subject.getText().toString().isEmpty() || body.getText().toString().isEmpty()){

                    Toast.makeText(getContext(),getString(R.string.empty_fields),Toast.LENGTH_LONG).show();

                } else {

                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    //intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"rafael.escuderoortega@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT,subject.getText().toString());
                    intent.putExtra(Intent.EXTRA_TEXT,body.getText().toString());
                    startActivity(intent.createChooser(intent,getString(R.string.app_selector)));

                }

            }
        });

        return rootView;
    }
}