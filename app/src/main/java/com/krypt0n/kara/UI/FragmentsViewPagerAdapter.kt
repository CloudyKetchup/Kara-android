package com.krypt0n.kara.UI

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class FragmentsViewPagerAdapter (fragment : FragmentManager) : FragmentPagerAdapter(fragment) {

    private val listFragment = ArrayList<Fragment>()
    private val listTitles = ArrayList<String>()

    override fun getCount(): Int {
        return listTitles.size
    }
    override fun getItem(position: Int): Fragment {
        return listFragment.get(position)
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return listTitles.get(position)
    }
    fun addFragment(fragment: Fragment,title : String){
        listFragment.add(fragment)
        listTitles.add(title)
    }
}