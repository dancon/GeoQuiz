# GeoQuiz

FrameLayout 是最简单的 ViewGroup 组件，它不以特定的方式安排子视图的位置。 子视图的位置安排取决于各自的 android:layout_gravity 属性

Android 可以自动匹配最佳资源，但是必须通过新建一个 activity.

创建 Activity 的时候，IDE 会自动把新建的 activity 配置道 manifest 中。

使用 startActivity(Intent) 启动 activity 实际上是发送请求到系统的 ActivityManager, 然后 ActivityManager 负责创建实例，然后调用 onCreate 方法。