import java.util.ArrayList;
import java.util.List;

public class Person {
    private final String name;
    private final List<Person> friendsList;

    /**
     * initialize the instance with a name
     * @param name the name of the people
     */
    Person(String name) {
        this.name = name;
        friendsList = new ArrayList<>();
    }

    /**
     * add a friend to the person
     * @param friend the person to be added as the person's friend
     */
    public void addFriend(Person friend) {
        friendsList.add(friend);
    }

    /**
     * get one of the person's friend through an index
     * @param index the param index to find a person
     * @return the person instance
     */
    public Person getFriend(int index) {
        return friendsList.get(index);
    }

    /**
     *
     * @return the number of the person's friends
     */
    public int getNumberOfFriends() {
        return friendsList.size();
    }

    /**
     *
     * @return get the name of the person
     */
    public String getName() {
        return name;
    }
}
