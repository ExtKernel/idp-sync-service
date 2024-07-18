package com.iliauni.idpsyncservice.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class UsergroupSerializer extends StdSerializer<Usergroup> {

    public UsergroupSerializer() {
        this(null);
    }

    protected UsergroupSerializer(Class<Usergroup> t) {
        super(t);
    }

    @Override
    public void serialize(
            Usergroup usergroup,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider
    ) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", usergroup.getName());
        String usergroupDescription = usergroup.getDescription();
        if (usergroupDescription != null) jsonGenerator.writeStringField("description", usergroupDescription);
        jsonGenerator.writeEndObject();
    }
}
