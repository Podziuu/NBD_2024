package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator("comics")
public class Comics extends ItemType {

    public Comics() {
    }

    @BsonIgnore
    @Override
    public String getItemTypeInfo() {
        return "Comics.";
    }
}
