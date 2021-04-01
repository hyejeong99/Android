package com.example.guru_oneulmaru

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Boolean

class LoginActivity : AppCompatActivity() {//로그인 activity
    private var auth : FirebaseAuth?=null
    private lateinit var ed:SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        //change 전에 자동 저장 버튼을 눌렀을 때
        val pref=getSharedPreferences("pref", Context.MODE_PRIVATE)
        ed=pref.edit()
        //이메일, 비밀번호 불러오기
        txtEmail_login.setText(pref.getString("email", ""))
        txtPassword_login.setText(pref.getString("password", ""))
        checkLogin.setChecked(pref.getBoolean("check", false))

        btnjoin.setOnClickListener {//회원가입 버튼
            val intent= Intent(this, JoinActivity::class.java)//join 인텐트로 넘겨주기
            startActivity(intent)
        }
        btnLogin.setOnClickListener {//로그인 버튼

            if(checkLogin.isChecked){//자동저장 체크박스 눌렀을 때
                //이메일,비밀번호 저장하기
                ed.putString("email", txtEmail_login.text.toString())
                ed.putString("password", txtPassword_login.text.toString())
                ed.putBoolean("check", true)
                ed.apply()
            }else{//체크박스 안눌렀을 때
                ed.putString("email", "")
                ed.putString("password", "")
                ed.putBoolean("check", false)
                ed.apply()
            }
            eamilLogin()//로그인 실행
        }

    }
    private fun eamilLogin(){//로그인
        if(txtEmail_login.text.toString().isNullOrEmpty()||txtPassword_login.text.toString().isNullOrEmpty()){//텍스트뷰 비었을 때
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()//토스트 메세지 띄워주기
            return
        }
        progressBar_login.visibility= View.VISIBLE
        auth?.signInWithEmailAndPassword(txtEmail_login.text.toString(), txtPassword_login.text.toString())
            ?.addOnCompleteListener { task->
                progressBar_login.visibility= View.GONE
                if(task.isSuccessful){//로그인 성공했을 때
                    Toast.makeText(this, "Email 로그인 성공", Toast.LENGTH_LONG).show()//토스트 메세지 띄워주기

                    val intent= Intent(this, MainActivity::class.java)//mainactivity로 넘겨주기
                    //MainActivity에 값 넘겨주기
                    val email=txtEmail_login.text.toString()//change
                    intent.putExtra("email", email)//change 이메일 값 넣어주기
                    startActivity(intent)
                }else{//로그인 실패했을 때
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()//토스트 메세지 띄워주기
                }
            }

    }
}
