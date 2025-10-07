import com.mongodb.client.*;

import org.bson.Document;

import javax.print.Doc;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;

public class MainLoop {
    public static void main(String[] args) {
        int answer = 0;
        String uri = "mongodb://localhost:27017/";

        try(Scanner scanner = new Scanner(System.in); MongoClient mongoClient = MongoClients.create(uri)) {
            while (answer != 3) {
                //Selecting database
                MongoDatabase database = mongoClient.getDatabase("expenseTrackerDb");
                //Selecting collection
                MongoCollection<Document> collection = database.getCollection("expenses");

                //Printing the options that the user will be able to select
                System.out.println("Please select an option:");
                System.out.println("1. See all expenses");
                System.out.println("2. Add a new expense");
                System.out.println("3. Exit");

                answer = scanner.nextInt();

                switch (answer) {
                    case 1: {
                        FindIterable<Document> docs = collection.find();
                        if (docs != null) {
                            for (Document doc : docs) {
                                System.out.println(doc);
                            }
                        } else {
                            System.out.println("No matching documents found");
                        }
                        break;
                    }
                    case 2: {
                        System.out.println("Please add the name of the expense:");
                        break;
                    }
                    case 3: break;

                    default: System.out.println("Please select a valid option...");
                }
            }
        } catch (InputMismatchException err) {
            System.out.println(err);
        }
    }
}
