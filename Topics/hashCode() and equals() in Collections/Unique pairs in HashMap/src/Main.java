import java.util.Scanner;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<Person, Integer> map = new HashMap();
        map.put(new Person(scanner.nextLine()), 1995);
        map.put(new Person(scanner.nextLine()), 1995);

        System.out.println(map.size());
    }
}

class Person {
    
    private String name;

    public Person() {

    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof Person)) return false;

        Person person = (Person) obj;
        return Objects.equals(name, ((Person) obj).getName());
    }
}