package com.kakao.smartmemo.ApiConnect

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface ApiInterface {
    //키워드로 검색
    @GET("v2/local/search/keyword.json")
    fun getSearchLocation(
        @Header("Authorization") token: String?,
        @Query("query") query: String?,
        @Query("size") size: Int
    ): Call<CategoryResult?>?

    //키워드로 검색(특정 위치안의 요)
    @GET("v2/local/search/keyword.json")
    fun getSearchAroundLocation(
        @Header("Authorization") token: String?,
        @Query("query") query: String?,
        @Query("size") size: Int,
        @Query("radius") radius: Int?,
        @Query("x") x: String?,
        @Query("y") y: String?
    ): Call<CategoryResult?>?

    //카테고리로 검색
    @GET("v2/local/search/category.json")
    fun getSearchCategory(
        @Header("Authorization") token: String?,
        @Query("category_group_code") category_group_code: String?,
        @Query("x") x: String?,
        @Query("y") y: String?,
        @Query("radius") radius: Int
    ): Call<CategoryResult?>?

    //키워드로 특정위치기준으로 검색
    @GET("v2/local/search/keyword.json")
    fun getSearchLocationDetail(
        @Header("Authorization") token: String?,
        @Query("query") query: String?,
        @Query("x") x: String?,
        @Query("y") y: String?,
        @Query("radius") radius: Int,
        @Query("size") size: Int
    ): Call<CategoryResult?>?

    //주소로 검색 (아직안쓰는중)
    @GET("v2/local/search/address.json")
    fun getSearchAddress(
        @Header("Authorization") token: String?,
        @Query("query") query: String?,
        @Query("size") size: Int
    ): Call<AddressResult?>?

    //좌표로 주소 변환하기
    @GET("v2/local/geo/coord2address.json")
    fun getConvertAddressFromCoordinate(
        @Header("Authorization") token: String?,
        @Query("x") x: String?,
        @Query("y") y: String?
    ): Call<AddressResult?>?
}