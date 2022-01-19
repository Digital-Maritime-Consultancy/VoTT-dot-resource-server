package org.dmc.vottdotserver.models.dto;

import org.dmc.vottdotserver.models.JsonSerializable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

public class TaskDto implements Serializable, JsonSerializable {
    private static final long serialVersionUID = -1173956383755083179L;

    @NotNull
    private String id;
    private String stella_url;
    private String description;
    private Map<String, String> class_list;
    @NotNull
    private String type;
    @NotNull
    private String status;
    private Map<String, String> image_list;
    private String createdAt;
    private String lastUpdatedAt;
    private Map<String, String> progress;

    public Map<String, String> getProgress() {
        return progress;
    }

    public void setProgress(Map<String, String> progress) {
        this.progress = progress;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
