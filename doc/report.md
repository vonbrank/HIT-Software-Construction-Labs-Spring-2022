![](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiv49DJqSGUcZjoRnB8eIJo1b9wcqqcVpllg&usqp=CAU)

<h1 style="text-align:center">
    <p>2022年春季学期计算学部《软件构造》课程</p>
    <p>Lab 2 实验报告</p>
</h1>

|  姓名  |         VonBrank          |
|:----:|:--------------------:|
|  学号  |      120L02****      |
|  班号  |       20030**        |
| 电子邮件 | vonbrank@outlook.com |
| 手机号码 |           |

## 1 实验目标概述

本次实验训练抽象数据类型（ADT）的设计、规约、测试，并使用面向对象 编程（OOP）技术实现 ADT。具体来说： 

+ 针对给定的应用问题，从问题描述中识别所需的 ADT； 
+ 设计 ADT 规约（pre-condition、post-condition）并评估规约的质量； 
+ 根据 ADT 的规约设计测试用例； 
+ ADT 的泛型化； 
+ 根据规约设计 ADT 的多种不同的实现；针对每种实现，设计其表示 （representation）、表示不变性（rep invariant）、抽象过程（abstraction function） 
+ 使用 OOP 实现 ADT，并判定表示不变性是否违反、各实现是否存在表 示泄露（rep exposure）； 
+ 测试 ADT 的实现并评估测试的覆盖度； 
+ 使用 ADT 及其实现，为应用问题开发程序； 
+ 在测试代码中，能够写出 testing strategy 并据此设计测试用例。

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

  ```kotlin
  sourceSets {
      val p1 = create("P1") {
          java.srcDir("./src/P1")
          task<Jar>("jarP1") {
              archiveBaseName.set("P1")
              manifest.attributes("Main-Class" to "poet.Main")
              dependsOn(runtimeClasspath)
              from(runtimeClasspath)
          }
      }
  
      val p2 = create("P2") {
          java.srcDir("./src/P2")
          compileClasspath += p1.output
          runtimeClasspath += p1.output
          task<Jar>("jarP2") {
              archiveBaseName.set("P2")
              manifest.attributes("Main-Class" to "FriendshipGraph")
              dependsOn(runtimeClasspath)
              from(runtimeClasspath)
          }
      }
  
      create("P1Test") {
          java.srcDir("./test/P1")
          compileClasspath += configurations.testCompileClasspath + p1.output
          runtimeClasspath += configurations.testRuntimeClasspath + p1.output
      }
  
      create("P2Test") {
          java.srcDir("./test/P2")
          compileClasspath += configurations.testCompileClasspath + p1.output + p2.output
          runtimeClasspath += configurations.testRuntimeClasspath + p1.output + p2.output
      }
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
https://github.com/ComputerScienceHIT/HIT-Lab2-120L02****
```

## 3 实验过程

请仔细对照实验手册，针对三个问题中的每一项任务，在下面各节中记录你的实验过程、阐述你的设计思路和问题求解思路，可辅之以示意图或关键源代码加以说明。

### 3.1 Poetic Walks

本题需要编写一个实现图 `Graph` 的数据结构类，支持添加顶点、边，查询以某个顶点为起点/终点的终点集合、起点集合及其权重。

然后使用该数据结构编写 `GraphPoet` 类，用来对输入的语句进行扩展。

#### 3.1.1 Get the code and prepare Git repository

+ 进入链接 `https://classroom.github.com/a/NL2TjK2z` 按照指示创建仓库，并将仓库名更改为 `HIT-Lab2-120L02****` 。
+ 创建一个 `READMD.md` 并使用 `git clone git@github.com:ComputerScienceHIT/HIT-Lab2-120L02****.git` 将仓库 `clone` 下来，即可开始工作。

#### 3.1.2 Problem 1: Test Graph \<String\>

本题要求编写一个测试用例来测试 `Graph<String>` ，测试用例需要满足前置条件，本人编写的用例如下，可以测试各种操作：

