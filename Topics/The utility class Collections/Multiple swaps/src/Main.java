import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> numbers = new java.util.ArrayList<>(Arrays.stream(scanner.nextLine().split(" "))
                .mapToInt(Integer::parseInt).boxed().toList());
        int numberOfSwaps = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numberOfSwaps; i++) {
            int[] indexesForSwap = Arrays.stream(scanner.nextLine().split(" "))
                    .mapToInt(Integer::parseInt).toArray();
            Collections.swap(numbers, indexesForSwap[0], indexesForSwap[1]);
        }

        System.out.println(numbers.toString()
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .replaceAll(",", ""));
    }
}