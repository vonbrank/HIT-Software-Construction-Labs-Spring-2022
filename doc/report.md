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

客户端使用这几个场景过程中，过程是类似的：

```java
public class Main {
    public static void mainn(String[] args) {
        Poll<...> poll = new ...;
        poll.setInfo(...);
        poll.addVoters(...);
        poll.addCandidates(...);
        poll.addVote(...);
        poll.statistics(...);
        poll.selection(...);
        System.out.printfln(poll.result());
    }
}
```

主要差异在于各类 `stragety` 的实现和 `addVote` `statistics` `selectoin` 的具体行为中。

## 3.2 ADT识别与设计

该节是本实验的核心部分。

### 3.2.1 任务1：投票类型 `VoteType`

 `VoteType` 是用来存储投票中选项及其对应分数的 ADT，其 rep 等如图所示：

```java
//immutable
public class VoteType {

    // key为选项名、value为选项名对应的分数
    private Map<String, Integer> options = new HashMap<>();

    // Rep Invariants
    //   每个选项名字都不相同
    //   至少有一个选项
    // Abstract Function
    //   AF(options) = 一类投票选项，对于任意 (k, v) 属于 options ，表示存在名为 k 选项，其分数为 v
    // Safety from Rep Exposure
    //   VoteType 本身是 immutable 的，没有 mutator ，保证初始化后不会被修改
    //   构造时传入的 Map 在赋值给 options 前会拷贝一份
}
```

测试策略比较简单，本例中只提供一组测试样例即可完成所有方法的测试：

```java
class VoteTypeTest {

	/* Testing strategy
	 *
	 * 提供一组 VoteType 选项及其分数：(["喜欢", 2], ["不喜欢", 0], ["无所谓", 1])
	 * 测试 VoteType 的所有方法
	 *
	 */

	@Test
	void test() {
		Map<String, Integer> options = new HashMap<>();
		options.put("喜欢", 2);
		options.put("不喜欢", 0);
		options.put("无所谓", 1);
		VoteType voteType = new VoteType(options);

		assertTrue(voteType.checkLegality("喜欢"));
		assertTrue(voteType.checkLegality("无所谓"));
		assertFalse(voteType.checkLegality("非常喜欢！(❤ ω ❤)"));

		assertEquals(2, voteType.getScoreByOption("喜欢"));
		assertEquals(0, voteType.getScoreByOption("不喜欢"));
		assertEquals(1, voteType.getScoreByOption("无所谓"));

		Map<String, Integer> otherOptions = new HashMap<>();
		otherOptions.put("喜欢", 2);
		otherOptions.put("无所谓", 1);
		VoteType otherVoteType = new VoteType(otherOptions);
		assertNotEquals(voteType, otherVoteType);
		assertNotEquals(voteType.hashCode(), otherVoteType.hashCode());
		otherOptions.put("不喜欢", 0);
		otherVoteType = new VoteType(otherOptions);
		assertEquals(voteType, otherVoteType);
		assertEquals(voteType.hashCode(), otherVoteType.hashCode());

	}

}
```

实现方面，`VoteType` 所有接口的 spec 描述都十分显然，直接模拟即可，以 `checkLegality` 为例：

```java
class VoteTypeTest {
    ...
    /**
     * 判断一个投票选项是否合法（用于Poll中对选票的合法性检查）
     * <p>
     * 例如，投票人给出的投票选项是“Strongly reject”，但options中不包含这个选项，因此它是非法的
     * <p>
     * 不能改动该方法的参数
     *
     * @param option 一个投票选项
     * @return 合法则true，否则false
     */
    public boolean checkLegality(String option) {
        return options.get(option) != null;
    }
}
```



### 3.2.2 任务2：投票项 `VoteItem<C>` 

 `VoteItem<C>` 是存储一张选票中，每个候选对象及其选项的对应关系，是 immutable 的，rep 等如下：

