package org.dmc.vottdotserver.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.dmc.vottdotserver.models.JsonSerializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class TaskDto implements Serializable, JsonSerializable {
    private static final long serialVersionUID = -1173956383755083179L;

    @NotNull
    private String id;
    @NotNull
    private String stellaUrl;
    private String description;
    private Map<String, String> classList;
    @NotNull
    private String type;
    @NotNull
    private String status;
    private Map<String, String> imageList;
    private String createdAt;
    private String lastUpdatedAt;
    private Map<String, String> progress;
    @NotNull
    private String vottBackendUrl;
    @NotNull
    private String imageServerUrl;
    @NotNull
    private String taskServerUrl;
}
