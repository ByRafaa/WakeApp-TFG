package com.example.wakeupapplication.vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.wakeupapplication.MainActivity;
import com.example.wakeupapplication.R;
import com.example.wakeupapplication.controladores.DialogAdapter;
import com.example.wakeupapplication.modelos.Grupos;
import com.example.wakeupapplication.vistas.dialogs.ConfirmDeleteAlarm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VistaAlarmas extends AppCompatActivity implements ConfirmDeleteAlarm.DialogListener{

    NumberPicker hour, minute;
    ToggleButton monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    FloatingActionButton delete,back,activate;
    TextView title;
    ArrayList<Integer> alarmDays;

    String groupName;

    boolean editado = false;

    DatabaseReference database;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_alarmas);
        getSupportActionBar().setTitle("");

        //HOOKS
        hour = findViewById(R.id.vHours);
        minute = findViewById(R.id.vMinutes);
        monday = findViewById(R.id.vMonday);
        tuesday = findViewById(R.id.vTuesday);
        wednesday = findViewById(R.id.vWednesday);
        thursday = findViewById(R.id.vThursday);
        friday = findViewById(R.id.vFriday);
        saturday = findViewById(R.id.vSaturday);
        sunday = findViewById(R.id.vSunday);
        delete = findViewById(R.id.bRemoveAlarm);
        back = findViewById(R.id.bBackFromVistaAlarmas);
        activate = findViewById(R.id.bActivateAlarm);
        title = findViewById(R.id.vMessage);
        //INSTANTIATE
        database = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        editado = false;

        alarmDays = new ArrayList<Integer>();

        Bundle b = getIntent().getExtras();

        groupName = b.getString("groupName");

        hour.setEnabled(false);

        hour.setMinValue(0);
        hour.setMaxValue(23);

        hour.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        minute.setEnabled(false);

        minute.setMinValue(0);
        minute.setMaxValue(59);

        minute.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        hour.setValue(b.getInt("hora"));
        minute.setValue(b.getInt("minuto"));


        title.setEnabled(false);
        title.setText(b.getString("title"));

        alarmDays = b.getIntegerArrayList("alarmDays");

        if (alarmDays == null) {

        } else {

            if (alarmDays.contains(1)) {

                sunday.setChecked(true);

            }
            if (alarmDays.contains(2)) {

                monday.setChecked(true);

            }
            if (alarmDays.contains(3)) {

                tuesday.setChecked(true);

            }
            if (alarmDays.contains(4)) {

                wednesday.setChecked(true);

            }
            if (alarmDays.contains(5)) {

                thursday.setChecked(true);

            }
            if (alarmDays.contains(6)) {

                friday.setChecked(true);

            }
            if (alarmDays.contains(7)) {

                saturday.setChecked(true);

            }

        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmDeleteAlarm dialog = new ConfirmDeleteAlarm(groupName,hour.getValue(),minute.getValue(),b.getString("title"));
                dialog.show(getSupportFragmentManager(),"alarma");

            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent buscarAlarma = new Intent(AlarmClock.ACTION_DISMISS_ALARM)
                        .putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_LABEL)
                        .putExtra(AlarmClock.EXTRA_MESSAGE,title.getText().toString());
                startActivity(buscarAlarma);

            }
        });

    }
}