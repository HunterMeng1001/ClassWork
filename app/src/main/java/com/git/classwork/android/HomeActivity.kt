package com.git.classwork.android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color

class HomeActivity : AppCompatActivity() {
    private lateinit var btnViewMore: Button
    private lateinit var btnNews: Button
    private lateinit var btnKnowledge: Button
    private lateinit var btnTrend: Button
    private lateinit var btnCommunity: Button
    private lateinit var btnNavHome: Button
    private lateinit var btnNavInfo: Button
    private lateinit var btnNavMine: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
        
        initViews()
        setupListeners()
        
        // 设置当前页面导航按钮为选中状态
        setNavigationSelectedState("home")
    }
    
    private fun initViews() {
        btnViewMore = findViewById(R.id.buttonViewMore)
        btnNews = findViewById(R.id.btnNews)
        btnKnowledge = findViewById(R.id.btnKnowledge)
        btnTrend = findViewById(R.id.btnTrend)
        btnCommunity = findViewById(R.id.btnCommunity)
        btnNavHome = findViewById(R.id.btnNavHome)
        btnNavInfo = findViewById(R.id.btnNavInfo)
        btnNavMine = findViewById(R.id.btnNavMine)
    }
    
    private fun setupListeners() {
        // 今日推荐立即查看按钮
        btnViewMore.setOnClickListener {
            startActivity(Intent(this, InfoListActivity::class.java))
        }
        
        // 四个功能按钮的点击事件
        btnNews.setOnClickListener {
            Toast.makeText(this, "进入资讯页面", Toast.LENGTH_SHORT).show()
        }
        
        btnKnowledge.setOnClickListener {
            Toast.makeText(this, "进入知识页面", Toast.LENGTH_SHORT).show()
        }
        
        btnTrend.setOnClickListener {
            Toast.makeText(this, "进入趋势页面", Toast.LENGTH_SHORT).show()
        }
        
        btnCommunity.setOnClickListener {
            Toast.makeText(this, "进入社区页面", Toast.LENGTH_SHORT).show()
        }
        
        // 底部导航栏按钮
        btnNavHome.setOnClickListener { 
            setNavigationSelectedState("home")
            // 已经在首页，无需跳转
        }
        btnNavInfo.setOnClickListener { 
            setNavigationSelectedState("info")
            navigateToInfoList() 
        }
        btnNavMine.setOnClickListener { 
            setNavigationSelectedState("mine")
            navigateToMine() 
        }
    }
    
    private fun setNavigationSelectedState(selectedTab: String) {
        // 重置所有按钮状态
        btnNavHome.setBackgroundColor(Color.WHITE)
        btnNavInfo.setBackgroundColor(Color.WHITE)
        btnNavMine.setBackgroundColor(Color.WHITE)
        btnNavHome.setTextColor(Color.BLACK)
        btnNavInfo.setTextColor(Color.BLACK)
        btnNavMine.setTextColor(Color.BLACK)
        
        // 设置选中按钮状态
        when (selectedTab) {
            "home" -> {
                btnNavHome.setTextColor(Color.BLUE)
            }
            "info" -> {
                btnNavInfo.setTextColor(Color.BLUE)
            }
            "mine" -> {
                btnNavMine.setTextColor(Color.BLUE)
            }
        }
    }
    
    private fun navigateToInfoList() {
        startActivity(Intent(this, InfoListActivity::class.java))
        finish()
    }
    
    private fun navigateToMine() {
        val intent = Intent(this, MineActivity::class.java)
        // 可以传递用户名等信息
        startActivity(intent)
        finish()
    }
}