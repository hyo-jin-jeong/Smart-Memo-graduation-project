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
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kakao.smartmemo.Adapter.LocationAdapter
import com.kakao.smartmemo.ApiConnect.*
import com.kakao.smartmemo.Contract.MapContract
import com.kakao.smartmemo.Presenter.MapPresenter
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_dialog.toolbar
import kotlinx.android.synthetic.main.map_fragment.view.*
import net.daum.mf.map.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment(), MapView.POIItemEventListener, MapView.MapViewEventListener,
    MapContract.View,
    MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener,
    MapView.OpenAPIKeyAuthenticationResultListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var presenter: MapPresenter
    private lateinit var goCurLocation: FloatingActionButton

    private lateinit var mapView: MapView
    private lateinit var mapViewContainer: ViewGroup
    private var usingMapView = false

    private lateinit var recyclerView: RecyclerView
    private lateinit var locationAdapter: LocationAdapter

    private var documentList: ArrayList<Document> = ArrayList()
    private var bus = BusProvider().getInstance()

    private var isLongTouch: Boolean = false

    private var mLocationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mServiceHandler: Handler? = null
    private var curLocationMarker: MapPOIItem = MapPOIItem()
    private var convertedAddress: String? = null
    private var currentLocation: Location? = null

    private val GPS_ENABLE_REQUEST_CODE: Int = 2001
    private val PERMISSIONS_REQUEST_CODE: Int = 100
    var REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bus.register(this)
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


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        locationSetting()

        when {
            !checkLocationServicesStatus() -> {
                Log.e("jieun", "첫번째 여기 들어옴")
                showDialogForLocationServiceSetting()
            }
            else -> {
                Log.e("jieun", "else 여기 들어옴")
                checkRunTimePermission()
            }
        }

        mapViewContainer = view.map_view as ViewGroup
        mapViewContainer.addView(mapView)

        mapView.setPOIItemEventListener(this)
        mapView.setMapViewEventListener(this)
        mapView.setCurrentLocationEventListener(this)
        mapView.setOpenAPIKeyAuthenticationResultListener(this)

        goCurLocation = view.findViewById(R.id.go_curLocation)
        goCurLocation.setOnClickListener {
            if(mapView.currentLocationTrackingMode == MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving) {
                goCurLocation.setImageResource(R.drawable.current_location_click_icon)
                mapView.currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
            } else {
                goCurLocation.setImageResource(R.drawable.current_location_icon)
                mapView.currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
            }
        }
        recyclerView = view.findViewById(R.id.map_recyclerview)
        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) //레이아웃매니저 생성

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        ) //아래구분선 세팅

        recyclerView.layoutManager = layoutManager

    }

    private fun locationSetting() {
        createLocationRequest()
        getLastLocation()
        val handlerThread = HandlerThread(MapFragment::class.java.simpleName)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 10000
        mLocationRequest!!.fastestInterval = 5000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient!!.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        currentLocation = task.result
                        mapView.setMapCenterPoint(
                            MapPoint.mapPointWithGeoCoord(currentLocation!!.latitude, currentLocation!!.longitude!!),
                            false
                        )
                    } else {
                        Log.w(
                            "check",
                            "Failed to get location."
                        )
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e(
                "check",
                "Lost location permission.$unlikely"
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (!usingMapView) {
            mapView = MapView(view!!.context)
            mapViewContainer.addView(mapView)

            mapView.setPOIItemEventListener(this)
            mapView.setMapViewEventListener(this)
            mapView.setCurrentLocationEventListener(this)
            mapView.setOpenAPIKeyAuthenticationResultListener(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        mapView.setShowCurrentLocationMarker(false)
    }


    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title = resources.getString(R.string.tab_text_1)
        val plusButton = (activity as MainActivity).fab

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search_view_in_map, menu)

        val searchItem: MenuItem? = menu.findItem(R.id.search)
        val searchView = searchItem!!.actionView as SearchView

        var firstX: String? = null
        var firstY: String? = null

        locationAdapter = LocationAdapter(documentList, context!!, searchView, recyclerView)
        recyclerView.adapter = locationAdapter

        searchView.setOnCloseListener {
            goCurLocation.visibility = FloatingActionButton.VISIBLE
            plusButton.visibility = Button.VISIBLE
            false
        }
        searchView.setOnSearchClickListener {
            goCurLocation.visibility = FloatingActionButton.INVISIBLE
            plusButton.visibility = Button.INVISIBLE
            false
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { // do your logic here
                locationAdapter.notifyDataSetChanged()
                plusButton.visibility = Button.VISIBLE
                goCurLocation.visibility = FloatingActionButton.VISIBLE
                recyclerView.visibility = View.GONE
                Toast.makeText(context, query, Toast.LENGTH_SHORT).show()
                if(locationAdapter.clicked) {
                    changeMapCenterPoint(locationAdapter.selectedX, locationAdapter.selectedY)
                } else {
                    //첫번째 요소의 포인트로 이동
                    changeMapCenterPoint(firstX, firstY)
                }
                searchView.clearFocus()
                locationAdapter.clicked = false
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    documentList.clear()
                    locationAdapter.clear()
                    locationAdapter.notifyDataSetChanged()
                    val apiClient: ApiClient =
                        ApiClient()
                    val apiInterface: ApiInterface = apiClient.getApiClient()!!.create(
                        ApiInterface::class.java
                    )
                    val callKeyword: Call<CategoryResult?>? = apiInterface.getSearchLocation(
                        getString(R.string.kakao_restapi_key),
                        "$newText ",
                        15
                    )
                    val callbackKeyword: Callback<CategoryResult?> = object : Callback<CategoryResult?> {
                        //리스폰 시, 대응할 구현체
                        override fun onResponse(
                            call: Call<CategoryResult?>,
                            response: Response<CategoryResult?>
                        ) {
                            if (response.isSuccessful) { //check for Response status
                                assert(response.body() != null)
                                for (document in response.body()?.getDocuments()!!) {
                                    firstX = response.body()?.getDocuments()!![0]!!.x
                                    firstY = response.body()?.getDocuments()!![0]!!.y
                                    locationAdapter.addItem(document!!)
                                }
                                locationAdapter.notifyDataSetChanged()
                            } else {
                                val statusCode = response.code()
                                val responseBody = response.body()
                            }
                        }

                        override fun onFailure(
                            call: Call<CategoryResult?>,
                            t: Throwable
                        ) {
                        }
                    }
                    callKeyword!!.enqueue(callbackKeyword)

                    recyclerView.visibility = View.VISIBLE
                } else {
                    goCurLocation.visibility = FloatingActionButton.INVISIBLE
                    recyclerView.visibility = View.GONE
                    plusButton.visibility = Button.INVISIBLE
                }
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
        val dialog = DialogFragment()
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
        mapView.removePOIItem(curLocationMarker)
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        recyclerView.visibility = View.GONE
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        val handler = Handler()
        val then: Long = 0
        Log.i("jieun", "LongPress 시작")

        //이거는 확인하고 시간있으면 고쳐야해서 둠
        p0!!.setOnTouchListener(object : View.OnTouchListener {
            private val longClickDuration = 1500L

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
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
        val curPoint = p1.mapPointGeoCoord
        val longitude = curPoint.longitude
        val latitude = curPoint.latitude

        //주소 정보도 포함하기
        when {
            !isLongTouch -> isLongTouch = true
            else -> mapView.removePOIItem(curLocationMarker)
        }
        curLocationMarker = createMarker("current marker", p1!!, R.drawable.cur_location_icon)
        mapView.addPOIItem(curLocationMarker)
        presenter.convertAddressFromMapPOIItem(curLocationMarker)

        val items = arrayOf<CharSequence>("메모", "TODO 장소알람")
        val listDialog: AlertDialog.Builder = AlertDialog.Builder(
            this.context,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        )
        listDialog.setTitle("이 곳에 추가할 것을 선택하세요")
            .setItems(items, DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> {
                        val addMemoIntent = Intent(this.context, AddMemo::class.java)
                        addMemoIntent.putExtra("longitude", longitude)
                        addMemoIntent.putExtra("latitude", latitude)
                        addMemoIntent.putExtra("address", convertedAddress)
                        Log.e("jieun", "long press한 위치의 주소는 $convertedAddress")
                        startActivity(addMemoIntent)
                        this.onDestroyView()
                        mapView.removePOIItem(curLocationMarker)
                    }
                    1 -> {
                        val addTodoIntent = Intent(this.context, PlaceAlarmDetailActivity::class.java)
                        addTodoIntent.putExtra("longitude", longitude)
                        addTodoIntent.putExtra("latitude", latitude)
                        addTodoIntent.putExtra("address", convertedAddress)
                        startActivity(addTodoIntent)
                        this.onDestroyView()
                        usingMapView = false
                        mapView.onPause()
                        mapViewContainer.removeAllViews()
                        mapView.removePOIItem(curLocationMarker)
                    }
                }
            })
            .show()
    }


    override fun onCurrentLocationUpdateFailed(p0: MapView?) {
    }

    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {

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
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
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
        val marker = MapPOIItem()
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
                MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
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

    override fun onDaumMapOpenAPIKeyAuthenticationResult(p0: MapView?, p1: Int, p2: String?) {

    }

    fun changeMapCenterPoint(x: String?, y: String?) {
        if(x != null && y != null) {
            mapView.moveCamera(CameraUpdateFactory.newMapPoint(MapPoint.mapPointWithGeoCoord(y.toDouble(), x.toDouble())))
        }
    }

    override fun getLocationName(mapPOIItem: MapPOIItem, locationName: String?) {
        convertedAddress = locationName
    }

}




