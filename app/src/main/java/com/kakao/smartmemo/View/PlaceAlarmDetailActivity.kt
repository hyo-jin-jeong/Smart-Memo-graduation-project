package com.kakao.smartmemo.View

import android.app.Activity
import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Adapter.LocationAdapter
import com.kakao.smartmemo.Adapter.LocationListAdapter
import com.kakao.smartmemo.ApiConnect.ApiClient
import com.kakao.smartmemo.ApiConnect.ApiInterface
import com.kakao.smartmemo.ApiConnect.CategoryResult
import com.kakao.smartmemo.ApiConnect.Document
import com.kakao.smartmemo.Contract.PlaceAlarmDetailContract
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Presenter.PlaceAlarmDetailPresenter
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.main_dialog.*
import net.daum.mf.map.api.CameraUpdateFactory
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlaceAlarmDetailActivity : AppCompatActivity(), PlaceAlarmDetailContract.View,
    MapView.POIItemEventListener, MapView.MapViewEventListener {
    lateinit var presenter: PlaceAlarmDetailContract.Presenter
    private lateinit var myToolbar: Toolbar
    private lateinit var mapView: MapView
    private lateinit var mapViewContainer: ViewGroup
    private var placeId = ""
    private val context: Context = this

    private var curLongitude: Double? = null
    private var curLatitude: Double? = null
    private var curMarker: MapPOIItem? = null
    private var curAddress: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var locationAdapter: LocationAdapter
    private var documentList: ArrayList<Document> = ArrayList()

    private lateinit var listAdapter: LocationListAdapter
    private lateinit var saveButton: Button
    private lateinit var addButton: Button
    private lateinit var locationTextView: TextView
    private val locationNames = ArrayList<String>()
    private val locations = ArrayList<Location>()
    private var mapPOIItem: MapPOIItem? = null
    private var locationItems = ArrayList<MapPOIItem>()
    private var aroundLocationItems = ArrayList<MapPOIItem>()
    private var placeList = arrayListOf<PlaceData>()

    private var isUp: Boolean = false
    private lateinit var translateUp: Animation
    private lateinit var translateDown: Animation
    private lateinit var listLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.place_alarm_detail_settings)

        presenter = PlaceAlarmDetailPresenter(this)

        // Toolbar
        myToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(myToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mapView = MapView(context)
        mapViewContainer = findViewById<ViewGroup>(R.id.map_view)
        mapViewContainer.addView(mapView)

        saveButton = findViewById(R.id.saveButton)
        addButton = findViewById(R.id.addButton)
        locationTextView = findViewById(R.id.location)

        translateUp = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_up)
        translateDown = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_down)

        listLayout = findViewById(R.id.listLayout)

        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        mapView.setShowCurrentLocationMarker(false)
        mapView.setMapViewEventListener(this)
        mapView.setPOIItemEventListener(this)

        recyclerView = findViewById(R.id.search_recyclerview)
        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) //레이아웃매니저 생성

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        ) //아래구분선 세팅

        recyclerView.layoutManager = layoutManager

        val listView: RecyclerView = findViewById(R.id.listView)
        listAdapter = LocationListAdapter(
            context,
            locationNames,
            locations,
            locationItems,
            aroundLocationItems,
            mapView
        )
        listView.adapter = listAdapter

        val layoutManager2 =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) //레이아웃매니저 생성

        listView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        listView.layoutManager = layoutManager2
        // MapFragment에서 LongTouch로 들어왔을 때랑 fab로 생성했을 때 현재 위치 받기
        if(intent.getParcelableExtra<PlaceData>("placeData") != null) {
            placeId = intent.getParcelableExtra<PlaceData>("placeData").placeId
            curLatitude = intent.getParcelableExtra<PlaceData>("placeData").latitude
            curLongitude = intent.getParcelableExtra<PlaceData>("placeData").longitude
            curAddress = intent.getParcelableExtra<PlaceData>("placeData").place
        } else {
            curAddress = "한성대학교"
            curLatitude = 37.582276
            curLongitude = 127.009850
        }
        // 생성된 Todo에서(AddTodo에서) 값 넘겨 받은 경우
        if (intent.hasExtra("todoPlaceAlarm")) {
            placeList = intent.getParcelableArrayListExtra("todoPlaceAlarm")
        }
        if (!placeList.isNullOrEmpty()) {
            for (placeData in placeList!!.iterator()) {
                var location = Location("")
                location.longitude = placeData.longitude
                location.latitude = placeData.latitude
                listAdapter.addItem(placeData.place)
                listAdapter.addItemLocation(location)
                val item = createMarker(
                    placeData.place,
                    MapPoint.mapPointWithGeoCoord(location.latitude, location.longitude)
                )
                item.markerType = MapPOIItem.MarkerType.YellowPin
                mapView.addPOIItem(item)
                locationItems.add(item)
            }
        }

        val mapPoint: MapPoint = MapPoint.mapPointWithGeoCoord(curLatitude!!, curLongitude!!)
        mapView.setMapCenterPoint(mapPoint, true)

        val initialItem: MapPOIItem
        initialItem = if (curAddress == null)
            createMarker("여기 널이라능 ㅠㅠ", mapPoint)
        else
            createMarker(curAddress!!, mapPoint)
        if (!locationNames.contains(initialItem.itemName)) {
            mapView.addPOIItem(initialItem)
        }

        saveButton.setOnClickListener {
            if(locations.isEmpty()) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(
                    ContextThemeWrapper(
                        context,
                        R.style.Theme_AppCompat_Light_Dialog_Alert
                    )
                )
                builder.setTitle("장소 미 선택")
                builder.setMessage("알람을 설정할 장소가 선택 되어 있지 않습니다. 장소를 선택해주세요.")
                builder.setPositiveButton("확인") { dialog, id ->

                }
                builder.show()
            } else {
                curAddress = curMarker!!.itemName
                replaceWithData()
                val placeData = PlaceData(placeId, curAddress!!, curLatitude!!, curLongitude!!)
                //long pressed 로 들어왔을 때 AddTodo로 넘기기
                if (intent.getStringExtra("mode") == "longPressed") {
                    val todoIntent = Intent(this.context, AddTodo::class.java)
                    todoIntent.putExtra("placeData", placeData)
                    todoIntent.putParcelableArrayListExtra("todoPlaceAlarm", placeList)
                    todoIntent.putExtra("mode", "longPressed")
                    startActivity(todoIntent)
                    finish()

                } else {  // fab 버튼으로 들어왔을 때 AddTodo로 넘김
                    val intent = Intent()
                    intent.putExtra("placeData", placeData)
                    intent.putParcelableArrayListExtra("todoPlaceAlarm", placeList)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

            }
        }

    }

    private fun replaceWithData() {
        placeList.clear()

        for (i in locations.indices) {
            var placeData =
                PlaceData(placeId, locationNames[i], locations[i].latitude, locations[i].longitude)
            placeList.add(placeData)
        }
    }

    override fun onPause() {
        super.onPause()
        mapViewContainer.removeAllViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_view_in_map, menu)
        this.toolbar.title = "장소 알람 설정"

        val searchItem: MenuItem? = menu!!.findItem(R.id.search)
        var searchView = searchItem!!.actionView as SearchView

        val apiClient = ApiClient()
        val apiInterface: ApiInterface = apiClient.getApiClient()!!.create(
            ApiInterface::class.java
        )

        locationAdapter = LocationAdapter(documentList, context!!, searchView, recyclerView)
        recyclerView.adapter = locationAdapter

        var firstX: String? = null
        var firstY: String? = null

        searchView.setOnCloseListener {
            saveButton.visibility = Button.VISIBLE
            false
        }
        searchView.setOnSearchClickListener {
            saveButton.visibility = Button.INVISIBLE
            listLayout.visibility = LinearLayout.INVISIBLE
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { // do your logic here
                locationAdapter.notifyDataSetChanged()
                saveButton.visibility = Button.VISIBLE
                recyclerView.visibility = View.GONE

                Toast.makeText(context, query, Toast.LENGTH_SHORT).show()

                if (locationAdapter.clicked) {

                    //해당 요소만 찍고 위치 이동.
                    var mapPoint: MapPoint = MapPoint.mapPointWithGeoCoord(
                        locationAdapter.selectedY!!.toDouble(),
                        locationAdapter.selectedX!!.toDouble()
                    )
                    val selectedItem = createMarker("address", mapPoint)
                    mapView.addPOIItem(selectedItem)

                    changeMapCenterPoint(locationAdapter.selectedX, locationAdapter.selectedY)

                    presenter.convertAddressFromMapPOIItem(selectedItem)
                } else {
                    val call: Call<CategoryResult?>? = apiInterface.getSearchAroundLocation(
                        getString(R.string.kakao_restapi_key),
                        query,
                        7, 500, curLongitude.toString(), curLatitude.toString()
                    )
                    val callback: Callback<CategoryResult?> = object : Callback<CategoryResult?> {
                        override fun onResponse(
                            call: Call<CategoryResult?>,
                            response: Response<CategoryResult?>
                        ) {
                            if (response.isSuccessful) { //check for Response status
                                assert(response.body() != null)
                                for (document in response.body()?.getDocuments()!!) {
                                    firstX = response.body()?.getDocuments()!![0]!!.x
                                    firstY = response.body()?.getDocuments()!![0]!!.y
                                    makeAroundItem(document!!.x, document!!.y, document!!.placeName)
                                }
                                changeMapCenterPoint(firstX, firstY)
                            } else {
                                val statusCode = response.code()
                                val responseBody = response.body()
                                Log.e("jieun", statusCode.toString())
                            }
                        }

                        override fun onFailure(
                            call: Call<CategoryResult?>,
                            t: Throwable
                        ) {
                        }
                    }
                    call!!.enqueue(callback)
                    //근처의 요소들 화면에 찍기. 그리고 이동.
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

                    val callKeyword: Call<CategoryResult?>? = apiInterface.getSearchLocation(
                        getString(R.string.kakao_restapi_key),
                        "$newText ",
                        15
                    )
                    val callbackKeyword: Callback<CategoryResult?> =
                        object : Callback<CategoryResult?> {
                            //리스폰 시, 대응할 구현체
                            override fun onResponse(
                                call: Call<CategoryResult?>,
                                response: Response<CategoryResult?>
                            ) {
                                if (response.isSuccessful) { //check for Response status
                                    assert(response.body() != null)
                                    for (document in response.body()?.getDocuments()!!) {
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
                    recyclerView.visibility = View.GONE
                    saveButton.visibility = Button.INVISIBLE
                }
                return false
            }
        })

        val searchManager = this.context!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return true
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewInitialized(p0: MapView?) {
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
        curLatitude = mapView.mapCenterPoint.mapPointGeoCoord.latitude
        curLongitude = mapView.mapCenterPoint.mapPointGeoCoord.longitude
        curMarker = createMarker("", MapPoint.mapPointWithGeoCoord(curLatitude!!, curLongitude!!))
        if(listLayout.visibility == View.INVISIBLE) {
            presenter.convertAddressFromMapPOIItem(curMarker!!)
        }
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        allMapItemShow()
        if (isUp) {
            saveButton.visibility = Button.VISIBLE
            listLayout.visibility = View.INVISIBLE
            listLayout.startAnimation(translateDown)
            isUp = false
        }
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        listAdapter.notifyDataSetChanged()
        allMapItemShow()
        mapPOIItem = createMarker(" ", p1!!)
        mapPOIItem!!.markerType = MapPOIItem.MarkerType.RedPin

        var location = Location("")
        location.latitude = p1.mapPointGeoCoord.latitude
        location.longitude = p1.mapPointGeoCoord.longitude


        presenter.convertAddressFromMapPOIItem(mapPOIItem!!)
        mapView.addPOIItem(mapPOIItem)
        if (!isUp) {
            saveButton.visibility = Button.INVISIBLE
            listLayout.visibility = View.VISIBLE
            listLayout.startAnimation(translateUp)
            isUp = true
        }

        addButton.setOnClickListener {
            if (!locationNames.contains(mapPOIItem!!.itemName)) {
                listAdapter.addItem(mapPOIItem!!.itemName)
                listAdapter.addItemLocation(location)
                listAdapter.notifyDataSetChanged()
                mapPOIItem!!.markerType = MapPOIItem.MarkerType.YellowPin
                listAdapter.notifyDataSetChanged()
                locationItems.add(mapPOIItem!!)
                allMapItemShow()
            }
        }
    }

    private fun createMarker(name: String, point: MapPoint): MapPOIItem {
        var marker = MapPOIItem()
        marker.itemName = name
        marker.mapPoint = point
        marker.markerType = MapPOIItem.MarkerType.BluePin
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
        marker.isCustomImageAutoscale = false
        marker.isShowCalloutBalloonOnTouch = false
        marker.setCustomImageAnchor(0.5f, 1.0f)

        return marker
    }

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

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
        recyclerView.visibility = View.GONE
        listAdapter.notifyDataSetChanged()


        var location = Location("")
        location.latitude = p1!!.mapPoint.mapPointGeoCoord.latitude
        location.longitude = p1!!.mapPoint.mapPointGeoCoord.longitude

        if (!isUp) {
            saveButton.visibility = Button.INVISIBLE
            listLayout.visibility = View.VISIBLE
            listLayout.startAnimation(translateUp)
            isUp = true
        }
        locationTextView.text = p1!!.itemName
        addButton.setOnClickListener {
            if (!locationNames.contains(p1!!.itemName)) {
                p1!!.markerType = MapPOIItem.MarkerType.YellowPin
                listAdapter.addItem(p1!!.itemName)
                listAdapter.addItemLocation(location)
                listAdapter.notifyDataSetChanged()
                locationItems.add(p1!!)
                allMapItemShow()
            }
        }
    }

    private fun allMapItemShow() {
        mapView.removeAllPOIItems()
        for (mapItem in locationItems) {
            mapView.addPOIItem(mapItem)
        }
        for (aroundMapItem in aroundLocationItems) {
            mapView.addPOIItem(aroundMapItem)
        }
    }

    override fun getLocationName(mapPOIItem: MapPOIItem, locationName: String?) {
        mapPOIItem.itemName = locationName
        locationTextView.text = locationName
    }

    fun changeMapCenterPoint(x: String?, y: String?) {
        if (x != null && y != null) {
            mapView.moveCamera(
                CameraUpdateFactory.newMapPoint(
                    MapPoint.mapPointWithGeoCoord(
                        y.toDouble(),
                        x.toDouble()
                    )
                )
            )
        }
    }

    fun makeAroundItem(x: String?, y: String?, location: String?) {
        if (x != null && y != null && location != null) {
            var mapPoint: MapPoint = MapPoint.mapPointWithGeoCoord(y.toDouble(), x.toDouble())
            var item = createMarker(location, mapPoint)
            mapView.addPOIItem(item)
            aroundLocationItems.add(item)
        }

    }
}