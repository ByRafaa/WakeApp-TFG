package com.example.wakeupapplication.vistas.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wakeupapplication.R;
import com.example.wakeupapplication.controladores.DialogAdapter;
import com.example.wakeupapplication.modelos.Alarmas;
import com.example.wakeupapplication.modelos.Grupos;
import com.example.wakeupapplication.vistas.CreateAlarm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AsignGroupDialog extends AppCompatDialogFragment {

    private RecyclerView groups;
    private ArrayList<Grupos> grupos;
    private DialogListener listener;

    private DatabaseReference database;
    private FirebaseUser currentUser;

    public AsignGroupDialog(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_asign,null);

        builder.setView(view).setTitle(getString(R.string.dialog_title)).setMessage(getString(R.string.dialog_message)).setNegativeButton(getString(R.string.dialog_deny), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        database = FirebaseDatabase.getInstance().getReference();

        cargarGrupos(view);

        return builder.create();
    }

    private void cargarGrupos(View view) {

        database.child("grupos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                groups = view.findViewById(R.id.rvGroups);
                groups.setLayoutManager(new LinearLayoutManager(getContext()));
                groups.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

                grupos = new ArrayList<Grupos>();

                for (DataSnapshot child : snapshot.getChildren()){

                    if (child.child("userId").getValue().toString().equals(currentUser.getUid())){

                        Grupos g = child.getValue(Grupos.class);

                        grupos.add(g);

                    }

                }
                    grupos.add(new Grupos(getString(R.string.create_group),0,currentUser.getUid()));

                    DialogAdapter adapter = new DialogAdapter(grupos, CreateAlarm.alarma);
                    groups.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface DialogListener{

    }
}
