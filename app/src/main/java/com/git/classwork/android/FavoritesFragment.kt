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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import java.util.*

class FavoritesFragment : Fragment() {
    private lateinit var listViewFavorites: ListView
    private lateinit var btnBack: Button
    private lateinit var favoriteManager: FavoriteManager
    private lateinit var userManager: UserManager
    private val favoriteList = ArrayList<FavoriteItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_favorites, container, false)
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

        loadFavorites()
        return view
    }

    private fun initViews(view: View) {
        listViewFavorites = view.findViewById(R.id.listViewFavorites)
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

        listViewFavorites.adapter = FavoritesAdapter(favoriteList)
    }

    // 处理取消收藏
    private fun handleRemoveFavorite(infoId: String) {
        val removed = favoriteManager.removeFavorite(infoId)
        if (removed) {
            Toast.makeText(requireContext(), "取消收藏成功", Toast.LENGTH_SHORT).show()
            loadFavorites()
        }
    }

    // 收藏列表适配器
    private inner class FavoritesAdapter(private val favorites: List<FavoriteItem>) : BaseAdapter() {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        override fun getCount(): Int = favorites.size

        override fun getItem(position: Int): FavoriteItem = favorites[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val holder: ViewHolder

            if (convertView == null) {
                view = LayoutInflater.from(requireContext()).inflate(R.layout.item_favorite, parent, false)
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