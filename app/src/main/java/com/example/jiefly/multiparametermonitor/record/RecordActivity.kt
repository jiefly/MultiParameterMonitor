package com.example.jiefly.multiparametermonitor.record

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.example.jiefly.multiparametermonitor.R
import com.example.jiefly.multiparametermonitor.record.fragment.BaseRecordItemFragment
import com.example.jiefly.multiparametermonitor.record.fragment.HeartRateRecordFragment

class RecordActivity : AppCompatActivity() {
    private var mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener? = null
    private var toolbarTitle: TextView? = null
    private var navigation: BottomNavigationView? = null
    private var viewpager: ViewPager? = null
    private var prevMenuItem: MenuItem? = null
    val fragmentCollection = ArrayList<BaseRecordItemFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        toolbarTitle = findViewById(R.id.id_tool_bar_title) as TextView?
        toolbarTitle?.text = "历史记录"
        findViewById(R.id.id_tool_bar_left_arrow)?.setOnClickListener { onBackPressed() }
        initNavigation()
        initViewPager()

    }


    private fun initViewPager() {
        viewpager = findViewById(R.id.id_record_vp) as ViewPager?
        initFragmentData()
        viewpager!!.adapter = ViewPagerAdapter(supportFragmentManager)
        viewpager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                invalidateOptionsMenu()
                /**
                 * 该方法只有在有新的页面被选中时才会回调
                 *
                 * 如果 preMenuItem 为 null，说明该方法还没有被回调过
                 * 则ViewPager从创建到现在都处于 position 为 0 的页面
                 * 所以当该方法第一次被回调的时候，直接将 position 为 0 的页面的 selected 状态设为 false 即可
                 *
                 * 如果 preMenuItem 不为 null，说明该方法内的
                 * "prevMenuItem = bottomNavView.getMenu().getItem(position);"
                 * 之前至少被调用过一次
                 * 所以当该方法再次被回调的时候，直接将上一个 prevMenuItem 的 selected 状态设为 false 即可
                 * 在做完上一句的事情后，将当前页面设为 prevMenuItem，以备下次调用
                 *
                 * 我注释写这么详细，是不是要给我搭个赏~ (ಥ_ಥ)
                 */
                if (prevMenuItem == null) {
                    navigation!!.menu.getItem(0).isChecked = false
                } else {
                    prevMenuItem!!.isChecked = false
                }

                navigation!!.menu.getItem(position).isChecked = true
                prevMenuItem = navigation!!.menu.getItem(position)
            }
        })
    }

    private fun initFragmentData() {
        for (i in 0..4) {
            if (i == 3) {
                fragmentCollection.add(HeartRateRecordFragment())
            } else {
                fragmentCollection.add(BaseRecordItemFragment())
            }
        }
    }


    private fun initNavigation() {
        mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    //fragmentCollection[0].setTitle("心电")
                    viewpager!!.currentItem = 0
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    //fragmentCollection[1].setTitle("血压")
                    viewpager!!.currentItem = 1
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    //fragmentCollection[2].setTitle("血氧")
                    viewpager!!.currentItem = 2
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_heart_rate -> {
                    //fragmentCollection[3].setTitle("心率")
                    viewpager!!.currentItem = 3
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_tem -> {
                    //fragmentCollection[4].setTitle("体温")
                    viewpager!!.currentItem = 4
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
        navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    inner class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            return fragmentCollection[position]
        }

        override fun getCount(): Int {
            return fragmentCollection.size
        }
    }

}
