package com.example.guru_oneulmaru

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {//회원가입 activity
    private var auth: FirebaseAuth?=null
    private var firestore: FirebaseFirestore?=null//firebase에 값을 넣기 위해

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth=FirebaseAuth.getInstance()

        btnJoin.setOnClickListener {//회원가입 버튼을 누르면
            createAndLoginEmail()
        }


        // 도시의 분류번호를 부여하는 변수
        var citycode = 0
        val adapter1 = ArrayAdapter.createFromResource(
            this@JoinActivity,
            R.array.array_city,
            android.R.layout.simple_spinner_item
        )
        spinner2.adapter = adapter1
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            )

            {
                Log.d(
                    "fragment_home",
                    "onItemSelected:"+position+" / "+ spinner3.getItemAtPosition(position)
                )
                //spinner_city안에서 선택된 값일 때 해당 값에 citycode를 부여함
                when (spinner2.getItemAtPosition(position)) {
                    "서울특별시" -> {
                        citycode = 1
                    }
                    "인천광역시" -> {
                        citycode = 2
                    }
                    "부산광역시" -> {
                        citycode = 3
                    }
                    "대구광역시" -> {
                        citycode = 4
                    }
                    "광주광역시" -> {
                        citycode = 5
                    }
                    "대전광역시" -> {
                        citycode = 6
                    }
                    "경기도" -> {
                        citycode = 7
                    }
                    "강원도" -> {
                        citycode = 8
                    }
                    "충청북도" -> {
                        citycode = 9
                    }
                    "충청남도" -> {
                        citycode = 10
                    }
                    "경상북도" -> {
                        citycode = 11
                    }
                    "경상남도" -> {
                        citycode = 12
                    }
                    "전라북도" -> {
                        citycode = 13
                    }
                    "전라남도" -> {
                        citycode = 14
                    }
                    "제주도" -> {
                        citycode = 15
                    }

                }


                //citycode에 해당하는 항목들이 spinner에 들어가도록 설정하는 함수 호출, 함수의 매개변수는 citycode.
                region(citycode)


            }


            //citycode를 받아 해당되는 도시에 하위 배열을 배치하는 함수
            private fun region(code: Int) {
                //citycode의 값을 받았을 때 어떤 배열을 불러와야하는지 지정
                var array: Int = when (code) {
                    1 -> R.array.array_seoul
                    2 -> R.array.array_incheon
                    3 -> R.array.array_busan
                    4 -> R.array.array_daegu
                    5 -> R.array.array_gwangju
                    6 -> R.array.array_daejeon
                    7 -> R.array.array_gyeonggi
                    8 -> R.array.array_gangwon
                    9 -> R.array.array_chungbook
                    10 -> R.array.array_chungnam
                    11 -> R.array.array_gyeongbuk
                    12 -> R.array.array_gyeongnam
                    13 -> R.array.array_jeonnam
                    14 -> R.array.array_jeonnam
                    15 -> R.array.array_jeju
                    else -> R.array.array_seoul
                }


                //배열의 이름을 넣어 spinner에 항목을 배치
                val arrayAdapter = ArrayAdapter.createFromResource(
                    this@JoinActivity,
                    array,
                    android.R.layout.simple_spinner_item
                )

                spinner3.adapter = arrayAdapter
                spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                    }
                }
            }

        }



    }
    private fun createAndLoginEmail(){//회원가입 진행
        if(txtEmail_join.text.toString().isNullOrEmpty()||txtPassword_join.text.toString().isNullOrEmpty()){//텍스트뷰 비었을 때
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()//토스트 메세지 띄우기
            return
        }
        progressBar_join.visibility= View.VISIBLE   //progressbar보이도록

        auth?.createUserWithEmailAndPassword(txtEmail_join.text.toString(), txtPassword_join.text.toString())
            ?.addOnCompleteListener { task->
                progressBar_join.visibility= View.GONE
                if(task.isSuccessful){//회원가입 성공 시
                    //데이터베이스에 저장하기(change)
                    val city=spinner2.getSelectedItem().toString()//시
                    val region=spinner3.selectedItem.toString()//지역
                    val email=txtEmail_join.text.toString()//이메일
                    val password=txtPassword_join.text.toString()//비밀번호
                    val area=Region(city, region, email, password)
                    firestore= FirebaseFirestore.getInstance()
                    firestore?.collection("Email")?.document(area.email!!)?.set(area)
                        ?.addOnCompleteListener { task ->
                            if(task.isSuccessful){//제대로 넣었을 때
                                Toast.makeText(this, "지역 삽입 성공", Toast.LENGTH_LONG).show()//토스트 메세지 띄워주기
                            }else{//실패했을 때
                                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()//토스트 메세지 띄워주기
                            }
                        }//change
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_LONG).show()//회원가입 성공 토스트 메세지
                    val intent= Intent(this, LoginActivity::class.java)//login 인텐트로 넘겨주기
                    startActivity(intent)
                }else{//회원가입 실패 시
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()//회원가입 실패 토스트 메세지
                }
            }
    }
}
