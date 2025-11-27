package com.git.classwork.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {
    private lateinit var btnViewMore: Button
    private lateinit var btnNews: Button
    private lateinit var btnKnowledge: Button
    private lateinit var btnTrend: Button
    private lateinit var btnCommunity: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 加载布局
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initViews(view)
        setupListeners()
        return view
    }

    private fun initViews(view: View) {
        btnViewMore = view.findViewById(R.id.buttonViewMore)
        btnNews = view.findViewById(R.id.btnNews)
        btnKnowledge = view.findViewById(R.id.btnKnowledge)
        btnTrend = view.findViewById(R.id.btnTrend)
        btnCommunity = view.findViewById(R.id.btnCommunity)
    }

    private fun setupListeners() {
        // 今日推荐立即查看按钮 - 使用导航组件
        btnViewMore.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_infoList)
        }

        // 四个功能按钮的点击事件
        btnNews.setOnClickListener {
            Toast.makeText(requireContext(), "进入资讯页面", Toast.LENGTH_SHORT).show()
        }

        btnKnowledge.setOnClickListener {
            Toast.makeText(requireContext(), "进入知识页面", Toast.LENGTH_SHORT).show()
        }

        btnTrend.setOnClickListener {
            Toast.makeText(requireContext(), "进入趋势页面", Toast.LENGTH_SHORT).show()
        }

        btnCommunity.setOnClickListener {
            Toast.makeText(requireContext(), "进入社区页面", Toast.LENGTH_SHORT).show()
        }
    }
}