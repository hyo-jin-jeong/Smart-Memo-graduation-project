package com.kakao.smartmemo.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Animatable
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.toColorInt
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.kakao.smartmemo.Adapter.SectionsPagerAdapter
import com.kakao.smartmemo.Contract.MainContract
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Model.MainLocationModel
import com.kakao.smartmemo.Object.FolderObject
import com.kakao.smartmemo.Object.UserObject
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
    lateinit var navigationView: NavigationView
    lateinit var mDrawerLayout: DrawerLayout

    private lateinit var profileImageView: ImageView
    private lateinit var userId: TextView
    private lateinit var editFolderImageView: ImageView
    private val context: Context = this
    private lateinit var groupIdList : MutableList<String>
    var openFlag:Boolean = false

    private lateinit var mainLocationModel: MainLocationModel
    private var mLocationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mServiceHandler: Handler? = null

    override fun onStart() {
        super.onStart()

        userId.text = UserObject.email
        presenter.getGroupInfo()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)

        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        presenter.setMainAdapterModel(sectionsPagerAdapter)
        presenter.setMainAdapterView(sectionsPagerAdapter)
        navigationView = findViewById(R.id.nav_view)
        mDrawerLayout = findViewById(R.id.drawer_layout)

        val naviHeaderView = navigationView.getHeaderView(0)
        profileImageView = naviHeaderView.findViewById(R.id.nav_member_icon)
        userId = naviHeaderView.findViewById(R.id.nav_id)
        editFolderImageView = naviHeaderView.findViewById(R.id.nav_edit_folder)

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

//        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//
//            }
//
//        })

        // Toolbar
        myToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(myToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout!!.closeDrawers()
            when(val id = menuItem.itemId) {
                0 -> {
                }
                groupIdList.size+1 -> {
                    val nextIntent = Intent(this, AddFolder::class.java)
                    startActivity(nextIntent)
                }
                else -> {
                    
//                    var groupSettingIntent = Intent(this, ModifyGroup::class.java)
//                    groupSettingIntent.putExtra("groupId", groupIdList[menuItem.itemId-1])
//
//                    startActivity(groupSettingIntent)
                }
            }
            menuItem.isChecked=false
            true
        }
        profileImageView.setOnClickListener {
            if (UserObject == null) {
                Toast.makeText(this, "회원정보를 가져오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                Log.e("GetProfile Error", "UserObject is null")
            } else {
                val memberData = Intent(this, MemberData::class.java)
                startActivity(memberData)
            }
        }
        editFolderImageView.setOnClickListener {
            //Folder edit activity 만들기
        }

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

    @SuppressLint("ResourceType")
    override fun setNavigationView(groupInfoList: MutableList<String>){ // call back func
            groupIdList = groupInfoList
            var i = 1
            navigationView.menu.clear()
            FolderObject.folderInfo.toSortedMap()
            groupInfoList.forEach {
                if(FolderObject.folderInfo[it] == "내 폴더"){
                    navigationView.menu.add(0,0,0,FolderObject.folderInfo[it])
                }
                else{
                    navigationView.menu.add(1,i,i,FolderObject.folderInfo[it])
                    if(FolderObject.folerShare[it]!!){
                        navigationView.menu.getItem(i-1).setIcon(R.drawable.share)
//                        FolderObject.folderColor[it]?.toInt()?.let { it1 ->
//                            navigationView.menu.getItem(i-1).icon.setColorFilter(it1, PorterDuff.Mode.SRC_IN)
//                        }//공유 폴더 icon색바꾸기
                    }
                    i++
                }

            }
            navigationView.menu.add(2,groupInfoList.size+1,groupInfoList.size+1,"폴더 추가").setIcon(R.drawable.plus_group)

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
                    val placeData = PlaceData(mainLocationModel.locationAddress!!, mainLocationModel.latitude!!.toDouble(), mainLocationModel.longitude!!.toDouble())
                    addMemoIntent.putExtra("placeData", placeData)
                }
                startActivity(addMemoIntent)
            }
            R.id.fab_todo -> {
                anim()
                val addTodoIntent = Intent(this, AddTodo::class.java)
                if(mainLocationModel.checkValue()) {
                    val placeData = PlaceData(mainLocationModel.locationAddress!!, mainLocationModel.latitude!!.toDouble(), mainLocationModel.longitude!!.toDouble())
                    addTodoIntent.putExtra("placeData", placeData)
                    addTodoIntent.putExtra("mode", "fromMain")
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



}
