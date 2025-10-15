import com.mongodb.client.*;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Expense {
    String expenseName;
    int wholeExpense;
    Date date;
    ArrayList<Document> products = new ArrayList<Document>();

    //Expense constructor
    Expense(String expenseName, String productName, int productPrice, int productAmount) {
        this.expenseName = expenseName;
        this.wholeExpense += productPrice * productAmount;

        this.date = Date.from(LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

        this.products.add(new Document("expense_name", productName)
                .append("product_price", productPrice)
                .append("product_amount", productAmount));
    }

    //Method for adding new products into the expense
    public void AddProduct(String productName, int productPrice, int productAmount) {
        this.products.add(new Document("expense_name", productName)
                .append("product_price", productPrice)
                .append("product_amount", productAmount));
    }
}
