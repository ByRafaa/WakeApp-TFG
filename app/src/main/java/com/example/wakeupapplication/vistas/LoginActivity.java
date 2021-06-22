package com.example.wakeupapplication.vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button login, register, google, mobile;

    private final int GOOGLE_SIGN_IN = 100;

    TextView email, pass;

    DatabaseReference database;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("WakeApp");

        //HOOKS
        login = findViewById(R.id.login);
        register = findViewById(R.id.registro);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        google = findViewById(R.id.google);
        mobile = findViewById(R.id.telefono);

        database = FirebaseDatabase.getInstance().getReference();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        setup();

    }

    /**
     * Asigna los listener a los botones y controla la conexión a la base de datos.
     */

    private void setup() {

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().isEmpty() && pass.getText().toString().isEmpty()) {

                    Toast.makeText(getApplicationContext(), getString(R.string.login_error_message), Toast.LENGTH_LONG).show();

                } else {

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                                email.setText("");
                                pass.setText("");

                                Toast.makeText(getApplicationContext(), getString(R.string.login_successful), Toast.LENGTH_LONG).show();

                            } else {

                                email.setText("");
                                pass.setText("");

                                Toast.makeText(getApplicationContext(), getText(R.string.login_error), Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }

            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                google.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

                        GoogleSignInClient googleClient = GoogleSignIn.getClient(getApplicationContext(), gso);

                        googleClient.signOut();

                        Intent signInIntent = googleClient.getSignInIntent();
                        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);

                    }
                });
            }
        });

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SMS.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {

            GoogleSignInAccount acc = null;

            try {

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                acc = task.getResult(ApiException.class);

            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            if (acc != null) {

                AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);

                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user.getUid().equals(database.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                    Toast.makeText(getApplicationContext(), getString(R.string.login_successful), Toast.LENGTH_LONG).show();

                                } else {

                                    Usuarios usuario = new Usuarios(user.getEmail(), user.getDisplayName());

                                    database.child("users").child(user.getUid()).setValue(usuario);

                                    Intent intent = new Intent(getApplicationContext(), AlarmsActivity.class);
                                    startActivity(intent);

                                    Toast.makeText(getApplicationContext(), getText(R.string.login_successful), Toast.LENGTH_LONG).show();

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(getApplicationContext(), getString(R.string.google_error), Toast.LENGTH_LONG).show();

                            }
                        }))) ;

                    }
                });

            }

        }

    }

    /**
     * Anula el onBackPressed para evitar problemas.
     */
    @Override
    public void onBackPressed() {
    }

}