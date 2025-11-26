package com.git.classwork.android

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

/**
 * 收藏管理器，负责收藏信息的存储和管理
 */
class FavoriteManager(context: Context) {
    private val sharedPreferences: SharedPreferences
    private val gson = Gson()
    private val FAVORITES_KEY = "favorites"
    private val FAVORITES_FILE = "favorite_prefs"

    init {
        sharedPreferences = context.getSharedPreferences(FAVORITES_FILE, Context.MODE_PRIVATE)
    }

    /**
     * 添加收藏
     */
    fun addFavorite(item: FavoriteItem): Boolean {
        val favorites = getFavorites()
        // 检查是否已经收藏
        if (favorites.any { it.infoId == item.infoId }) {
            return false
        }
        favorites.add(item)
        saveFavorites(favorites)
        return true
    }

    /**
     * 移除收藏
     */
    fun removeFavorite(infoId: String): Boolean {
        val favorites = getFavorites()
        val removed = favorites.removeIf { it.infoId == infoId }
        if (removed) {
            saveFavorites(favorites)
        }
        return removed
    }

    /**
     * 检查是否已收藏
     */
    fun isFavorite(infoId: String): Boolean {
        return getFavorites().any { it.infoId == infoId }
    }

    /**
     * 获取所有收藏
     */
    fun getFavorites(): ArrayList<FavoriteItem> {
        val json = sharedPreferences.getString(FAVORITES_KEY, "")
        if (json.isNullOrEmpty()) {
            return ArrayList()
        }
        try {
            val type = object : TypeToken<ArrayList<FavoriteItem>>() {}.type
            return gson.fromJson(json, type)
        } catch (e: Exception) {
            // 如果解析失败，返回空列表
            return ArrayList()
        }
    }

    /**
     * 保存收藏列表
     */
    private fun saveFavorites(favorites: ArrayList<FavoriteItem>) {
        val json = gson.toJson(favorites)
        sharedPreferences.edit().putString(FAVORITES_KEY, json).apply()
    }

    /**
     * 清空所有收藏
     */
    fun clearAllFavorites() {
        sharedPreferences.edit().remove(FAVORITES_KEY).apply()
    }

    /**
     * 获取收藏数量
     */
    fun getFavoriteCount(): Int {
        return getFavorites().size
    }
}