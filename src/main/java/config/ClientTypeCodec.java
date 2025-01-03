package config;

import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import models.ClientType;

public class ClientTypeCodec extends MappingCodec<String, ClientType> {

    public ClientTypeCodec() {
        super(TypeCodecs.TEXT, GenericType.of(ClientType.class));
    }

    @Override
    protected ClientType innerToOuter(String s) {
        return s == null ? null : ClientType.fromString(s);
    }

    @Override
    protected String outerToInner(ClientType clientType) {
        return clientType == null ? null : clientType.toString();
    }
}