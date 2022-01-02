package org.dmc.vottdotserver.controllers;

import org.dmc.vottdotserver.components.DomainDtoMapper;
import org.dmc.vottdotserver.exceptions.DataNotFoundException;
import org.dmc.vottdotserver.models.domain.Task;
import org.dmc.vottdotserver.models.dto.TaskDto;
import org.dmc.vottdotserver.services.TaskService;
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
import java.util.List;
import java.util.UUID;

//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;

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

    @GetMapping("")
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
        return saveTask(UUID.fromString(id), this.taskDtoToDomainMapper.convertTo(taskDto, Task.class), !this.taskService.doesExist(UUID.fromString(id)));
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteTask(@RequestParam("uuid") String id){
        try {
            this.taskService.delete(UUID.fromString(id));
        }
        catch (Exception e)
        {
            return ResponseEntity.notFound()
                    .build();
        }
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<TaskDto> saveTask(UUID id, Task task, boolean newInstance) {
        try {
            task.setId(id);
            task = this.taskService.save(task);
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(this.taskDomainToDtoMapper.convertTo(task, TaskDto.class));
        }

        return newInstance ?
                new ResponseEntity<>(this.taskDomainToDtoMapper.convertTo(task, TaskDto.class), HttpStatus.CREATED) :
                new ResponseEntity<>(this.taskDomainToDtoMapper.convertTo(task, TaskDto.class), HttpStatus.ACCEPTED);
    }
}
