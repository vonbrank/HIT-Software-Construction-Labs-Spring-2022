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

## 1 实验目标概述

本次实验覆盖课程第 2、3 章的内容，目标是编写具有可复用性和可维护性 的软件，主要使用以下软件构造技术： 

+ 子类型、泛型、多态、重写、重载 
+ 继承、委派、CRP 
+ 语法驱动的编程、正则表达式 
+ 设计模式 

本次实验给定了多个具体应用，学生不是直接针对每个应用分别编程实现， 而是通过 ADT 和泛型等抽象技术，开发一套可复用的 ADT 及其实现，充分考虑 这些应用之间的相似性和差异性，使 ADT 有更大程度的复用（可复用性）和更 容易面向各种变化（可维护性）。

## 2 实验环境配置

[这篇博客](https://blog.vonbrank.com/archives/hit-software-construction-lab1-config/)详细介绍了本人给出的在 IDEA 下配置本次实验的其中一种解决方案。

### 2.1 先决条件

+ 安装 JDK （个人使用 JDK 18）并添加环境变量
+ 安装 JetBrains IDEA

### 2.2 使用 `Gradle` 管理项目

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
          task<Jar>("ElectionApp") {
              archiveBaseName.set("ElectionApp")
              manifest.attributes("Main-Class" to "app.ElectionApp")
              dependsOn(runtimeClasspath)
              from(runtimeClasspath)
          }
          task<Jar>("DinnerOrderApp") {
              archiveBaseName.set("DinnerOrderApp")
              manifest.attributes("Main-Class" to "app.DinnerOrderApp")
              dependsOn(runtimeClasspath)
              from(runtimeClasspath)
          }
          task<Jar>("BusinessVotingApp") {
              archiveBaseName.set("BusinessVotingApp")
              manifest.attributes("Main-Class" to "app.BusinessVotingApp")
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
  tasks["build"].dependsOn("ElectionApp", "DinnerOrderApp", "BusinessVotingApp")
  ```

+ 最后可以使用以下命令构建项目：

  ```bash
  ./gradlew build
  ```

### 2.3 Git Repo Url

在这里给出你的GitHub Lab1仓库的URL地址：

```txt
https://github.com/ComputerScienceHIT/HIT-Lab3-120L02****
```

## 3 实验过程

请仔细对照实验手册，针对每一项任务，在下面各节中记录你的实验过程、阐述你的设计思路和问题求解思路，可辅之以示意图或关键源代码加以说明（但千万不要把你的源代码全部粘贴过来！）。

### 3.1 待开发的三个应用场景

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

### 3.2 ADT识别与设计

该节是本实验的核心部分。

#### 3.2.1 任务1：投票类型 `VoteType`

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



#### 3.2.2 任务2：投票项 `VoteItem<C>` 

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



#### 3.2.3 任务3：选票 `Vote`

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

#### 3.2.4 任务4：投票活动 `Poll<C>` 的测试

考虑到 `Poll<C>` 接口中，只有 `result` 和 `toString` 返回值不是 `void` 对其测试需要结合整体行为进行。

在本例中，考虑构造一组点菜投票数据，调用 `Poll` 接口，测试 `result` 和 `toString` 方法是否返回期望结果：

```java
class PollTest {

    /*
     * Testing strategy
     *
     * 给出一组点菜表决的数据，及其表决结果的字符串，测试 Poll 的正确性
     * Poll 的实例由 Poll.create() 返回
     *
     * 覆盖 Poll 中完成一次表决的所有方法
     */
    ...
}
```

对 `Poll<C>` 接口的调用比较简单，比较麻烦的部分是创建选票数据，此处省略创建数据过程，只展示调用：

```java
class PollTest {
	@Test
    void test() {
        Poll<Dish> poll = Poll.create();
        
        ... //创建数据过程略

        poll.setInfo("Dishes Vote", Calendar.getInstance(), voteType, 4);

        poll.addCandidates(dishes);

        poll.addVoters(voters);

        poll.setCheckVoteValidityStrategy(new DefaultCheckVoteValidityStrategy());

        poll.addVote(voteGrandpa);
        poll.addVote(voteDad);
        poll.addVote(voteMum);
        poll.addVote(voteSon);

        poll.statistics(new DinnerOrderStatisticStrategy());
        poll.selection(new DinnerOrderSelectionStrategy(4));

        System.out.println(poll.result());

        assertTrue("A, 1\nB, 2\nC, 3\nD, 4".equals(poll.result()) ||
                "A, 1\nB, 2\nD, 3\nC, 4".equals(poll.result()));
        assertEquals("name: Dishes Vote, candidates: (A, B, C, D, E, F)", poll.toString());

    }
}
```

其中 `DefaultCheckVoteValidityStrategy`  `DinnerOrderStatisticStrategy` `DinnerOrderSelectionStrategy` 等是计票过程中采用策略模式的辅助 ADT，稍后会实现。



#### 3.2.5 任务5：投票活动 `Poll<C>` 的实现类 `GeneralPollImpl`

我们知道，完成依次表决的大致流程为：

+ 添加投票信息
+ 添加投票人、候选对象列表
+ 添加投票
+ 计票
+ 根据计票结果选择

根据代码框架与实际情况， `GeneralPollImpl` 的 rep 等可以是：

```java
public class GeneralPollImpl<C> implements Poll<C> {

    // 投票活动的名称
    private String name;
    // 投票活动发起的时间
    private Calendar date;
    // 候选对象集合
    protected List<C> candidates;
    // 投票人集合，key为投票人，value为其在本次投票中所占权重
    protected Map<Voter, Double> voters;
    // 拟选出的候选对象最大数量
    protected int quantity;
    // 本次投票拟采用的投票类型（合法选项及各自对应的分数）
    protected VoteType voteType;
    // 所有选票及其有效性
    protected Map<Vote<C>, Boolean> votes;
    // 计票结果，key为候选对象，value为其得分
    protected Map<C, Double> statistics;
    // 遴选结果，key为候选对象，value为其排序位次
    protected Map<C, Double> results;
    // 检查选票合法性策略
    CheckVoteValidityStrategy checkVoteValidityStrategy;

    // Rep Invariants
    //   quantity >= 1
    //   投票人 voters 数量 >= 1
    //   候选对象 candidates 数量 >= 1
    //	 选票 votes 数量 >= 1
    // Abstract Function
    //   AF(rep) = 一次投票，名称为 name , 日期为 date , 候选人集合为 candidates ,
    //   投票人及其权重集合为 votes ,
    //   拟选出选票最大数量为 quantity ,
    //   合法选项及其分数、所有选票集合、机票结果和遴选结果分别为
    //   voteType, votes, statistics, results
    // Safety from Rep Exposure
    //   由方法参数传入的 mutable 对象将通过防御式拷贝赋值给 rep
    //   ADT 本身是 mutable 的，但是各 mutable 的 rep 不会作为某个方法的返回值。
    ...
}
```

测试方面，`Poll<C>` 接口的测试前文已经说明。

实现方面，需要注意 `addVote` `statistics` `selection`方法中检查票据合法性、计票、选择的操作可以使用策略模式代理给特定的ADT完成。

如 `addVote` 时可以先检查其合法性，此处将 rep 中的 `votes` 修改为 `Map<Vote<C>, Boolean>` 类型，便于标记其合法性：

```java
public class GeneralPollImpl<C> implements Poll<C> {
    @Override
    public void addVote(Vote<C> vote) {
        // 此处应首先检查该选票的合法性并标记，为此需扩展或修改rep
        this.votes.put(vote, checkVoteValidityStrategy.checkAddVoteValidity(
                new ArrayList<>(candidates), vote, voteType));

        assert checkRep();
    }
}
```

又如 `statistics` 计票时同样可以调用 `checkVoteValidityStrategy` 检查合法性、标记不合法的票，然后调用 `StatisticsStrategy ss` 的方法进行计票，返回结果赋值给 `this.statistics` ：

```java
public class GeneralPollImpl<C> implements Poll<C> {
    @Override
    public void statistics(StatisticsStrategy ss) {
        // 此处应首先检查当前所拥有的选票的合法性
        Map<Vote<C>, Boolean> tmp;
        try {
            tmp = checkVoteValidityStrategy.checkVoteValidity(voters, votes);
        } catch (VoteInvalidException e) {
            System.out.println(e.toString());
            return;
        }
        votes.clear();
        tmp.forEach((k, v) -> {
            if (v) votes.put(k, true);
        });
        this.statistics = ss.getVoteStatistics(voteType, voters, candidates, votes.keySet());

    }
}
```

`result` 实现上，考虑需求中要求每一行一个选中对象名，后接其名次，可以使用函数式编程进行化简：

```java
public class GeneralPollImpl<C> implements Poll<C> {
    @Override
    public String result() {
        List<Map.Entry<C, Double>> list = new ArrayList<>(results.entrySet());
        list.sort((o1, o2) -> (int) Math.round(o1.getValue() - o2.getValue()));
        return list.stream()
                .map(entry -> entry.getKey() + ", " + (entry.getValue()).intValue())
                .collect(Collectors.joining("\n"));
    }
}
```

`GeneralPollImpl` 实现的其他部分简单模式即可，缺失部分稍后补充并进行测试。



#### 3.2.6 任务6：投票活动 `Poll<C>` 的子类型

`Poll<C>` 的子类型的行为与父类存在部分差异，也有新特性。考虑到 `Election` `BusinessVoting` `DinnerOrder` 并不是 `Poll` 接口相同功能的不同实现，因此客户端使用时不宜采用依赖转置原则，即无需依赖其接口，而直接依赖其实现就行。

以 `BusinessVoting` 为例，其 rep 修改为：

```java
public class BusinessVoting extends GeneralPollImpl<Proposal> implements Poll<Proposal> {
    // Rep Invariants
    //   quantity = 1
    //   投票人 voters 数量 >= 1
    //   候选对象 candidates 数量 = 1
    //	 选票 votes 数量 >= 1
    //   候选结果数量 results = 0, 1
    //   voteType 有 (支持, 1), (反对, -1), (弃权, 0)
    // Abstract Function
    //   AF(rep) = 一次投票，名称为 name , 日期为 date , 候选人集合为 candidates ,
    //   投票人及其权重集合为 votes ,
    //   拟选出选票最大数量为 quantity,
    //   合法选项及其分数、所有选票集合、机票结果和遴选结果分别为
    //   voteType, votes, statistics, results
    // Safety from Rep Exposure
    //   由方法参数传入的 mutable 对象将通过防御式拷贝赋值给 rep
    //   ADT 本身是 mutable 的，但是各 mutable 的 rep 不会作为某个方法的返回值。
	...
}
```

遵循 Liskov 替换原则，但客户端无需知道计票策略的实现细节，而只需要直接调用 `statistics` `selection` 无需传入任何参数就能进行计票、选择。`result` 同样存在差异，即 `BusinessVoting` 需要输出每个提案是否通过，`results` 字段中的 `value` 取值 `0` 或 `1` 就是用来表示其是否通过的：

```java
public class BusinessVoting extends GeneralPollImpl<Proposal> implements Poll<Proposal> {
    @Override
    public void setInfo(String name, Calendar date, VoteType type, int quantity) {
        super.setInfo(name, date, type, quantity);
        this.checkVoteValidityStrategy = new DefaultCheckVoteValidityStrategy();
    }
    public void statistics() {
        statistics(new BusinessVotingStatisticStrategy());
    }
    public void selection() {
        selection(new BusinessVotingSelectionStrategy());
    }
    @Override
    public String result() {
        boolean res = false;
        for(var item : results.entrySet()) {
            if (item.getValue() == 1.0) {
                res = true;
                break;
            }
        }
        if(res) return "表决通过！";
        return "表决未通过！";
    }
}
```

又如 `Election` 是匿名投票，因此计票前无法进行与实名相关的合法性检查，可以重写其 `statistics` 方法：

```java
public class Election extends GeneralPollImpl<Person> implements Poll<Person> {
    @Override
    public void statistics(StatisticsStrategy ss) {
        Map<Vote<Person>, Boolean> tmp = new HashMap<>(votes);
        votes.clear();
        tmp.forEach((k, v) -> {
            if (v) votes.put(k, true);
        });
        this.statistics = ss.getVoteStatistics(voteType, voters, candidates, votes.keySet());
    }
}
```

要实现无参数调用 `statistics` `selection` 方法，只需要在新建的方法里调用原有的 `statisctis` 和 `selection` 并传入对应策略即可。



### 3.3 ADT行为的设计与实现



#### 3.3.1 任务7：合法性检查

#### 3.3.2 任务8：采用 Strategy 设计模式实现灵活的计票规则

#### 3.3.3 任务9：采用 Strategy 设计模式实现灵活的遴选规则

#### 3.3.4 任务10：处理匿名和实名投票

#### 3.3.5 任务11：采用 Visitor 设计模式实现功能扩展

#### 3.3.6 任务12：基于语法的数据读入

### 3.4 任务13：应用设计与开发

#### 3.4.1 商业表决系统

#### 3.4.2 代表选举系统

#### 3.4.3 聚餐点菜系统

### 3.5 任务14：应对面临的新变化

#### 3.5.1 商业表决应用：可以一次表决多个商业提案

评估之前的设计是否可应对变化、代价如何

如何修改设计以应对变化

#### 3.5.2 代表选举应用：遴选规则变化

评估之前的设计是否可应对变化、代价如何

如何修改设计以应对变化

#### 3.5.3 聚餐点菜应用：取消权重设置、只计算“喜欢”的票数

评估之前的设计是否可应对变化、代价如何

如何修改设计以应对变化

### 3.6 Git仓库结构

请在完成全部实验要求之后，利用 `git log` 指令或 Git 图形化客户端或 GitHub 上项目仓库的 Insight 页面，给出你的仓库到目前为止的 Object Graph ，尤其是区分清楚 change 分支和 master 分支所指向的位置。

## 4 实验进度记录

请使用表格方式记录你的进度情况，以超过半小时的连续编程时间为一行。

每次结束编程时，请向该表格中增加一行。不要事后胡乱填写。

不要嫌烦，该表格可帮助你汇总你在每个任务上付出的时间和精力，发现自己不擅长的任务，后续有意识的弥补。

|     日期     |    时间段     |                           计划任务                           | 实际完成情况 |
| :----------: | :-----------: | :----------------------------------------------------------: | :----------: |
| `2022-06-02` | `10:30-11:30` |                           项目配置                           |     完成     |
| `2022-06-04` | `10:30-12:00` |                   `VoteType` 的设计与实现                    |     完成     |
| `2022-06-04` | `15:45-17:00` |                `VoteItem` `Vote` 的设计与实现                |     完成     |
| `2022-06-04` | `20:00-20:30` |               `Poll<Dish>` 部分单元测试的编写                |     完成     |
| `2022-06-05` | `20:00-21:15` |        `GeneralPollImpl` 的单元测试<br>与部分功能编写        |     完成     |
| `2022-06-22` | `15:00-18:30` | `GeneralPollImpl` 及其子类部分设计与实现<br>计票有效性检查设计 |     完成     |
| `2022-06-22` | `19:30-22:00` | `ChechVoteValidityStrategy` 及其子类的设计、实现、测试<br>`StatisticsStrategy` 及其子类的设计、实现、测试 |     完成     |
| `2022-06-23` | `09:30-10:15` | `StatisticsStrategy` 重构为<br>支持 `Map<Voter, Double>` 传入投票人及其权重 |     完成     |
| `2022-06-23` | `20:00-00:30` | 完成 `SelectionStrategy`及其子类的设计、实现、测试<br>为`Dish` `Person` `Proposal` `Voter` 重写 `toString` 方法 |     完成     |
| `2022-06-24` | `17:30-20:00` |              完成`Poll`继承树的设计、实现、测试              |     完成     |
| `2022-06-25` | `10:00-11:00` |               为 `Poll` 接口添加访问者模式特性               |     完成     |
| `2022-06-25` | `16:00-18:00` | 为`VoteType`添加 regex 构造器，并进行测试<br>完成 `BusinessVotingApp` `DinnerOrderApp` `ElectionApp` 设计与实现 |     完成     |
| `2022-06-25` | `19:00-23:00` | 为项目添加本地依赖项与持续集成<br>修复一些 bug <br>在 `change` 分支上实现新需求 |     完成     |

## 5 实验过程中遇到的困难与解决途径

| 遇到的难点 | 解决途径 |
| :--------: | :------: |
|            |          |
|            |          |
|            |          |

## 6 实验过程中收获的经验、教训、感想

### 6.1 实验过程中收获的经验和教训（必答）

### 6.2 针对以下方面的感受（必答）

(1)  重新思考 Lab2 中的问题：面向 ADT 的编程和直接面向应用场景编程，你体会到二者有何差异？本实验设计的 ADT 在三个不同的应用场景下使用，你是否体会到复用的好处？

(2)  重新思考 Lab2 中的问题：为 ADT 撰写复杂的 specification, invariants, RI, AF，时刻注意 ADT是否有 rep exposure，这些工作的意义是什么？你是否愿意在以后的编程中坚持这么做？

(3)  之前你将别人提供的 ADT/API 用于自己的程序开发中，本次实验你尝试着开发给别人使用的 ADT/API ，是否能够体会到其中的难处和乐趣？

(4)  你之前在使用其他软件时，应该体会过输入各种命令向系统发出指令。本次实验你开发了一个简单的解析器，使用语法和正则表达式去解析一个遵循特定规则的字符串并据此构造对象。你对语法驱动编程有何感受？

(5)  Lab1和Lab2的工作都不是从0开始，而是基于他人给出的设计方案和初始代码。本次实验中也提供了一部分基础代码。假如本实验要求你完全从0开始进行ADT的设计并用OOP实现，你觉得自己是否能够完全搞定？你认为“设计ADT”的难度主要体现在哪些地方？

(6)  “抽象”是计算机科学的核心概念之一，也是ADT和OOP的精髓所在。本实验的三个应用既不能完全抽象为同一个ADT，也不是完全个性化，如何利用“接口、抽象类、类”三层体系以及接口的组合、类的继承、委派、设计模式等技术完成最大程度的抽象和复用，你有什么经验教训？

(7)  关于本实验的工作量、难度、deadline。

(8)  课程结束了，你对《软件构造》课程内容和任课教师的评价如何？

 

