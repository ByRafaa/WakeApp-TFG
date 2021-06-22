package com.example.wakeupapplication.controladores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wakeupapplication.MainActivity;
import com.example.wakeupapplication.R;
import com.example.wakeupapplication.modelos.Alarmas;
import com.example.wakeupapplication.modelos.Grupos;
import com.example.wakeupapplication.vistas.CreateGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolderGroups> {

    ArrayList<Grupos> listGroups;

    DatabaseReference database;

    FirebaseUser currentUser;

    Alarmas alarma;

    Context c;

    public DialogAdapter(ArrayList<Grupos> listGroups, Alarmas alarma){

        this.listGroups = listGroups;
        this.alarma = alarma;

    }

    @Override
    public ViewHolderGroups onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_list,null,false);
        return new ViewHolderGroups(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderGroups holder, int position) {

        holder.asignarDatos(listGroups.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listGroups.get(position).nombre.equals(c.getString(R.string.create_group))){

                    Intent intent = new Intent(c, CreateGroup.class);
                    intent.putExtra("horas",alarma.horas);
                    intent.putExtra("minutos",alarma.minutos);
                    intent.putExtra("mensaje",alarma.mensaje);
                    intent.putExtra("alarmDays",alarma.alarmDays);
                    if(alarma.ringtone != null){
                        intent.putExtra("ringtone",alarma.ringtone.toString());
                    } else {
                        intent.putExtra("ringtone","");
                    }

                    c.startActivity(intent);

                } else {

                    Grupos g = listGroups.get(position);

                    g.setCantidad(g.cantidad+1);

                    String nombreGrupo = listGroups.get(position).nombre;

                    alarma.setGrupo(nombreGrupo);
                    alarma.setUserId(currentUser.getUid());

                    if (alarma.ringtone == null){
                        alarma.ringtone = Uri.parse("");
                    }

                    Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                            .putExtra(AlarmClock.EXTRA_MESSAGE,alarma.mensaje)
                            .putExtra(AlarmClock.EXTRA_HOUR,alarma.horas)
                            .putExtra(AlarmClock.EXTRA_MINUTES,alarma.minutos)
                            .putExtra(AlarmClock.EXTRA_DAYS,alarma.alarmDays)
                            .putExtra(AlarmClock.EXTRA_RINGTONE,alarma.ringtone.toString());

                    alarma.setRingtone(null);

                    database.child("alarmas").child(currentUser.getUid()+"_"+alarma.grupo+"_"+alarma.horas+"-"+alarma.minutos+"_"+alarma.mensaje).setValue(alarma);
                    database.child("grupos").child(currentUser.getUid()+"_"+nombreGrupo).setValue(g);

                    if (intent.resolveActivity(c.getPackageManager())!=null){

                        Intent back = new Intent(c, MainActivity.class);
                        c.startActivity(back);
                        c.startActivity(intent);

                    }


                }



            }
        });

    }

    @Override
    public int getItemCount() {
        return listGroups.size();
    }

    public class ViewHolderGroups extends RecyclerView.ViewHolder {

        TextView nombre;

        /**
         * Inicializa variables usando la view pasada por parámetro
         * @param itemView
         */

        public ViewHolderGroups(@NonNull View itemView) {
            super(itemView);
            c = itemView.getContext();
            database = FirebaseDatabase.getInstance().getReference();
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            nombre = itemView.findViewById(R.id.groupName);
        }

        /**
         * Rellena los items de los layouts con los datos de los grupos que nos conviene
         * @param grupos
         */

        public void asignarDatos(Grupos grupos) {

            nombre.setText(grupos.nombre);

        }
    }
}
