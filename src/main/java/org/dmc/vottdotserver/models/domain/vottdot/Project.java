package org.dmc.vottdotserver.models.domain.vottdot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.dmc.vottdotserver.models.domain.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Project implements Serializable {
    private String id;
    private String name;
    private Connection sourceConnection;
    private Connection targetConnection;
    private String description;
    private VideoSetting videoSettings;
    private DotToRectSetting dotToRectSettings;
    private String taskId;
    private String taskType;
    private String taskStatus;
    private List<Tag> tags;
    private String version;
    private String useSecurityToken;
    private String securityToken;
    private String stellaUrl;
    private Map<String, String> assets = new HashMap<>();
    private ActiveLearningSetting activeLearningSettings;
    private ExportFormat exportFormat;

    // Overriding toString() method of String class
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static JSONObject generateProjectJsonFromTask(Task task) {
        JSONObject obj = new JSONObject();

        obj.put("id", task.getId());
        obj.put("name", task.getId());

        JSONObject sourceConnection = new JSONObject();
        sourceConnection.put("name", "source");
        sourceConnection.put("providerType", "remoteStorage");
        JSONObject sourceConnectionProviderOptions = new JSONObject();
        sourceConnectionProviderOptions.put("url", task.getImageServerUrl());
        sourceConnectionProviderOptions.put("taskId", task.getId());
        sourceConnectionProviderOptions.put("taskServerUrl", task.getTaskServerUrl());
        sourceConnection.put("providerOptions", sourceConnectionProviderOptions);
        obj.put("sourceConnection", sourceConnection);

        JSONObject targetConnection = new JSONObject();
        targetConnection.put("name", "target");
        targetConnection.put("providerType", "remoteStorage");
        JSONObject targetConnectionProviderOptions = new JSONObject();
        targetConnectionProviderOptions.put("url", task.getVottBackendUrl());
        targetConnectionProviderOptions.put("taskId", task.getId());
        targetConnectionProviderOptions.put("taskServerUrl", task.getTaskServerUrl());
        targetConnection.put("providerOptions", targetConnectionProviderOptions);
        obj.put("targetConnection", targetConnection);

        obj.put("description", task.getDescription());
        JSONObject videoSettings = new JSONObject();
        videoSettings.put("frameExtractionRate", 15);
        obj.put("videoSettings", videoSettings);

        JSONObject dotToRectSettings = new JSONObject();
        dotToRectSettings.put("url", task.getDotToRectUrl());
        obj.put("dotToRectSettings", dotToRectSettings);

        obj.put("taskId", task.getId());
        obj.put("taskType", task.getType());
        obj.put("taskStatus", task.getStatus());

        JSONArray arrayElementArray = new JSONArray();
        for (Map.Entry<String, String> entry : task.getClassList().entrySet()) {
            JSONObject arrayElementOne = new JSONObject();
            arrayElementOne.put("name", entry.getKey());
            arrayElementOne.put("color", entry.getValue());
            arrayElementArray.put(arrayElementOne);
        }
        obj.put("tags", arrayElementArray);
        obj.put("version", "2.2.0");
        obj.put("useSecurityToken", false);
        obj.put("securityToken", task.getId() + " Token");

        obj.put("stellaUrl", task.getStellaUrl());

        Map<String, String> hashMap = new HashMap<>();
        JSONObject jsonObject = new JSONObject(hashMap);
        obj.put("assets", jsonObject);

        JSONObject activeLearningSettings = new JSONObject();
        activeLearningSettings.put("autoDetect", false);
        activeLearningSettings.put("predictTag", true);
        activeLearningSettings.put("modelPathType", "coco");
        obj.put("activeLearningSettings", activeLearningSettings);

        JSONObject exportFormat = new JSONObject();
        exportFormat.put("providerType", "vottJson");
        JSONObject exportFormatProviderOptions = new JSONObject();
        exportFormatProviderOptions.put("assetState", "visited");
        exportFormatProviderOptions.put("includeImages", true);
        exportFormat.put("providerOptions", exportFormatProviderOptions);
        obj.put("exportFormat", exportFormat);

        return obj;
    }

    static public Project convert(Task task) throws JsonProcessingException {
        JSONObject obj = generateProjectJsonFromTask(task);
        ObjectMapper mapper = new ObjectMapper();
        // JSON format validation
        return mapper.readValue(obj.toString(), Project.class);
    }
}
