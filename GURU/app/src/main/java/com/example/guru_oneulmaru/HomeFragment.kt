package com.example.guru_oneulmaru


import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import org.w3c.dom.Element
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.CountDownLatch
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {//homefragment
    private var firestore : FirebaseFirestore?=null//change

    lateinit var url: String
    lateinit var serviceKey: String

    // 지역을 정리해놓은 xml에서 받아올 데이터
    lateinit var step1: String
    lateinit var step2: String
    lateinit var x: String
    lateinit var y: String
    lateinit var regionCode: String
    lateinit var region: String
    lateinit var cityHome:String//change
    lateinit var regionHome:String///change


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //change
        //MainActivity에서 email값 받아오기
        var email=arguments?.getString("email")


        //email값에 맞는 지역 찾기
        //Email collection에서 해당 email과 맞는 객체 찾기
//        firestore = FirebaseFirestore.getInstance()
//        firestore?.collection("Email")?.document(email!!)?.get()?.addOnSuccessListener { task ->
//            val regionObj = task?.toObject(Region::class.java)
//            cityHome = regionObj!!.city
//            regionHome = regionObj!!.region
//        }?.addOnFailureListener {
//                exception ->
//            Log.d("abcde", exception.message)
//        }
//        //lateinit property cityHome has not been initialized
//        Log.d("cityHome",cityHome)

        // getRegion(cityHome, regionHome)


        layout_uvi.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            val builderView = layoutInflater.inflate(R.layout.dialog_custom_uvi, null)
            builder.setView(builderView)
            builder.setNegativeButton("확인", null)
                .show()
        }
        layout_finedust.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            val builderView = layoutInflater.inflate(R.layout.dialog_custom_finedust, null)
            builder.setView(builderView)
            builder.setNegativeButton("확인", null)
                .show()
        }


        // 현재시각 받아오기
        val dateAndtime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        val formatter = DateTimeFormatter.ofPattern("MM")
        val month = dateAndtime.format(formatter).toInt()

        when (month) {
            11, 12, 1, 2, 3 -> {//1,2,3,11,12월에는 체감온도
                tv2.text = "체감온도"
                layout_tmp_dc_tem.setOnClickListener {
                    val builder = AlertDialog.Builder(activity)
                    val builderView = layoutInflater.inflate(R.layout.dialog_custom_tmp, null)
                    builder.setView(builderView)
                    builder.setNegativeButton("확인", null)
                        .show()
                }
            }
            in 6..9 -> {//6~9월에는 불쾌지수
                tv2.text = "불쾌지수"
                layout_tmp_dc_tem.setOnClickListener {
                    val builder = AlertDialog.Builder(activity)
                    val builderView = layoutInflater.inflate(R.layout.dialog_custom_dc, null)
                    builder.setView(builderView)
                    builder.setNegativeButton("확인", null)
                        .show()
                }

            }
            4, 5, 10 -> {//4,5,10월에는 기온
                tv2.text = "기온"
            }

        }


        // --- 스피너 ---

        // 도시의 분류번호를 부여하는 변수
        var citycode = 0

        // spn_arrays에 들어있는 도시 배열을 가져와서 spinner_city에 띄우게함
        val adapter = ArrayAdapter.createFromResource(
            this.context!!,
            R.array.array_city,
            android.R.layout.simple_spinner_item
        )

        spinner_step1.adapter = adapter
        spinner_step1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {

                //spinner_city안에서 선택된 값일 때 해당 값에 citycode를 부여함
                when (spinner_step1.getItemAtPosition(position)) {
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


                //spinner_step2에 들어갈 배열 배치
                setSpinner(citycode)


                //change
                //change
                //city, region index 설정해주기

                //change

//                spinner_step1.setPromptId(3)
//                //change
//                //change
//                //city, region index 설정해주기
//                var cityIndex=0
//                var regionIndex=0
//                val cityString=R.array.array_city
//                //정보에 맞게 spinner 세팅해주기
//                for(i in 0..14){//city 고르기
//                    val citycodeIndex=0
//                    if(cityHome.equals(cityString[i])){
//                        cityIndex=citycode-1
//                    }
//                }
//                spinner_step1.setSelection(cityIndex)
//                spinner_step2.setSelection(regionIndex)
//                //change


            }
        }

