package com.kakao.smartmemo.Model

import android.location.Location
import android.util.Log
import com.kakao.smartmemo.ApiConnect.AddressResult
import com.kakao.smartmemo.ApiConnect.ApiClient
import com.kakao.smartmemo.ApiConnect.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainLocationModel {
    private val restApiKey = "KakaoAK bc0903a8612361df4014927c8a95563c"
    var locationAddress: String? = null
    var longitude: String? = null
    var latitude: String? = null

    fun convertAddressFromPoint(x: String, y: String) {

        val apiClient = ApiClient()
        val apiInterface: ApiInterface = apiClient.getApiClient()!!.create(
            ApiInterface::class.java
        )

        longitude = x
        latitude = y

        val call: Call<AddressResult?>? = apiInterface.getConvertAddressFromCoordinate(
            restApiKey, x, y)
        val callback: Callback<AddressResult?> = object : Callback<AddressResult?> {
            //리스폰 시, 대응할 구현체
            override fun onResponse(
                call: Call<AddressResult?>,
                response: Response<AddressResult?>
            ) {
                if (response.isSuccessful) { //check for Response status
                    assert(response.body() != null)
                    for (document in response.body()?.getDocuments()!!) {
                        if(document!!.roadAddress != null) {
                            var curRoadAddress = document!!.roadAddress!!.addressName.toString()
                            var curBuildingName = document!!.roadAddress!!.buildingName.toString()
                            if(curBuildingName == "")
                                locationAddress = curRoadAddress
                            else
                                locationAddress = curBuildingName
                        }
                        else {
                            locationAddress = document!!.address!!.addressName.toString()
                        }
                    }
                } else {
                    val statusCode = response.code()
                    Log.i("jieun", "실패 이유 코드 $statusCode")
                }
            }

            override fun onFailure(
                call: Call<AddressResult?>,
                t: Throwable
            ) {
            }
        }
        call!!.enqueue(callback)
    }


    fun checkValue(): Boolean {
        return locationAddress != null && longitude != null && latitude != null
    }

    fun setLocation(location: Location) {
        longitude = location.longitude.toString()
        latitude = location.latitude.toString()
    }

}