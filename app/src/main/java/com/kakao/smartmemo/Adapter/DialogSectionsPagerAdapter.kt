package com.kakao.smartmemo.Adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kakao.smartmemo.Contract.DialogAdapterContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Data.PlaceAlarmData
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.View.MemoDialog
import com.kakao.smartmemo.View.PlaceAlarmDialog

class DialogSectionsPagerAdapter(memo: MutableList<MemoData>, todo: MutableList<PlaceAlarmData>, fm: FragmentManager) : FragmentPagerAdapter(fm), DialogAdapterContract.View, DialogAdapterContract.Model {
    private var type: Int? = null
    private var memoList = memo
    private var todoList = todo

    override fun getItem(position: Int): Fragment {
        when (type) {
            0 -> return MemoDialog(memoList)
            1 -> return PlaceAlarmDialog(todoList)
            else -> {
                when (position) {
                    0 -> {
                        return MemoDialog(memoList)
                    }
                    1 -> {
                        return PlaceAlarmDialog(todoList)
                    }
                }
                return MemoDialog(memoList)
            }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        //Log.e("FragmentPagerAdapter", "destroyItem position : $position")
    }

    override fun getCount(): Int {
        return when (type) {
            0 -> 1
            1 -> 1
            else -> 2
        }
    }

    fun setCurType(type: Int) {
        this.type = type
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }
}