package com.git.classwork.android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InfoListFragment : Fragment() {
    private lateinit var recyclerViewInfo: RecyclerView
    private lateinit var btnAll: Button
    private lateinit var btnTechnology: Button
    private lateinit var btnKnowledge: Button
    private lateinit var btnFinance: Button
    private lateinit var btnSocial: Button
    
    private lateinit var infoManager: InfoManager
    private lateinit var favoriteManager: FavoriteManager
    private lateinit var userManager: UserManager
    private lateinit var currentAdapter: InfoAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var currentCategory = InfoCategory.ALL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_list, container, false)
        infoManager = InfoManager()
        favoriteManager = FavoriteManager(requireContext())
        userManager = UserManager(requireContext())
        initViews(view)
        setupCategoryButtons()
        setupListView()
        return view
    }
    
    private fun initViews(view: View) {
        recyclerViewInfo = view.findViewById(R.id.recyclerViewInfo)
        btnAll = view.findViewById(R.id.btnAll)
        btnTechnology = view.findViewById(R.id.btnTechnology)
        btnKnowledge = view.findViewById(R.id.btnKnowledge)
        btnFinance = view.findViewById(R.id.btnFinance)
        btnSocial = view.findViewById(R.id.btnSocial)
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
        
        // 初始化布局管理器
        layoutManager = LinearLayoutManager(requireContext())
        recyclerViewInfo.layoutManager = layoutManager
        
        // 设置RecyclerView的适配器
        currentAdapter = InfoAdapter(infoList)
        recyclerViewInfo.adapter = currentAdapter
        
        // 设置点击事件
        currentAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickedInfo = infoList[position]
                // 这里可以跳转到详情页面
            }
        })
    }
    
    // 处理收藏操作
    private fun handleFavorite(infoData: InfoData) {
        // 检查是否已登录
        if (!userManager.isLoggedIn()) {
            Toast.makeText(requireContext(), "请先登录", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            return
        }
        
        if (favoriteManager.isFavorite(infoData.id.toString())) {
            // 取消收藏
            val removed = favoriteManager.removeFavorite(infoData.id.toString())
            if (removed) {
                Toast.makeText(requireContext(), "取消收藏成功", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), "收藏成功", Toast.LENGTH_SHORT).show()
                // 刷新列表
                setupListView()
            } else {
                Toast.makeText(requireContext(), "已收藏", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    // 点击事件监听器接口
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    
    // 信息列表适配器
    inner class InfoAdapter(private val infoList: List<InfoData>) : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {
        
        private var onItemClickListener: OnItemClickListener? = null
        
        fun setOnItemClickListener(listener: OnItemClickListener) {
            this.onItemClickListener = listener
        }
        
        inner class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
            val contentTextView: TextView = itemView.findViewById(R.id.textViewContent)
            val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
            val view = LayoutInflater.from(requireContext())
                .inflate(R.layout.list_item_info, parent, false)
            return InfoViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
            val info = infoList[position]
            
            holder.titleTextView.text = info.title
            holder.contentTextView.text = info.content
            holder.dateTextView.text = info.date
            
            // 设置点击事件
            holder.itemView.setOnClickListener {
                onItemClickListener?.onItemClick(position)
            }
        }
        
        override fun getItemCount(): Int = infoList.size
    }
}