package com.example.wakeupapplication.vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeupapplication.MainActivity;
import com.example.wakeupapplication.R;
import com.example.wakeupapplication.controladores.GroupAdapter;
import com.example.wakeupapplication.modelos.Alarmas;
import com.example.wakeupapplication.modelos.Grupos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CreateGroup extends AppCompatActivity {

    TextView nombre;
    Button crear, back;

    Alarmas alarm;

    boolean creado = false;

    boolean existe = false;

    FirebaseUser currentUser;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        getSupportActionBar().setTitle("WakeApp");

        //HOOKS
        nombre = findViewById(R.id.newGroupName);
        crear = findViewById(R.id.bCreateGroup);
        back = findViewById(R.id.bBackFromCreateGroup);
        //INSTANCIATE
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        alarm = new Alarmas(getIntent().getExtras().getInt("horas"), getIntent().getExtras().getInt("minutos"), getIntent().getExtras().getString("mensaje"), getIntent().getExtras().getIntegerArrayList("alarmDays"), Uri.parse(getIntent().getExtras().getString("ringtone")));
        creado = false;

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nombre.getText().toString().isEmpty()) {

                    Toast.makeText(getApplicationContext(), getString(R.string.group_name_empty), Toast.LENGTH_SHORT).show();

                } else {

                    database.child("temp").setValue("a");

                    creado = false;

                    existe = false;

                    database.child("grupos").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (!creado) {


                                if (snapshot.getValue() == null) {

                                    Grupos grupo = new Grupos(nombre.getText().toString(), 1, currentUser.getUid());

                                    database.child("grupos").child(currentUser.getUid() + "_" + grupo.nombre).setValue(grupo);
                                    alarm.setGrupo(grupo.nombre);
                                    alarm.setUserId(currentUser.getUid());
                                    Uri ringtone = alarm.ringtone;
                                    alarm.setRingtone(null);
                                    database.child("alarmas").child(currentUser.getUid() + "_" + grupo.nombre + "_" + alarm.horas + "-" + alarm.minutos).setValue(alarm);

                                    Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                                            .putExtra(AlarmClock.EXTRA_MESSAGE, alarm.mensaje)
                                            .putExtra(AlarmClock.EXTRA_HOUR, alarm.horas)
                                            .putExtra(AlarmClock.EXTRA_MINUTES, alarm.minutos)
                                            .putExtra(AlarmClock.EXTRA_DAYS, alarm.alarmDays)
                                            .putExtra(AlarmClock.EXTRA_RINGTONE, ringtone.toString());

                                    if (intent.resolveActivity(getPackageManager()) != null) {

                                        Intent back = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(back);
                                        startActivity(intent);

                                    }
                                }

                                for (DataSnapshot child : snapshot.getChildren()) {

                                    if (child.child("userId").getValue().toString().equals(currentUser.getUid()) && child.child("nombre").getValue().toString().equals(nombre.getText().toString())) {

                                        Toast.makeText(getApplicationContext(), getString(R.string.group_exist), Toast.LENGTH_LONG).show();

                                        existe = true;

                                    }

                                }

                                if (!existe) {

                                    Grupos grupo = new Grupos(nombre.getText().toString(), 1, currentUser.getUid());

                                    database.child("grupos").child(currentUser.getUid() + "_" + grupo.nombre).setValue(grupo);
                                    alarm.setGrupo(grupo.nombre);
                                    alarm.setUserId(currentUser.getUid());
                                    Uri ringtone = alarm.ringtone;
                                    alarm.setRingtone(null);
                                    database.child("alarmas").child(currentUser.getUid() + "_" + grupo.nombre + "_" + alarm.horas + "-" + alarm.minutos+"_"+alarm.mensaje).setValue(alarm);

                                    Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                                            .putExtra(AlarmClock.EXTRA_MESSAGE, alarm.mensaje)
                                            .putExtra(AlarmClock.EXTRA_HOUR, alarm.horas)
                                            .putExtra(AlarmClock.EXTRA_MINUTES, alarm.minutos)
                                            .putExtra(AlarmClock.EXTRA_DAYS, alarm.alarmDays)
                                            .putExtra(AlarmClock.EXTRA_RINGTONE, ringtone.toString());

                                    if (intent.resolveActivity(getPackageManager()) != null) {

                                        Intent back = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(back);
                                        startActivity(intent);
                                    }
                                }

                                creado = true;

                            }
                            database.child("temp").removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
