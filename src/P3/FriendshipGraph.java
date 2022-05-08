import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class FriendshipGraph {

    private final List<Person> peopleList;

    FriendshipGraph() {
        peopleList = new ArrayList<>();
    }

    public void addVertex(Person person) throws RuntimeException {
        peopleList.forEach(p -> {
            if (person.getName().equals(p.getName()))
                throw new RuntimeException("Each person has a unique name!");
        });
        peopleList.add(person);
    }

    public void addEdge(Person u, Person v) {
        u.addFriend(v);
    }

    public int getDistance(Person u, Person v) {

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

    public Person getPerson(String personName) {
        Person res = null;
        for (Person person : peopleList) {
            if (person.getName().equals(personName)) res = person;
        }
        return res;
    }

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

        /*
        * > addVertex Rachel
        > addVertex Ross
        > addEdge Rachel Ross
        > getDistance Rachel Ross
        * */

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
/*
        Person rachel = new Person("Rachel");
//        Person ross = new Person("Ross");
        Person ross = new Person("Rachel");
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
        System.out.println(graph.getDistance(rachel, ross));
        //should print 1
        System.out.println(graph.getDistance(rachel, ben));
        //should print 2
        System.out.println(graph.getDistance(rachel, rachel));
        //should print 0
        System.out.println(graph.getDistance(rachel, kramer));
        //should print -1
        */
    }

}
