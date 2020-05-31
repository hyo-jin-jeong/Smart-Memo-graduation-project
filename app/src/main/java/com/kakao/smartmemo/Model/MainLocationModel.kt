package com.kakao.smartmemo.Model

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
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

    fun convertAddressFromMapPOIItem(x: String, y: String) {

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

    @SuppressLint("MissingPermission")
    fun getLocation(context: Context): Location? {
        val MIN_TIME_BW_UPDATES = 10000L
        val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10000F
        var location: Location? = null
        val listener: LocationListener = object : LocationListener {
            //provider의 상태가 변경되때마다 호출
            override fun onStatusChanged(provider: String?, tatus: Int, extras: Bundle?) {

            }

            //provider가 사용 가능한 상태가 되는 순간 호출
            override fun onProviderEnabled(provider: String?) {

            }

            //provider가 사용 불가능 상황이 되는 순간 호출
            override fun onProviderDisabled(provider: String?) {

            }

            //위치 정보 전달 목적으로 호출
            override fun onLocationChanged(location: Location?) {

            }
        }
        try {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isPassiveEnabled = locationManager
                .isProviderEnabled(LocationManager.PASSIVE_PROVIDER)
            val isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (isGPSEnabled || isNetworkEnabled || isPassiveEnabled) {
                // if GPS Enabled get lat/long using GPS Services
                if (checkPermissions(context)) {
                    if (isGPSEnabled && location == null) {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, listener
                        )
                        Log.d("GPS", "GPS Enabled")
                        location =
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    }
                    if (isPassiveEnabled && location == null) {
                        locationManager.requestLocationUpdates(
                            LocationManager.PASSIVE_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, listener
                        )
                        Log.d("Network", "Network Enabled")
                        location =
                            locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

                        return location
                    }
                    if (isNetworkEnabled && location == null) {
                        locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, listener
                        )
                        Log.d("Network", "Network Enabled")
                        location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    }
                } else {
                    return null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return location
    }

    private fun checkPermissions(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
               context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


}