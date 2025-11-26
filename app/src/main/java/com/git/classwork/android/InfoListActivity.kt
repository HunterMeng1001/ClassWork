package com.git.classwork.android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color

class InfoListActivity : AppCompatActivity() {
    private lateinit var listViewInfo: ListView
    private lateinit var btnNavHome: Button
    private lateinit var btnNavMine: Button
    private lateinit var btnNavInfo: Button
    private lateinit var btnAll: Button
    private lateinit var btnTechnology: Button
    private lateinit var btnKnowledge: Button
    private lateinit var btnFinance: Button
    private lateinit var btnSocial: Button
    
    private lateinit var infoManager: InfoManager
    private lateinit var favoriteManager: FavoriteManager
    private lateinit var userManager: UserManager
    private lateinit var currentAdapter: InfoAdapter
    private var currentCategory = InfoCategory.ALL
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_list)
        infoManager = InfoManager()
        favoriteManager = FavoriteManager(this)
        userManager = UserManager(this)
        initViews()
        setupCategoryButtons()
        setupListView()
        setupListeners()
        // 设置导航栏选中状态
        setNavigationSelectedState("info")
    }
    
    private fun initViews() {
        listViewInfo = findViewById(R.id.listViewInfo)
        btnNavHome = findViewById(R.id.btnNavHome)
        btnNavMine = findViewById(R.id.btnNavMine)
        btnNavInfo = findViewById(R.id.btnNavInfo)
        btnAll = findViewById(R.id.btnAll)
        btnTechnology = findViewById(R.id.btnTechnology)
        btnKnowledge = findViewById(R.id.btnKnowledge)
        btnFinance = findViewById(R.id.btnFinance)
        btnSocial = findViewById(R.id.btnSocial)
    }
    
    private fun setupCategoryButtons() {
        // 设置分类按钮的点击事件
        btnAll.setOnClickListener { switchCategory(InfoCategory.ALL) }
        btnTechnology.setOnClickListener { switchCategory(InfoCategory.TECHNOLOGY) }
        btnKnowledge.setOnClickListener { switchCategory(InfoCategory.KNOWLEDGE) }
        btnFinance.setOnClickListener { switchCategory(InfoCategory.FINANCE) }
        btnSocial.setOnClickListener { switchCategory(InfoCategory.SOCIAL) }
        
        // 默认选中"全部"分类
        updateCategoryButtonState(InfoCategory.ALL)
    }
    
    private fun switchCategory(category: InfoCategory) {
        currentCategory = category
        updateCategoryButtonState(category)
        setupListView() // 重新加载列表
    }
    
    private fun updateCategoryButtonState(selectedCategory: InfoCategory) {
        // 重置所有按钮状态
        resetAllButtonStates()
        
        // 设置选中按钮的状态
        when (selectedCategory) {
            InfoCategory.ALL -> btnAll.setBackgroundResource(R.drawable.category_button_selected)
            InfoCategory.TECHNOLOGY -> btnTechnology.setBackgroundResource(R.drawable.category_button_selected)
            InfoCategory.KNOWLEDGE -> btnKnowledge.setBackgroundResource(R.drawable.category_button_selected)
            InfoCategory.FINANCE -> btnFinance.setBackgroundResource(R.drawable.category_button_selected)
            InfoCategory.SOCIAL -> btnSocial.setBackgroundResource(R.drawable.category_button_selected)
        }
    }
    
    private fun resetAllButtonStates() {
        btnAll.setBackgroundResource(R.drawable.category_button_normal)
        btnTechnology.setBackgroundResource(R.drawable.category_button_normal)
        btnKnowledge.setBackgroundResource(R.drawable.category_button_normal)
        btnFinance.setBackgroundResource(R.drawable.category_button_normal)
        btnSocial.setBackgroundResource(R.drawable.category_button_normal)
    }
    
    private fun setupListView() {
        // 获取当前分类的信息列表
        val infoList = infoManager.getInfoByCategory(currentCategory)
        
        // 设置ListView的适配器
        currentAdapter = InfoAdapter(infoList)
        listViewInfo.adapter = currentAdapter
        
        // 设置点击事件
        listViewInfo.setOnItemClickListener { _, _, position, _ ->
            val clickedInfo = infoList[position]
            // 这里可以跳转到详情页面
        }
    }
    
    private fun setupListeners() {
        btnNavHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        
        btnNavMine.setOnClickListener {
            startActivity(Intent(this, MineActivity::class.java))
            finish()
        }
    }
    
    private fun setNavigationSelectedState(selectedTab: String) {
        // 重置所有按钮状态
        btnNavHome.setBackgroundColor(Color.WHITE)
        btnNavMine.setBackgroundColor(Color.WHITE)
        btnNavInfo.setBackgroundColor(Color.WHITE)
        btnNavHome.setTextColor(Color.BLACK)
        btnNavInfo.setTextColor(Color.BLACK)
        btnNavMine.setTextColor(Color.BLACK)
        
        // 设置选中按钮状态
        when (selectedTab) {
            "home" -> {
                btnNavHome.setBackgroundColor(Color.WHITE)
                btnNavHome.setTextColor(Color.BLUE)
            }
            "info" -> {
                btnNavInfo.setBackgroundColor(Color.WHITE)
                btnNavInfo.setTextColor(Color.BLUE)
            }
            "mine" -> {
                btnNavMine.setBackgroundColor(Color.WHITE)
                btnNavMine.setTextColor(Color.BLUE)
            }
        }
    }
    
    // 处理收藏操作
    private fun handleFavorite(infoData: InfoData) {
        // 检查用户是否登录
        if (!userManager.isLoggedIn()) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            return
        }
        
        if (favoriteManager.isFavorite(infoData.id.toString())) {
            // 取消收藏
            val removed = favoriteManager.removeFavorite(infoData.id.toString())
            if (removed) {
                Toast.makeText(this, "取消收藏成功", Toast.LENGTH_SHORT).show()
                // 刷新列表
                setupListView()
            }
        } else {
            // 添加收藏
            val favoriteItem = FavoriteItem(
                infoId = infoData.id.toString(),
                title = infoData.title,
                content = infoData.content,
                date = infoData.date,
                category = infoData.category.name,
                favoriteTime = System.currentTimeMillis()
            )
            val added = favoriteManager.addFavorite(favoriteItem)
            if (added) {
                Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show()
                // 刷新列表
                setupListView()
            } else {
                Toast.makeText(this, "已收藏", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    // 信息列表适配器
    inner class InfoAdapter(private val infoList: List<com.git.classwork.android.InfoData>) : BaseAdapter() {
        override fun getCount(): Int = infoList.size
        
        override fun getItem(position: Int): Any = infoList[position]
        
        override fun getItemId(position: Int): Long = position.toLong()
        
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(this@InfoListActivity)
                .inflate(R.layout.list_item_info, parent, false)
            
            val info = infoList[position]
            
            val titleTextView = view.findViewById<TextView>(R.id.textViewTitle)
            val contentTextView = view.findViewById<TextView>(R.id.textViewContent)
            val dateTextView = view.findViewById<TextView>(R.id.textViewDate)
            
            titleTextView.text = info.title
            contentTextView.text = info.content
            dateTextView.text = info.date
            
            // 由于布局中不存在收藏按钮，移除相关逻辑
            // 可以考虑在后续版本中添加收藏功能，需要先在布局文件中添加对应的按钮
            
            return view
        }
    }
}