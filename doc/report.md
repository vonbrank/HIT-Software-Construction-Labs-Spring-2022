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

根据要求，投票过程中有两处需要检查合法性的地方。

在 `addVote` 方法中，需要检查并标记每张选票的合法性，满足：

+ 一张选票中没有包含本次投票活动中的所有候选人 
+ 一张选票中包含了不在本次投票活动中的候选人 
+  一张选票中出现了本次投票不允许的选项值 
+ 一张选票中有对同一个候选对象的多次投票 
+ （仅应用 2）一张选票中对所有候选对象的支持票数量大于 $k$ 

如上文所述，此处采用的默认处理方法是，对于传入的 `vote` ，检查其合法性，将得到的合法性传入 `votes` ，这是一个 `Vote` 到 `Boolean` 的映射，用来标记选票合法性：

```java
public class GeneralPollImpl<C> implements Poll<C> {
    ...
    @Override
    public void addVote(Vote<C> vote) {
        // 此处应首先检查该选票的合法性并标记，为此需扩展或修改rep
        this.votes.put(vote, checkVoteValidityStrategy.checkAddVoteValidity(
                new ArrayList<>(candidates), vote, voteType));

        assert checkRep();
    }
    ...
}
```

其中 `checkVoteValidityStrategy` 同样是作为委托，用于检查选票合法性的辅助 ADT，其接口形式如下：

```java
public interface CheckVoteValidityStrategy {
    /**
     * 根据固有候选对象、投票选项，检查新增投票是否合法
     *
     * @param targetCandidates 候选对象
     * @param newVote          新增投票
     * @param voteType         投票选项
     * @param <C>              候选对象类型
     * @return 合法返回 true ，否则返回 false
     */
    <C> boolean checkAddVoteValidity(List<C> targetCandidates, Vote<C> newVote, VoteType voteType);

    /**
     * 根据投票人列表和选票列表，在开始计票前检验其合法性
     * @param voters 投票人列表及其权重
     * @param votes 选票列表及其有效性，若一个投票人提交了多张选票，则它们均为非法，计票时不计算在内，即被标记为 false
     *              此方法将直接修改这个对象
     * @param <C> 候选人类型
     * @throws VoteInvalidException 有人没有投票时抛出异常
     * @return 返回修改后的有效票集合
     */
    <C> Map<Vote<C>, Boolean> checkVoteValidity(
        Map<Voter, Double> voters, Map<Vote<C>, Boolean> votes) throws VoteInvalidException;
}

```

实现类 `DefaultCheckVoteValidityStrategy` 默认选票是实名选票，非实名选票将报异常。 `ElectionCheckVoteValidityStrategy` 用来检查非实名选票的合法性。

在 `addVote` 阶段，选票使用 `checkAddVoteValidity` 来检查，不合法的选票返回 `false` 。

在 `statistics` 阶段也要检查选票合法性，满足：

+ 若尚有投票人还未投票，则不能开始计票；
+ 若一个投票人提交了多次选票，则它们均为非法，计票时不计算在内

这两者都依赖实名选票，因此 `Election` 的 `statistics` 方法中不会调用 `checkVoteValidity` 检查合法性。匿名实名选票的实现将在稍后讲述。`checkVoteValidity` 返回的是一个新的 `Map<Vote, Boolean>` ，`Vote` 和无效和非法两种，无效选票将标记为 `false` ，在返回的 `Map` 中体现，而选票的非法表明又人未投票，将抛出自定义的 `checked` 异常 `VoteInvalidException` 。

测试方面，针对 `addVote` 阶段的测试策略如下：

```txt
/*
 * Testing strategy
 * 等价类划分：
 * 添加投票阶段有效性：
 * 1. newVote 没有包含 targetCandidates 中所有候选对象
 * 2. newVote 包含了不在 targetCandidates 中的候选对象
 * 3. newVote 出现了 voteType 中没有的选项
 * 4. newVote 中出现了对 同一个对象的多次投票
 * 5. newVote 的支持数超出限制（仅针对 ElectionCheckVoteValidityStrategy ）
 */
```

