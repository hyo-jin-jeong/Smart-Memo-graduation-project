package com.kakao.smartmemo.View


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast

import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.kakao.smartmemo.*
import com.kakao.smartmemo.Adapter.SectionsPagerAdapter
import com.kakao.smartmemo.Contract.MainContract
import com.kakao.smartmemo.Object.UserObject
import com.kakao.smartmemo.Presenter.MainPresenter

class MainActivity : AppCompatActivity(), View.OnClickListener,MainContract.View {
    lateinit var presenter : MainContract.Presenter
    private lateinit var myToolbar: Toolbar
    private lateinit var fabRotateStart:Animation
    private lateinit var fabRotateEnd:Animation
    private lateinit var fabOpen:Animation
    private lateinit var fabClose:Animation
    lateinit var fab:FloatingActionButton
    private lateinit var fabMemo:FloatingActionButton
    private lateinit var fabTodo:FloatingActionButton
    lateinit var navigationView: NavigationView
    lateinit var mDrawerLayout: DrawerLayout
    private val context: Context = this
    private lateinit var groupList : MutableList<String>
    private var REQUEST_CODE = 1234;
    var openFlag:Boolean = false

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
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        // Toolbar
        myToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(myToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)

        initGroup()//drawerlayout init func

        navigationView = findViewById(R.id.nav_view)

        mDrawerLayout = findViewById(R.id.drawer_layout)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout!!.closeDrawers()
            when(val id = menuItem.itemId) {

                R.id.nav_my_memo -> {

                }
                R.id.nav_group_add -> {
                    val nextIntent = Intent(this, AddGroup::class.java)
                    startActivity(nextIntent)
                }
                else -> {
                    var groupSettingIntent = Intent(this, ModifyGroup::class.java)
                    groupSettingIntent.putExtra("groupName", groupList[id])
                    startActivity(groupSettingIntent)
                }
            }
            menuItem.isChecked=false
            true
        }

        val naviHeaderView =navigationView.getHeaderView(0)
        val memberIcon = naviHeaderView.findViewById<ImageView>(R.id.member_icon)

        memberIcon.setOnClickListener {
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
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when(requestCode){
//            1234->{
//                if(resultCode == Activity.RESULT_OK){
//
//                }
//            }
//        }
//    }
    override fun setNavigationView(name : MutableList<String>){ // call back func
            groupList = name
            for (i in 0 until groupList.size) {
                //Log.e("navi",groupList[i])
                navigationView.menu.add(groupList[i])
            }
    }

    private fun initGroup(){ // group data setting
        presenter.getGroupData()
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
                val memoIntent = Intent(this, AddMemo::class.java)
                startActivity(memoIntent)
            }
            R.id.fab_todo -> {
                anim()
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
                fabMemo.isClickable = true
                openFlag = true
            }
        }
    }

}