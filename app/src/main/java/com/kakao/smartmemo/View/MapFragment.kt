package com.kakao.smartmemo.View

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.kakao.smartmemo.ApiConnect.ApiClient
import com.kakao.smartmemo.ApiConnect.ApiInterface
import com.kakao.smartmemo.ApiConnect.CategoryResult
import com.kakao.smartmemo.Contract.MapContract
import com.kakao.smartmemo.Presenter.MapPresenter
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.main_dialog.*
import kotlinx.android.synthetic.main.map_fragment.view.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView
import org.jetbrains.annotations.NotNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment(), MapView.POIItemEventListener, MapView.MapViewEventListener,
    MapContract.View,
    MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener,
    MapView.OpenAPIKeyAuthenticationResultListener {
    private lateinit var presenter: MapPresenter

    lateinit var mapView: MapView
    lateinit var mapViewContainer: ViewGroup
    private var usingMapView = false

    private var isLongTouch: Boolean = false
    private var curLocationMarker: MapPOIItem = MapPOIItem()

    private var canGetLocation = false
    private lateinit var locationManager: LocationManager
    private val GPS_ENABLE_REQUEST_CODE: Int = 2001
    private val PERMISSIONS_REQUEST_CODE: Int = 100
    var REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    //private lateinit var searchQuery: String?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = MapPresenter(this)
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = MapView(view.context)
        usingMapView = true

        var curLocation: Location? = getLocation()
        if (curLocation != null) {
            var curLongitude = curLocation.longitude
            var curLatitude = curLocation.latitude


            //중심점 설정하는
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(curLatitude, curLongitude), false)
            mapViewContainer = view.map_view as ViewGroup
            mapViewContainer.addView(mapView)
        }

        mapView.setPOIItemEventListener(this)
        mapView.setMapViewEventListener(this)
        mapView.setCurrentLocationEventListener(this)
        mapView.setOpenAPIKeyAuthenticationResultListener(this)

        when {
            !checkLocationServicesStatus() -> {
                showDialogForLocationServiceSetting()
            }
            else -> {
                checkRunTimePermission();
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(!usingMapView) {
            Log.i("jieun", "이거시 실행되었다.")
            mapView = MapView(view!!.context)
            mapViewContainer.addView(mapView)

            mapView.setPOIItemEventListener(this)
            mapView.setMapViewEventListener(this)
            mapView.setCurrentLocationEventListener(this)
            mapView.setOpenAPIKeyAuthenticationResultListener(this)
        }
    }

//    fun searchLocation() {
//        try {
//            if (searchQuery != null) {
//                var query = searchQuery
//                val radius = 10000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
//                val page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
//                var geoCoordinate: MapPoint.GeoCoordinate = mapView.mapCenterPoint.mapPointGeoCoord; //좌표 정보 지오코딩.
//                var latitude = geoCoordinate.latitude; // 위도
//                var longitude = geoCoordinate.longitude; // 경도
//
//                val searcher = SearchModel()
//                var apiKey = searcher.myApiKey
//
//                searcher.searchKeyword(context, query, latitude, longitude, radius, page, apiKey,
//                    object : OnFinishSearchListener {
//                        override fun onSuccess(itemList: List<LocationData>) {
//                            mapView.removeAllPOIItems(); // 기존 검색 결과 삭제.
//                            showResult(itemList) // 검색 결과 보여줌.
//                        }
//
//                        override fun onFail() {
//                            Log.w("오류: ", "오류")
//                        }
//                    })
//            }
//        } catch (e: Exception){
//            e.printStackTrace()
//        }
//
//    }
//
//    private fun showResult(itemList: List<LocationData>) {
//        //화면에 보여질 영역 설정을 위한 객체.
//        val mapPointBounds = MapPointBounds()
//        //검색 api를 통해 호출받은 List의 크기만큼 반복.
//        for (i in 0 until itemList.count()) {
//            //마커와 CalloutBallon을 설정하기 위한 옵션들.
//            val item: LocationData = itemList[i]
//            val poiItem = MapPOIItem()
//            poiItem.tag = i
//            //길 찾기 기능 설정을 위해 해당 POI(관심지점) 객체에 mapPoint(경위도 좌표 값)를 등록.
//            val mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude)
//            poiItem.mapPoint = mapPoint
//            //...중략
//            mapView.addPOIItem(poiItem)
//            mTagItemMap.put(poiItem.tag, item)
//        }
//        //화면 이동.
//        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds))
//        //트랙킹모드 실행.
//        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
//        val poiItems: Array<MapPOIItem> = mapView.getPOIItems()
//        if (poiItems.isNotEmpty()) {
//            mapView.selectPOIItem(poiItems[0], false)
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        mapView.setShowCurrentLocationMarker(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title = resources.getString(R.string.tab_text_1)

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search_view_in_map, menu)

        val searchItem: MenuItem? = menu.findItem(R.id.search)
        var searchView = searchItem!!.actionView as SearchView
        searchView.setOnCloseListener { false }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { // do your logic here
                //locationAdapter.clear()
                //locationAdapter.notifyDataSetChanged()
                var apiClient: ApiClient =
                    ApiClient()
                val apiInterface: ApiInterface = apiClient.getApiClient()!!.create(
                    ApiInterface::class.java)
                val call: Call<CategoryResult?>? = apiInterface.getSearchLocation(
                    getString(R.string.kakao_app_key),
                    query,
                    15
                )
                call!!.enqueue(object : Callback<CategoryResult?> {
                    override fun onResponse(
                        @NotNull call: Call<CategoryResult?>?,
                        @NotNull response: Response<CategoryResult?>
                    ) {
                        if (response.isSuccessful) {
                            assert(response.body() != null)
                            for (document in response.body()?.getDocuments()!!) {
                                //값을 넣는 부분임. locationAdapter가 리스트를 나타내는데 필요한 adapter 같음.
                                //locationAdapter.addItem(document)
                            }
                            //locationAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(
                        @NotNull call: Call<CategoryResult?>?,
                        @NotNull t: Throwable?
                    ) {
                    }
                })
                Toast.makeText(context, query, Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val searchManager = this.context!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo((activity as MainActivity).componentName))

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                (activity as MainActivity).mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    //setPOIItemEventListener override method
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {

    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

    }

    //marker 선택 시
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
        var dialog = DialogFragment()
        //type은 memo만이면 0, todo만이면 1, 둘다면 2
        when (p1?.customImageResourceId) {
            R.drawable.memo_icon -> {
                dialog.setCurType(0)
                dialog.show(super.getChildFragmentManager(), "show dialog")
            }
            R.drawable.todo_icon -> {
                dialog.setCurType(1)
                dialog.show(super.getChildFragmentManager(), "show dialog")
            }
            R.drawable.memo_todo_icon -> {
                dialog.setCurType(2)
                dialog.show(super.getChildFragmentManager(), "show dialog")
            }
            else -> {
            }
        }
    }


    //setMapViewEventListener override method
    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewInitialized(p0: MapView?) {
        val mapPoint1 = MapPoint.mapPointWithGeoCoord(37.565841, 126.976825)
        val memoAndTodo = createMarker("Memo And Todo", mapPoint1, R.drawable.memo_todo_icon)
        mapView.addPOIItem(memoAndTodo)

        val mapPoint2 = MapPoint.mapPointWithGeoCoord(37.565799, 126.975183)
        val memo = createMarker("Memo", mapPoint2, R.drawable.memo_icon)
        mapView.addPOIItem(memo)

        val mapPoint3 = MapPoint.mapPointWithGeoCoord(37.564170, 126.978471)
        val todo = createMarker("Todo", mapPoint3, R.drawable.todo_icon)
        mapView.addPOIItem(todo)
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        val handler = Handler()
        var then: Long = 0
        Log.i("jieun", "LongPress 시작")

        p0!!.setOnTouchListener(object : View.OnTouchListener {
            private val longClickDuration = 1500L

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                if (event?.action == MotionEvent.ACTION_DOWN) {
//                    then = System.currentTimeMillis()
//                    Log.i("jieun", "down then = $then")
//                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    if ((System.currentTimeMillis() - then) > longClickDuration) {
                        Log.i("jieun", "클릭을 뗌!")
                        startLongPress(p1!!)
                    }
                }
                return true
            }
        })


        handler.postDelayed({
            p0!!.setOnTouchListener(null)
            Log.i("jieun", "삭제 ㅠ")
        }, 2000L)

    }


    fun startLongPress(p1: MapPoint) {
        var curPoint = p1.mapPointGeoCoord
        var longitude = curPoint.longitude
        var latitude = curPoint.latitude
        //주소 정보도 포함하기
        when {
            !isLongTouch -> isLongTouch = true
            else -> mapView.removePOIItem(curLocationMarker)
        }
        curLocationMarker = createMarker("current marker", p1!!, R.drawable.cur_location_icon)
        mapView.addPOIItem(curLocationMarker)

        val items = arrayOf<CharSequence>("메모", "TODO 장소알람")
        val listDialog: AlertDialog.Builder = AlertDialog.Builder(
            this.context,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        )
        listDialog.setTitle("이 곳에 추가할 것을 선택하세요")
            .setItems(items, DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> {
                        var addMemoIntent = Intent(this.context, AddMemo::class.java)
                        addMemoIntent.putExtra("Current Point", "나중에 좌표값 넣어")
                        startActivity(addMemoIntent)
                        this.onDestroyView()
                    }
                    else -> {
                        var addTodoIntent = Intent(this.context, PlaceAlarmDetailActivity::class.java)
                        addTodoIntent.putExtra("longitude", longitude)
                        addTodoIntent.putExtra("latitude", latitude)
                        startActivity(addTodoIntent)
                        this.onDestroyView()
                        usingMapView = false
                        mapView.onPause()
                        mapViewContainer.removeAllViews()
                    }
                }
            })
            .show()

    }


    override fun onCurrentLocationUpdateFailed(p0: MapView?) {

    }

    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
        val mapPointGeo: MapPoint.GeoCoordinate = p1!!.mapPointGeoCoord
        Log.i(
            "check",
            String.format(
                "MapView onCurrentLocationUpdate(%f, %f) accuracy (%f)",
                mapPointGeo.latitude,
                mapPointGeo.longitude,
                p2
            )
        )
    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {

    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {

    }

    override fun onReverseGeoCoderFailedToFindAddress(p0: MapReverseGeoCoder?) {

    }

    override fun onReverseGeoCoderFoundAddress(p0: MapReverseGeoCoder?, p1: String?) {
        p0.toString()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode === PERMISSIONS_REQUEST_CODE && grantResults.size === REQUIRED_PERMISSIONS.size) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var check_result = true


            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {
                Log.d("@@@", "start")
                //위치 값을 가져올 수 있음
                mapView.currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOff
                mapView.setShowCurrentLocationMarker(true)
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this.requireActivity(),
                        REQUIRED_PERMISSIONS.get(0)
                    )
                ) {
                    Toast.makeText(
                        this@MapFragment.context,
                        "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@MapFragment.context,
                        "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun createMarker(name: String, point: MapPoint, imageResourceId: Int): MapPOIItem {
        var marker = MapPOIItem()
        marker.itemName = name
        marker.mapPoint = point
        marker.markerType = MapPOIItem.MarkerType.CustomImage
        marker.customImageResourceId = imageResourceId
        marker.isCustomImageAutoscale = false
        marker.isShowCalloutBalloonOnTouch = false
        marker.setCustomImageAnchor(0.5f, 1.0f)

        return marker
    }

    private fun checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this@MapFragment.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
            mapView.currentLocationTrackingMode =
                MapView.CurrentLocationTrackingMode.TrackingModeOff
            mapView.setShowCurrentLocationMarker(true)
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MapFragment.requireActivity(),
                    REQUIRED_PERMISSIONS[0]
                )
            ) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(
                        this@MapFragment.context,
                        "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Toast.LENGTH_LONG
                    )
                    .show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@MapFragment.requireActivity(), REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@MapFragment.requireActivity(), REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting() {
        val builder =
            AlertDialog.Builder(this@MapFragment.context)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            """
                앱을 사용하기 위해서는 위치 서비스가 필요합니다.
                위치 설정을 수정하실래요?
                """.trimIndent()
        )
        builder.setCancelable(true)
        builder.setPositiveButton("설정") { dialog, id ->
            val callGPSSettingIntent =
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        }
        builder.setNegativeButton(
            "취소"
        ) { dialog, id -> dialog.cancel() }
        builder.create().show()
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음")
                        checkRunTimePermission()
                        return
                    }
                }
        }
    }

    private fun checkLocationServicesStatus(): Boolean {
        val locationManager: LocationManager? = this@MapFragment.requireActivity().getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager?
        return (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    //map 현 위치 찾는 메소드
    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
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
            locationManager =
                this.context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isPassiveEnabled = locationManager
                .isProviderEnabled(LocationManager.PASSIVE_PROVIDER)
            val isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (isGPSEnabled || isNetworkEnabled || isPassiveEnabled) {
                canGetLocation = true
                // if GPS Enabled get lat/long using GPS Services
                if (checkPermissions()) {
                    if (isGPSEnabled && location == null) {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, listener
                        )
                        Log.d("GPS", "GPS Enabled")
                        if (locationManager != null) {
                            location =
                                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        }
                    }
                    if (isPassiveEnabled && location == null) {
                        locationManager.requestLocationUpdates(
                            LocationManager.PASSIVE_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, listener
                        )
                        Log.d("Network", "Network Enabled")
                        if (locationManager != null) {
                            location =
                                locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

                            return location
                        }
                    }
                    if (isNetworkEnabled && location == null) {
                        locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, listener
                        )
                        Log.d("Network", "Network Enabled")
                        if (locationManager != null) {
                            location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        }
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

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission( this@MapFragment.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission( this@MapFragment.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    override fun onDaumMapOpenAPIKeyAuthenticationResult(p0: MapView?, p1: Int, p2: String?) {

    }

}




