package com.kakao.smartmemo.View

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.smartmemo.Adapter.TodoAdapter
import com.kakao.smartmemo.Adapter.TodoDeleteAdapter
import com.kakao.smartmemo.Contract.TodoContract
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.Presenter.TodoPresenter
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Receiver.DeviceBootTodoReceiver
import com.kakao.smartmemo.Receiver.TodoReceiver
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.util.*


class TodoListFragment : Fragment(), TodoContract.View {

    private lateinit var presenter : TodoContract.Presenter
    private lateinit var todolist : ListView
    private lateinit var bottomnavigationview : BottomNavigationView
    private lateinit var textviewTodolist : TextView
    private var todoArrayList = arrayListOf<TodoData>(TodoData("약먹기"), TodoData("도서관 책 반납"))
    private lateinit var adapter : TodoAdapter
    private lateinit var deleteAdapter : TodoDeleteAdapter
    val date: LocalDateTime = LocalDateTime.now()
    val todoCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.todolist_fragment, container, false)

        bottomnavigationview = view.findViewById(R.id.navigationview_bottom)
        textviewTodolist = view.findViewById(R.id.textView_todolist)

        presenter = TodoPresenter(this)
        adapter = TodoAdapter(view.context, todoArrayList)
        deleteAdapter = TodoDeleteAdapter(view.context, todoArrayList)

        todolist = view.findViewById(R.id.todolist) as ListView
        todolist.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        todolist.adapter = adapter
        presenter.setTodoAdapterModel(adapter)
        presenter.setTodoAdapterView(adapter)

        //setTodoAlarm(todoCalendar)

        todolist.isClickable = true
        todolist.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(view.context, AllTodoSettingActivity::class.java)
            intent.putExtra("todo_id", view.id)
            startActivity(intent)
        }


        //하단 메뉴
        bottomnavigationview.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.removeItem -> {
                    var position = todolist.checkedItemPosition
                    todoArrayList.removeAt(position+1)
                    todolist.clearChoices()
                    adapter.notifyAdapter()
                    bottomnavigationview.visibility = GONE //하단메뉴 안보이게
                    todolist.adapter = TodoAdapter(view.context, todoArrayList)
                    true
                }
                R.id.cancelItem -> {
                    bottomnavigationview.visibility = GONE //하단메뉴 안보이게
                    todolist.adapter = TodoAdapter(view.context, todoArrayList)
                    true
                }
            }
            true
        }

        bottomnavigationview.visibility = GONE; //하단메뉴 안보이게

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title="Todo List"
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_group_in_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                (activity as MainActivity).mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            R.id.select_group -> {
                selectGroup()
                return true
            }
            R.id.delete_memo ->{
                deleteTodo()
                return true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    private fun selectGroup(){
        val items = arrayOf<CharSequence>("메모", "TODO 장소알람")//데베에서 가져오기
        val listDialog: AlertDialog.Builder = AlertDialog.Builder(
            this.context,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        )
        listDialog.setTitle("그룹 선택")
            .setItems(items, DialogInterface.OnClickListener { _, which ->
               //그룹선택 구현
            })
            .show()
    }
    private fun deleteTodo(){
        todolist.adapter = deleteAdapter
        presenter.setTodoDeleteAdapterModel(deleteAdapter)
        presenter.setTodoDeleteAdapterView(deleteAdapter)
        bottomnavigationview.visibility = VISIBLE //하단메뉴 보이게
    }

    private fun setTodoAlarm(calendar: Calendar) {
        val pm = context!!.packageManager
        val receiver = ComponentName(context, DeviceBootTodoReceiver::class.java)
        val alarmIntent = Intent(context, TodoReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if(alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }

            //부팅후 실행되는 리시버 사용가능하게 설정함.
            pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        }
        else { // 알람을 허용하지 않았다면
            if(PendingIntent.getBroadcast(context, 0, alarmIntent, 0)!=null && alarmManager!=null) {
                alarmManager.cancel(pendingIntent)
            }
            pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
        }
    }
}