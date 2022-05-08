
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

##### 算法解释

当 `N` 为奇数时，我们可以通过下方法构建一个幻方：

首先将 `1` 写在第一行的中间。

之后，按如下方式从小到大依次填写每个数 `K (K = 2, 3, ... N x N)`：

1. 若 `K-1` 在第一行但不在最后一列，则将填 `K` 在最后一行，  `K-1` 所在列的右一列；
2. 若 `K-1` 在最后一列但不在第一行，则将 `K` 填在第一列， `K-1` 所在行的上一行；
3. 若 `K-1` 在第一行最后一列，则将填 `K` 在 `K-1` 的正下方；
4. 若 `K-1` 既不在第一行，也不在最后一列，如果 `K-1` 的右上方还未填数，则将 `K` 填在 `K-1` 的右上方，否则将 `K` 填在 `K-1` 的正下方。

```java
public static boolean generateMagicSquare(int n) {

        if (n % 2 == 0) {
            System.out.println("It is illegal to pass an odd number to generate a magic square.");
            return false;
        }

        if (n < 0) {
            System.out.println("It is illegal to pass an negative number to generate a magic square.");
            return false;
        }

        int magic[][] = new int[n][n];
        int row = 0, col = n / 2, i, j, square = n * n;
        for (i = 1; i <= square; i++) { // 每次向方阵中填一个数，迭代 n * n 次
            magic[row][col] = i;
            if (i % n == 0) // 如果左上方已经填入一个数，即 i 可以被 n 整除，则从下一行开始填
                row++;
            else {  // 否则下一个数的位置是当前数的左上方
                if (row == 0)   // 若已经到方阵顶端，则下一个数在方阵底端，否则在当前行的上一行
                    row = n - 1;
                else
                    row--;
                if (col == (n - 1)) // 若已经到达方阵右端，则下一个数在方阵最左端，否则在当前列的右一列
                    col = 0;
                else
                    col++;
            }
        }
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++)
                System.out.print(magic[i][j] + "\t");
            System.out.println();
        }

        try (PrintWriter out = new PrintWriter(
            getFullPathString(relativePathStringToTxt, "6.txt"))) {
            for (i = 0; i < n; i++) {
                for (j = 0; j < n; j++)
                    out.print(magic[i][j] + "\t");
                out.println();
            }
        } catch (FileNotFoundException e) {
            System.out.printf("Can not find file %s\n", 
                              getFullPathString(relativePathStringToTxt, "6.txt"));
        }

        return true;
    }
```



##### 【回答问题】异常解释

+ 当 $n$ 为偶数时，第一个数将不会填在第一行的中央，且将会出现 “当前在最后一行时，因为 `i % n == 0` ，使得转跳到下一行，导致数组越界，抛出异常。 
+ 当 $n$ 为负数时，第一个数的行索将是负数，从而导致数组越界，抛出异常。 

### 3.2 Turtle Graphics

此任务要求按照 MIT 官网要求，依据 `TurtleSoup` 中的接口规约，实现所有未实现的接口，并运行海龟绘图程序。

#### 3.2.1 Problem 1: Clone and import

