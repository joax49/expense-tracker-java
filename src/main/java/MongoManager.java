import com.mongodb.client.MongoClient;

public class MongoManager {

    MongoClient mongoClient;

    MongoManager(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

}
