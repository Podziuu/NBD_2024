package config;

import jakarta.json.bind.adapter.JsonbAdapter;
import org.bson.types.ObjectId;

public class ObjectIdAdapter implements JsonbAdapter<ObjectId, String> {
    @Override
    public String adaptToJson(ObjectId obj) {
        return obj != null ? obj.toHexString() : null;
    }

    @Override
    public ObjectId adaptFromJson(String obj) {
        return obj != null ? new ObjectId(obj) : null;
    }
}