package org.dmc.vottdotserver.controllers;

import org.dmc.vottdotserver.components.DomainDtoMapper;
import org.dmc.vottdotserver.exceptions.DataNotFoundException;
import org.dmc.vottdotserver.models.domain.Task;
import org.dmc.vottdotserver.models.dto.TaskDto;
import org.dmc.vottdotserver.services.FileService;
import org.dmc.vottdotserver.services.TaskService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    FileService fileService;

    /**
     * Object Mapper from Domain to DTO.
     */
    @Autowired
    DomainDtoMapper<Task, TaskDto> taskDomainToDtoMapper;

    /**
     * Object Mapper from DTO to Domain.
     */
    @Autowired
    DomainDtoMapper<TaskDto, Task> taskDtoToDomainMapper;

    @GetMapping(value = "/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        try {
            List<Task> tasks = this.taskService.findAll();

            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> getTask(@RequestParam("uuid") String id) throws DataNotFoundException{
        return ResponseEntity.ok()
                .body(this.taskDomainToDtoMapper.convertTo(this.taskService.findOne(UUID.fromString(id)), TaskDto.class));
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<TaskDto> updateTask(@RequestParam("uuid") String id, @Valid @RequestBody TaskDto taskDto) throws DataNotFoundException{
        boolean newTask = !this.taskService.doesExist(UUID.fromString(id));
        Task task = this.taskDtoToDomainMapper.convertTo(taskDto, Task.class);
        try {
            task.setId(UUID.fromString(id));
            task = this.taskService.save(task);
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(this.taskDomainToDtoMapper.convertTo(task, TaskDto.class));
        }

        return newTask ?
                new ResponseEntity<>(this.taskDomainToDtoMapper.convertTo(task, TaskDto.class), HttpStatus.CREATED) :
                new ResponseEntity<>(this.taskDomainToDtoMapper.convertTo(task, TaskDto.class), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("")
    public ResponseEntity<HttpStatus> deleteTask(@RequestParam("uuid") String id){
        try {
            this.taskService.delete(UUID.fromString(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/newProject", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> createNewProject(@RequestParam("uuid") String id) {
        Task task;
        try {
            task = this.taskService.findOne(UUID.fromString(id));
        } catch (DataNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        try {
            JSONObject project = generateProjectJsonFromTask(task);
            this.fileService.save(id + ".vott", project.toString());
            return new ResponseEntity<>(project.toString(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private JSONObject generateProjectJsonFromTask(Task task) {
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

        if (task.getType().equals("Cleansing")){
            if (task.getStatus().equals("In_progress")){
                obj.put("taskType", "purification");
            } else if (task.getStatus().equals("Review")){
                obj.put("taskType", "revise-purification");
            }
        } else if (task.getType().equals("Annotation")){
            if (task.getStatus().equals("In_progress")){
                obj.put("taskType", "annotation");
            } else if (task.getStatus().equals("Review")){
                obj.put("taskType", "revise-annotation");
            }
        } else if (task.getType().equals("Evaluation")){
            obj.put("taskType", "audit");
        } else {
            obj.put("taskType", "notassigned");
        }

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
        obj.put("securityToken", "");
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
}