```java
Graph<Integer> graph = Graph.empty();
assertTrue(graph.add(1));
assertTrue(graph.add(3));
assertFalse(graph.add(1));
assertTrue(graph.add(2));
assertTrue(graph.add(5));
assertFalse(graph.add(3));
assertTrue(graph.add(0));
assertTrue(graph.add(4));
assertEquals(0, graph.set(0, 2, 1));
assertEquals(0, graph.set(0, 4, 4));
assertEquals(0, graph.set(0, 5, 2));
assertEquals(1, graph.set(0, 2, 0));
assertEquals(0, graph.set(0, 2, 4));
assertEquals(0, graph.set(1, 4, 8));
assertEquals(0, graph.set(1, 5, 5));
assertEquals(0, graph.set(2, 3, 7));
assertEquals(0, graph.set(2, 4, 3));
assertEquals(5, graph.set(1, 5, 1));
assertEquals(0, graph.set(4, 5, 5));
assertEquals(8, graph.set(1, 4, 8));
assertTrue(graph.remove(1));
assertFalse(graph.remove(1));
assertEquals(Map.of(0, 4, 2, 3), graph.sources(4));
assertEquals(Map.of(5, 2, 4, 4, 2, 4), graph.targets(0));
```

#### 3.1.3 Problem 2: Implement Graph \<String\>

这个部分要求实现 `Graph<String>` 接口的两个具体实现。

##### 3.1.3.1 Implement ConcreteEdgesGraph

本题要求实现以边存储图，具体来说，提供点集 `Set<String> vertices` 和边表 `List<Edge<String>> edges` 这两个 `rep` 。`ConcreteEdgesGraph` 和 `Edge` 的 `RI, AF` 等如图所示：

```java
public class ConcreteEdgesGraph<L> implements Graph<L> {

    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();

    // Abstraction function:
    //   AF(vertices, edges) = a graph with all vertices in the filed vertices and all the edges in the filed edges.
    // Representation invariant:
    //   vertices is the set of all vertices. Every vertex is labeled with a String which is unique in a Graph
    //   edges is the list of all edges. Once an edge has a source "a" and a target "b", then the vertices "a" and "b"
    //   must be in the set vertices
    // Safety from rep exposure:
    //   All fields are private;
    //   vertices is a HashSet<String>, so every element in the Set is guaranteed immutable, and the whole field
    //   vertices itself is a mutable, so the method vertices() makes a defensive copy to avoid sharing the ref of
    //   the field vertices
    //   edges is mutable, but no edge will be returned
    // TODO constructor
}

class Edge<L> {
    // TODO fields
    private final L source;
    private final L target;
    private final int weight;

    // Abstraction function:
    //   AF(source, target, weight) = an edge from source to target has a weight.
    // Representation invariant:
    //   source and target are Strings
    //   weight is the weight of the edge and must be positive. Once the weight is zero, the edge should not exist
    // Safety from rep exposure:
    //   source and target are String, so are guaranteed immutable.
    //   the whole Edge class is immutable, so all fields are private final
}
```

关于其他接口的实现，直接照着模拟就行。值得注意的是，使用函数式编程范式、充分利用 `lambda` 表达式和 `API` ，可以大大简化代码量，以 `public Map<L, Integer> sources(L target)` 为例：

```java
@Override
public Map<L, Integer> sources(L target) {
    return edges.stream()
        .filter(edge -> edge.getTarget().equals(target))
        .collect(Collectors.toMap(Edge::getSource, Edge::getWeight));
}
```

另外 `public int set(L source, L target, int weight)` 实现时可以分 “`weight == 0`” 和 “`weight != 0`” 两种情况，等于 `0` 时，找出相关边从边表中删除即可；否则从边表中找出相关边然后更新权值。 

##### 3.1.3.2 Implement ConcreteVerticesGraph

这是使用点来存边，传统上需要使用链式前向星等数据结构存图，但是Java提供了非常好的标准数据结构，使得我们可以单独创建一种 `Vertex` 类来实现。`ConcreteVerticesGraph` 和 `Vertex` 的 `RI, AF` 等如图所示：

```java
public class ConcreteVerticesGraph<L> implements Graph<L> {

    private final List<Vertex<L>> vertices = new ArrayList<>();

    // Abstraction function:
    //   AF(vertices) = a graph with all vertices in the field vertices and edges such that the target of each edge is
    //   the key of a element in the field edges of each Vertex, and the weight of the edge is the value of the element
    // Representation invariant:
    //   Each Vertex in the list vertices has a unique label
    // Safety from rep exposure:
    //   vertices is mutable, so it makes a defensive copy as a return value in the method vertices()
}

class Vertex<L> {

    // TODO fields
    private final L label;
    private final Map<L, Integer> edges;

    // Abstraction function:
    //   AF(label, edges) = a vertex with the label, and is the source of some edges such that for each edge whose
    //   weight is w and target is v, satisfying edges.put(v) equals w.
    // Representation invariant:
    //   The weight of each edge is greater than zero
    // Safety from rep exposure:
    //   L is immutable.
    //   Each method that returns a Map, we construct a new one making it satisfying the post condition, then return.
}
```

