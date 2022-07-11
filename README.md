# 哈工大软件构造实验 - 2022年春 &middot; ![HIT-CS32123](https://img.shields.io/badge/HIT-CS32123-red) ![License](https://img.shields.io/badge/License-GPL-green) ![Mail](https://img.shields.io/badge/Email-vonbrank@outlook.com-blue?style=flat&logo=mail.cn)

## 快速开始

哈尔滨工业大学【CS32123: 软件构造】课程在 2022 年春季学期共设计了三次实验。三次实验的源码分别在 [`lab1-master`](https://github.com/vonbrank/HIT-Software-Construction-Labs-Spring-2022/tree/lab1-master) [`lab2-master`](https://github.com/vonbrank/HIT-Software-Construction-Labs-Spring-2022/tree/lab2-master) 和 [`lab3-master`](https://github.com/vonbrank/HIT-Software-Construction-Labs-Spring-2022/tree/lab3-master) 分支，额外的 [`lab3-change`](https://github.com/vonbrank/HIT-Software-Construction-Labs-Spring-2022/tree/lab3-change) 源自实验三的新需求，详见实验指导分支 [`lab-manuals`](https://github.com/vonbrank/HIT-Software-Construction-Labs-Spring-2022/tree/lab-manuals)。

三次实验均使用 [Gradle](https://gradle.org/) 进行项目管理，可以使用如下命令进行构建：

```bash
./gradlew build
```

然后可以在 `./build/libs` 下找到构建得到的 `jar` 包，使用如下命令即可执行：

```bash
java [options] -jar <jar filename> [args...]
```

## 注意事项

使用前应注意，这份实验源码在总分 $35$ 分的情况下，最终得分 $33.6$ 分 ~~，分数是高还是低你本身也要判断~~ 。

注意到本项目使用 [GPL-v3.0开源协议](https://zh.wikipedia.org/zh-cn/GNU%E9%80%9A%E7%94%A8%E5%85%AC%E5%85%B1%E8%AE%B8%E5%8F%AF%E8%AF%81) ，因此使用源码时应遵守以下原则：

- License and copyright notice
- State changes （标明你在哪里更改过）
- Disclose source （必须要把这个项目的源代码也附上）
- Same license （你要使用同样的协议）

敬请按需使用，祝您学习愉快！

---

# HIT Software Construction Labs - Spring 2022 &middot; ![HIT-CS32123](https://img.shields.io/badge/HIT-CS32123-red) ![License](https://img.shields.io/badge/License-GPL-green) ![Mail](https://img.shields.io/badge/Email-vonbrank@outlook.com-blue?style=flat&logo=mail.cn)

## Getting Started

Three labs were designed for the course \[CS32123: Software Construction\] at Harbin Institute of Technology. The source code for the three experiments is available in  [`lab1-master`](https://github.com/vonbrank/HIT-Software-Construction-Labs-Spring-2022/tree/lab1-master) [`lab2-master`](https://github.com/vonbrank/HIT-Software-Construction-Labs-Spring-2022/tree/lab2-master) and [`lab3-master`](https://github.com/vonbrank/HIT-Software-Construction-Labs-Spring-2022/tree/lab3-master)  branches. The additional [`lab3-change`](https://github.com/vonbrank/HIT- Software-Construction-Labs-Spring-2022/tree/lab3-change) is derived from the new requirements of lab-3, and you can see the [`lab-manuals`](https://github.com/vonbrank/HIT-Software-Construction-Labs-Spring-2022/tree/lab-manuals) branch for details. 

All three labs are managed by the project management tool [Gradle](https://gradle.org/) and can be built with the following command: 

```bash
./gradlew build
```

Then you can find out the `.jar` packages as the results of the build command from the path `./build/libs` , which can be executed with the following command: 

```bash
java [options] -jar <jar filename> [args...]
```

## Notice

Before start, you should note that in the case of a total score of $35$ points , the source code of this repo finally gained $33.6$ points. 

 And note that this project is protected by [GPL-3.0 license](https://en.wikipedia.org/wiki/GNU_General_Public_License) . So when using the source code, you should comply with the following principles: 

- License and copyright notice 
- State changes （indicate where you have changed）
- Disclose source （you have to attach the source code of this project as well）
- Same license （you have to use the same open source license）

Please use it as needed and have fun! 
