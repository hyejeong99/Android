package com.cookandroid.haje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SignupActivity3 extends AppCompatActivity {
    EditText PassName;
    EditText PassBirth;
    EditText PassBirth2;
    EditText PassNumber;
    EditText PassSecurity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup3);

        hideSystemUI();

        ImageButton btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                genderauth();
            }

        });
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void genderauth() {

        PassName = findViewById(R.id.PassName);
        PassBirth = findViewById(R.id.PassBirth);
        PassBirth2 = findViewById(R.id.PassBirth2);
        PassNumber = findViewById(R.id.PassNumber);
        PassSecurity = findViewById(R.id.PassSecurity);

        if (PassName.getText().toString().isEmpty() ||
                PassBirth.getText().toString().isEmpty() ||
                PassBirth2.getText().toString().isEmpty() ||
                PassNumber.getText().toString().isEmpty() ||
                PassSecurity.getText().toString().isEmpty()) {
            Toast.makeText(SignupActivity3.this, "비어있는 정보를 입력해주세요", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(SignupActivity3.this, "여성인증 완료되었습니다.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();

        }


    }


}