针对 `statistics` 阶段的测试策略如下：

```txt
/*
 * 计票前阶段有效性：
 * Default:
 * 1. 还有人没有投票
 * 2. 都投票了，但有人没有实名
 * 2. 都投票了，都实名了，但有人重复投票
 * 3. 都投票了，都实名了，且没人重复投票
 */
```

实现方面，`DefaultCheckVoteValidityStrategy` 的 `checkAddVoteValidity` 用于检查默认状态下选票的上述前 $4$ 点要求，`ElectionCheckVoteValidityStrategy` 是前者的子类，额外检查第 $5$ 点要求。`checkVoteValidity` 是公用的，反正 `ElectionCheckVoteValidityStrategy` 受委托时也不会被调用，毕竟是匿名投票。



#### 3.3.2 任务8：采用 Strategy 设计模式实现灵活的计票规则

三种场景的计票方式各不相同，适合使用策略模式。计票策略的总接口为 `StatisticsStrategy` ，功能如下：

```java
public interface StatisticsStrategy {
    /**
     * 根据候选人列表、投票信息，统计投票结果
     * @param voteType 投票选项类型
     * @param voters 投票人及其权重
     * @param candidates 输入的候选人列表
     * @param votes 输入有效投票信息
     * @return 计票结果，任意 (k, v) 属于返回的 Map ，表示候选人 k 的得分为 v
     */
     <C> Map<C, Double> getVoteStatistics(VoteType voteType,  Map<Voter, Double> voters, List<C> candidates, Set<Vote<C>> votes);
}
```

传入选票数据，返回一个候选人到 `Double` 的得分，表示候选对象的评分。

三种计票方式各不相同，故设计 `StatisticsStrategy` 的三种实现：`BusinessVotingStatisticStrategy` `DinnerOrderStatisticStrategy` 和 `ElectionStatisticStrategy` 。

以 `BusinessVotingSelectionStrategy` 为例，商业表决中，默认提案只有一个，计算前应该先检查，不合法将抛出异常。而后计算对这个表决的所有选票，将支持的股东的权重之和：

```java
public class BusinessVotingStatisticStrategy implements StatisticsStrategy {
    @Override
    public <Proposal> Map<Proposal, Double> getVoteStatistics(
            VoteType voteType, Map<Voter, Double> voters, 
        	List<Proposal> candidates, Set<Vote<Proposal>> votes) {
        if (candidates.size() != 1) throw new RuntimeException("候选提案数只能为 1 .");
        Map<Proposal, Double> res = new HashMap<>();
        candidates.forEach(candidate -> res.put(candidate, 0.0));
        votes.forEach(vote -> {
            if (!(vote instanceof RealNameVote)) throw new RuntimeException("商业提案为实名计票");
            RealNameVote<Proposal> realNameVote = (RealNameVote<Proposal>) vote;
            double weight = voters.get(((RealNameVote<Proposal>) vote).getVoter());
            vote.getVoteItems().forEach(voteItem -> {
                Proposal candidate = voteItem.getCandidate();
                if (voteItem.getVoteValue().equals("支持")) {
                    double newWeight = res.get(candidate) + weight;
                    res.put(candidate, newWeight);
                }
            });
        });
        return res;

}
```

`DinnerOrderStatisticStrategy` 和 `ElectionStatisticStrategy` 与此大同小异。`Poll<C>` 中对计票策略的使用上文已经演示，在此不再赘述。

测试方面，三种场景的测试策略如下：

