package com.kakao.smartmemo

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.kakao.smartmemo.Adapter.TodoDeleteAdapter
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.View.MainActivity
import com.kakao.smartmemo.com.kakao.smartmemo.Adapter.TodoAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.todolist_fragment.*
import kotlinx.android.synthetic.main.todolist_fragment.view.*

class TodoListFragment : Fragment() {

    private lateinit var todolist : ListView
    private lateinit var todoEditingbtn : ImageButton
    private lateinit var todoDeletebtn : ImageButton
    private var todoArrayList = arrayListOf<TodoData>(TodoData("약먹기"), TodoData("도서관 책 반납"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.todolist_fragment, container, false)
        todoEditingbtn = view.findViewById(R.id.imagebtn_save) as ImageButton
        todoDeletebtn = view.findViewById(R.id.imagebtn_delete) as ImageButton

        todolist = view.findViewById(R.id.todolist) as ListView
        todolist.adapter = TodoAdapter(view.context, todoArrayList)
      
        todoDeletebtn.setOnClickListener(View.OnClickListener {
              todolist.choiceMode = ListView.CHOICE_MODE_MULTIPLE
              todolist.adapter = TodoDeleteAdapter(
                  view.context,
                  todoArrayList
              )
              todoDeletebtn.visibility = GONE
              todoEditingbtn.visibility = VISIBLE
              todo_delete_cancel.visibility= VISIBLE
            todo_delete_cancel.cancel_btn.setOnClickListener {  //취소버튼 눌렀을시
                todolist.adapter = TodoAdapter(view.context, todoArrayList)
                todoDeletebtn.visibility = VISIBLE
                todoEditingbtn.visibility = GONE
                todo_delete_cancel.visibility= GONE
            }
        })
      
        todoEditingbtn.setOnClickListener(View.OnClickListener {
              todolist.choiceMode = ListView.CHOICE_MODE_MULTIPLE
              todolist.adapter =
                  TodoAdapter(
                      view.context,
                      todoArrayList
                  )
              todoDeletebtn.visibility = VISIBLE
              todoEditingbtn.visibility = GONE
              todo_delete_cancel.visibility= GONE
        })
//        todoEditingbtn.setOnClickListener {
//
//            for(i in 0 until todolist.adapter.count-1) {
//                Toast.makeText(context, "todoEditingBtn 클릭됨", Toast.LENGTH_SHORT).show()
//                var listItem = todolist.adapter.getItem(i)
//                var deleteImg = getResources().getDrawable(R.drawable.delete_todo)
//                //listItem.btn_todo.background = deleteImg
//
//            }
//        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title="Todo List"
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_group_in_todolist, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                (activity as MainActivity).mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            R.id.action_settings_select -> { //그룹선택
                return true
            }
            R.id.action_settings_total -> { //전체 TODOLIST
                return true
            }
            R.id.action_settings_my -> { // 내 TODOLIST
                return true
            }
            R.id.action_settings_group1 -> { //그룹1
                return true
            }
            R.id.action_settings_group2 -> { //그룹2
                return true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    fun onCreateDialog() : Dialog {
        return let {
            val builder = AlertDialog.Builder(activity)
            val edit_inflater: LayoutInflater = LayoutInflater.from(context)
            val editDialogView: View = edit_inflater.inflate(R.layout.todolist_editing_dialog, null)
            builder.setTitle("TODO LIST 입력")
            val spinner: Spinner = editDialogView.findViewById(R.id.spinner)
            val adapter = ArrayAdapter.createFromResource(context, R.array.group, android.R.layout.simple_spinner_item)
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