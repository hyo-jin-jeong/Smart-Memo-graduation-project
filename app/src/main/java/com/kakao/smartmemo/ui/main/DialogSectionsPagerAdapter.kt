package com.kakao.smartmemo.ui.main

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kakao.smartmemo.FragmentDialog
import com.kakao.smartmemo.MemoDialog
import com.kakao.smartmemo.PlaceAlarmDialog

class DialogSectionsPagerAdapter(private val context: FragmentDialog, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {return MemoDialog()
            }
            1 ->  {return PlaceAlarmDialog()
            }
        }
        return MemoDialog()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        //Log.e("FragmentPagerAdapter", "destroyItem position : $position")
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}