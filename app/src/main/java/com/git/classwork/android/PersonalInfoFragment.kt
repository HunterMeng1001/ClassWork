package com.git.classwork.android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class PersonalInfoFragment : Fragment() {
    private lateinit var btnBack: Button
    private lateinit var btnSave: Button
    private lateinit var etUserName: EditText
    private lateinit var etUserEmail: EditText
    private lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_personal_info, container, false)
        userManager = UserManager(requireContext())

        // 检查登录状态
        if (!userManager.isLoggedIn()) {
            Toast.makeText(requireContext(), "请先登录", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            return null
        }

        initViews(view)
        displayUserInfo()
        setupListeners()

        return view
    }

    private fun initViews(view: View) {
        btnBack = view.findViewById(R.id.btnBack)
        btnSave = view.findViewById(R.id.btnSave)
        etUserName = view.findViewById(R.id.etUserName)
        etUserEmail = view.findViewById(R.id.etUserEmail)
    }

    private fun displayUserInfo() {
        etUserName.setText(userManager.getUserName())
        etUserEmail.setText(userManager.getUserEmail())
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnSave.setOnClickListener {
            val userName = etUserName.text.toString().trim()
            val userEmail = etUserEmail.text.toString().trim()

            if (userName.isEmpty()) {
                Toast.makeText(requireContext(), "用户名不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userManager.saveUserInfo(userName, userEmail)
            Toast.makeText(requireContext(), "信息保存成功", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }
}