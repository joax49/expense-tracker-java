import java.util.InputMismatchException;
import java.util.Scanner;

public class MainLoop {
    public static void Main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int answer = 0;

        try {
            while (answer != 3) {

                System.out.println("Please select an option:");
                System.out.println("1. See all expenses");
                System.out.println("2. Add a new expense");
                System.out.println("3. Exit");

                answer = scanner.nextInt();

                switch (answer) {
                    case 1: System.out.println("Expenses");
                    case 2: System.out.println("Please add the name of the expense:");
                    case 3: break;

                    default: System.out.println("Please select a valid option...");
                }
            }
        } catch (InputMismatchException err) {
            System.out.println(err);
        }
    }
}
