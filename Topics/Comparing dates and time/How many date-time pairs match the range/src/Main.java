import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDateTime firstBorder = LocalDateTime.parse(scanner.nextLine());
        LocalDateTime secondBorder = LocalDateTime.parse(scanner.nextLine());
        int numberOfPairs = Integer.parseInt(scanner.nextLine());
        int matchCounter = 0;

        for (int i = 0; i < numberOfPairs; i++) {
            if (firstBorder.isBefore(secondBorder)) {
                LocalDateTime dateTimePair = LocalDateTime.parse(scanner.nextLine());
                if ((dateTimePair.isAfter(firstBorder) && dateTimePair.isBefore(secondBorder))
                        || dateTimePair.isEqual(firstBorder)) {
                    matchCounter++;
                }
            } else if (firstBorder.isAfter(secondBorder)) {
                LocalDateTime dateTimePair = LocalDateTime.parse(scanner.nextLine());
                if ((dateTimePair.isAfter(secondBorder) && dateTimePair.isBefore(firstBorder))
                        || dateTimePair.isEqual(secondBorder)) {
                    matchCounter++;
                }
            } else {
                scanner.nextLine();
            }
        }

        System.out.println(matchCounter);
    }
}