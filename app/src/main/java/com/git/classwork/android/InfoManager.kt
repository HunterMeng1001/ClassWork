package com.git.classwork.android

import java.text.SimpleDateFormat
import java.util.*

class InfoManager {
    // 获取所有信息
    fun getAllInfo(): List<InfoData> {
        return generateMockData()
    }
    
    // 根据分类获取信息
    fun getInfoByCategory(category: InfoCategory): List<InfoData> {
        val allInfo = generateMockData()
        return if (category == InfoCategory.ALL) {
            allInfo
        } else {
            allInfo.filter { it.category == category }
        }
    }
    
    // 生成模拟数据
    private fun generateMockData(): List<InfoData> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())
        val yesterday = sdf.format(Date(System.currentTimeMillis() - 86400000))
        val twoDaysAgo = sdf.format(Date(System.currentTimeMillis() - 172800000))
        val threeDaysAgo = sdf.format(Date(System.currentTimeMillis() - 259200000))
        
        return listOf(
            // 科技类信息
            InfoData(
                id = 1,
                title = "最新科技动态",
                content = "人工智能技术在各个领域的应用越来越广泛，正在改变我们的生活方式。",
                date = today,
                category = InfoCategory.TECHNOLOGY
            ),
            InfoData(
                id = 2,
                title = "智能安全小贴士",
                content = "分享几个保护个人信息安全的小技巧：使用强密码、定期更新软件、注意网络钓鱼。",
                date = yesterday,
                category = InfoCategory.TECHNOLOGY
            ),
            
            // 知识类信息
            InfoData(
                id = 3,
                title = "职场提升技巧",
                content = "如何在职场中快速成长？保持学习心态、主动承担责任、建立良好的人际关系网络。",
                date = yesterday,
                category = InfoCategory.KNOWLEDGE
            ),
            InfoData(
                id = 4,
                title = "高效学习方法",
                content = "费曼学习法：通过向他人解释概念来检验自己的理解程度，是一种非常有效的学习方式。",
                date = threeDaysAgo,
                category = InfoCategory.KNOWLEDGE
            ),
            
            // 财经类信息
            InfoData(
                id = 5,
                title = "股市行情分析",
                content = "财经专家分析当前股市走势，建议投资者理性看待市场波动，做好资产配置。",
                date = twoDaysAgo,
                category = InfoCategory.FINANCE
            ),
            InfoData(
                id = 6,
                title = "理财入门指南",
                content = "新手理财应该从哪里开始？先建立应急基金，再考虑保险保障，最后进行投资规划。",
                date = threeDaysAgo,
                category = InfoCategory.FINANCE
            ),
            
            // 社会类信息
            InfoData(
                id = 7,
                title = "教育创新模式",
                content = "新型学习方式增加了互动性和个性化，为教育领域带来了新的活力。",
                date = threeDaysAgo,
                category = InfoCategory.SOCIAL
            ),
            InfoData(
                id = 8,
                title = "环保生活方式",
                content = "从日常生活做起，减少浪费，保护环境，让我们的地球更加美好。",
                date = threeDaysAgo,
                category = InfoCategory.SOCIAL
            )
        )
    }
}