package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roadkill.databinding.FragmentManagerHistoryBinding

class ManagerHistoryFragment : Fragment() {
    private lateinit var binding: FragmentManagerHistoryBinding
    var accidentHistoryList: ArrayList<History> = ArrayList<History>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManagerHistoryBinding.inflate(layoutInflater)

        accidentHistoryList.add(History("ㄴㅇㄴ", "2023-07-20", "중부내륙고속도로"))
        accidentHistoryList.add(History("낸", "2023-07-21", "호남선"))

        val managerHistoryRVAdapter = ManagerHistoryRVAdapter(accidentHistoryList)
        managerHistoryRVAdapter.setOnItemClickListener(object : ManagerHistoryRVAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int) {
                val intent = Intent(activity, ManagerReceiptDetailActivity::class.java)
                startActivity(intent)
            }
        })

        val managerHistoryLinearLayoutManager = LinearLayoutManager(activity)
        managerHistoryLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvHistory.layoutManager = managerHistoryLinearLayoutManager
        binding.rvHistory.adapter = managerHistoryRVAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // View 객체 초기화 및 이벤트 처리 등을 수행
    }
}