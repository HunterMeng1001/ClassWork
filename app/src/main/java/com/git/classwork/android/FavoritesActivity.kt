package com.git.classwork.android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class FavoritesActivity : AppCompatActivity() {
    private lateinit var listViewFavorites: ListView
    private lateinit var btnBack: Button
    private lateinit var favoriteManager: FavoriteManager
    private lateinit var userManager: UserManager
    private val favoriteList = ArrayList<FavoriteItem>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        
        initViews()
        setupListeners()
        
        favoriteManager = FavoriteManager(this)
        userManager = UserManager(this)
        
        // 检查登录状态
        if (!userManager.isLoggedIn()) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        
        loadFavorites()
    }
    
    private fun initViews() {
        listViewFavorites = findViewById(R.id.listViewFavorites)
        btnBack = findViewById(R.id.btnBack)
    }
    
    private fun setupListeners() {
        btnBack.setOnClickListener { finish() }
    }
    
    private fun loadFavorites() {
        favoriteList.clear()
        favoriteList.addAll(favoriteManager.getFavorites())
        
        // 按收藏时间倒序排序
        favoriteList.sortByDescending { it.favoriteTime }
        
        listViewFavorites.adapter = FavoritesAdapter(favoriteList)
    }
    
    // 处理取消收藏
    private fun handleRemoveFavorite(infoId: String) {
        val removed = favoriteManager.removeFavorite(infoId)
        if (removed) {
            Toast.makeText(this, "取消收藏成功", Toast.LENGTH_SHORT).show()
            loadFavorites()
        }
    }
    
    // 收藏列表适配器
    private inner class FavoritesAdapter(private val favorites: List<FavoriteItem>) : BaseAdapter() {
        private val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        
        override fun getCount(): Int = favorites.size

        override fun getItem(position: Int): FavoriteItem = favorites[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val holder: ViewHolder

            if (convertView == null) {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.item_favorite, parent, false)
                holder = ViewHolder()
                holder.tvTitle = view.findViewById(R.id.tvTitle)
                holder.tvContent = view.findViewById(R.id.tvContent)
                holder.tvDate = view.findViewById(R.id.tvDate)
                holder.tvFavoriteTime = view.findViewById(R.id.tvFavoriteTime)
                holder.btnRemove = view.findViewById(R.id.btnRemove)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }

            val favoriteItem = favorites[position]
            holder.tvTitle.text = favoriteItem.title
            holder.tvContent.text = favoriteItem.content
            holder.tvDate.text = "发布日期: ${favoriteItem.date}"
            holder.tvFavoriteTime.text = "收藏时间: ${dateFormat.format(Date(favoriteItem.favoriteTime))}"

            holder.btnRemove.setOnClickListener {
                handleRemoveFavorite(favoriteItem.infoId)
            }

            return view
        }

        inner class ViewHolder {
            lateinit var tvTitle: TextView
            lateinit var tvContent: TextView
            lateinit var tvDate: TextView
            lateinit var tvFavoriteTime: TextView
            lateinit var btnRemove: Button
        }
    }
}