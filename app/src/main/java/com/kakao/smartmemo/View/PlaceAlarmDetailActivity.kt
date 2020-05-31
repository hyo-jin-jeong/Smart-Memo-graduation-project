package com.kakao.smartmemo.View

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kakao.smartmemo.Adapter.LocationListAdapter
import com.kakao.smartmemo.ApiConnect.ApiClient
import com.kakao.smartmemo.ApiConnect.ApiInterface
import com.kakao.smartmemo.ApiConnect.CategoryResult
import com.kakao.smartmemo.Contract.PlaceAlarmDetailContract
import com.kakao.smartmemo.Presenter.PlaceAlarmDetailPresenter
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.main_dialog.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.jetbrains.annotations.NotNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class PlaceAlarmDetailActivity: AppCompatActivity(), PlaceAlarmDetailContract.View, MapView.POIItemEventListener, MapView.MapViewEventListener {
    lateinit var presenter : PlaceAlarmDetailContract.Presenter
    private lateinit var myToolbar: Toolbar
    private lateinit var mapView: MapView
    private lateinit var mapViewContainer: ViewGroup
    private val context: Context = this

    private lateinit var listAdapter: LocationListAdapter
    private lateinit var saveButton: Button
    private lateinit var addButton: Button
    private lateinit var locationTextView: TextView
    private val locations = ArrayList<String>()
    private var mapPOIItem: MapPOIItem? = null
    private var locationItems = ArrayList<MapPOIItem>()

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

        mapView.setMapViewEventListener(this)
        mapView.setPOIItemEventListener(this)

        val listView: ListView = findViewById(R.id.listView)
        listAdapter = LocationListAdapter(context, locations)
        listView.adapter = listAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            locations.remove(locations[position])
            listAdapter.notifyDataSetChanged()
        }


        var longitude = intent.getDoubleExtra("longitude", 0.0)
        var latitude = intent.getDoubleExtra("latitude", 0.0)
        var address = intent.getStringExtra("address")

        var mapPoint: MapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)
        mapView.setMapCenterPoint(mapPoint, false)

        val longPressedItem = createMarker(address, mapPoint, R.drawable.cur_location_icon)
        mapView.addPOIItem(longPressedItem)
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
        searchView.setOnCloseListener {
            saveButton.visibility = Button.VISIBLE
            false
        }
        searchView.setOnSearchClickListener {
            saveButton.visibility = Button.INVISIBLE
        }

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
        allMapItemShow()
        mapPOIItem = createMarker(" ", p1!!, R.drawable.cur_location_icon)

        presenter.convertAddressFromMapPOIItem(mapPOIItem!!)
        mapView.addPOIItem(mapPOIItem)
        if(!isUp) {
            saveButton.visibility = Button.INVISIBLE
            listLayout.visibility = View.VISIBLE
            listLayout.startAnimation(translateUp)
            isUp = true
        }

        addButton.setOnClickListener {
            if(!locations.contains(mapPOIItem!!.itemName)) {
                locations.add(mapPOIItem!!.itemName)
                listAdapter.notifyDataSetChanged()
                locationItems.add(mapPOIItem!!)
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
        if(!isUp) {
            saveButton.visibility = Button.INVISIBLE
            listLayout.visibility = View.VISIBLE
            listLayout.startAnimation(translateUp)
            isUp = true
        }
        locationTextView.text = p1!!.itemName
        addButton.setOnClickListener {
            if(!locations.contains(p1!!.itemName)) {
                locations.add(p1!!.itemName)
                listAdapter.notifyDataSetChanged()
                locationItems.add(p1!!)
            }
        }
    }

    private fun allMapItemShow() {
        mapView.removeAllPOIItems()
        for (mapItem in locationItems) {
            mapView.addPOIItem(mapItem)
        }
    }

    override fun getLocationName(mapPOIItem: MapPOIItem, locationName: String?) {
        mapPOIItem.itemName = locationName
        locationTextView.text = locationName
    }
}