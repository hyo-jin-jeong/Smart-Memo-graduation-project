package com.kakao.smartmemo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.kakao.smartmemo.Contract.ManagementAdapterContract
import com.kakao.smartmemo.Object.FolderObject
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.folder_list_view.view.*

class ManagementAdapter(val context: Context, private val folderList: MutableList<String>) : BaseAdapter(), ManagementAdapterContract.Model, ManagementAdapterContract.View {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.folder_list_view, null)
        val folder = folderList[position]
        var checkedTodo = false
        view.folder_name.text = FolderObject.folderInfo[folder]
        return view
    }

    override fun getItem(position: Int) : String {
        return folderList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return folderList.size
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }
}