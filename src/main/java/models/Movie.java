package models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;

@BsonDiscriminator("movie")
public class Movie extends ItemType {
    public Movie() {
    }

    @BsonIgnore
    @Override
    public String getItemTypeInfo() {
        return "Movie.";
    }
}