package com.cookandroid.haje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    TextView inputID;
    TextView inputPW;
    ImageButton btnSignIn;
    ImageButton btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });




    }

    public void login(){
        inputID = findViewById(R.id.inputID);
        inputPW = findViewById(R.id.inputPW);

        if(inputID.getText().toString().isEmpty() || inputPW.getText().toString().isEmpty()){
            Toast.makeText(this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_LONG).show();
        }

        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(inputID.getText().toString(), inputPW.getText().toString())
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       Toast.makeText(this, "로그인 되었습니다", Toast.LENGTH_LONG).show();
                       Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
                       intent.putExtra("email", inputID.getText().toString());
                       startActivity(intent);
                  }
                   else{
                       Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                   }
                });
    }

    public void onShowClick(View v){
        Intent intent = new Intent(getApplicationContext(), ShowRideActivity.class);
        startActivity(intent);
    }


}