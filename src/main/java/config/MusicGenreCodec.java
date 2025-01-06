package config;

import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import models.MusicGenre;

public class MusicGenreCodec extends MappingCodec<Integer, MusicGenre> {

    public MusicGenreCodec() {
        super(TypeCodecs.INT, GenericType.of(MusicGenre.class));
    }

    @Override
    protected MusicGenre innerToOuter(Integer value) {
        return value == null ? null : MusicGenre.fromValue(value);
    }

    @Override
    protected Integer outerToInner(MusicGenre musicGenre) {
        return musicGenre == null ? null : musicGenre.getValue();
    }
}
