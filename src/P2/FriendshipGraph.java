import graph.*;

import java.util.*;

public class FriendshipGraph {
    private final Graph<Person> graph;
    // Abstraction function:
    //   AF(graph) = a friendship graph such that evey vertex is labeled by a Person, and there is an edge between
    //   person1 and person2 iff there is a connection from person1 to person2
    // Representation invariant:
    //   Each person has a unique name
    // Safety from rep exposure:
    //   the field graph is final and no method will mutate it.


    FriendshipGraph() {
        graph = new ConcreteVerticesGraph<>();
    }

    /**
     * add person as a vertex
     *
     * @param person the person which will be added as a vertex
     * @throws RuntimeException if the person's name is the same as another one that has existed，
     *                          such exception will be thrown
     */
    public void addVertex(Person person) throws RuntimeException {
        graph.add(person);
    }

    /**
     * add an edge from u to v
     *
     * @param u the source of the edge
     * @param v the target of the edge
     */
    public void addEdge(Person u, Person v) {
        graph.set(u, v, 1);
    }

    /**
     * get length of the path from u to v, each edge has the weight of 1
     *
     * @param u the source of the path
     * @param v the target of the path
     * @return return the length is u and v are connected,
     * return -1 if u and v are not connected or one of which is null
     */
    public int getDistance(Person u, Person v) {
        if (u == null || v == null) return -1;

        Queue<PersonQueueNode> personQueue = new LinkedList<>();
        Set<Person> personSet = new HashSet<>();
        personQueue.offer(new PersonQueueNode(u, 0));
        while (!personQueue.isEmpty()) {
            PersonQueueNode currentPersonNode = personQueue.poll();
            Person currentPerson = currentPersonNode.person();
            personSet.add(currentPerson);
            if (currentPerson.equals(v)) {
                return currentPersonNode.step();
            }
            Map<Person, Integer> friendsMap = graph.targets(currentPerson);
            friendsMap.forEach((friend, value) -> {
                if (!personSet.contains((friend)))
                    personQueue.offer(new PersonQueueNode(friend, currentPersonNode.step() + 1));
            });
        }

        return -1;
    }


    /**
     * a utility class to help storage the node information in the midst of BFS
     */
    private static class PersonQueueNode {
        private final Person person;
        private final int step;

        PersonQueueNode(Person person, int step) {
            this.person = person;
            this.step = step;
        }

        public Person person() {
            return person;
        }

        public int step() {
            return step;
        }
    }

    public static void main(String[] args) {
        FriendshipGraph graph = new FriendshipGraph();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to this interactive prompt, type in 'help' to see what you can do.");
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
                    System.out.println(e.getMessage());
                    break;
                }
            } else if (commandArgs.get(0).equals("addEdge")) {
                if (commandArgs.size() < 3) continue;
                Person u = new Person(commandArgs.get(1));
                Person v = new Person(commandArgs.get(2));
                graph.addEdge(u, v);
            } else if (commandArgs.get(0).equals("getDistance")) {
                if (commandArgs.size() < 3) continue;
                Person u = new Person(commandArgs.get(1));
                Person v = new Person(commandArgs.get(2));
                System.out.println(graph.getDistance(u, v));
            } else if (commandArgs.get(0).equals("help")) {
                System.out.println("Command List:");
                System.out.println("\taddVertex <Person Name> -- add a vertex with a name");
                System.out.println("\taddEdge <Person 1 Name> <Person 2 Name> -- add an edge from u to v");
                System.out.println("\tgetDistance <Person 1 Name> <Person 2 Name> -- get the distance from u to v");
                System.out.println("\tquit -- leave the interactive prompt");

            } else if (commandArgs.get(0).equals("quit")) break;
            else System.out.println("Command not found, type in 'help' to see what commands are available.");
        }
        System.out.println("Interactive prompt terminated.");
    }
}
