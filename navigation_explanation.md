# 导航图结构解释

## 图的含义

这张图展示的是**Jetpack Navigation**组件的导航图（nav_graph）结构，用于管理应用内的页面导航关系。

## 结构详解

1. **nav_graph**：
   - 根节点，代表整个应用的导航图配置
   - 位于 `res/navigation/nav_graph.xml` 文件中

2. **mainNavHost**：
   - 导航宿主（Navigation Host），是导航的容器
   - 通常对应布局文件中的 `<fragment android:name="androidx.navigation.fragment.NavHostFragment"` 标签
   - 负责承载和管理各个导航目的地（Fragment）

3. **Fragment 目的地**：
   - 图中包含5个Fragment作为导航目的地：
     - `homeFragment`：首页Fragment
     - `infoListFragment`：信息列表Fragment
     - `mineFragment`：个人中心Fragment
     - `favoritesFragment`：收藏Fragment
     - `personallInfoFragment`：个人信息Fragment（注意：拼写应为 `personalInfoFragment`）

## 导航关系

- 这些Fragment通过底部导航栏（BottomNavigationView）进行切换
- 导航图定义了Fragment之间的跳转关系和参数传递
- 当前应用采用Fragment-based架构，替代了传统的Activity跳转

## 与之前工作的关联

- 之前我们将 `HomeActivity`、`InfoListActivity`、`MineActivity` 转换为对应的Fragment
- 这些Fragment现在通过Jetpack Navigation统一管理
- 导航图确保了页面间导航的一致性和可维护性

## 技术意义

- 使用Jetpack Navigation可以简化导航代码，避免手动管理Fragment事务
- 支持深层链接、导航动画、参数传递等高级功能
- 可视化的导航图便于理解应用的页面结构
- 符合现代Android应用架构最佳实践