```txt
/*
 * Testing strategy
 * 提案计票的等价类划分：
 * 1. 提案数不是 1
 * 2. 提案数为 1 ，vote 类型为 Vote
 * 3. 提案数为 1 ，vote 类型为 RealNameVote，其 voter 类型为 Voter
 *
 * 点菜计票的等价类划分：
 * 1. vote 类型为 Vote
 * 2. vote 类型为 RealNameVote，其 voter 类型为 Voter
 * 3. vote 类型为 RealNameVote，其 voter 类型为 WeightVoter
 *
 * 选举计票的等价类划分：
 * 任意一种合法的输入
 *
 */
```

单元测试代码照例模拟即可。



#### 3.3.3 任务9：采用 Strategy 设计模式实现灵活的遴选规则

三种场景的遴选方式也不相同，同样使用策略模式，在 `selection` 方法中传入特定策略，返回计票结果赋值给 `results` 即可。总接口为 `SelectionStrategy` ，定义如下：

```java
public interface SelectionStrategy {
    /**
     * 根据候选人得分计算每个候选人的名次
     *
     * @param statistics 输入的候选人得分，任意 (k, v) 属于 statistics 表示候选人 k 得分为 v
     * @param <C>        候选对象类型
     * @return 候选对象名次，任意 (k, v) 属于返回值，表示候选对象 k 的名次为 v
     */
    <C> Map<C, Double> getSelectionResult(Map<C, Double> statistics);
}
```

即传入数据，返回候选对象到一个 `Double` 的映射，表示其名次。

`BusinessVotingStatisticStrategy` `DinnerOrderStatisticStrategy` 和 `ElectionStatisticStrategy` 代表三个场景的不同遴选策略实现。

同样以 `BusinessVoting` 为例，只需要比较唯一提案的得分与 `2/3 * 100` 的大小，若大于，说明提案通过，提案映射为 `1.0` ；否则不通过，映射为 `0.0` ：

```java

public class BusinessVotingSelectionStrategy implements SelectionStrategy {
    @Override
    public <C> Map<C, Double> getSelectionResult(Map<C, Double> statistics) {
        if (statistics.size() != 1) throw new RuntimeException("应该只有一个提案");
        for (C candidate : statistics.keySet()) {
            if (!(statistics.get(candidate) >= 0 && statistics.get(candidate) <= 100))
                throw new RuntimeException("提案支持率百分比应该在 0 - 100 之间");
            return Map.of(candidate, statistics.get(candidate) >= 200 / 3.0 ? 1.0 : 0.0);
        }
        return null;
    }
}
```

使用方式同样容易，由于三个场景遴选都是针对 `statistics` 字段进行的，不需要额外特殊操作，可以在 `GeneralPollImpl` 中完成：

```java
public class GeneralPollImpl<C> implements Poll<C> {
    @Override
    public void selection(SelectionStrategy ss) {
        this.results = ss.getSelectionResult(statistics);
    }
}
```

测试方法，有测试策略：

```txt
/*
 * Testing strategy
 * 等价类划分：
 *
 * 商业投票:
 * 1. statistics.size() != 1
 * 2. statistics.size() = 1 ，提案百分比不在 0 - 100 之间
 * 3. statistics.size() = 1 ，提案百分比在 0 - 100 之间，表决通过
 * 4. statistics.size() = 1 ，提案百分比在 0 - 100 之间，表决不通过
 *
 * 选举:
 * 1. statistics.size() <= k
 * 2. statistics.size() > k ，存在不确定
 * 3. statistics.size() > k ，不存在不确定
 *
 * 针对点菜的样例
 * 1. statistics.size() <= k
 * 2. statistics.size() > k ，存在不确定
 * 3. statistics.size() > k ，不存在不确定
 */
```

照例模拟即可。



#### 3.3.4 任务10：处理匿名和实名投票

实现匿名和实名投票的分别有直接继承和使用装饰器模式。直接继承较为简单，直接增加一个新的 `voter` 字段即可，易于编写 `equals` 和 `hashcode` 方法也使得其易于在集合中使用，本项目中将使用这种方法。装饰器模式使得扩展和组合 `Vote` 的特性非常容易。