此处以 `public boolean remove(L vertex);` 为例解说。删除一个点需要将点从点集中删除，然后遍历点集中所有点，删除每个点中关于目标点的关联（如果有的话）：

```java
@Override
public boolean remove(L vertex) {
    if (vertices.stream().noneMatch(currentVertex -> currentVertex.getLabel().equals(vertex)))
        return false;
    vertices.removeIf(currentVertex -> currentVertex.getLabel().equals(vertex));
    for (Vertex<L> currentVertex : vertices) {
        currentVertex.removeEdge(vertex);
    }
    checkRep();
    return true;
}
```

#### 3.1.4 Problem 3: Implement generic Graph\<L\>

本节要求将 `Graph` 接口转换问真正的泛型数据结构。

##### 3.1.4.1 Make the implementations generic

这个过程实际上非常简单，只需要将 `Concrete­EdgesGraph`, `Concrete­VerticesGraph`, `Edge`, and `Vertex` 中的`<String>` 替换成 `<L>` ，然后将所有关系到泛型参数的 `String` 也修改为 `L` 即可。

##### 3.1.4.2 Implement Graph.empty()

为了隐藏 `Graph` 的两个内部实现（生产环境中可能不需要这样做），`Graph` 接口的静态 `empty` 方法可以返回 `Graph` 的任意实现类的一个空实例，这里我们选择 `ConcreteEdgesGraph`：

```java
    /**
     * Create an empty graph.
     *
     * @param <L> type of vertex labels in the graph, must be immutable
     * @return a new empty weighted directed graph
     */
    public static <L> Graph<L> empty() {
        return new ConcreteEdgesGraph<>();
    }
```

#### 3.1.5 Problem 4: Poetic walks

本部分要求使用写好的 `Graph` 接口，利用输入的诗句构造图，并输入句子，根据构造的图来扩句。

如果两个单次之间存在长度为 `2` 的路径，中继节点被称为`桥`，我们需要为输入的句子增加尽可能多的桥。

##### 3.1.5.1 Test GraphPoet

本题需要先为 `GraphPoet` 编写测试用例：

```java
GraphPoet graphPoet = new GraphPoet(new File("src/P1/poet/example.txt"));
assertEquals("Seek to explore strange new life and exciting synergies!",
             graphPoet.poem("Seek to explore new and exciting synergies!"));

graphPoet = new GraphPoet(new File("src/P1/poet/mugar-omni-theater.txt"));
assertEquals("Test of the system.",
             graphPoet.poem("Test the system."));
```

测试文件如图所示：

```txt
# example.txt
To explore strange new worlds
To seek out new life and new civilizations

# mugar-omni-theater.txt
This is a test of the Mugar Omni Theater sound system.

```

根据规则，调用 `graphPoet.poem` 应该分别输出 `Seek to explore new and exciting synergies!` 和 `Test the system.`

##### 3.1.5.2 Implement GraphPoet

此处需要实现 `GraphPoet` 的功能。

在 `GraphPoet` 的构造函数中，需要先切分字符串，然后在相邻单词之间添加一条边。

接着生成诗句时，通过检查相邻单词中，前一个单词的 `target` 的后一个单词的 `source` 集合的交集是否为空，来判断是否可以插入单词来扩写句子。

