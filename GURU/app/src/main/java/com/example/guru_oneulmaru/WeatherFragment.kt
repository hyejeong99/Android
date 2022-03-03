package com.example.guru_oneulmaru


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_weather.*
import okhttp3.*
import org.w3c.dom.Element
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.arrayListOf as arrayListOf1

/**
 * A simple [Fragment] subclass.
 */
class WeatherFragment : Fragment() {

    lateinit var url: String
    lateinit var serviceKey: String

    lateinit var dateAndtime : LocalDateTime
    lateinit var weather : Weather

    private lateinit var adapter: WeatherAdapter//adapter
    private var adapterList = ArrayList<Weather>()
    private lateinit var weatherList : ArrayList<ArrayList<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //list에 추가하기
        //가로형 recyclerView
        recyclerView.layoutManager=LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
        adapter= WeatherAdapter(this.requireContext()){ //adapter 선언
            Toast.makeText(this.context, "시각: ${it.time}, 온도: ${it.temp}", Toast.LENGTH_LONG).show()    //토스트 메세지 띄워주기
        }
        adapter.setItems(adapterList)//adapter에 list 추가하기
        recyclerView.adapter=adapter//recyclerView에 adapter 추가하기


        // 현재시각 불러오기
       dateAndtime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))


        // ---공공 데이터 가져오기---

        serviceKey = "3zl1JXo0FzylHYcafOFJhdaEtvyqKaqM%2Fcg2Ic%2Bi72npV2z2KAY3BmFMR52WqCKsmvtnlyEDYaMeFYjngvcSig%3D%3D"

        val numOfRows50 = "50"
        val formatter2 = DateTimeFormatter.ofPattern("yyyyMMdd")
        val base_date= dateAndtime.format(formatter2)
        val formatter3 = DateTimeFormatter.ofPattern("HH")
        val formatTime = dateAndtime.format(formatter3)
        val hour = (formatTime.substring(0,2).toInt())-1    // 현재 시간을 입력하면 아직 데이터가 제공되지 않은 경우가 있으므로 base_time을 (현재 시간-1)로 설정해 가장 최근 데이터를 가져온다
        val base_time = if (hour<10) { "0" + hour.toString() + "00" } else { hour.toString() + "00" }
        // nx, ny 위치에 따라 바뀌어야 함
        val nx = "61"
        val ny = "129"
        val pageNo = "1"
        val type = "xml"


        getWeather(base_date, base_time, nx, ny, numOfRows50, pageNo, type)

    }


    // --- getData() ---
    // 초단기예보(날씨)
    private fun getWeather(base_data: String, base_time: String, nx: String, ny: String,
                           numOfRows: String, pageNo: String, type: String) {

        url = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastTimeData"
        val request = getRequestUriW(base_data, base_time, nx, ny, numOfRows, pageNo, type)
        val client = OkHttpClient()


        client.newCall(request).enqueue(object : Callback { // ?
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call, response: Response) {

                val body = response.body()?.string()?.byteInputStream()
                val buildFactory = DocumentBuilderFactory.newInstance()
                val docBuilder = buildFactory.newDocumentBuilder()
                val doc = docBuilder.parse(body, null)
                val nList = doc.getElementsByTagName("item")

                var tmp = ""
                var sky = ""
                var pty = ""
                var time = ""

                var category = ""
                weatherList = kotlin.collections.arrayListOf()
                var tmpList = kotlin.collections.arrayListOf<String>()
                var skyList = kotlin.collections.arrayListOf<String>()
                var ptyList = kotlin.collections.arrayListOf<String>()
                var timeList = kotlin.collections.arrayListOf<String>()


                // 데이터 꺼내와서 딕셔너리? 리스트?에 데이터값 넣기
                for(n in 0 until nList.length) {
                    val element = nList.item(n) as Element

                    category = getValueFromKey(element, "category")

                    if (category == "T1H") {
                        tmp = getValueFromKey(element, "fcstValue")
                        tmpList.add(tmp)

                        time = getValueFromKey(element, "fcstTime")
                        timeList.add(time)
                    }
                    if (category == "SKY") {
                        sky = getValueFromKey(element, "fcstValue")
                        skyList.add(sky)
                    }
                    if (category == "PTY") {
                        pty = getValueFromKey(element, "fcstValue")
                        ptyList.add(pty)
                    }

                }
                weatherList.add(tmpList)
                weatherList.add(skyList)
                weatherList.add(ptyList)
                weatherList.add(timeList)

                activity?.runOnUiThread { // 성공
                    // RecyclerView와 연결할 클래스 > weatherFragment
                    // pty(0:없음, 1:비, 2:진눈개비, 3:눈, 4:소나기)
                    // sky(1:맑음, 3:구름많음, 4:흐림)

                    // RecyclerView
                    for(n in  0 until 4) {
                        tmp = weatherList[0][n] + "º"
                        sky = weatherList[1][n]
                        pty = weatherList[2][n]
                        time = weatherList[3][n].substring(0,2)+" 시"

                        if (pty.toInt() == 0) {  // 비, 눈이 내리지 않는다면
                            when (sky.toInt()) {
                                1 -> weather = Weather(tmp, time, R.drawable.icon_sky1b)
                                3 -> weather = Weather(tmp, time, R.drawable.icon_sky3b)
                                4 -> weather = Weather(tmp, time, R.drawable.icon_sky4b)
                                else -> Toast.makeText(
                                    activity,
                                    "이미지를 불러올 수 없습니다.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else if (pty.toInt() == 1 || pty.toInt() == 4) {  // 비가 내린다면
                            weather = Weather(tmp, time, R.drawable.icon_pty1and4b)
                        } else {  // 눈, 진눈개비가 내리면
                            weather = Weather(tmp, time, R.drawable.icon_pty2and3b)
                        }
                        adapter.addItem(weather)
                        adapter.notifyDataSetChanged()

                        if (n == 0) {
                            when (weather.photo) {
                                R.drawable.icon_sky1b -> imgWeather_weather.setImageResource(R.drawable.icon_sky1)
                                R.drawable.icon_sky3b -> imgWeather_weather.setImageResource(R.drawable.icon_sky3)
                                R.drawable.icon_sky4b -> imgWeather_weather.setImageResource(R.drawable.icon_sky4)
                                R.drawable.icon_pty1and4b -> imgWeather_weather.setImageResource(R.drawable.icon_pty1and4)
                                R.drawable.icon_pty2and3b -> imgWeather_weather.setImageResource(R.drawable.icon_pty2and3)
                            }

                            txtTemp_weather.text = tmp
                            txtWeather_weather.text = when(sky.toInt()) {
                                1 -> "맑음"
                                3 -> "구름많음"
                                4 -> "흐림"
                                else -> ""
                            }
                        }

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


    // --- getRequestUri() ---
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

