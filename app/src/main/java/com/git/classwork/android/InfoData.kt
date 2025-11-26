package com.git.classwork.android

// 信息分类枚举
enum class InfoCategory {
    ALL, // 全部
    TECHNOLOGY, // 科技
    KNOWLEDGE, // 知识
    FINANCE, // 财经
    SOCIAL // 社会
}

// 信息数据模型类
data class InfoData(
    val id: Int,
    val title: String,
    val content: String,
    val date: String,
    val category: InfoCategory
)