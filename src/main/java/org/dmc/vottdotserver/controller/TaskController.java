package org.dmc.vottdotserver.controller;

import org.dmc.vottdotserver.model.Task;
import org.dmc.vottdotserver.repository.TaskRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskRepository taskRepository;

    @GetMapping("")
    public ResponseEntity<List<Task>> getAllTasks() {
        try {
            List<Task> Task = new ArrayList<Task>();

            taskRepository.findAll().forEach(Task::add);

            if (Task.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(Task, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getTaskById(@RequestParam("uuid") String id) {
        Optional<Task> metadatum = taskRepository.findById(id);

        if (metadatum.isPresent()) {
            return new ResponseEntity<>(metadatum.get().getData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Task> createTask(@RequestBody String TaskBody) {
        try {
            final JSONObject obj = new JSONObject(TaskBody);
            final String metadatumId = obj.getString("token");
            final String owner = obj.getString("owner");
            if (metadatumId.length() == 0 || owner.length() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
            try {
                Task _metadatum = taskRepository
                        .save(new Task(metadatumId, TaskBody));
                return new ResponseEntity<>(_metadatum, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> updateTask(@RequestParam("uuid") String id, @RequestBody String TaskBody) {
        Optional<Task> metadatum = taskRepository.findById(id);

        if (TaskBody.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        if (metadatum.isPresent()) {
            Task _metadatum = metadatum.get();
            _metadatum.setData(TaskBody);
            return new ResponseEntity<>(taskRepository.save(_metadatum).getData(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(taskRepository.save(new Task(id, TaskBody)).getData(), HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<HttpStatus> deleteTask(@RequestParam("uuid") String id) {
        try {
            Optional<Task> metadatum = taskRepository.findById(id);

            if (metadatum.isPresent()) {
                Task _metadatum = metadatum.get();
                taskRepository.deleteById(_metadatum.getUuid());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
