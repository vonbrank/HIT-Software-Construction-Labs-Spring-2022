![](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiv49DJqSGUcZjoRnB8eIJo1b9wcqqcVpllg&usqp=CAU)

<h1 style="text-align:center">
    <p>2022年春季学期计算学部《软件构造》课程</p>
    <p>Lab 3 实验报告</p>
</h1>


|   姓名   |        VonBrank        |
| :------: | :------------------: |
|   学号   |      120L02****      |
|   班号   |       20030**        |
| 电子邮件 | vonbrank@outlook.com |
| 手机号码 |           |

[TOC]

# 1 实验目标概述

本次实验覆盖课程第 2、3 章的内容，目标是编写具有可复用性和可维护性 的软件，主要使用以下软件构造技术： 

+ 子类型、泛型、多态、重写、重载 
+ 继承、委派、CRP 
+ 语法驱动的编程、正则表达式 
+ 设计模式 

本次实验给定了多个具体应用，学生不是直接针对每个应用分别编程实现， 而是通过 ADT 和泛型等抽象技术，开发一套可复用的 ADT 及其实现，充分考虑 这些应用之间的相似性和差异性，使 ADT 有更大程度的复用（可复用性）和更 容易面向各种变化（可维护性）。

# 2 实验环境配置

[这篇博客](https://blog.vonbrank.com/archives/hit-software-construction-lab1-config/)详细介绍了本人给出的在 IDEA 下配置本次实验的其中一种解决方案。

### 先决条件

+ 安装 JDK （个人使用 JDK 18）并添加环境变量
+ 安装 JetBrains IDEA

### 使用 `Gradle` 管理项目

+ 设置依赖项：

  ```kotlin
  dependencies {
      implementation("junit:junit:4.13.2")
      testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
      testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
  }
  ```

  下载完成后将其移至 `./lib`，并将此项修改为：

  ```kotlin
  dependencies {
      implementation(fileTree("lib") { include("*.jar") })
  }
  ```

+ 配置资源

  ```kotlin
  sourceSets {
      main {
          java.srcDir("./src")
          task<Jar>("main") {
              archiveBaseName.set("main")
              manifest.attributes("Main-Class" to "ElectionApp")
              dependsOn(runtimeClasspath)
              from(runtimeClasspath)
          }
      }
  
      test {
          java.srcDir("./test")
          compileClasspath += configurations.testCompileClasspath
          runtimeClasspath += configurations.testRuntimeClasspath
      }
  
  }
  ```
  
+ 配置 `build` 任务依赖

  我们希望每次构建都能构建 `main` 的 `jar` 包：

  ```kotlin
  tasks["build"].dependsOn("main")
  ```

+ 最后可以使用以下命令构建项目：

  ```bash
  ./gradlew build
  ```

### Git Repo Url

在这里给出你的GitHub Lab1仓库的URL地址：

```txt
https://github.com/ComputerScienceHIT/HIT-Lab2-120L02****
```

# 3 实验过程

请仔细对照实验手册，针对每一项任务，在下面各节中记录你的实验过程、阐述你的设计思路和问题求解思路，可辅之以示意图或关键源代码加以说明（但千万不要把你的源代码全部粘贴过来！）。

## 3.1 待开发的三个应用场景

简要介绍四个应用。

分析四个应用场景的异同，理解需求：它们在哪些方面有共性、哪些方面有差异。

## 3.2 ADT识别与设计

该节是本实验的核心部分。

### 3.2.1 任务1：投票类型 `VoteType`

 

### 3.2.2 任务2：投票项 `VoteItem<C>` 

 

### 3.2.3 任务3：选票 `Vote`

### 3.2.4 任务4：投票活动 `Poll<C>` 的测试

### 3.2.5 任务5：投票活动 `Poll<C>` 的实现类 `GeneralPollImpl` 

### 3.2.6 任务6：投票活动 `Poll<C>` 的子类型

## 3.3 ADT行为的设计与实现



### 3.3.1 任务7：合法性检查

### 3.3.2 任务8：采用 Strategy 设计模式实现灵活的计票规则

### 3.3.3 任务9：采用 Strategy 设计模式实现灵活的遴选规则

### 3.3.4 任务10：处理匿名和实名投票

### 3.3.5 任务11：采用 Visitor 设计模式实现功能扩展

### 3.3.6 任务12：基于语法的数据读入

## 3.4 任务13：应用设计与开发

### 3.4.1 商业表决系统

### 3.4.2 代表选举系统

### 3.4.3 聚餐点菜系统

## 3.5 任务14：应对面临的新变化

### 3.5.1 商业表决应用：可以一次表决多个商业提案

评估之前的设计是否可应对变化、代价如何

如何修改设计以应对变化

### 3.5.2 代表选举应用：遴选规则变化

评估之前的设计是否可应对变化、代价如何

如何修改设计以应对变化

### 3.5.3 聚餐点菜应用：取消权重设置、只计算“喜欢”的票数

评估之前的设计是否可应对变化、代价如何

如何修改设计以应对变化

## 3.6 Git仓库结构

请在完成全部实验要求之后，利用 `git log` 指令或 Git 图形化客户端或 GitHub 上项目仓库的 Insight 页面，给出你的仓库到目前为止的 Object Graph ，尤其是区分清楚 change 分支和 master 分支所指向的位置。

# 4 实验进度记录

请使用表格方式记录你的进度情况，以超过半小时的连续编程时间为一行。

每次结束编程时，请向该表格中增加一行。不要事后胡乱填写。

不要嫌烦，该表格可帮助你汇总你在每个任务上付出的时间和精力，发现自己不擅长的任务，后续有意识的弥补。

|     日期     |    时间段     | 计划任务 | 实际完成情况 |
| :----------: | :-----------: | :------: | :----------: |
| `2022-06-02` | `10:30-11:30` | 项目配置 |     完成     |
|              |               |          |              |
|              |               |          |              |

# 5 实验过程中遇到的困难与解决途径

| 遇到的难点 | 解决途径 |
| :--------: | :------: |
|            |          |
|            |          |
|            |          |

# 6 实验过程中收获的经验、教训、感想

## 6.1 实验过程中收获的经验和教训（必答）

## 6.2 针对以下方面的感受（必答）

(1)  重新思考 Lab2 中的问题：面向 ADT 的编程和直接面向应用场景编程，你体会到二者有何差异？本实验设计的 ADT 在三个不同的应用场景下使用，你是否体会到复用的好处？

(2)  重新思考 Lab2 中的问题：为 ADT 撰写复杂的 specification, invariants, RI, AF，时刻注意 ADT是否有 rep exposure，这些工作的意义是什么？你是否愿意在以后的编程中坚持这么做？

(3)  之前你将别人提供的 ADT/API 用于自己的程序开发中，本次实验你尝试着开发给别人使用的 ADT/API ，是否能够体会到其中的难处和乐趣？

(4)  你之前在使用其他软件时，应该体会过输入各种命令向系统发出指令。本次实验你开发了一个简单的解析器，使用语法和正则表达式去解析一个遵循特定规则的字符串并据此构造对象。你对语法驱动编程有何感受？

(5)  Lab1和Lab2的工作都不是从0开始，而是基于他人给出的设计方案和初始代码。本次实验中也提供了一部分基础代码。假如本实验要求你完全从0开始进行ADT的设计并用OOP实现，你觉得自己是否能够完全搞定？你认为“设计ADT”的难度主要体现在哪些地方？

(6)  “抽象”是计算机科学的核心概念之一，也是ADT和OOP的精髓所在。本实验的三个应用既不能完全抽象为同一个ADT，也不是完全个性化，如何利用“接口、抽象类、类”三层体系以及接口的组合、类的继承、委派、设计模式等技术完成最大程度的抽象和复用，你有什么经验教训？

(7)  关于本实验的工作量、难度、deadline。

(8)  课程结束了，你对《软件构造》课程内容和任课教师的评价如何？

 

