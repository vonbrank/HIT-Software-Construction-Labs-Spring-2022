import java.util.*;

public class FriendshipGraph {

    private final List<Person> peopleList;

    FriendshipGraph() {
        peopleList = new ArrayList<>();
    }

    public void addVertex(Person person) {
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
        System.out.println(graph.getDistance(rachel, ross));
        //should print 1
        System.out.println(graph.getDistance(rachel, ben));
        //should print 2
        System.out.println(graph.getDistance(rachel, rachel));
        //should print 0
        System.out.println(graph.getDistance(rachel, kramer));
        //should print -1
    }

}
