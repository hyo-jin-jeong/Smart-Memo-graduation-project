package com.kakao.smartmemo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.kakao.smartmemo.ui.main.DialogSectionsPagerAdapter

class FragmentDialog : DialogFragment() {
    private lateinit var adapter: DialogSectionsPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.main_dialog, container, false)

        // tab slider
        adapter = DialogSectionsPagerAdapter(this, childFragmentManager)

        val indicator = CircleIndicator(context)
        // Set up the ViewPager with the sections adapter.
        viewPager = view.findViewById(R.id.dialog_pager)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) { }
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) { }
            override fun onPageSelected(p0: Int) {
                indicator.selectDot(p0)
            }
        })

        indicator.createDotPanel(2, R.drawable.indicator_dot_off, R.drawable.indicator_dot_on, 0)

        return view
    }

}