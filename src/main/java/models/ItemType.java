package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

@BsonDiscriminator("itemType")
public class ItemType {
    @BsonId
    private ObjectId id;
    @BsonCreator
    public ItemType() {

    }

    @BsonIgnore
    public String getItemTypeInfo() {
        return "Przedmiot typu: ";
    }

    public ObjectId getItemId() {
        return id;
    }

    public void setItemId(ObjectId id) {
        this.id = id;
    }
}
