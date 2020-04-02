package com.kakao.smartmemo

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.kakao.smartmemo.ui.main.DialogSectionsPagerAdapter

class FragmentDialog : DialogFragment() {
    private lateinit var adapter: DialogSectionsPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var myDialog: Dialog
    private lateinit var myToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        myDialog = super.onCreateDialog(savedInstanceState)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setContentView(R.layout.main_dialog)
        var params: WindowManager.LayoutParams = myDialog.window.attributes
        params.width = 1000
        params.height = 1200
        myDialog.window.attributes = params

        return myDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.main_dialog, container, false)

        // tab slider
        adapter = DialogSectionsPagerAdapter(this, childFragmentManager)

        val indicator = view.findViewById<CircleIndicator>(R.id.circle_indicator)

        myToolbar = view.findViewById<Toolbar>(R.id.toolbar)
        myToolbar.setNavigationIcon(R.drawable.back)
        myToolbar.setNavigationOnClickListener {
            dismiss()
        }

        // Set up the ViewPager with the sections adapter.
        viewPager = view.findViewById<ViewPager>(R.id.dialog_pager)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) { }
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) { }
            override fun onPageSelected(p0: Int) {
                indicator.selectDot(p0)
            }
        })

        indicator.createDotPanel(2, R.drawable.indicator_dot_on, R.drawable.indicator_dot_off, 0)

        return view
    }

}