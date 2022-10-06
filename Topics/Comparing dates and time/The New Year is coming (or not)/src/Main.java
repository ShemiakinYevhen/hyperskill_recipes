import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDate date = LocalDate.parse(scanner.next());
        int daysToAdd = scanner.nextInt();

        boolean willNewYearCome = Integer.compare(date.getYear() + 1, date.plusDays(daysToAdd).getYear()) == 0;

        System.out.println(willNewYearCome);
    }
}