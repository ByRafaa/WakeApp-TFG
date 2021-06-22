package com.example.wakeupapplication.vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.wakeupapplication.R;
import com.example.wakeupapplication.controladores.AlarmsAdapter;
import com.example.wakeupapplication.controladores.GroupAdapter;
import com.example.wakeupapplication.modelos.Alarmas;
import com.example.wakeupapplication.modelos.Grupos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlarmsActivity extends AppCompatActivity {

    FloatingActionButton back,add;
    RecyclerView rvAlarms;

    FirebaseUser currentUser;
    DatabaseReference database;

    String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);

        Bundle b = getIntent().getExtras();

        groupName = b.getString("groupName");

        getSupportActionBar().setTitle(groupName);

        //HOOKS
        back = findViewById(R.id.goBackFromAlarms);
        add = findViewById(R.id.addAlarmFromAlarms);

        //INSTANCIATE
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();

        cargarAlarmas();

        setup();

    }

    /**
     * Rellena el recyclerview de alarmas con el arraylist que contiene todas las alarmas de la base de datos que cumplen ciertas condiciones
     */
    private void cargarAlarmas() {

        database.child("alarmas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                rvAlarms = findViewById(R.id.rv_alarm);
                rvAlarms.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvAlarms.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

                ArrayList<Alarmas> alarmas = new ArrayList<Alarmas>();

                for (DataSnapshot child : snapshot.getChildren()){

                    if (child.child("grupo").getValue().toString().equals(groupName)){

                        Alarmas a = child.getValue(Alarmas.class);

                        alarmas.add(a);

                    }

                }

                AlarmsAdapter adapter = new AlarmsAdapter(alarmas);
                rvAlarms.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setup() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateAlarm.class);
                startActivity(intent);
            }
        });

    }
}