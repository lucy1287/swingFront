package com.example.roadkill

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class StatusVPAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ManagerHistoryFragment()
            1 -> ManagerHistoryFragmentTrue()
            else -> Fragment()
        }
    }

    override fun getItemCount(): Int {
        return 2 // 페이지 수
    }
}