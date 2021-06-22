package com.example.wakeupapplication.vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeupapplication.MainActivity;
import com.example.wakeupapplication.R;
import com.example.wakeupapplication.modelos.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Verify extends AppCompatActivity {

    Button verify,edit,back;

    TextView code,number;

    String tNumber,tNick;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        getSupportActionBar().setTitle("WakeApp");

        //HOOKS

        verify = findViewById(R.id.verify);
        edit = findViewById(R.id.editNumber);
        code = findViewById(R.id.code);
        back = findViewById(R.id.bBackFromVerify);
        number = findViewById(R.id.numberText);

        String storedVerificationId = getIntent().getStringExtra("storedVerificationId");

        tNumber = getIntent().getStringExtra("number");
        tNick = getIntent().getStringExtra("nick");

        database = FirebaseDatabase.getInstance().getReference().child("users");

        number.setText(getString(R.string.verification_sent) + tNumber);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tCode = code.getText().toString().trim();

                if (!tCode.isEmpty()){

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(storedVerificationId.toString(),tCode);
                    signInWithPhoneAuthCredential(credential);

                } else {
                    Toast.makeText(getApplicationContext(),getString(R.string.verification_empty), Toast.LENGTH_LONG).show();
                }

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     *
     * @param credential
     *
     * Este método se llama cuando quieres iniciar sesión con las credenciales del número de teléfono
     *
     * Comprueba si las credenciales pasadas como parámetro son correctas y, si lo son, crea un usuario en la base de datos.
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Usuarios u = new Usuarios(Integer.parseInt(tNumber),tNick);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    database.child(user.getUid()).setValue(u);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(),getString(R.string.verification_error),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}