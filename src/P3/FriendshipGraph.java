import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class FriendshipGraph {

    private final List<Person> peopleList;

    FriendshipGraph() {
        peopleList = new ArrayList<>();
    }

    /**
     * add person as a vertex
     * @param person the person which will be added as a vertex
     * @throws RuntimeException if the person's name is the same as another one that has existed，
     * such exception will be thrown
     */
    public void addVertex(Person person) throws RuntimeException {
        if (person == null) return;
        peopleList.forEach(p -> {
            if (person.getName().equals(p.getName()))
                throw new RuntimeException("Each person has a unique name!");
        });
        peopleList.add(person);
    }

    /**
     * add an edge from u to v
     * @param u the source of the edge
     * @param v the target of the edge
     */
    public void addEdge(Person u, Person v) {
        if (u == null || v == null) return;
        u.addFriend(v);
    }

    /**
     * get length of the path from u to v, each edge has the weight of 1
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
            for (int i = 0; i < currentPerson.getNumberOfFriends(); i++) {
                if (personSet.contains(currentPerson.getFriend(i))) continue;
                personQueue.offer(new PersonQueueNode(currentPerson.getFriend(i), currentPersonNode.step() + 1));
            }
        }

        return -1;
    }

    /**
     * get an instance from the people list through a person name
     * @param personName the name to find the person
     * @return return a Person instance or null if no one matches
     */
    public Person getPerson(String personName) {
        Person res = null;
        for (Person person : peopleList) {
            if (person.getName().equals(personName)) res = person;
        }
        return res;
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
