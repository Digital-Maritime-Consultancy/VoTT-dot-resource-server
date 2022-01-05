package org.dmc.vottdotserver.models.domain;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.dmc.vottdotserver.models.domain.enums.AssetState;
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
    private Map<String, String> image_list = new HashMap<>();
    @Type( type = "json" )
    private Map<String, AssetState> progress = new HashMap<>();
    @NotNull
    @NotBlank
    private String type;
    @NotNull
    @NotBlank
    private String status;
    private String createdAt;
    private String lastUpdatedAt;


    public Map<String, String> getClass_list() {
        return class_list;
    }

    public void setClass_list(Map<String, String> class_list) {
        this.class_list = class_list;
    }

    public Map<String, String> getImage_list() {
        return image_list;
    }

    public void setImage_list(Map<String, String> image_list) {
        this.image_list = image_list;
    }

    public Map<String, AssetState> getProgress() {
        return progress;
    }

    public void setProgress(Map<String, AssetState> progress) {
        this.progress = progress;
    }

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

    public String getStatus()            {
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

    public void update(Task original) {
        this.setCreatedAt(original.getCreatedAt());
        if (this.getProgress() == null){
            this.setProgress(original.getProgress());
        }
        if (this.getClass_list() == null){
            this.setClass_list(original.getClass_list());
        }
        if (this.getImage_list() == null){
            this.setImage_list(original.getImage_list());
        }
    }
}
