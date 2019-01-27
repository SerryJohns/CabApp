package com.serionz.mytaxi.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.serionz.mytaxi.CabMapFragment
import com.serionz.mytaxi.CabsListFragment

/**
 * Created by johns on 23/01/2019.
 */

class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return if (position < 1) {
            CabsListFragment()
        } else {
            CabMapFragment()
        }
    }

    override fun getCount() = 2
}