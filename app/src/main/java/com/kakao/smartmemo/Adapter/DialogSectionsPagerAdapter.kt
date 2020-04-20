package com.kakao.smartmemo.Adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kakao.smartmemo.Contract.DialogAdapterContract
import com.kakao.smartmemo.View.FragmentDialog
import com.kakao.smartmemo.View.MemoDialog
import com.kakao.smartmemo.View.PlaceAlarmDialog

class DialogSectionsPagerAdapter(private val context: FragmentDialog, fm: FragmentManager) : FragmentPagerAdapter(fm), DialogAdapterContract.View, DialogAdapterContract.Model {
    private var type: Int? = null

    override fun getItem(position: Int): Fragment {
        when (type) {
            0 -> return MemoDialog()
            1 -> return PlaceAlarmDialog()
            else -> {
                when (position) {
                    0 -> {
                        return MemoDialog()
                    }
                    1 -> {
                        return PlaceAlarmDialog()
                    }
                }
                return MemoDialog()
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