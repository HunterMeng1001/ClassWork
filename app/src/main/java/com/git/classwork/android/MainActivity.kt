package com.git.classwork.android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var buttonLogin: Button
    private lateinit var buttonForgotPassword: Button
    private lateinit var buttonWeChat: Button
    private lateinit var buttonApple: Button
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var userManager: UserManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        userManager = UserManager(this)
        
        // 确保应用启动时重置登录状态，避免循环跳转问题
        // 可以在正式版中移除这行代码
        userManager.logout()
        
        // 检查是否已登录，如果已登录则直接跳转到首页
        /*if (userManager.isLoggedIn()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }*/
        
        initViews()
        setupListeners()
    }
    
    private fun initViews() {
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonForgotPassword = findViewById(R.id.buttonForgotPassword)
        buttonWeChat = findViewById(R.id.buttonWeChat)
        buttonApple = findViewById(R.id.buttonApple)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
    }
    
    private fun setupListeners() {
        // 登录按钮点击事件
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            
            // 简单的非空验证
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请输入邮箱和密码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // 使用邮箱格式验证
            if (isValidEmail(email)) {
                // 保存用户信息
                userManager.saveUserInfo(email.substringBefore('@'), email)
                
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                
                // 跳转到主导航页面
                val intent = Intent(this, MainNavigationActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "请输入有效的邮箱地址", Toast.LENGTH_SHORT).show()
            }
        }
        
        // 忘记密码按钮
        buttonForgotPassword.setOnClickListener {
            Toast.makeText(this, "忘记密码功能暂未实现", Toast.LENGTH_SHORT).show()
        }
        
        // 微信登录按钮
        buttonWeChat.setOnClickListener {
            // 模拟微信登录
            userManager.saveUserInfo("微信用户", "wechat@example.com")
            Toast.makeText(this, "微信登录成功", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainNavigationActivity::class.java))
            finish()
        }
        
        // Apple登录按钮
        buttonApple.setOnClickListener {
            // 模拟Apple登录
            userManager.saveUserInfo("Apple用户", "apple@example.com")
            Toast.makeText(this, "Apple登录成功", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainNavigationActivity::class.java))
            finish()
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }
}