原本的 `Vote` 的 rep 与构造器如下：

```java
public class Vote<C> implements Voteable<C> {

    // 缺省为“匿名”投票，即不需要管理投票人的信息

    // 一个投票人对所有候选对象的投票项集合
    private Set<VoteItem<C>> voteItems = new HashSet<>();
    // 投票时间
    private Calendar date = Calendar.getInstance();
    // 选票 id
    long id;
    public Vote(Set<VoteItem<C>> voteItems) {
        this.voteItems.addAll(voteItems);
        Random r = new Random();
        this.id = r.nextLong();
    }
    ...
}
```

`RealNameVote` 继承之，增加 `Voter` 字段：

```java
//immutable
public class RealNameVote<C> extends Vote<C> {

    //投票人
    private final Voter voter;

    public RealNameVote(Set<VoteItem<C>> voteItems, Voter voter) {
        super(voteItems);
        this.voter = voter;
    }
    ...
}
```

再重写 `equals` 和 `hashcode` 方法即可。

要使用装饰器模式向 `Vote` 上装饰 `Voter` 属性，可以参考装饰器模式结构：

![](https://pic2.zhimg.com/v2-c9f694b243f901a2bae58d54e2353b95_b.png)

先将 `Vote` 抽象为 `Voteable` 接口，对应图中的 `Component` ：

```java
public interface Voteable<C> {
    /**
     * 查询该选票中包含的所有投票项
     *
     * @return 所有投票项
     */
    public Set<VoteItem<C>> getVoteItems();
    /**
     * 一个特定候选人是否包含本选票中
     *
     * @param candidate 待查询的候选人
     * @return 若包含该候选人的投票项，则返回true，否则false
     */
    public boolean candidateIncluded(C candidate);
}
```

原来的 `Vote` 对应图中的 `ConcreteComponent` ，在装饰器模式中属于核心类地位。

再创建总装饰器类和实名选票装饰器：

```java
public abstract class VoteDecorator<C> implements Voteable<C> {
    Voteable<C> target;
    public VoteDecorator(Voteable<C> target) {
        this.target = target;
    }
    @Override
    public Set<VoteItem<C>> getVoteItems() {
        return target.getVoteItems();
    }
    @Override
    public boolean candidateIncluded(C candidate) {
        return target.candidateIncluded(candidate);
    }
}
public class RealNameVoteDecorator<C> extends VoteDecorator<C> {
    private final Voter voter;
    public RealNameVoteDecorator(Voteable<C> target, Voter voter) {
        super(target);
        this.voter = voter;
    }
    public Voter getVoter() {
        return this.voter;
    }
}
```

分别对应图中的 `Decorator` 和 `ConcreteDecorator1, ConcreteDecorator2, ...` 。要创建一个带实名装饰器的 `Voteable` ，可以写成：

```java
public class VoteApp {
    public static void main(String[] args) {
        Voter voter2 = new Voter("Voter-02");
        Voteable<Person> realNameVoteDecorator = new RealNameVoteDecorator<>(vote, voter2);
    }
}
```



#### 3.3.5 任务11：采用 Visitor 设计模式实现功能扩展

访问者模式是将数据与操作解耦的设计模式，要使用访问者模式操作 `Poll<C>` 中的数据，可以先定义访问者接口：

```java
public interface PollVisitor<C> {
    void visitVote(Map<Vote<C>, Boolean> votes);
}
```

因为本例的需求中需要计算有效投票的百分比，因此访问者接口中暂时只有 `visitVote` 方法，接受一个选票到 `Boolean` 的映射。

实际使用时，将有效率保存在一个 `voteValidityRate` 字段中，使用 `getter` 取出：

```java
public class VoteValidityRateVisitor<C> implements PollVisitor<C> {
    private double voteValidityRate = 0;
    @Override
    public void visitVote(Map<Vote<C>, Boolean> votes) {
        int total = votes.size();
        AtomicInteger validityNum = new AtomicInteger();
        votes.forEach((k, v) -> {
            if (v) validityNum.getAndIncrement();
        });
        voteValidityRate = validityNum.get() / (double) total;
    }
    public double getVoteValidityRate() {
        return voteValidityRate;
    }
}
```

`Poll<C>` 中增加一个接口 `public void accept(PollVisitor<C> pollVisitor);` 用于传入访问者，然后进行调用：

```java
public class GeneralPollImpl<C> implements Poll<C> {
	@Override
    public void accept(PollVisitor<C> pollVisitor) {
        pollVisitor.visitVote(votes);
    }
}
```

 在客户端中使用如下所示：

```java
public class BusinessVotingApp {

	public static void main(String[] args) throws Exception {
        ...
        VoteValidityRateVisitor<Proposal> voteValidityRateVisitor = new VoteValidityRateVisitor<>();
        poll.accept(voteValidityRateVisitor);
        System.out.printf("选票有效率为：%s\n", voteValidityRateVisitor.getVoteValidityRate());
        ...
    }
}
```



#### 3.3.6 任务12：基于语法的数据读入

这是针对 `VoteType` 的新需求，即要求传入一个形如 `"支持"(1)|"不支持"(-1)|"无所谓"(0)` 字符串，将其解析为选项与评分的映射。其中选项名称长度不大于 $5$ ，分数只允许整数。还需要支持形如 `"Yes"|"No"|"SoSo"` 的无得分选项。

针对这两种情况，可以分别编写正则表达式：

+ 有得分：`"[^"]+"\(-?\d\)(\|"[^"]+"\(-?\d\))*`

+ 无得分：`"[^"]+"(\|"[^"]+")*`

只有通过这两种 regex 中的任意一种测试才能继续，否则将抛出自定义的 `StringFormatException` 异常。

而后使用 `|` 分割字符串，依次使用表达式 `"[^"]{1,5}"` 匹配选项部分，`\(-?\d\)` 匹配数值部分（如果有的话），使用 Java 类库 `Pattern` 和 `Matcher` 截断出所需要的字符，完成匹配。

```java
public class VoteType {
    public VoteType(String option) throws StringFormatException {
        String errorMsg = "选项格式错误，参考格式：\"喜欢\"(2)|\"不喜欢\"(0)|\"无所谓\"(1)。";
        String regexWithScore = "\"[^\"]+\"\\(-?\\d\\)(\\|\"[^\"]+\"\\(-?\\d\\))*";
        String regexWithoutScore = "\"[^\"]+\"(\\|\"[^\"]+\")*";
        boolean withScore;
        if (option.matches(regexWithScore))
            withScore = true;
        else if (option.matches(regexWithoutScore)) {
            withScore = false;
        } else throw new StringFormatException(errorMsg);
        List<String> optionsList = List.of(option.split("\\|"));
        Pattern optionNamePattern = Pattern.compile("\"[^\"]{1,5}\"");
        Pattern optionScorePattern = Pattern.compile("\\(-?\\d\\)");
        ...// 以下内容此处略
    }
}
```

测试方面，考虑 $5$ 字符长度限制，测试策略如下：

```txt
/*
*  Testing strategy
*
*  1. 英语，5 个字符以内   "Yes"(1)|"No"(-1)|"SoSo"(0)
*  2. 英语，超过 5 个字符  "Support"(1)|"Oppose"(-1)|"Waive"(0)
*  3. 汉语，5 个字符以内   "支持"(1)|"不支持"(-1)|"无所谓"(0)
*  4. 汉语，超过 5 个字符  "超级无敌支持"(1)|"不支持"(-1)|"无所谓"(0)
*  5. 英语，无分数        "Yes"|"No"|"SoSo"
*  6. 汉语，无分数        "支持"|"不支持"|"无所谓"
*/
```



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

 

