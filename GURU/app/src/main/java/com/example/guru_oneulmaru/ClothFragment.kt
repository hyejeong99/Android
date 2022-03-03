package com.example.guru_oneulmaru

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_cloth.*
import okhttp3.*
import org.w3c.dom.Element
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

/**
 * A simple [Fragment] subclass.
 */
class ClothFragment : Fragment() {//옷장 fragment

    lateinit var url: String
    lateinit var serviceKey: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cloth, container, false)
        //imgCloth.setImageResource(R.drawable.weather_cloth1)
        //imgCloth.invalidate()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 현재시각 불러오기
        val dateAndtime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))


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

                var category = ""
                var weather : Weather? = null

                // 데이터 꺼내와서 포맷에 맞춰 RecyclerView에 넣도록 수정
                for(n in 0 until nList.length) {
                    val element = nList.item(n) as Element

                    category = getValueFromKey(element, "category")

                    if (category == "T1H") {
                        tmp = getValueFromKey(element, "fcstValue")
                        break
                    }

                }
                activity?.runOnUiThread { // 성공
                    // RecyclerView와 연결할 클래스 > weatherFragment
                    // pty(0:없음, 1:비, 2:진눈개비, 3:눈, 4:소나기)
                    // sky(1:맑음, 3:구름많음, 4:흐림)

                    // ****이미지 소스 바꾸기****
                    // ****weatherFragment에 맞게 코드 수정**** : text > 기온, imageSource > 기온

                    tv_clothtmp.text = tmp + "ºC"

                    when(tmp.toDouble()) {
                        in -30.0..5.9 -> img_cloth.setImageResource(R.drawable.cloth8)
                        in 6.0..9.9 -> img_cloth.setImageResource(R.drawable.cloth7)
                        in 10.0..11.9 -> img_cloth.setImageResource(R.drawable.cloth6)
                        in 12.0..16.9 -> img_cloth.setImageResource(R.drawable.cloth5)
                        in 17.0..19.9 -> img_cloth.setImageResource(R.drawable.cloth4)
                        in 20.0..22.9 -> img_cloth.setImageResource(R.drawable.cloth3)
                        in 23.0..26.9 -> img_cloth.setImageResource(R.drawable.cloth2)
                        else -> img_cloth.setImageResource(R.drawable.cloth1)
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
