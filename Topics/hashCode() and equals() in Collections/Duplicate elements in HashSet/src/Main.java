import java.util.Scanner;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Set<Person> set = new HashSet();
        set.add(new Person(scanner.nextLine()));
        set.add(new Person(scanner.nextLine()));

        System.out.println(set.size());
    }
}

class Person {

    private String name;

    public Person() {}

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}