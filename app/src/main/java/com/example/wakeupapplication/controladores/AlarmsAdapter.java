package com.example.wakeupapplication.controladores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wakeupapplication.R;
import com.example.wakeupapplication.modelos.Alarmas;
import com.example.wakeupapplication.vistas.VistaAlarmas;

import java.util.ArrayList;

public class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.ViewHolderAlarms> {

    ArrayList<Alarmas> listAlarms;

    Context c;

    public AlarmsAdapter(ArrayList<Alarmas> listAlarms){

        this.listAlarms = listAlarms;

    }

    @Override
    public ViewHolderAlarms onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm_list,null,false);
        return new AlarmsAdapter.ViewHolderAlarms(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAlarms holder, int position) {

        holder.asignarDatos(listAlarms.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(c, VistaAlarmas.class);
                intent.putExtra("hora",listAlarms.get(position).horas);
                intent.putExtra("minuto",listAlarms.get(position).minutos);
                intent.putExtra("title",listAlarms.get(position).mensaje);
                intent.putExtra("alarmDays",listAlarms.get(position).alarmDays);
                intent.putExtra("groupName",listAlarms.get(position).grupo);
                c.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listAlarms.size();
    }

    public class ViewHolderAlarms extends RecyclerView.ViewHolder {

        TextView horas,minutos,title;
        ToggleButton monday,tuesday,wednesday,thursday,friday,saturday,sunday;

        /**
         * Inicializa variables usando la view pasada por parámetro
         * @param itemView
         */

        public ViewHolderAlarms(@NonNull View itemView) {
            super(itemView);
            c = itemView.getContext();
            horas = itemView.findViewById(R.id.tvHoras);
            minutos = itemView.findViewById(R.id.tvMinutos);
            title = itemView.findViewById(R.id.tvTitle);
            monday = itemView.findViewById(R.id.monday);
            tuesday = itemView.findViewById(R.id.tuesday);
            wednesday = itemView.findViewById(R.id.wednesday);
            thursday = itemView.findViewById(R.id.thursday);
            friday = itemView.findViewById(R.id.friday);
            saturday = itemView.findViewById(R.id.saturday);
            sunday = itemView.findViewById(R.id.sunday);

        }

        /**
         * Rellena los items de los layouts con los datos de los grupos que nos conviene
         * @param alarmas
         */

        public void asignarDatos(Alarmas alarmas) {

            if (alarmas.horas < 10){
                horas.setText("0" + alarmas.horas);
            } else {
                horas.setText("" + alarmas.horas);
            }

            if (alarmas.minutos < 10){
                minutos.setText("0" + alarmas.minutos);
            } else {
                minutos.setText("" + alarmas.minutos);
            }

            title.setText("" + alarmas.mensaje);

            if (alarmas.alarmDays == null){

            } else {

                if (alarmas.alarmDays.contains(1)){

                    sunday.setChecked(true);

                } if (alarmas.alarmDays.contains(2)){

                    monday.setChecked(true);

                } if (alarmas.alarmDays.contains(3)){

                    tuesday.setChecked(true);

                } if (alarmas.alarmDays.contains(4)){

                    wednesday.setChecked(true);

                } if (alarmas.alarmDays.contains(5)){

                    thursday.setChecked(true);

                } if (alarmas.alarmDays.contains(6)){

                    friday.setChecked(true);

                } if (alarmas.alarmDays.contains(7)){

                    saturday.setChecked(true);

                }

            }
        }
    }

}
