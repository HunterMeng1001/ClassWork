package com.git.classwork.android

/**
 * 收藏项数据模型
 */
data class FavoriteItem(
    val infoId: String,      // 信息ID
    val title: String,       // 标题
    val content: String,     // 内容摘要
    val date: String,        // 发布日期
    val category: String,    // 分类
    val favoriteTime: Long   // 收藏时间戳
)