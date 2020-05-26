package com.kakao.smartmemo.Data

data class LocationData(var place_name: String? = null) {
    var place_url: String? = null
    var address_name: String? = null
    var road_address_name: String? = null
    var phone: String? = null
    var x = 0.0
    var y = 0.0
    var distance = 0.0
    var category_group_name: String? = null
    var category_group_code: String? = null
    var id: String? = null
    var placeUrl: String? = null
}