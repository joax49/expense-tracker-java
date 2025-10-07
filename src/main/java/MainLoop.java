import com.mongodb.client.*;

import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;

public class MainLoop {
    public static void main(String[] args) {
        int answer1 = 0;
        String answer2 = "";
        String uri = "mongodb://localhost:27017/";

        try(Scanner scanner = new Scanner(System.in); MongoClient mongoClient = MongoClients.create(uri)) {
            while (answer1 != 3) {
                //Selecting database
                MongoDatabase database = mongoClient.getDatabase("expenseTrackerDb");
                //Selecting collection
                MongoCollection<Document> collection = database.getCollection("expenses");

                //Printing the options that the user will be able to select
                System.out.println("Please select an option:");
                System.out.println("1. See all expenses");
                System.out.println("2. Add a new expense");
                System.out.println("3. Exit");

                answer1 = scanner.nextInt();
                scanner.nextLine();

                switch (answer1) {
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
                        ArrayList<Document> products = new ArrayList<Document>();
                        int wholePrice = 0;

                        System.out.println("Please insert the name of the expense:");
                        String expenseName = scanner.nextLine();

                        System.out.println("Insert the name of a product:");
                        String productName = scanner.nextLine();
                        System.out.println("Insert the price of the product:");
                        int productPrice = scanner.nextInt();
                        System.out.println("Insert the amount of products:");
                        int productAmount = scanner.nextInt();
                        scanner.nextLine();

                        wholePrice += productPrice * productAmount;
                        Document product0 = new Document("product_name", productName)
                                .append("product_price", productPrice)
                                .append("product_amount", productAmount);
                        products.add(product0);

                        while (!answer2.equals("n")) {

                            System.out.println("Do you want to add another product? (y/n)");
                            answer2 = scanner.nextLine();

                            switch (answer2) {
                                case "y": {
                                    System.out.println("Insert the name of a product:");
                                    productName = scanner.nextLine();
                                    System.out.println("Insert the price of the product:");
                                    productPrice = scanner.nextInt();
                                    System.out.println("Insert the amount of products:");
                                    productAmount = scanner.nextInt();
                                    scanner.nextLine();

                                    wholePrice += productPrice * productAmount;
                                    Document product = new Document("product_name", productName)
                                            .append("product_price", productPrice)
                                            .append("product_amount", productAmount);
                                    products.add(product);
                                }
                                case "n": break;
                                default: System.out.println("The answer must be 'y' or 'n'");
                            }

                        }

                        Document doc = new Document("expense_name", expenseName)
                                .append("full_price", wholePrice)
                                .append("products", products)
                                .append("date", LocalDate.now());

                        InsertOneResult result = collection.insertOne(doc);
                        System.out.println("Expense added");

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
