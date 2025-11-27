package com.git.classwork.android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class FavoritesFragment : Fragment() {
    private lateinit var recyclerViewFavorites: RecyclerView
    private lateinit var btnBack: Button
    private lateinit var favoriteManager: FavoriteManager
    private lateinit var userManager: UserManager
    private val favoriteList = ArrayList<FavoriteItem>()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        initViews(view)
        setupListeners()

        favoriteManager = FavoriteManager(requireContext())
        userManager = UserManager(requireContext())

        // 检查登录状态
        if (!userManager.isLoggedIn()) {
            Toast.makeText(requireContext(), "请先登录", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            return null
        }

        // 初始化RecyclerView
        recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        favoritesAdapter = FavoritesAdapter(favoriteList)
        recyclerViewFavorites.adapter = favoritesAdapter

        loadFavorites()
        return view
    }

    private fun initViews(view: View) {
        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites)
        btnBack = view.findViewById(R.id.btnBack)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadFavorites() {
        favoriteList.clear()
        favoriteList.addAll(favoriteManager.getFavorites())

        // 按收藏时间倒序排序
        favoriteList.sortByDescending { it.favoriteTime }

        favoritesAdapter.notifyDataSetChanged()
    }

    // 处理取消收藏
    private fun handleRemoveFavorite(infoId: String) {
        val removed = favoriteManager.removeFavorite(infoId)
        if (removed) {
            Toast.makeText(requireContext(), "取消收藏成功", Toast.LENGTH_SHORT).show()
            loadFavorites()
        }
    }

    // 收藏列表RecyclerView适配器
    private inner class FavoritesAdapter(private val favorites: List<FavoriteItem>) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_favorite, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val favoriteItem = favorites[position]
            holder.tvTitle.text = favoriteItem.title
            holder.tvContent.text = favoriteItem.content
            holder.tvDate.text = "发布日期: ${favoriteItem.date}"
            holder.tvFavoriteTime.text = "收藏时间: ${dateFormat.format(Date(favoriteItem.favoriteTime))}"

            holder.btnRemove.setOnClickListener {
                handleRemoveFavorite(favoriteItem.infoId)
            }
        }

        override fun getItemCount(): Int = favorites.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            val tvContent: TextView = itemView.findViewById(R.id.tvContent)
            val tvDate: TextView = itemView.findViewById(R.id.tvDate)
            val tvFavoriteTime: TextView = itemView.findViewById(R.id.tvFavoriteTime)
            val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
        }
    }
}