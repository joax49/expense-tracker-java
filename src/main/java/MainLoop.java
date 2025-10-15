import com.mongodb.client.*;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Filter;

public class MainLoop {
    public static void main(String[] args) {
        int answer1 = 0;
        String answer2 = "";
        String uri = "mongodb://localhost:27017/";

        //Main loop
        try(Scanner scanner = new Scanner(System.in); MongoClient mongoClient = MongoClients.create(uri)) {
            while (answer1 != 4) {
                //Selecting database
                MongoDatabase database = mongoClient.getDatabase("expenseTrackerDb");
                //Selecting collection
                MongoCollection<Document> collection = database.getCollection("expenses");

                //Printing the options that the user will be able to select
                System.out.println("Please select an option:");
                System.out.println("1. See all expenses");
                System.out.println("2. Add a new expense");
                System.out.println("3. See expenses by date");
                System.out.println("4. Exit");

                answer1 = scanner.nextInt();
                scanner.nextLine();

                switch (answer1) {
                    //Printing all the expenses
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

                    //Adding a new expense
                    case 2: {
                        System.out.println("Please insert the name of the expense:");
                        String expenseName = scanner.nextLine();

                        //Storing the variables for the first product
                        System.out.println("Insert the name of a product:");
                        String productName = scanner.nextLine();
                        System.out.println("Insert the price of the product:");
                        int productPrice = scanner.nextInt();
                        System.out.println("Insert the amount of products:");
                        int productAmount = scanner.nextInt();
                        scanner.nextLine();

                        //Constructing the expense
                        Expense expense = new Expense(expenseName,
                                productName, productPrice, productAmount);

                        //A loop that allows the user to add as many products as they want
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

                                    expense.AddProduct(productName, productPrice, productAmount);
                                }
                                case "n": break;
                                default: System.out.println("The answer must be 'y' or 'n'");
                            }

                        }

                        //Constructing the product that will be inserted to the DB
                        Document doc = new Document("expense_name", expense.expenseName)
                                .append("full_price", expense.wholeExpense)
                                .append("products", expense.products)
                                .append("date", expense.date);

                        //Inserting the expense into the DB
                        InsertOneResult result = collection.insertOne(doc);
                        System.out.println("Expense added");

                        break;
                    }

                    //Printing expenses by date
                    case 3: {
                        System.out.println("From what date do you want to see?");
                        System.out.println("Insert year:");
                        int fromYear = scanner.nextInt();

                        System.out.println("Insert month:");
                        int fromMonth = scanner.nextInt();

                        System.out.println("Insert day:");
                        int fromDay = scanner.nextInt();

                        System.out.println("To what date do you want to see?");
                        System.out.println("Insert year:");
                        int toYear = scanner.nextInt();

                        System.out.println("Insert month:");
                        int toMonth = scanner.nextInt();

                        System.out.println("Insert day:");
                        int toDay = scanner.nextInt();

                        LocalDate startDate = LocalDate.of(fromYear, fromMonth, fromDay);
                        LocalDate endDate = LocalDate.of(toYear, toMonth, toDay);

                        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                        Bson filter = Filters.and(
                                Filters.gte("date", start),
                                Filters.lt("date", end)
                        );

                        FindIterable<Document> results = collection.find(filter);

                        for (Document doc : results) {
                            System.out.println(doc.toJson());
                        }
                    }

                    case 4: break;

                    default: System.out.println("Please select a valid option...");
                }
            }
        } catch (InputMismatchException err) {
            System.out.println(err);
        }
    }
}
