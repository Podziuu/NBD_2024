package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator("music")
public class Music extends ItemType {
    public Music() {
    }

    @BsonIgnore
    @Override
    public String getItemTypeInfo() {
        return "Music.";
    }
}