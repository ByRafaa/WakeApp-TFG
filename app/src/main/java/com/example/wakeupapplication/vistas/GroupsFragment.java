package com.example.wakeupapplication.vistas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wakeupapplication.R;
import com.example.wakeupapplication.controladores.DialogAdapter;
import com.example.wakeupapplication.controladores.GroupAdapter;
import com.example.wakeupapplication.modelos.Grupos;
import com.example.wakeupapplication.vistas.CreateAlarm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {

    FloatingActionButton add;
    RecyclerView rvAlarms;
    DatabaseReference database;
    FirebaseUser currentUser;

    public GroupsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate( R.layout.fragment_alarms, container, false );

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.my_alarms_menu));

        add = rootView.findViewById(R.id.addAlarm);
        rvAlarms = rootView.findViewById(R.id.rv_groups);

        database = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateAlarm.class);
                startActivity(intent);
            }
        });

        cargarGrupos(rootView);

        return rootView;

    }

    /**
     *
     * @param view
     *
     * Rellena el arraylist con los grupos que mostrará el recyclerview y se lo asigna al adapter, que hace la conversión y muestra los grupos dentro del recyclerview
     */

    private void cargarGrupos(View view) {

        database.child("grupos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                rvAlarms = view.findViewById(R.id.rv_groups);
                rvAlarms.setLayoutManager(new LinearLayoutManager(getContext()));
                rvAlarms.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

                ArrayList<Grupos> grupos = new ArrayList<Grupos>();

                for (DataSnapshot child : snapshot.getChildren()){

                    if (child.child("userId").getValue().toString().equals(currentUser.getUid())){

                        Grupos g = child.getValue(Grupos.class);

                        grupos.add(g);

                    }

                }

                GroupAdapter adapter = new GroupAdapter(grupos,getActivity().getSupportFragmentManager());
                rvAlarms.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}