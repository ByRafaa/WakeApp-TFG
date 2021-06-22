package com.example.wakeupapplication.vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeupapplication.R;
import com.example.wakeupapplication.modelos.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    Button register,back;
    TextView email,pass,nick;

    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("WakeApp");

        //HOOKS

        register = findViewById(R.id.bRegistrar);
        back = findViewById(R.id.bBackFromRegister);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        nick = findViewById(R.id.nick);
        database = FirebaseDatabase.getInstance().getReference().child("users");

        setup();

    }

    /**
     * Asigna los listener a los botones
     */

    private void setup() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().isEmpty() || pass.getText().toString().isEmpty() || nick.getText().toString().isEmpty()) {

                    Toast.makeText(getApplicationContext(), getText(R.string.empty_fields), Toast.LENGTH_LONG).show();
                } else {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                onBackPressed();

                                Usuarios u = new Usuarios(email.getText().toString(), nick.getText().toString());

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                database.child(user.getUid()).setValue(u);

                                Toast.makeText(getApplicationContext(), getText(R.string.registered), Toast.LENGTH_LONG).show();

                            } else {

                                Toast.makeText(getApplicationContext(), getString(R.string.register_pass_error), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

            }
        });

    }
}