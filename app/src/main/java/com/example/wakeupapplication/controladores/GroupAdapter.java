package com.example.wakeupapplication.controladores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wakeupapplication.R;
import com.example.wakeupapplication.modelos.Grupos;
import com.example.wakeupapplication.vistas.AlarmsActivity;
import com.example.wakeupapplication.vistas.dialogs.ConfirmDeleteGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolderGroups> implements ConfirmDeleteGroup.DialogListener {

    ArrayList<Grupos> listGroups;

    DatabaseReference database;
    FirebaseUser currentUser;

    Context c;

    FragmentManager supportFragmentManager;

    public GroupAdapter(ArrayList<Grupos> listGroups, FragmentManager supportFragmentManager){

        this.listGroups = listGroups;
        this.supportFragmentManager = supportFragmentManager;

    }

    @Override
    public ViewHolderGroups onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_list,null,false);
        return new ViewHolderGroups(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderGroups holder, int position) {

        holder.asignarDatos(listGroups.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(c, AlarmsActivity.class);
                intent.putExtra("groupName",listGroups.get(position).nombre);
                c.startActivity(intent);

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listGroups.get(position).cantidad == 0){

                    ConfirmDeleteGroup dialog = new ConfirmDeleteGroup(listGroups.get(position).nombre);
                    dialog.show(supportFragmentManager,"");

                } else {

                    Toast.makeText(c, c.getString(R.string.cant_delete_group), Toast.LENGTH_SHORT).show();

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
        TextView cantidad;
        ImageButton delete;

        /**
         * Inicializa variables usando la view pasada por parámetro
         * @param itemView
         */

        public ViewHolderGroups(@NonNull View itemView) {
            super(itemView);
            c = itemView.getContext();
            database = FirebaseDatabase.getInstance().getReference();
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            nombre = itemView.findViewById(R.id.tvGroup);
            cantidad = itemView.findViewById(R.id.tvCantidad);
            delete = itemView.findViewById(R.id.bDeleteGroup);
        }

        /**
         * Rellena los items de los layouts con los datos de los grupos que nos conviene
         * @param grupos
         */

        public void asignarDatos(Grupos grupos) {

            nombre.setText(grupos.nombre);
            cantidad.setText(c.getString(R.string.alarms_quantity) + grupos.cantidad);

        }
    }
}
