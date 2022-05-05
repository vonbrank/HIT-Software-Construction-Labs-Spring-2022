
![](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiv49DJqSGUcZjoRnB8eIJo1b9wcqqcVpllg&usqp=CAU)

<h1 style="text-align:center">
    <p>2022年春季学期计算学部《软件构造》课程</p>
    <p>Lab 1实验报告</p>
<h1>


| 姓名     | VonBrank               |
| -------- | -------------------- |
| 学号     | 120L02****           |
| 班号     | 20030**              |
| 电子邮件 | vonbrank@outlook.com |
| 手机号码 |           |



## 1 实验目标概述

本次实验通过求解三个问题，训练基本 Java 编程技能，能够利用 Java OO 开 发基本的功能模块，能够阅读理解已有代码框架并根据功能需求补全代码，能够 为所开发的代码编写基本的测试程序并完成测试，初步保证所开发代码的正确性。 另一方面，利用 Git 作为代码配置管理的工具，学会 Git 的基本使用方法。

+ 基本的 Java OO 编程 

+ 基于 Eclipse IDE 进行 Java 编程

+ 基于 JUnit 的测试 

+ 基于 Git 的代码配置管理



## 2 实验环境配置

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

  由 `P2` 代码框架可知，将 `P1` `P2` `P3`  看作独立的模块比将其看作包更好，并生成独立的 `jar` 包。以 `P2` 为例可在 `build.gradle.kts` 中将其配置为：

  ```kotlin
  val p2 = create("P2") {
      java.srcDir("./src/P2")
      compileClasspath += configurations.testCompileClasspath
      runtimeClasspath += configurations.testRuntimeClasspath
  
      task<Jar>("jarP2") {
          archiveBaseName.set("P2")
          manifest.attributes("Main-Class" to "turtle.TurtleSoup")
          dependsOn(runtimeClasspath)
          from(runtimeClasspath)
      }
  }
  create("P2Test") {
      java.srcDir("./test/P2")
      compileClasspath += configurations.testCompileClasspath + p2.output
      runtimeClasspath += configurations.testRuntimeClasspath + p2.output
  }
  ```

+ 配置 `build` 任务依赖

  我们希望每次构建能同时构建 `P1` `P2` `P3` 的 `jar` 包：

  ```kotlin
  tasks["build"].dependsOn("jarP1", "jarP2", "jarP3")
  ```

+ 最后可以使用以下命令构建项目：
  
  ```kotlin
  ./gradlew build
  ```

### Git Repo Url

在这里给出你的GitHub Lab1仓库的URL地址：

```txt
https://github.com/ComputerScienceHIT/HIT-Lab1-120L02****
```



## 3 实验过程

请仔细对照实验手册，针对四个问题中的每一项任务，在下面各节中记录你的实验过程、阐述你的设计思路和问题求解思路，可辅之以示意图或关键源代码加以说明（但无需把你的源代码全部粘贴过来！）。

为了条理清晰，可根据需要在各节增加三级标题。



### 3.1 Magic Squares

`N` 阶幻方是一个 `N x N` 的矩阵，填充着 `1 - N x N` ，每个数互不相同，且每行每列每对角线之和相等。 

本题要求我们理解幻方的意义和生成幻方的算法，以便写出一个算法来检查幻方是否合法。

#### 3.1.1 `isLegalMagicSquare()`

题目给出了若干文件并将其中的数据看作幻方，判断其是否合法，可以由以下步骤实现：

+ 读取文件，此处使用 `Files.lines`
+ 获得矩阵行数
+ 以 `\t` 切片每行，若某一行切片个数与矩阵行数不相等说明不合法返回 `false`
+ 单独写一个函数 `isNotPositiveInteger` ，里面用 `Integer.parseInt` 判断字符串是否是整数，然后判断是否大于等于 `1`，如果不是则不合法，返回 `false`
+ 遍历矩阵每行、列、对角线判断和是否相等

最终可得：

```txt
1.txt is a legal magic square.

2.txt is a legal magic square.

Too few numbers at line 18.
3.txt is not a legal magic square.

The number [-35] at [2, 5] is not a positive integer or there are some numbers split without '\t'.
4.txt is not a legal magic square.

The number [12673   12796] at [120, 44] is not a positive integer or there are some numbers split without '\t'.
5.txt is not a legal magic square.
```



#### 3.1.2 `generateMagicSquare()`





### 3.2 Turtle Graphics



#### 3.2.1 Problem 1: Clone and import



#### 3.2.2 Problem 3: Turtle graphics and drawSquare



#### 3.2.3 Problem 5: Drawing polygons



#### 3.2.4 Problem 5: Drawing polygons



#### 3.2.5 Problem 7: Convex Hulls



#### 3.2.6 Problem 8: Personal art



#### 3.2.7 Submitting



### 3.3 Social Network



#### 3.3.1 设计/实现 `FriendshipGraph` 类



#### 3.3.2 设计/实现 `Person` 类



#### 3.3.3 设计/实现客户端代码 `main()`



#### 3.3.4 设计/实现测试用例



## 4 实验进度记录



## 5 实验过程中遇到的困难与解决途径



## 6 实验过程中收获的经验、教训、感想

### 6.1 实验过程中收获的经验和教训（必答）



### 6.2 针对以下方面的感受（必答）

+ Java编程语言是否对你的口味？与你熟悉的其他编程语言相比，Java有何优势和不足？

+ 关于Eclipse或IntelliJ IDEA，它们作为IDE的优势和不足；

+ 关于Git和GitHub，是否感受到了它在版本控制方面的价值；

+ 关于CMU和MIT的作业，你有何感受；

+ 关于本实验的工作量、难度、deadline；

+ 关于初接触 `软件构造` 课程；
