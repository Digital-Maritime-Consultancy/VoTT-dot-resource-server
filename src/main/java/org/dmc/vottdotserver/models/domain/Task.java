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
