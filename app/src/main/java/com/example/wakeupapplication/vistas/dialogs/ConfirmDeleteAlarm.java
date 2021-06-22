package com.example.wakeupapplication.vistas.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.wakeupapplication.MainActivity;
import com.example.wakeupapplication.R;
import com.example.wakeupapplication.modelos.Grupos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfirmDeleteAlarm extends AppCompatDialogFragment {

    DatabaseReference database;
    FirebaseUser currentUser;

    boolean editado = false;

    String groupName,mensaje;
    int alarmHour,alarmMinute;

    private DialogListener listener;

    public ConfirmDeleteAlarm(String groupName,int alarmHour,int alarmMinute,String mensaje){

        this.groupName = groupName;
        this.alarmHour = alarmHour;
        this.alarmMinute = alarmMinute;
        this.mensaje = mensaje;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_remove_group,null);

        database = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        editado = false;

        builder.setView(view).setTitle(getString(R.string.alarm_dialog_title)).setMessage(getString(R.string.alarm_dialog_message)).setNegativeButton(getString(R.string.dialog_deny), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(getString(R.string.dialog_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent eliminarAlarma = new Intent(AlarmClock.ACTION_DISMISS_ALARM)
                        .putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_LABEL);

                database.child("alarmas").child(currentUser.getUid()+"_"+groupName+"_"+alarmHour+"-"+alarmMinute+"_"+mensaje).removeValue();

                Toast.makeText(getContext(), getString(R.string.alarm_dialog_deleted), Toast.LENGTH_SHORT).show();

                database.child("grupos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot child : snapshot.getChildren()) {

                            if (!editado) {

                                if (child.child("nombre").getValue().toString().equals(groupName) && child.child("userId").getValue().toString().equals(currentUser.getUid())) {

                                    Grupos g = child.getValue(Grupos.class);

                                    g.cantidad = g.cantidad - 1;

                                    database.child("grupos").child(currentUser.getUid() + "_" + groupName).setValue(g);

                                    editado = true;

                                }

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent back = new Intent(getContext(), MainActivity.class);
                startActivity(back);

                startActivity(eliminarAlarma);

                Toast.makeText(getContext(), getString(R.string.alarm_search), Toast.LENGTH_SHORT).show();

            }
        });

        return builder.create();

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

    public interface DialogListener{}

}
