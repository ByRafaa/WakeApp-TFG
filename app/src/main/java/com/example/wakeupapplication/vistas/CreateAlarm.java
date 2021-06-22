package com.example.wakeupapplication.vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.wakeupapplication.R;
import com.example.wakeupapplication.modelos.Alarmas;
import com.example.wakeupapplication.vistas.dialogs.AsignGroupDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;

public class CreateAlarm extends AppCompatActivity implements AsignGroupDialog.DialogListener {

    NumberPicker hours, minutes;
    TextView message, ringtone;
    ImageButton browseSong;
    ImageView mute;
    FloatingActionButton create, back;
    ToggleButton mo, tu, we, th, fr, sa, su;

    FirebaseUser currentUser;

    boolean creada = false;

    boolean existe = false;

    DatabaseReference database;

    Uri song;

    ArrayList<Integer> alarmDays = new ArrayList<Integer>();

    public static Alarmas alarma = new Alarmas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        getSupportActionBar().setTitle("");

        //HOOKS
        hours = findViewById(R.id.npHours);
        minutes = findViewById(R.id.npMinutes);
        message = findViewById(R.id.alarmMessage);
        ringtone = findViewById(R.id.alarmRingtone);
        create = findViewById(R.id.bCreateAlarm);
        back = findViewById(R.id.bBackFromCreateAlarm);
        browseSong = findViewById(R.id.alarmRingtoneBrowser);
        mute = findViewById(R.id.alarmRingtoneMuted);
        mo = findViewById(R.id.tbMonday);
        tu = findViewById(R.id.tbTuesday);
        we = findViewById(R.id.tbWednesday);
        th = findViewById(R.id.tbThursday);
        fr = findViewById(R.id.tbFriday);
        sa = findViewById(R.id.tbSaturday);
        su = findViewById(R.id.tbSunday);

        //INSTANCIATE

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        creada = false;

        hours.setMinValue(0);
        hours.setMaxValue(23);

        hours.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        minutes.setMinValue(0);
        minutes.setMaxValue(59);

        minutes.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database.child("temp").setValue("a");

                creada = false;

                existe = false;

                database.child("alarmas").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!creada) {


                            if (snapshot.getValue() == null) {

                                AsignGroupDialog dialog = new AsignGroupDialog();
                                dialog.show(getSupportFragmentManager(), "Dialogo");

                                setAlarm(message.getText().toString(), hours.getValue(), minutes.getValue());

                            }


                            for (DataSnapshot child : snapshot.getChildren()) {


                                if (Integer.parseInt(child.child("horas").getValue().toString()) == hours.getValue() && Integer.parseInt(child.child("minutos").getValue().toString()) == minutes.getValue() && child.child("mensaje").getValue().toString().equals(message.getText().toString()) && child.child("userId").getValue().toString().equals(currentUser.getUid())) {


                                    Toast.makeText(getApplicationContext(), getString(R.string.alarm_exist), Toast.LENGTH_LONG).show();

                                    existe = true;

                                }

                            }

                            if (!existe){

                                AsignGroupDialog dialog = new AsignGroupDialog();
                                dialog.show(getSupportFragmentManager(), "Dialogo");

                                setAlarm(message.getText().toString(), hours.getValue(), minutes.getValue());

                            }

                            creada = true;

                        }

                        database.child("temp").removeValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        browseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent.createChooser(intent, getString(R.string.app_selector)), 10);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (song == null) {

                    song = Uri.parse("silent");

                    ringtone.setText(getString(R.string.ringtone_muted));

                } else if (song.toString().equals("silent")) {

                    song = Uri.parse("content://settings/system/alarm_alert");

                    ringtone.setText(getString(R.string.ringtone_default));

                } else {

                    song = Uri.parse("silent");

                    ringtone.setText(getString(R.string.ringtone_muted));

                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            song = data.getData();

            ringtone.setText(getFileName(song));

        }

    }

    /**
     * A partir de la uri pasada por parámetro, encuentra el nombre del archivo al que conecta esa uri y lo asigna al campo de texto
     * @param uri
     * @return
     */

    private String getFileName(Uri uri) {

        String result = null;

        if (uri.getScheme().equals("content")) {

            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            try {
                if (cursor != null && cursor.moveToFirst()) {

                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }
            } finally {
                //cursor.close();
            }
        }

        if (result == null) {

            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }

        }

        return result;

    }

    /**
     * Crea la alarma con los parámetros, comprobando los días que están activados y que se repetirán en el caso en el que lo estén.
     * @param msg
     * @param hour
     * @param minute
     */

    public void setAlarm(String msg, int hour, int minute) {

        if (mo.isChecked()) {

            alarmDays.add(Calendar.MONDAY);

        }

        if (tu.isChecked()) {

            alarmDays.add(Calendar.TUESDAY);

        }

        if (we.isChecked()) {

            alarmDays.add(Calendar.WEDNESDAY);

        }

        if (th.isChecked()) {

            alarmDays.add(Calendar.THURSDAY);

        }

        if (fr.isChecked()) {

            alarmDays.add(Calendar.FRIDAY);

        }

        if (sa.isChecked()) {

            alarmDays.add(Calendar.SATURDAY);

        }

        if (su.isChecked()) {
            alarmDays.add(Calendar.SUNDAY);
        }

        if (song == null) {
            alarma = new Alarmas(hour, minute, msg, alarmDays);
        } else {
            alarma = new Alarmas(hour, minute, msg, alarmDays, song);
        }


    }

}