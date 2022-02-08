package org.dmc.vottdotserver.models.domain;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.dmc.vottdotserver.models.domain.enums.AssetState;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Task extends Resource {
    @NotNull
    @NotBlank
    private String stellaUrl;
    @NotNull
    @NotBlank
    private String vottBackendUrl;
    @NotNull
    @NotBlank
    private String imageServerUrl;
    @NotNull
    @NotBlank
    private String taskServerUrl;
    private String dotToRectUrl;
    private String description;
    @Type( type = "json" )
    private Map<String, String> classList = new HashMap<>();
    @Type( type = "json" )
    private Map<String, String> imageList = new HashMap<>();
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
        if (this.getClassList() == null){
            this.setClassList(original.getClassList());
        }
        if (this.getImageList() == null){
            this.setImageList(original.getImageList());
        }
    }
}
