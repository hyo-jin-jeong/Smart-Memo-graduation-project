package com.kakao.smartmemo.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast

import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.kakao.smartmemo.Adapter.SectionsPagerAdapter
import com.kakao.smartmemo.Contract.MainContract
import com.kakao.smartmemo.Model.MainLocationModel
import com.kakao.smartmemo.Object.GroupObject
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
    lateinit var memberName: TextView
    private val context: Context = this
    private lateinit var groupIdList : MutableList<String>
    var openFlag:Boolean = false

    private lateinit var mainLocationModel: MainLocationModel

    override fun onStart() {
        super.onStart()
        Log.e("onStart - main", "onStart")
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
        memberName = naviHeaderView.findViewById(R.id.nav_name)

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
                -1 -> {
                }
                groupIdList.size+1 -> {
                    val nextIntent = Intent(this, AddGroup::class.java)
                    startActivity(nextIntent)
                }
                else -> {
                    var groupSettingIntent = Intent(this, ModifyGroup::class.java)
                    groupSettingIntent.putExtra("groupId", groupIdList[menuItem.itemId-1])

                    startActivity(groupSettingIntent)
                }
            }
            menuItem.isChecked=false
            true
        }
        naviHeaderView.setOnClickListener {
            if (UserObject == null) {
                Toast.makeText(this, "회원정보를 가져오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                Log.e("GetProfile Error", "UserObject is null")
            } else {
                val memberData = Intent(this, MemberData::class.java)
                startActivity(memberData)
            }
        }

        setFloatingIcon()
        fab.setOnClickListener(this)
        fabMemo.setOnClickListener(this)
        fabTodo.setOnClickListener(this)

        mainLocationModel = MainLocationModel()
        val location = mainLocationModel.getLocation(context)
        if(location != null)
            mainLocationModel.convertAddressFromMapPOIItem(location.longitude.toString(), location.latitude.toString())
    }

    @SuppressLint("ResourceType")
    override fun setNavigationView(groupInfoList: MutableList<String>){ // call back func
            groupIdList = groupInfoList
            var i = 1
            navigationView.menu.clear()

            groupInfoList.forEach {
                if(it == "내 폴더"){
                    navigationView.menu.add(0,-1,0,GroupObject.groupInfo[it]).setIcon(R.drawable.setting_icon)
                }
                else{
                    navigationView.menu.add(1,i,i,GroupObject.groupInfo[it]).setIcon(R.drawable.setting_icon)
                    i++
                }

            }
            navigationView.menu.add(2,groupInfoList.size+1,groupInfoList.size+1,"폴더 추가").setIcon(R.drawable.plus_group)
    }



    override fun onResume() {
        super.onResume()
        memberName.text = UserObject.user_name
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
                    addMemoIntent.putExtra("longitude", mainLocationModel.longitude.toString())
                    addMemoIntent.putExtra("latitude", mainLocationModel.latitude.toString())
                    addMemoIntent.putExtra("address", mainLocationModel.locationAddress)
                }
                startActivity(addMemoIntent)
            }
            R.id.fab_todo -> {
                anim()
                val addTodoIntent = Intent(this, AddTodo::class.java)
                if(mainLocationModel.checkValue()) {
                    addTodoIntent.putExtra("longitude", mainLocationModel.longitude.toString())
                    addTodoIntent.putExtra("latitude", mainLocationModel.latitude.toString())
                    addTodoIntent.putExtra("address", mainLocationModel.locationAddress)
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
