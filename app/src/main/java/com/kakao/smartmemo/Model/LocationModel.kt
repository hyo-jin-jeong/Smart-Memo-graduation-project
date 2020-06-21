package com.kakao.smartmemo.Model

import com.kakao.smartmemo.ApiConnect.AddressResult
import com.kakao.smartmemo.ApiConnect.ApiClient
import com.kakao.smartmemo.ApiConnect.ApiInterface
import com.kakao.smartmemo.Contract.MapContract
import com.kakao.smartmemo.Contract.PlaceAlarmDetailContract
import net.daum.mf.map.api.MapPOIItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationModel {
    private var onTodoDetailListener:PlaceAlarmDetailContract.OnTodoDetailListener? = null
    private var onMapListener: MapContract.OnMapListener? = null

    private val restApiKey = "KakaoAK bc0903a8612361df4014927c8a95563c"
    var curNumberAddress: String? = null
    var curRoadAddress: String? = null
    var curBuildingName: String? = null

    constructor(onTodoDetailListener: PlaceAlarmDetailContract.OnTodoDetailListener) {
        this.onTodoDetailListener = onTodoDetailListener
    }
    constructor(onMapListener: MapContract.OnMapListener) {
        this.onMapListener = onMapListener
    }

    fun convertAddressFromMapPOIItem(mapPOIItem: MapPOIItem) {

        val apiClient = ApiClient()
        val apiInterface: ApiInterface = apiClient.getApiClient()!!.create(ApiInterface::class.java)

        var longitude = mapPOIItem.mapPoint.mapPointGeoCoord.longitude.toString()
        var latitude =  mapPOIItem.mapPoint.mapPointGeoCoord.latitude.toString()

        val call: Call<AddressResult?>? = apiInterface.getConvertAddressFromCoordinate(restApiKey, longitude, latitude)
        val callback: Callback<AddressResult?> = object : Callback<AddressResult?> {
            override fun onResponse(
                call: Call<AddressResult?>,
                response: Response<AddressResult?>
            ) {
                if (response.isSuccessful) { //check for Response status
                    assert(response.body() != null)
                    for (document in response.body()?.getDocuments()!!) {
                        if(document!!.roadAddress != null) {
                            curRoadAddress = document!!.roadAddress!!.addressName.toString()
                            curBuildingName = document!!.roadAddress!!.buildingName.toString()

                            if(curBuildingName.equals("")) {    //빌딩 이름이 없을 경우
                                if(onTodoDetailListener != null)
                                    onTodoDetailListener!!.onSuccess(mapPOIItem, curRoadAddress)
                                else
                                    onMapListener!!.onMapSuccess(mapPOIItem, curRoadAddress)
                            } else {
                                if(onTodoDetailListener != null)
                                    onTodoDetailListener!!.onSuccess(mapPOIItem, curBuildingName)
                                else
                                    onMapListener!!.onMapSuccess(mapPOIItem, curBuildingName)
                            }
                        }
                        else {
                            curNumberAddress = document!!.address!!.addressName.toString()
                            if(curNumberAddress.equals("")){    //주소 이름이 없을 경우
                                if(onTodoDetailListener != null)
                                    onTodoDetailListener!!.onSuccess(mapPOIItem, "주소를 가져올 수 없음")
                                else
                                    onMapListener!!.onMapSuccess(mapPOIItem, "주소를 가져올 수 없음")
                            } else {
                                if(onTodoDetailListener != null)
                                    onTodoDetailListener!!.onSuccess(mapPOIItem, curNumberAddress)
                                else
                                    onMapListener!!.onMapSuccess(mapPOIItem, curNumberAddress)
                            }
                        }
                    }
                } else {
                    val statusCode = response.code()
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
}
