package com.kakao.smartmemo.View

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.kakao.smartmemo.Contract.MapContract
import com.kakao.smartmemo.Presenter.MapPresenter
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.main_dialog.*
import kotlinx.android.synthetic.main.map_fragment.view.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class MapFragment : Fragment(), MapView.POIItemEventListener, MapView.MapViewEventListener, MapContract.View {
    private lateinit var presenter: MapPresenter
    lateinit var mapView :MapView
    lateinit var mapViewContainer :ViewGroup
    private var isLongTouch: Boolean = false
    private val curLocationMarker: MapPOIItem = MapPOIItem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        mapViewContainer = view.map_view as ViewGroup
        mapViewContainer.addView(mapView)

        mapView.setPOIItemEventListener(this)
        mapView.setMapViewEventListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater:MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        (activity as MainActivity).toolbar.title = resources.getString(R.string.tab_text_1)

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_group_in_map, menu)
        menu.getItem(1)?.isChecked = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                (activity as MainActivity).mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            //학교를 눌렀을 때
            R.id.action_settings1 -> {
                item.isChecked = !item.isChecked
                when(item.isChecked) {
                    true -> Toast.makeText(view?.context, item.title, Toast.LENGTH_SHORT).show()
                }
                true
            }
            //가족을 눌렀을 때
            R.id.action_settings2 -> {
                item.isChecked = !item.isChecked
                when(item.isChecked) {
                    true -> Toast.makeText(view?.context, item.title, Toast.LENGTH_SHORT).show()
                }
                true
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
        var dialog = FragmentDialog()
        //type은 memo만이면 0, todo만이면 1, 둘다면 2
        when (p1?.customImageResourceId) {
            R.drawable.memo_icon -> {
                dialog.setCurType(0)
            }
            R.drawable.todo_icon -> {
                dialog.setCurType(1)
            }
            else -> {
                dialog.setCurType(2)
            }
        }
        dialog.show(super.getChildFragmentManager(), "Oh?!")
    }


    //setMapViewEventListener override method
    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewInitialized(p0: MapView?) {
        val mapPoint1 = MapPoint.mapPointWithGeoCoord(37.565841, 126.976825)
        val memoAndTodo = MapPOIItem()
        memoAndTodo.itemName = "Memo And Todo"
        memoAndTodo.tag = 1
        memoAndTodo.mapPoint = mapPoint1
        memoAndTodo.markerType = MapPOIItem.MarkerType.CustomImage // 마커타입을 커스텀 마커로 지정.
        //customMarker.markerType = MapPOIItem.MarkerType.BluePin   //기본 아이콘 사용
        //customMarker.selectedMarkerType = MapPOIItem.MarkerType.RedPin    //기본 아이콘 사용
        memoAndTodo.customImageResourceId =
            R.drawable.memo_todo_icon // 마커 이미지.
        memoAndTodo.isCustomImageAutoscale = false // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        memoAndTodo.setCustomImageAnchor(
            0.5f,
            1.0f
        ) // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        mapView.addPOIItem(memoAndTodo)

        val mapPoint2 = MapPoint.mapPointWithGeoCoord(37.565799, 126.975183)
        val memo = MapPOIItem()
        memo.itemName = "Memo"
        memo.tag = 2
        memo.mapPoint = mapPoint2
        memo.markerType = MapPOIItem.MarkerType.CustomImage
        memo.customImageResourceId = R.drawable.memo_icon
        memo.isCustomImageAutoscale = false
        memo.setCustomImageAnchor(0.5f, 1.0f)

        mapView.addPOIItem(memo)

        val mapPoint3 = MapPoint.mapPointWithGeoCoord(37.564170, 126.978471)
        val todo = MapPOIItem()
        todo.itemName = "Todo"
        todo.tag = 3
        todo.mapPoint = mapPoint3
        todo.markerType = MapPOIItem.MarkerType.CustomImage
        todo.customImageResourceId = R.drawable.todo_icon
        todo.isCustomImageAutoscale = false
        todo.setCustomImageAnchor(0.5f, 1.0f)

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

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        when {
            !isLongTouch -> isLongTouch = true
            else -> mapView.removePOIItem(curLocationMarker)
        }
        curLocationMarker.itemName = "이 곳에 추가"
        curLocationMarker.mapPoint = p1
        curLocationMarker.markerType = MapPOIItem.MarkerType.CustomImage
        curLocationMarker.customImageResourceId =
            R.drawable.cur_location_icon
        curLocationMarker.isCustomImageAutoscale = false
        curLocationMarker.setCustomImageAnchor(0.5f, 1.0f)

        mapView.addPOIItem(curLocationMarker)

    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

}




