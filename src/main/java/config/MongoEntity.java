package config;

import com.mongodb.client.MongoDatabase;

public class MongoEntity extends AbstractMongoEntity {
    public MongoEntity() {
        initDbConnection();
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }
}
