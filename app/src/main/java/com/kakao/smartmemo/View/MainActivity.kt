package com.kakao.smartmemo.View

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
    private lateinit var groupList : HashMap<String, Long>
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
        navigationView = findViewById(R.id.nav_view)
        mDrawerLayout = findViewById(R.id.drawer_layout)

        val naviHeaderView = navigationView.getHeaderView(0)
        memberName = naviHeaderView.findViewById(R.id.nav_name)

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        // Toolbar
        myToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(myToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)

        getGroupInfo()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout!!.closeDrawers()
            when(val id = menuItem.itemId) {

                -1 -> {

                }
                groupList.size -> {
                    val nextIntent = Intent(this, AddGroup::class.java)
                    startActivity(nextIntent)
                }
                else -> {
                    var groupSettingIntent = Intent(this, ModifyGroup::class.java)
                    groupSettingIntent.putExtra("groupName", menuItem.title)
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
    }

    private fun getGroupInfo(){
        presenter.getGroupInfo()
    }
    override fun setNavigationView(groupInfoList: HashMap<String, Long>){ // call back func
            groupList = groupInfoList
            var i = 0
            navigationView.menu.clear()
            navigationView.menu.add(-1,0,0,"내메모")
            groupInfoList.forEach {
                navigationView.menu.add(0,i,i,it.key)
                i++
            }

            navigationView.menu.add(1,groupInfoList.size,groupInfoList.size,"그룹추가").setIcon(R.drawable.plus_group)
        Log.e("groupId", GroupObject.groupInfo.size.toString())
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
                val memoIntent = Intent(this, AddMemo::class.java)
                startActivity(memoIntent)
            }
            R.id.fab_todo -> {
                anim()
                onCreateDialog()
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
    fun onCreateDialog() : Dialog {
        return let {
            val builder = AlertDialog.Builder(context)
            val edit_inflater: LayoutInflater = LayoutInflater.from(context)
            val editDialogView: View = edit_inflater.inflate(R.layout.todolist_editing_dialog, null)
            builder.setTitle("TODO LIST 입력")
            val spinner: Spinner = editDialogView.findViewById(R.id.spinner)
            val adapter = ArrayAdapter.createFromResource(context,
                R.array.group, android.R.layout.simple_spinner_item)
            spinner.adapter = adapter

            builder.setView(editDialogView).setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
                .setNegativeButton("취소", DialogInterface.OnClickListener {
                        dialog, which ->
                    dialog.dismiss()
                })
            builder.create()
            builder.show()
        }
    }
}