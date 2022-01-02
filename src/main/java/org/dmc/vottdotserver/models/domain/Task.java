package org.dmc.vottdotserver.models.domain;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.json.JSONObject;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Data
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Task extends Resource {
    @NotNull
    @NotBlank
    private String stella_url;
    private String description;
    @Type( type = "json" )
    private Map<String, String> class_list = new HashMap<>();
    @Type( type = "json" )
    private Map<String, String> image_list;
    @NotNull
    @NotBlank
    private String type;
    @NotNull
    @NotBlank
    private String status;

    private String createdAt;
    private String lastUpdatedAt;

    public String getStella_url() {
        return stella_url;
    }

    public void setStella_url(String stella_url) {
        this.stella_url = stella_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Task() {
        this.setCreatedAt("");
        this.setLastUpdatedAt("");
    }
}
