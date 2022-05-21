import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Person {
    private final String name;

    /**
     * initialize the instance with a name
     * @param name the name of the people
     */
    Person(String name) {
        this.name = name;
    }


    /**
     *
     * @return get the name of the person
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
