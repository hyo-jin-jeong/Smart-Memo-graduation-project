package com.kakao.smartmemo.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.kakao.smartmemo.Adapter.SectionsPagerAdapter
import com.kakao.smartmemo.Contract.MainContract
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Model.MainLocationModel
import com.kakao.smartmemo.Presenter.MainPresenter
import com.kakao.smartmemo.R


class MainActivity : AppCompatActivity(), View.OnClickListener,MainContract.View {
    lateinit var presenter : MainContract.Presenter
    private lateinit var myToolbar: Toolbar
    private lateinit var fabRotateStart:Animation
    private lateinit var fabRotateEnd:Animation
    private lateinit var fabOpen:Animation
    private lateinit var fabClose:Animation
    private lateinit var fab:FloatingActionButton
    private lateinit var fabMemo:FloatingActionButton
    private lateinit var fabTodo:FloatingActionButton
    lateinit var mDrawerLayout: DrawerLayout
    private val context: Context = this
    var openFlag:Boolean = false


    private lateinit var mainLocationModel: MainLocationModel
    private var mLocationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mServiceHandler: Handler? = null
    lateinit var dialog : InvitedDialog

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(intent.hasExtra("value")) {
            val value = intent.getStringExtra("value")
            val groupName = intent.getStringExtra("group_name")
            val groupId = intent.getStringExtra("groupId")
            Log.i("jieun", "MainActivity value=$value")
            if(value == "1") {
                dialog = InvitedDialog(this)
                dialog.setOnOKClickedListener { content->
                    if (content == "ok"){
                        presenter.checkFolderMember(groupId,groupName)

                    }
                    else {
                        //공유 초대 취소 눌렀을 경우
                    }
                }
                dialog.startDialog(groupName)
            }
        }

        presenter = MainPresenter(this)
        presenter.getGroupInfo()

        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )

        val viewPager: ViewPager = findViewById(R.id.view_pager)

        viewPager.adapter = sectionsPagerAdapter

        presenter.setMainAdapterModel(sectionsPagerAdapter)
        presenter.setMainAdapterView(sectionsPagerAdapter)
        mDrawerLayout = findViewById(R.id.drawer_layout)


        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)?.setIcon(R.drawable.pin)
        tabs.getTabAt(1)?.setIcon(R.drawable.memo)
        tabs.getTabAt(2)?.setIcon(R.drawable.todo)
        tabs.getTabAt(3)?.setIcon(R.drawable.more)

        tabs.getTabAt(2)?.icon?.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN)

        // Toolbar
        myToolbar = findViewById(R.id.toolbar)

        setSupportActionBar(myToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)


        setFloatingIcon()
        fab.setOnClickListener(this)
        fabMemo.setOnClickListener(this)
        fabTodo.setOnClickListener(this)

        mainLocationModel = MainLocationModel()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        createLocationRequest()
        getLastLocation()
        val handlerThread = HandlerThread(MainActivity::class.java.simpleName)
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
                        var location = task.result
                        mainLocationModel.setLocation(location!!)
                        Log.e("check", "current location : $location")
                        mainLocationModel.convertAddressFromPoint(location.longitude.toString(), location.latitude.toString())
                        //mLocation = task.result
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



    private fun setFloatingIcon() {
        // FloatingActionButton
        fabRotateStart = AnimationUtils.loadAnimation(context,
            R.anim.fab_rotate_start
        )
        fabRotateEnd = AnimationUtils.loadAnimation(context,
            R.anim.fab_rotate_end
        )
        fabOpen = AnimationUtils.loadAnimation(context,
            R.anim.fab_open
        )
        fabClose = AnimationUtils.loadAnimation(context,
            R.anim.fab_close
        )

        fab = findViewById(R.id.fab)
        fabMemo = findViewById(R.id.fab_memo)
        fabTodo = findViewById(R.id.fab_todo)

        fabMemo.startAnimation(fabClose)
        fabTodo.startAnimation(fabClose)
        fabMemo.isClickable = false
        fabTodo.isClickable = false

    }
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.fab -> {
                anim()
            }
            R.id.fab_memo -> {
                anim()
                val addMemoIntent = Intent(this.context, AddMemo::class.java)
                if (mainLocationModel.checkValue()) {
                    val placeData = PlaceData("", mainLocationModel.locationAddress!!, mainLocationModel.latitude!!.toDouble(), mainLocationModel.longitude!!.toDouble())
                    addMemoIntent.putExtra("placeData", placeData)
                    Log.e("jieun", "placeData = $placeData")
                }
                startActivity(addMemoIntent)
            }
            R.id.fab_todo -> {
                anim()
                val addTodoIntent = Intent(this, AddTodo::class.java)
                if(mainLocationModel.checkValue()) {
                    val placeData = PlaceData("", mainLocationModel.locationAddress!!, mainLocationModel.latitude!!.toDouble(), mainLocationModel.longitude!!.toDouble())
                    addTodoIntent.putExtra("placeData", placeData)
                    addTodoIntent.putExtra("mode", "fromMain")
                    Log.e("jieun", "placeData = $placeData")
                }
                startActivity(addTodoIntent)
            }

        }
    }

    private fun anim() {
        when {
            openFlag -> {
                fab.startAnimation(fabRotateEnd)
                fabMemo.startAnimation(fabClose)
                fabTodo.startAnimation(fabClose)
                fabMemo.isClickable = false
                fabTodo.isClickable = false
                openFlag = false
            }
            else -> {
                fab.startAnimation(fabRotateStart)
                fabMemo.startAnimation(fabOpen)
                fabTodo.startAnimation(fabOpen)
                fabMemo.isClickable = true
                fabTodo.isClickable = true
                openFlag = true
            }
        }
    }

    override fun onSuccess() {
        dialog.dialogDismiss()
    }


}