```java
//immutable
public class VoteItem<C> {

    // 本投票项所针对的候选对象
    private C candidate;
    // 对候选对象的具体投票选项，例如“支持”、“反对”等
    // 无需保存具体数值，需要时可以从投票活动的VoteType对象中查询得到
    private String value;

    // Rep Invariants
    //   本投票项针对的候选对象和投票选项都不为空
    // Abstract Function
    //   AF(candidate, value) = 一个针对 candidate 的投票项，投票选项为 value
    // Safety from Rep Exposure
    //   VoteItem<C> 是 immutable 的，candidate 和 value 只在构造函数中被赋值，没有其他 mutator
}
```

测试策略同样是给出一组数据即可测完所有接口：

```java
class VoteItemTest {

    /* Testing strategy
     * 给出一个对于 Dish 的具体表决：(("A", 10), "喜欢")
     * 测试 voteItem 中的所有方法
     */
    ...
}
```

实现方面，`VoteItem` 与 `VoteType` 都是 immutable 的，可以实现其 `equals` 和 `hashcode` 方法：

```java
public class VoteItem<C> {
    @Override
    public int hashCode() {
        return Objects.hash(candidate, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof VoteItem<?>)) return false;
        VoteItem<?> otherVoteItem = (VoteItem<?>) obj;
        return otherVoteItem.candidate.equals(this.candidate) && otherVoteItem.value.equals(this.value);
    }
}
```



### 3.2.3 任务3：选票 `Vote`

`Vote` 是一个投票人面对所有投票项的集合的 ADT，代表一个投票人对所有候选对象的选择。`Vote` 在生成时就是确定的，不可修改的，为了保证这一点，并区分不同的选票，可以为其添加一个字段 `id` ，这个 `id` 在构造时使用 Java 标准库提供的 `Random` 生成。`Vote` 的 rep 如下所示：

```java
//immutable
public class Vote<C> implements Voteable<C> {

    // 缺省为“匿名”投票，即不需要管理投票人的信息

    // 一个投票人对所有候选对象的投票项集合
    private Set<VoteItem<C>> voteItems = new HashSet<>();
    // 投票时间
    private Calendar date = Calendar.getInstance();
    // 选票 id
    long id;

    // Rep Invariants
    //   voteItems 和 date 都不为 null 且 Vote<C> 本身是 immutable 的
    // Abstract Function
    //   AF(voteItems, date) = 一次投票，voteItems 包含了改投票的所有内容，date 是这次投票的创建时间
    // Safety from Rep Exposure
    //   Vote<C> 是 immutable 的，voteItems 和 date 只在构造函数内被赋值，没有其他 mutator
    //   返回 voteItems 的方法将执行防御式拷贝
}
```

测试方面，仍然是采用一组数据尝试覆盖所有方法的策略：

```java
class VoteTest {

    /*
     * 提供一组对于 Dish 的表决：[("A", "喜欢"), ("B", "不喜欢")]
     * 覆盖 vote 中所有方法
     */
}
```

同时评测其 `equals` 与 `hashcode` 方法，为了构造两个 `id` 相同的 `Vote` 便于测试，可以采用反射实现：

```java
Vote<Dish> vote = new Vote<Dish>(dishSet);
Vote<Dish> otherVote = new Vote<Dish>(dishSet);

Field f = vote.getClass().getDeclaredField("id");
Object voteID = f.get(vote);
f.set(otherVote, voteID);
```

实现方面，`Vote` 的构造函数如下所示：

```java
class VoteTest {
    ...
    /**
     * 创建一个选票对象
     * <p>
     * 可以自行设计该方法所采用的参数
     */
    public Vote(Set<VoteItem<C>> voteItems) {
        this.voteItems.addAll(voteItems);
        Random r = new Random();
        this.id = r.nextLong();
    }
}
```

构造函数中只传入一个 `Set` 即可，然后进行防御式拷贝。

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

 

