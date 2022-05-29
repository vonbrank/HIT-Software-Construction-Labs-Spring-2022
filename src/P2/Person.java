import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Person {
    private final String name;

    // Abstraction function:
    //   AF(name) = a person with the name
    // Representation invariant:
    //   Each person has a unique name
    // Safety from rep exposure:
    //   String name is immutable.

    /**
     * initialize the instance with a name
     * @param name the name of the people
     */
    Person(String name) {
        this.name = name;
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