// +++파이어베이스에서 지역 불러와서 스피너 설정해주는 코드 넣기+++

        btnApply.setOnClickListener {
            step1 = spinner_step1.selectedItem.toString()
            step2 = spinner_step2.selectedItem.toString()

            // region.xml을 읽어 Spinner의 지역 변환해서 가져오기
            getRegion(step1, step2)

            // ---공공 데이터 가져오기---
            serviceKey = "3zl1JXo0FzylHYcafOFJhdaEtvyqKaqM%2Fcg2Ic%2Bi72npV2z2KAY3BmFMR52WqCKsmvtnlyEDYaMeFYjngvcSig%3D%3D"

            // 공공데이터 불러오기 - 생활지수 (time : 3시간단위로 발표)
            val formatter1= DateTimeFormatter.ofPattern("yyyyMMddHH")
            val pageNo = "1"
            val numOfRows1 = "1"
            val dataType = "XML"
            var areaNo = regionCode  // 선택한 값을 지역코드로 변환하는 것으로 수정
            val time = dateAndtime.format(formatter1)


            // 공공데이터 불러오기 - 초단기예보
            val numOfRows50 = "50"
            val formatter2 = DateTimeFormatter.ofPattern("yyyyMMdd")
            val base_date= dateAndtime.format(formatter2)
            val formatter3 = DateTimeFormatter.ofPattern("HH")
            val formatTime = dateAndtime.format(formatter3)

            //데이터확인!!!!!!!!
            val hour = (formatTime.substring(0,2).toInt())-1    // 현재 시간을 입력하면 아직 데이터가 제공되지 않은 경우가 있으므로 base_time을 (현재 시간-1)로 설정해 가장 최근 데이터를 가져온다
            val base_time = if (hour<10) { "0" + hour.toString() + "00" } else { hour.toString() + "00" }
            Log.d("base_time", base_time)
            // nx, ny 위치에 따라 바뀌어야 함
            val nx = x
            val ny = y
            // numOfRows, pageNo 생활지수 변수 사용
            val type = "xml"


            // 미세먼지
            // pageNo 생활지수 변수 사용, numOfRow1 사용
            val itemCode = "PM10"
            val dataGubun = "HOUR"
            val searchCondition = "WEEK"

            img_background.setImageResource(R.drawable.background_default)
            img_snowman.setImageResource(R.drawable.background_default)
            img_umbrella.setImageResource(R.drawable.background_default)
            img_sulgimask.setImageResource(R.drawable.background_default)

            getWindChill(pageNo, numOfRows1, dataType, areaNo, time) // 체감온도
            getUVI(pageNo, numOfRows1, dataType, areaNo, time)
            getWeather(base_date, base_time, nx, ny, numOfRows50, pageNo, type)
            getFineDust(numOfRows1, pageNo, itemCode, dataGubun, searchCondition)
        }



