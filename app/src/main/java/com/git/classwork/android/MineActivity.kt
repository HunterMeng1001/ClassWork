package com.git.classwork.android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color

class MineActivity : AppCompatActivity() {
    private lateinit var btnPersonalInfo: Button
    private lateinit var btnMyCollections: Button
    private lateinit var btnBrowseHistory: Button
    private lateinit var btnSettings: Button
    private lateinit var btnAboutUs: Button
    private lateinit var btnFeedback: Button
    private lateinit var btnNavHome: Button
    private lateinit var btnNavInfo: Button
    private lateinit var btnNavMine: Button
    private lateinit var userNameTextView: TextView
    private lateinit var userIntroTextView: TextView
    private lateinit var userManager: UserManager
    private lateinit var favoriteManager: FavoriteManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine)
        userManager = UserManager(this)
        favoriteManager = FavoriteManager(this)
        
        // 检查登录状态
        if (!userManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        
        initViews()
        displayUserInfo()
        updateFavoriteCount()
        setupListeners()
        
        // 设置选中状态
        setNavigationSelectedState("mine")
    }
    
    override fun onResume() {
        super.onResume()
        // 每次回到这个页面，都更新收藏数量
        if (userManager.isLoggedIn()) {
            updateFavoriteCount()
        }
    }
    
    private fun initViews() {
        userNameTextView = findViewById(R.id.userName)
        userIntroTextView = findViewById(R.id.userIntro)
        btnPersonalInfo = findViewById(R.id.btnPersonalInfo)
        btnMyCollections = findViewById(R.id.btnMyCollections)
        btnBrowseHistory = findViewById(R.id.btnBrowseHistory)
        btnSettings = findViewById(R.id.btnSettings)
        btnAboutUs = findViewById(R.id.btnAboutUs)
        btnFeedback = findViewById(R.id.btnFeedback)
        btnNavHome = findViewById(R.id.btnNavHome)
        btnNavInfo = findViewById(R.id.btnNavInfo)
        btnNavMine = findViewById(R.id.btnNavMine)
    }
    
    private fun displayUserInfo() {
        // 显示用户名，确保不为空
        val userName = userManager.getUserName()
        userNameTextView.text = if (userName.isNotEmpty()) userName else "用户"
        
        // 显示欢迎语，不再显示邮箱，使界面更简洁
        userIntroTextView.text = "欢迎来到信息App"
    }
    
    private fun updateFavoriteCount() {
        val favoriteCount = favoriteManager.getFavoriteCount()
        btnMyCollections.text = "我的收藏 ($favoriteCount)"
    }
    
    private fun setupListeners() {
        // 个人信息按钮
        btnPersonalInfo.setOnClickListener {
            Toast.makeText(this, "个人信息页面即将上线", Toast.LENGTH_SHORT).show()
        }
        
        // 我的收藏按钮
        btnMyCollections.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
        
        // 浏览历史按钮
        btnBrowseHistory.setOnClickListener {
            Toast.makeText(this, "浏览历史页面即将上线", Toast.LENGTH_SHORT).show()
        }
        
        // 设置按钮
        btnSettings.setOnClickListener {
            showLogoutDialog()
        }
        
        // 关于我们按钮
        btnAboutUs.setOnClickListener {
            showAboutDialog()
        }
        
        // 意见反馈按钮
        btnFeedback.setOnClickListener {
            Toast.makeText(this, "意见反馈页面即将上线", Toast.LENGTH_SHORT).show()
        }
        
        // 底部导航栏按钮
        btnNavHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        
        btnNavInfo.setOnClickListener {
            startActivity(Intent(this, InfoListActivity::class.java))
            finish()
        }
        
        btnNavMine.setOnClickListener {
            // 已经在我的页面
        }
    }
    
    // 设置导航按钮选中状态
    private fun setNavigationSelectedState(selectedTab: String) {
        // 重置所有按钮状态
        btnNavHome.setBackgroundColor(Color.WHITE)
        btnNavInfo.setBackgroundColor(Color.WHITE)
        btnNavMine.setBackgroundColor(Color.WHITE)
        btnNavHome.setTextColor(Color.BLACK)
        btnNavInfo.setTextColor(Color.BLACK)
        btnNavMine.setTextColor(Color.BLACK)
        
        // 设置选中状态
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
    
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("退出登录")
            .setMessage("确定要退出登录吗？")
            .setPositiveButton("确定") { _, _ ->
                userManager.logout()
            Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("关于我们")
            .setMessage("信息App v1.0.0\n\n一款提供各类信息的应用程序，让您随时了解最新资讯。")
            .setPositiveButton("确定", null)
            .show()
    }
}