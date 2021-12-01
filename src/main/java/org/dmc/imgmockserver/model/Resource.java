package org.dmc.imgmockserver.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Resource {
    public UUID getUuid() {
        return uuid;
    }

    @Id
    @Type(type = "pg-uuid")
    private UUID uuid;

    public Resource() {
        this.uuid = UUID.randomUUID();
    }
}
