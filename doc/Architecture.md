# 博客园Android架构设计文档

> 入门条件

该项目需要有一定的Android开发经验，新手学习会有所困难，在博客园开发之前你应该具备下条件：

- 1年Android开发经验以上
- 熟悉 Kotlin 语言
- 熟悉 Android 四大组件
- 熟悉常用的View组件、布局、数据加载方式
- 熟悉 SQLite 数据库

> 面向人群

本项目是一个完整的APP实现，主要面向的人群：

- Kotlin 项目的最佳实践方式
- 学习设计模式
- 学习主流App应用架构
- 主流技术的应用

> 架构选型

博客园Android客户端采用了`google` 推荐的最佳的`kotlin`开发方式。主体架构选型：

- ViewModel
- LiveData
- ViewBinding
- Room
- RxKotlin
- OKHttp
- Retrofit

> 设计模式

除了架构选型，更应用了设计模式：

- 工厂模式
- 代理模式
- 策略模式
- 责任链模式
