package repos;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import config.CassandraConfig;
import dao.ItemDao;
import mapper.ItemMapper;
import mapper.ItemMapperBuilder;
import models.*;

import java.util.UUID;

public class ItemRepository extends CassandraConfig {
    private final CqlSession session;
    private final ItemDao itemDao;

    public ItemRepository(CqlSession session) {
        this.session = session;
        createTable();
        ItemMapper itemMapper = new ItemMapperBuilder(session).build();
        this.itemDao = itemMapper.itemDao();
    }

    private void createTable() {
        SimpleStatement createItems = SchemaBuilder.createTable(CqlIdentifier.fromCql("items_by_id"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("item_id"), DataTypes.UUID)
                .withClusteringColumn(CqlIdentifier.fromCql("item_type"), DataTypes.TEXT)
                .withColumn("base_price", DataTypes.INT)
                .withColumn("item_name", DataTypes.TEXT)
                .withColumn("available", DataTypes.BOOLEAN)
                .withColumn("genre", DataTypes.INT)
                .withColumn("vinyl", DataTypes.BOOLEAN)
                .withColumn("minutes", DataTypes.INT)
                .withColumn("casette", DataTypes.BOOLEAN)
                .withColumn("page_number", DataTypes.INT)
                .build();
        session.execute(createItems);
    }

    public void addItem(Item item) {
        itemDao.create(item);
    }

    public Item getItem(UUID id) {
        Item item = itemDao.read(id);
        if (item != null) {
            switch (item.getItemType()) {
                case "music":
                    return itemDao.readMusic(id);
                case "movie":
                    return itemDao.readMovie(id);
                case "comics":
                    return itemDao.readComics(id);
                default:
                    return item;
            }
        }
        return null;
    }

    public void updateItem(Item item) {
        itemDao.update(item);
    }

    public void removeItem(UUID id) {
        itemDao.deleteById(id);
    }

    @Override
    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }
}