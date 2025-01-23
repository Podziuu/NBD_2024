import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Repository extends AbstractMongoEntity {
    private final MongoCollection<Document> collection;

    public Repository() {
        initDbConnection();
        this.collection = database.getCollection("mediastore");
    }

    public void save(String message) {
        Document doc = new Document().append("rents", message);
        collection.insertOne(doc);
        System.out.println("saved " + message);
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }
}
