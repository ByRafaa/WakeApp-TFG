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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SMS extends AppCompatActivity {

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    FirebaseUser currentUser;

    Button sms,back;

    TextView tNumber,tNick;

    String storedVerificationId;

    PhoneAuthProvider.ForceResendingToken resendToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_m_s);
        getSupportActionBar().setTitle("WakeApp");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        sms = findViewById(R.id.sendSMS);
        back = findViewById(R.id.bBackFromSMS);
        tNumber = findViewById(R.id.number);
        tNick = findViewById(R.id.numberNick);

        setup();
    }

    public void setup(){

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tNumber.getText().toString().isEmpty() || tNick.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(),getString(R.string.empty_fields),Toast.LENGTH_LONG).show();

                }

                login();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                storedVerificationId = s;
                resendToken = forceResendingToken;
                Intent intent = new Intent(getApplicationContext(), Verify.class);
                intent.putExtra("storedVerificationId",storedVerificationId);
                intent.putExtra("nick",tNick.getText().toString());
                intent.putExtra("number",tNumber.getText().toString());
                startActivity(intent);
            }
        };
    }

    /**
     * Envía el código de verificación al número de teléfono indicado anteriormente.
     */

    private void login() {

        String number = tNumber.getText().toString();

        if (!number.isEmpty()) {

            number = "+34" + number;
            sendVerificationCode(number);
        }
    }

    private void sendVerificationCode(String number){

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance()).setPhoneNumber(number).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(callbacks).build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

}