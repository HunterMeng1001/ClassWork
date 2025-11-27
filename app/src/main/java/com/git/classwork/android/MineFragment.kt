package com.git.classwork.android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MineFragment : Fragment() {
    private lateinit var btnPersonalInfo: Button
    private lateinit var btnMyCollections: Button
    private lateinit var btnBrowseHistory: Button
    private lateinit var btnSettings: Button
    private lateinit var btnAboutUs: Button
    private lateinit var btnFeedback: Button
    private lateinit var userNameTextView: TextView
    private lateinit var userIntroTextView: TextView
    private lateinit var userManager: UserManager
    private lateinit var favoriteManager: FavoriteManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mine, container, false)
        userManager = UserManager(requireContext())
        favoriteManager = FavoriteManager(requireContext())

        // 检查登录状态
        if (!userManager.isLoggedIn()) {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            return null
        }

        initViews(view)
        displayUserInfo()
        updateFavoriteCount()
        setupListeners()

        return view
    }

    override fun onResume() {
        super.onResume()
        // 每次回到这个页面，都更新收藏数量
        if (userManager.isLoggedIn()) {
            updateFavoriteCount()
        }
    }

    private fun initViews(view: View) {
        userNameTextView = view.findViewById(R.id.userName)
        userIntroTextView = view.findViewById(R.id.userIntro)
        btnPersonalInfo = view.findViewById(R.id.btnPersonalInfo)
        btnMyCollections = view.findViewById(R.id.btnMyCollections)
        btnBrowseHistory = view.findViewById(R.id.btnBrowseHistory)
        btnSettings = view.findViewById(R.id.btnSettings)
        btnAboutUs = view.findViewById(R.id.btnAboutUs)
        btnFeedback = view.findViewById(R.id.btnFeedback)
    }

    private fun displayUserInfo() {
        // 显示用户名，确保不为空
        val userName = userManager.getUserName()
        userNameTextView.text = if (userName.isNotEmpty()) userName else "用户"

        // 显示欢迎语
        userIntroTextView.text = "欢迎来到信息App"
    }

    private fun updateFavoriteCount() {
        val favoriteCount = favoriteManager.getFavoriteCount()
        btnMyCollections.text = "我的收藏 ($favoriteCount)"
    }

    private fun setupListeners() {
        // 个人信息按钮
        btnPersonalInfo.setOnClickListener {
            findNavController().navigate(R.id.action_mine_to_personalInfo)
        }

        // 我的收藏按钮
        btnMyCollections.setOnClickListener {
            findNavController().navigate(R.id.action_mine_to_favorites)
        }

        // 浏览历史按钮
        btnBrowseHistory.setOnClickListener {
            Toast.makeText(requireContext(), "浏览历史页面即将上线", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), "意见反馈页面即将上线", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("退出登录")
            .setMessage("确定要退出登录吗？")
            .setPositiveButton("确定") { _, _ ->
                userManager.logout()
                Toast.makeText(requireContext(), "已退出登录", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("关于我们")
            .setMessage("信息App v1.0.0\n\n一款提供各类信息的应用程序，让您随时了解最新资讯。")
            .setPositiveButton("确定", null)
            .show()
    }
}