```java
    /**
     * Create a new poet with the graph from corpus (as described above).
     *
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        AtomicReference<String> corpusStr = new AtomicReference<>("");
        try (Stream<String> stream = Files.lines(Paths.get(corpus.getAbsolutePath()), StandardCharsets.UTF_8)) {
            List<String> tempStrList = new ArrayList<>();
            stream.forEach(tempStrList::add);
            assert tempStrList.size() >= 1;
            tempStrList.forEach(str -> {
                corpusStr.set(corpusStr + " " + str.toLowerCase());
            });
        } catch (IOException | AssertionError e) {
            if (e instanceof IOException)
                System.out.println("Read file error! Make sure you are running the program at the right working directory.");
            else System.out.println("Read file error! Make sure your file contain has at least one line.");
            return;
        }
        corpusStr.set(corpusStr.get().trim().replaceAll(" +", " "));
        List<String> corpusWordsStr = List.of(corpusStr.get().split(" "));
        if (corpusWordsStr.size() == 0) return;
        if (corpusWordsStr.size() == 1) {
            graph.add(corpusWordsStr.get(0));
            return;
        }
        for (int i = 1; i < corpusWordsStr.size(); i++) {
            Integer weight = graph.targets(corpusWordsStr.get(i)).get(corpusWordsStr.get(i - 1));
            graph.set(corpusWordsStr.get(i - 1), corpusWordsStr.get(i), weight == null ? 1 : weight + 1);
        }
        checkRep();

    }

    /**
     * Generate a poem.
     *
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        List<String> srcStrings = List.of(input.trim().replaceAll(" +", " ").split(" "));
        List<String> dstStrings = new ArrayList<>();
        if (srcStrings.size() <= 1) return input;
        dstStrings.add(srcStrings.get(0));
        for (int i = 1; i < srcStrings.size(); i++) {
            Map<String, Integer> targets = graph.targets(srcStrings.get(i - 1).toLowerCase());
            Map<String, Integer> sources = graph.sources(srcStrings.get(i).toLowerCase());
            AtomicReference<String> bridge = new AtomicReference<>();
            targets.entrySet().forEach(entry -> {
                String target = entry.getKey().toLowerCase();
                if (sources.get(target) != null) bridge.set(target);
            });
            if (bridge.get() != null) dstStrings.add(bridge.get());
            dstStrings.add(srcStrings.get(i));
        }
        return String.join(" ", dstStrings);
    }
```

核心代码编写难度不大。

##### 3.1.5.3 Graph poetry slam

本题样例中的诗句是《星际迷航》系列的名句，完整原文是：

```txt
*Where no man has gone before*
Space: the final frontier.
These are the voyages of the starship Enterprise.
Its continuing mission: to explore strange new worlds.
To seek out new life and new civilizations.
To boldly go where no one has gone before!
```

将内容作为构造图的文本，输入 `Seek to explore new and exciting synergies!`，输出自然仍然是 `Seek to explore strange new life and exciting synergies!` 。

#### 3.1.6 使用Eclemma检查测试的代码覆盖度



### 3.2 Re-implement the Social Network in Lab1

#### 3.2.1 FriendshipGraph类

#### 3.2.2 Person 类

#### 3.2.3 客户端 main()

#### 3.2.4 测试用例

#### 3.2.5 提交至 Git 仓库

## 4 实验进度

![](https://s1.ax1x.com/2022/05/29/XMZon0.png)

|     日期     |    时间段     |                     任务                     | 实际完成情况 |
| :----------: | :-----------: | :------------------------------------------: | :----------: |
| `2022-05-10` | `16:30-17:30` |                配置Gradle项目                |     完成     |
| `2022-05-12` | `10:00-11:30` |        阅读实验需求；<br>编写单元测试        |     完成     |
| `2022-05-14` | `18:30-21:15` |            完成`Graph`的两个实现             |     完成     |
| `2022-05-15` | `09:45-10:15` | 将 `Edge` 和 `Vertex` 改为 `immutable class` |     完成     |
| `2022-05-19` | `10:40-11:20` |            泛型化 `Graph` 的实现             |     完成     |
| `2022-05-26` | `10:00-11:00` | 补充 `Edge` 和 `Vertex` 的测试和 `checkRep`  |     完成     |

## 5 实验过程中遇到的困难与解决途径

| 遇到难点 | 解决途径 |
|:----:|:----:|
|      |      |
|      |      |
|      |      |

## 6 实验过程中收获的经验、教训、感想

6.1 实验过程中收获的经验和教训（必答）

6.2 针对以下方面的感受（必答）

+ 面向ADT的编程和直接面向应用场景编程，你体会到二者有何差异？

+ 使用泛型和不使用泛型的编程，对你来说有何差异？

+ 在给出ADT的规约后就开始编写测试用例，优势是什么？你是否能够适应这种测试方式？

+ P1设计的ADT在多个应用场景下使用，这种复用带来什么好处？

+ 为ADT撰写specification, invariants, RI, AF，时刻注意ADT是否有rep exposure，这些工作的意义是什么？你是否愿意在以后编程中坚持这么做？

+ 关于本实验的工作量、难度、deadline。

+ 《软件构造》课程进展到目前，你对该课程有何收获和建议？
