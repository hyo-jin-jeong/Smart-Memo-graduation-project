package com.kakao.smartmemo

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.kakao.smartmemo.ui.main.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var myToolbar: Toolbar
    private lateinit var fab_rotate_start:Animation
    private lateinit var fab_rotate_end:Animation
    private lateinit var fab_open:Animation
    private lateinit var fab_close:Animation
    lateinit var fab:FloatingActionButton
    lateinit var fab_memo:FloatingActionButton
    lateinit var fab_todo:FloatingActionButton
    lateinit var mDrawerLayout: DrawerLayout
    private val context: Context = this
    var openFlag:Boolean = false
    // @JvmField val currActivity = getActivity(this,100,,Intent.FLAG_ACTIVITY_CLEAR_TOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        // Toolbar
        myToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(myToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)

        val navigationView: NavigationView = findViewById(R.id.nav_view) as NavigationView
        mDrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout!!.closeDrawers()
            val id = menuItem.itemId
            val title = menuItem.title.toString()
            if (id == R.id.nav_my_memo) {
                Toast.makeText(context, "$title: 내 메모를 확인합니다.", Toast.LENGTH_SHORT).show()
            } else if (id == R.id.addGroup) {

                val nextIntent = Intent(this, AddGroup::class.java)
                startActivity(nextIntent)
            }
            menuItem.isChecked=false
            true
        }

        // FloatingActionButton
        fab_rotate_start = AnimationUtils.loadAnimation(context, R.anim.fab_rotate_start)
        fab_rotate_end = AnimationUtils.loadAnimation(context, R.anim.fab_rotate_end)
        fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open)
        fab_close = AnimationUtils.loadAnimation(context, R.anim.fab_close)

        fab = findViewById(R.id.fab)
        fab_memo = findViewById(R.id.fab_memo)
        fab_todo = findViewById(R.id.fab_todo)

        fab_memo.startAnimation(fab_close)
        fab_todo.startAnimation(fab_close)
        fab_memo.isClickable = false
        fab_todo.isClickable = false

        fab.setOnClickListener(this)
        fab_memo.setOnClickListener(this)
        fab_todo.setOnClickListener(this)
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
                fab.startAnimation(fab_rotate_end)
                fab_memo.startAnimation(fab_close)
                fab_todo.startAnimation(fab_close)
                fab_memo.isClickable = false
                fab_todo.isClickable = false
                openFlag = false
            }
            else -> {
                fab.startAnimation(fab_rotate_start)
                fab_memo.startAnimation(fab_open)
                fab_todo.startAnimation(fab_open)
                fab_memo.isClickable = true
                fab_memo.isClickable = true
                openFlag = true
            }
        }
    }

}