//        var cityList= resources.getStringArray(R.array.array_city).asList()
//                for (index in 0..cityList.size) {
//                    if(cityList.get(index).equals(cityHome)){
//                        spinner_step1.setSelection(index)
//                    }
//                }
        setSpinner(citycode) //citycode에 해당하는 항목들이 spinner에 들어가도록 설정하는 함수 호출, 함수의 매개변수는 citycode
    }   // onViewCreated 끝


    //---citycode를 받아 해당되는 도시에 하위 배열을 배치---
    private fun setSpinner(code: Int) {

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
            this@HomeFragment.context!!,
            array,
            android.R.layout.simple_spinner_item)

        spinner_step2.adapter = arrayAdapter
        spinner_step2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {

            }
        }

    }


    // --- getData() ---

    // 생활지수 - 체감온도
    private fun getWindChill(pageNo: String, numOfRows: String, dataType: String,
                             areaNo: String, time: String) {

        url = "http://apis.data.go.kr/1360000/LivingWthrIdxService/getWindChillIdx"
        val request = getRequestUriL(pageNo, numOfRows, dataType, areaNo, time)
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {

                val body = response.body()?.string()?.byteInputStream()
                val buildFactory = DocumentBuilderFactory.newInstance()
                val docBuilder = buildFactory.newDocumentBuilder()
                val doc = docBuilder.parse(body, null)
                val nList = doc.getElementsByTagName("item")

                var windChill = ""
                for(n in 0 until nList.length) {
                    val element = nList.item(n) as Element

                    windChill = getValueFromKey(element, "h3")
                }
                activity?.runOnUiThread { // 성공
                    tv_tmp_nb.text = windChill+" ℃"

                    when(windChill.toInt()) {
                        in -100..-45 -> tv_tmp.text = "위험"
                        in -45..-25 -> tv_tmp.text = "경고"
                        in -25..-10 -> tv_tmp.text = "주의"
                        else -> tv_tmp.text = "관심"
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {    // 실패
                val body = e.message
                activity?.runOnUiThread {
                    Toast.makeText(activity, body, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    // 생활지수 - 자외선지수
    private fun getUVI(pageNo: String, numOfRows: String, dataType: String,
                       areaNo: String, time: String) {

        url = "http://apis.data.go.kr/1360000/LivingWthrIdxService/getUVIdx"
        val request = getRequestUriL(pageNo, numOfRows, dataType, areaNo, time)
        val client = OkHttpClient()


        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {

                val body = response.body()?.string()?.byteInputStream()
                val buildFactory = DocumentBuilderFactory.newInstance()
                val docBuilder = buildFactory.newDocumentBuilder()
                val doc = docBuilder.parse(body, null)
                val nList = doc.getElementsByTagName("item")

                var uvi = ""
                for(n in 0 until nList.length) {
                    val element = nList.item(n) as Element
                    uvi = getValueFromKey(element, "today")
                }
                activity?.runOnUiThread { // 성공
                    tv_uvi_nb.text = uvi

                    when(uvi.toInt()) {
                        in 0..2 -> tv_uvi.text="낮음"
                        in 3..5 -> tv_uvi.text="보통"
                        in 6..7 -> tv_uvi.text="높음"
                        in 8..10 -> tv_uvi.text="매우높음"
                        else -> tv_uvi.text="위험"
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {    // 실패
                val body = e.message
                activity?.runOnUiThread {
                    Toast.makeText(activity, body, Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    // 초단기예보(날씨) > 그림을 변경
    private fun getWeather(base_data: String, base_time: String, nx: String, ny: String,
                           numOfRows: String, pageNo: String, type: String) {

        url = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastTimeData"
        val request = getRequestUriW(base_data, base_time, nx, ny, numOfRows, pageNo, type)
        val client = OkHttpClient()


        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {

                val body = response.body()?.string()?.byteInputStream()
                val buildFactory = DocumentBuilderFactory.newInstance()
                val docBuilder = buildFactory.newDocumentBuilder()
                val doc = docBuilder.parse(body, null)
                val nList = doc.getElementsByTagName("item")


                var tmp = ""    // 기온
                var sky = ""    // 하늘 상태
                var pty = ""    // 강수 형태

                var category = ""
                var weather : Weather? = null

                // 데이터 꺼내와서 포맷에 맞춰 RecyclerView에 넣도록 수정
                for(n in 0 until nList.length) {
                    val element = nList.item(n) as Element

                    category = getValueFromKey(element, "category")

                    if (category == "T1H") {
                        tmp = getValueFromKey(element, "fcstValue")
                    }
                    if (category == "SKY") {
                        sky = getValueFromKey(element, "fcstValue")
                    }
                    if (category == "PTY") {
                        pty = getValueFromKey(element, "fcstValue")
                    }

                }
                activity?.runOnUiThread { // 성공
                    // RecyclerView와 연결할 클래스 > weatherFragment
                    // pty(0:없음, 1:비, 2:진눈개비, 3:눈, 4:소나기)
                    // sky(1:맑음, 3:구름많음, 4:흐림)

                    // ****이미지 소스 바꾸기****

                    // 배경
                    if(pty.toInt() == 0) {  // 비, 눈이 내리지 않는다면
                        when(sky.toInt()) {
                            1 -> img_background.setImageResource(R.drawable.sulgi_sky1)
                            3 -> img_background.setImageResource(R.drawable.sulgi_sky3)
                            4 -> img_background.setImageResource(R.drawable.sulgi_sky4)
                            else -> Toast.makeText(activity, "이미지를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()
                        }
                    }
                    else if(pty.toInt() == 1 || pty.toInt() == 4) {  // 비가 내린다면
                        img_background.setImageResource(R.drawable.sulgi_pty1and4)
                        img_umbrella.setImageResource(R.drawable.sulgi_umbrella)
                    }
                    else if(pty.toInt() == 2 || pty.toInt() == 3){  // 눈, 진눈개비가 내리면
                        img_background.setImageResource(R.drawable.sulgi_pty2and3)
                        img_umbrella.setImageResource(R.drawable.sulgi_umbrella)
                        img_snowman.setImageResource(R.drawable.sulgi_snowman)
                    }


                    // 모자
                    when(tmp.toDouble()) {
                        in -30.0..5.9 -> img_sulgihat.setImageResource(R.drawable.sulgi_hat8)
                        in 6.0..9.9 -> img_sulgihat.setImageResource(R.drawable.sulgi_hat7)
                        in 10.0..11.9 -> img_sulgihat.setImageResource(R.drawable.background)
                        in 12.0..16.9 -> img_sulgihat.setImageResource(R.drawable.background)
                        in 17.0..19.9 -> img_sulgihat.setImageResource(R.drawable.background)
                        in 20.0..22.9 -> img_sulgihat.setImageResource(R.drawable.background)
                        in 23.0..26.9 -> img_sulgihat.setImageResource(R.drawable.background)
                        else -> img_sulgihat.setImageResource(R.drawable.background)
                    }

                    //옷
                    when(tmp.toDouble()) {
                        in -30.0..5.9 -> img_sulgicloth.setImageResource(R.drawable.sulgi_cloth8)
                        in 6.0..9.9 -> img_sulgicloth.setImageResource(R.drawable.sulgi_cloth7)
                        in 10.0..11.9 -> img_sulgicloth.setImageResource(R.drawable.background)
                        in 12.0..16.9 -> img_sulgicloth.setImageResource(R.drawable.background)
                        in 17.0..19.9 -> img_sulgicloth.setImageResource(R.drawable.background)
                        in 20.0..22.9 -> img_sulgicloth.setImageResource(R.drawable.background)
                        in 23.0..26.9 -> img_sulgicloth.setImageResource(R.drawable.background)
                        else -> img_sulgicloth.setImageResource(R.drawable.background)
                    }

                }
            }

            override fun onFailure(call: Call, e: IOException) {    // 실패
                val body = e.message
                activity?.runOnUiThread {
                    Toast.makeText(activity, body, Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    // 미세먼지 : 수정하세요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private fun getFineDust(numOfRows: String, pageNo: String, itemCode: String, dataGubun: String, searchCondition: String) {

        url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst"
        val request = getRequestUriF(numOfRows, pageNo, itemCode, dataGubun, searchCondition)
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {

                val body = response.body()?.string()?.byteInputStream()
                val buildFactory = DocumentBuilderFactory.newInstance()
                val docBuilder = buildFactory.newDocumentBuilder()
                val doc = docBuilder.parse(body, null)
                val nList = doc.getElementsByTagName("item")

                var finedust = ""

                for(n in 0 until nList.length) {
                    val element = nList.item(n) as Element
                    finedust = getValueFromKey(element, region)
                }
                activity?.runOnUiThread { // 성공
                    tv_finedust_nb.text = finedust

                    img_sulgi.setImageResource(R.drawable.sulgi_default)

                    when(finedust.toInt()) {
                        in 0..30 -> tv_finedust.text = "좋음"
                        in 31..50 -> tv_finedust.text = "보통"
                        in 51..100 -> tv_finedust.text = "나쁨"
                        in 101..150 -> tv_finedust.text = "매우 나쁨"
                        else -> tv_finedust.text = "최악"
                    }

                    if(finedust.toInt() >= 51) {
                        img_sulgimask.setImageResource(R.drawable.sulgi_mask)
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {    // 실패
                val body = e.message
                activity?.runOnUiThread {
                    Toast.makeText(activity, body, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun getRegion(step1: String, step2: String) {

        // Assets 파일의 region.xml을 파싱
        val buildFactory = DocumentBuilderFactory.newInstance()
        val docBuilder = buildFactory.newDocumentBuilder()
        val assetManager = resources.assets
        val inputStream= assetManager.open("region.xml")
        val doc = docBuilder.parse(inputStream, null)
        val nList = doc.getElementsByTagName("item")

        for(n in 0 until nList.length) {
            val element = nList.item(n) as Element

            if(step1 == getValueFromKey(element, "step1") && step2 == getValueFromKey(element, "step2")) {
                x = getValueFromKey(element, "x")
                y = getValueFromKey(element, "y")
                regionCode = getValueFromKey(element, "regionCode")
                region = getValueFromKey(element, "region")
            }
        }

        when (region) {
            "서울" -> region = "seoul"
            "부산" -> region = "busan"
            "대구" -> region = "daegu"
            "인천" -> region = "incheon"
            "광주" -> region = "gwangju"
            "대전" -> region = "daejeon"
            "울산" -> region = "ulsan"
            "경기" -> region = "gyeonggi"
            "강원" -> region = "gangwon"
            "충북" -> region = "chungbuk"
            "충남" -> region = "chungnam"
            "전북" -> region = "jeonbuk"
            "전남" -> region = "jeonnam"
            "경북" -> region = "gyeongbuk"
            "경남" -> region = "gyeongnam"
            "제주" -> region = "jeju"
            "세종" -> region = "sejong"
        }
    }


    // --- getRequestUri() ---

    // 생활지수 getRequestUri()
    private fun getRequestUriL(pageNo: String, numOfRows: String, dataType: String,
                               areaNo: String, time: String) : Request {

        var httpUrl = HttpUrl.parse(url)
            ?.newBuilder()
            ?.addEncodedQueryParameter("serviceKey", serviceKey)
            ?.addQueryParameter("pageNo",pageNo)
            ?.addQueryParameter("numOfRows",numOfRows)
            ?.addQueryParameter("dataType",dataType)
            ?.addQueryParameter("areaNo",areaNo)
            ?.addQueryParameter("time",time)
            ?.build()

        return Request.Builder()
            .url(httpUrl)
            .addHeader("Content-Type",
                "application/x-www-form-urlencoded; text/xml; charset=uft-8")
            .build()
    }

    // 초단기예보 getRequestUri()
    private fun getRequestUriW(base_date: String, base_time: String, nx: String, ny: String,
                               numOfRows: String, pageNo: String, type: String) : Request {

        var httpUrl = HttpUrl.parse(url)
            ?.newBuilder()
            ?.addEncodedQueryParameter("serviceKey", serviceKey)
            ?.addQueryParameter("base_date",base_date)
            ?.addQueryParameter("base_time",base_time)
            ?.addQueryParameter("nx",nx)
            ?.addQueryParameter("ny",ny)
            ?.addQueryParameter("numOfRows",numOfRows)
            ?.addQueryParameter("pageNo",pageNo)
            ?.addQueryParameter("_type",type)
            ?.build()

        return Request.Builder()
            .url(httpUrl)
            .addHeader("Content-Type",
                "application/x-www-form-urlencoded; text/xml; charset=uft-8")
            .build()
    }

    // 미세먼지 getRequestUri()
    private fun getRequestUriF(numOfRows: String, pageNo: String, itemCode: String, dataGubun: String, searchCondition: String) : Request {
        var httpUrl = HttpUrl.parse(url)
            ?.newBuilder()
            ?.addEncodedQueryParameter("serviceKey", serviceKey)
            ?.addQueryParameter("numOfRows",numOfRows)
            ?.addQueryParameter("pageNo",pageNo)
            ?.addQueryParameter("itemCode",itemCode)
            ?.addQueryParameter("dataGubun",dataGubun)
            ?.addQueryParameter("searchCondition",searchCondition)
            ?.build()

        return Request.Builder()
            .url(httpUrl)
            .addHeader("Content-Type",
                "application/x-www-form-urlencoded; text/xml; charset=uft-8")
            .build()
    }


    // --- getValueFromKey() ---

    private fun getValueFromKey(element: Element, key: String) : String {
        var value = element.getElementsByTagName(key).item(0).firstChild.nodeValue
        if(value == null) {
            return "준비 중"   //데이터 제공이 아직 이루어지지 않았을 때
        }
        else {
            return value
        }
    }

}