+ 因为我们只需要代码框架而不需要其 `commit` 记录，因此只需要到[这里](https://github.com/rainywang/Spring2022_HITCS_SC_Lab1)，依次点击 `Code` 、`Download ZIP` 即可。
+ 在连接 Github Classroom 并创建属于的自己的仓库后，可以先创建一个 `README` 然后 `clone` ，也可以在本地 `git init` 然后 `git remote add <git-repo-url>` 连接自己的远程仓库。
+ 之后日常执行 `git add` `git commit` 等指令实现版本控制。

#### 3.2.2 Problem 3: Turtle graphics and drawSquare

本题是海龟绘图的最简单实践，代码框架已经为我们实现了画布和海龟，以及海龟绘图的最基本接口：

+ `forward` ：向当前方向移动
+ `turn`：以当前朝向为基准，顺时针旋转，默认朝上

此处我们先实现绘制正方形，调用绘制多边形的函数，并将边数设置为 `4` 即可，相关依赖稍后实现。

#### 3.2.3 Problem 5: Drawing polygons

本题要求绘制多边形，只需要先计算多边形外角，依次 `forward(sideLength)` `turn(turnAngle)` ，迭代 `sides` 次即可。

对于正 `n` 边形，内角公式为 `return 180 - 360.0 / sides`

#### 3.2.4 Problem 6: Calculating Bearings

要计算多个点的旋转角之和，只需要计算每三个顶点的旋转角之和，然后累加即可。

给出三个点，取一个为中心，取中心到另外两点的向量，使用叉积即可得到夹角。

#### 3.2.5 Problem 7: Convex Hulls

规约推荐使用 `gift-wrapping` ，此处用的也是该算法，首先取最下最右的点作为 `first`，然后维护三元组 `first, second, third` ，遍历所有点，选出由夹角 `first, second, p` 最大的 `p` 作为 `third` ，依次迭代，直到重复出现最初的 `first` 。给的测试样例要求严格凸包，所以对于相同的夹角，选模长最大的点。

#### 3.2.6 Problem 8: Personal art

绘制多边形时给予一定偏差，且边长递增，可以绘制出好看的螺旋形：

```java
public static void drawHelix(Turtle turtle, int sides, double offset, int iterations, int step) {
    double angle = (180 - calculateRegularPolygonAngle(sides)) + offset;
    for (int i = 1; i <= iterations; i += step) {
        turtle.forward(i);
        turtle.turn(angle);
    }
}
```

`sides = 3` `offset = 2.2`  时效果如图：

![](https://s1.ax1x.com/2022/05/08/O1qXnS.png)

#### 3.2.7 Submitting

最简单的提交方法是依次执行以下命令：

```bash
git add .
git commit -m "complete assignment"
git push origin master
```

### 3.3 Social Network

面向对象编程 (OOP) 的最简单实践，要求实现 `FriendshipGraph` 和 `Person` 两个类，`Friendship` 依赖于 `Person` ，并提供添加顶点，建立联系，查询距离等功能。

**回答问题**：

+ 如果将上述（实验指导中的代码）代码的第 10 行注释掉（意即 rachel 和 ross 之间只存在单向的 社交关系 ross->rachel），请人工判断第 14-17 行的代码应输出什么结果？

  `14-17` 行全部输出 `-1` 因为 `Rachel` 向社交网络的连接被切断了。

#### 3.3.1 设计/实现 `FriendshipGraph` 类

实现以下接口：

```java
public class FriendshipGraph {

    private final List<Person> peopleList;

    FriendshipGraph();
    public void addVertex(Person person);
    public void addEdge(Person u, Person v);
    public int getDistance(Person u, Person v);
    public Person getPerson(String personName);

    private static class PersonQueueNode {
        
        private final Person person;
        private final int step;

        PersonQueueNode(Person person, int step);
        public Person person();
        public int step();

    }

}
```

`peopleList` 维护关系网中所有人的索引，`Person` 实例维护与之有关系的人的列表 `friendsList` 。

`addVertx` 会将 `Person` 实例添加到列表中，发现名字出错时要抛出异常；`addEdge(u, v)` 会将 `u` 添加到 `v` 的关系列表中；`getDistance(u, v)` 可以通过 `BFS` 求出 `u` 到 `v` 的距离。

#### 3.3.2 设计/实现 `Person` 类

```java
public class Person {
    
    private String name;
    private final List<Person> friendsList;

    Person(String name);
    public void addFriend(Person friend);
    public Person getFriend(int index);
    public int getNumberOfFriends();
    public String getName();
}
```

`Person` 维护两个字段，其名字和关系列表，并提供一系列方法进行操作。

#### 3.3.3 设计/实现客户端代码 `main()`

此处实现了一种交互式客户端：

```java
public static void main(String[] args) {
    FriendshipGraph graph = new FriendshipGraph();
    Scanner scanner = new Scanner(System.in);

    while (true) {
        System.out.print("> ");
        String command = scanner.nextLine();
        List<String> commandArgs = List.of(command.split(" "));
        if (commandArgs.size() < 1) continue;
        if (commandArgs.get(0).equals("addVertex")) {
            if (commandArgs.size() < 2) continue;
            Person person = new Person(commandArgs.get(1));
            try {
                graph.addVertex(person);
            } catch (RuntimeException e) {
                System.out.println(e);
                break;
            }
        } else if (commandArgs.get(0).equals("addEdge")) {
            if (commandArgs.size() < 3) continue;
            graph.addEdge(
                graph.getPerson(commandArgs.get(1)),
                graph.getPerson(commandArgs.get(2))
            );
        } else if (commandArgs.get(0).equals("getDistance")) {
            if (commandArgs.size() < 3) continue;
            System.out.println(graph.getDistance(
                graph.getPerson(commandArgs.get(1)),
                graph.getPerson(commandArgs.get(2))
            ));
        } else if (commandArgs.get(0).equals("help")) {
            System.out.println("Command List:");
            System.out.println("\taddVertex <Person Name>");
            System.out.println("\taddEdge <Person 1 Name> <Person 2 Name>");
            System.out.println("\tgetDistance <Person 1 Name> <Person 2 Name>");

        } else if (commandArgs.get(0).equals("quit")) break;
    }
}
```

支持以下命令：

```bash
Command List:
	addVertex <Person Name> -- add a vertex with a name
	addEdge <Person 1 Name> <Person 2 Name> -- add an edge from u to v
	getDistance <Person 1 Name> <Person 2 Name> -- get the distance from u to v
	quit -- leave the interactive prompt
```



#### 3.3.4 设计/实现测试用例

交互式环境测试用例：

```bash
addVertex Rachel
addVertex Ross
addVertex Ben
addVertex Kramer
addEdge Rachel Ross
addEdge Ross Rachel
addEdge Ross Ben
addEdge Ben Ross
getDistance Rachel Ross
getDistance Rachel Ben
getDistance Rachel Rachel
getDistance Rachel Kramer
```



基于实验指导用例的单元测试：

```java
public class FriendshipGraphTest {
    @Test
    public void friendshipGraphFeatureTest() {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");
        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);
        assertEquals(1, graph.getDistance(rachel, ross));
        assertEquals(2, graph.getDistance(rachel, ben));
        assertEquals(0, graph.getDistance(rachel, rachel));
        assertEquals(-1, graph.getDistance(rachel, kramer));
    }
}
```



## 4 实验进度记录

![](https://s1.ax1x.com/2022/05/08/O3Jqns.png)

|     日期     |    时间段     |        任务        |           实际完成情况           |
| :----------: | :-----------: | :----------------: | :------------------------------: |
| `2022-04-28` | `10:00-11:30` | 研究 `Gradle` 配置 |             按时完成             |
| `2022-04-29` | `21:30-23:30` |        `P1`        |             按时完成             |
| `2022-04-30` | `12:30-13:45` | `P2` 除凸包外部分  |             按时完成             |
| `2022-04-30` | `15:30-19:00` |   `P2` 凸包部分    | 耗时过长，<br>凸包写炸了调不出来 |
| `2022-04-30` | `21:30-22:30` |        `P3`        |             顺利完成             |



## 5 实验过程中遇到的困难与解决途径

|              遇到的困难               |                        解决方案                         |
| :-----------------------------------: | :-----------------------------------------------------: |
| 如何将`P1` `P2` `P3` 组织成独立的组件 |         使用 IDEA 的 Module 或 Gradle 配置项目          |
|         严格凸包算法边界问题          | 每次选取极角最大，模最大的点，用 `ε` 判断 `double` 相等 |

## 6 实验过程中收获的经验、教训、感想

### 6.1 实验过程中收获的经验和教训（必答）

本次实验是对软件开发过程中，版本控制、面向对象编程、单元测试等问题的实践，有助于个人了解单元测试框架、持续集成工具、Gradle构建系统等工具的使用，也体验到帮助他人的乐趣。

### 6.2 针对以下方面的感受（必答）

+ Java编程语言是否对你的口味？与你熟悉的其他编程语言相比，Java有何优势和不足？

  对本人而言，`Java` 编程体验较为舒适；与 `C/C++` 相比，开发者不需要担心指针可能造成的内存泄露，标准库也提供了大量数据结构共开发者使用，降低开发门槛，但是也面临性能劣势；与 `Kotlin` 相比，`Java` 比较繁琐，在 `lambda` 表达式、面向对象编程语法上不如 `Kotlin` 写得爽。

+ 关于Eclipse或IntelliJ IDEA，它们作为IDE的优势和不足：

  IDEA 作为现代化的智能 IDE ，自动配置好完备的工具链供开发者使用，同时在图形化、代码高亮、依赖管理、代码提示与修改建议方面功能强大，给开发者提供了很多便利；但是对初学者而言，这可能掩盖部分编程语言的本质。

+ 关于 Git 和 GitHub ，是否感受到了它在版本控制方面的价值：

  Git 是强大的版本控制工具，通过只记录内容的修改等特性实现了高速、分布式的版本控制。Git 配合 Github 社区生态，在个人/企业级项目的版本控制中有重要作用。

+ 关于CMU和MIT的作业，你有何感受：

  CMU 与 MIT 模拟了部分生产环境与实际的编程需求，同时题目设计巧妙，有助于理解软件开发过程中部分概念的意义与作用。

+ 关于本实验的工作量、难度、deadline：

  工作量与难度适中，但是 deadline 过紧，在可能给刚了解 `Java`  `Git` 等技术栈的初学者非常大的压力。

+ 关于初接触 `软件构造` 课程：

  个人感觉这门课是对软件开发过程中的一些流程的概述，对已经有一些软件开发经验的同学比较有意义，这部分同学也更容易理解课程内容；但是如果实现不了解 `Java` 、  `Git` 、 面向对象编程等方面的知识，对课程内容的理解可能非常吃力。 
