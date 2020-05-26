package com.kakao.smartmemo.Model

import android.content.Context
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.os.AsyncTask
import android.util.Log
import com.kakao.smartmemo.Interface.OnFinishSearchListener
import com.kakao.smartmemo.Data.LocationData
import org.json.JSONObject
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList


class SearchModel {
    private val LOCAL_KEYWORD_SEARCH_API_FORMAT =
        "https://dapi.kakao.com/v2/local/search/keyword.json?query=%s&y=%s&x=%s&radius=%d&page=%d&apikey=%s"
    private lateinit var onFinishSearchListener: OnFinishSearchListener
    private var searchTask: SearchTask? = null
    var myApiKey = "a74a781e501d031236f6ee1960b4d00e"

    private class SearchTask : AsyncTask<String?, Void?, Void?>() {
        var str1: String? = null
        var str2: String? = null

        override fun doInBackground(vararg params: String?): Void? {
            str1 = strings[0]
            str2 = strings[1]
            Log.d("TAG", "$str1 $str2")
            return null
        }
    }

    fun searchKeyword(applicationContext: Context?, query: String?, latitude: Double, longitude: Double, radius: Int, page: Int, apikey: String?, onFinishSearchListener: OnFinishSearchListener) {
        this.onFinishSearchListener = onFinishSearchListener
        if (searchTask != null) {
            searchTask!!.cancel(true)
            searchTask = null
        }
        val url: String? = buildKeywordSearchApiUrlString(query!!, latitude, longitude, radius, page, apikey!!)
        searchTask = SearchTask()
        searchTask!!.execute(url)
    }

    //UTF-8로 키워드 인코딩 필요.
    private fun buildKeywordSearchApiUrlString(
        query: String,
        latitude: Double,
        longitude: Double,
        radius: Int,
        page: Int,
        apiKey: String
    ): String? {
        var encodedQuery = ""
        try {
            encodedQuery = URLEncoder.encode(query, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return java.lang.String.format(
            Locale.ENGLISH,
            LOCAL_KEYWORD_SEARCH_API_FORMAT,
            encodedQuery,
            latitude,
            longitude,
            radius,
            page,
            apiKey
        )
    }

    //API 호출을 위한 메서드
    private fun fetchData(
        urlString: String,
        header: Map<String, String>
    ): String? {
        return try {
            val url = URL(urlString)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.readTimeout = 4000
            conn.connectTimeout = 7000
            conn.requestMethod = "GET" // GET 방식으로  API 요청
            conn.setRequestProperty(
                "Authorization",
                "KakaoAK " + myApiKey
            ) // header 부분에 앱키 작성
            conn.doInput = true
            conn.connect()
            val inputStream: InputStream = conn.inputStream
            val scanner = Scanner(inputStream)
            scanner.useDelimiter("\\A")
            val data = if (scanner.hasNext()) scanner.next() else ""
            Log.w("data : ", data)
            data
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    //JSON 파싱용 메서드
    private fun parse(jsonString: String): List<LocationData>? {
        val itemList: List<LocationData> = ArrayList()
        try {
            val reader = JSONObject(jsonString)
            val objects = reader.getJSONArray("documents")
            for (i in 0 until objects.length()) {
                val obj = objects.getJSONObject(i)
                //item 클래스에 json 데이터 할당.
                val item = LocationData(obj.getString("place_name"))
                item.place_name = obj.getString("place_name")
                item.place_url = obj.getString("place_url")
                item.address_name = obj.getString("address_name")
                item.road_address_name = obj.getString("road_address_name")
                item.phone = obj.getString("phone")
                item.x = obj.getDouble("x")
                item.y = obj.getDouble("y")
                item.distance = obj.getDouble("distance")
                item.category_group_name = obj.getString("category_group_name")
                item.category_group_code = obj.getString("category_group_code")
                item.id = obj.getString("id")
                item.placeUrl = obj.getString("placeUrl")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return itemList
    }
}


