package config;

public class MongoEntity extends AbstractMongoEntity {
    public MongoEntity() {
        initDbConnection();
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }
}
