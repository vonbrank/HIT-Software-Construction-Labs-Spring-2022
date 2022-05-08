import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private final List<Person> friendsList;

    Person(String name) {
        this.name = name;
        friendsList = new ArrayList<>();
    }

    public void addFriend(Person friend) {
        friendsList.add(friend);
    }

    public Person getFriend(int index) {
        return friendsList.get(index);
    }

    public int getNumberOfFriends() {
        return friendsList.size();
    }

    public String getName() {
        return name;
    }
}
