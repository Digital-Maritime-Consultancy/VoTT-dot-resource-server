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
}
