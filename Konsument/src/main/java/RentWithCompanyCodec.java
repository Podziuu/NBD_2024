import models.Rent;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import com.mongodb.MongoClientSettings;
import org.bson.Document;

public class RentWithCompanyCodec implements Codec<Konsument.RentWithCompany> {
    private final Codec<Document> documentCodec;

    public RentWithCompanyCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, Konsument.RentWithCompany value, EncoderContext encoderContext) {
        Document doc = new Document()
                .append("rent", value.getRent())
                .append("rentalCompany", value.getRentalCompany());
        documentCodec.encode(writer, doc, encoderContext);
    }

    @Override
    public Konsument.RentWithCompany decode(BsonReader reader, DecoderContext decoderContext) {
        Document doc = documentCodec.decode(reader, decoderContext);
        Rent rent = doc.get("rent", Rent.class);
        String rentalCompany = doc.getString("rentalCompany");
        return new Konsument.RentWithCompany(rent, rentalCompany);
    }

    @Override
    public Class<Konsument.RentWithCompany> getEncoderClass() {
        return Konsument.RentWithCompany.class;
    }
}

