package org.dmc.imgmockserver.controller;

import org.dmc.imgmockserver.model.Project;
import org.dmc.imgmockserver.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    ProjectRepository projectRepository;

    @GetMapping("")
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            List<Project> projects = new ArrayList<Project>();

            projectRepository.findAll().forEach(projects::add);

            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getProjectById(@PathVariable("id") String id) {
        Optional<Project> projectData = projectRepository.findByProjectId(id);

        if (projectData.isPresent()) {
            return new ResponseEntity<>(projectData.get().getData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<Project> createProject(@RequestBody String jsonBody) {
        try {
            final JSONObject obj = new JSONObject(jsonBody);
            final String projectId = obj.getString("id");
            if (projectId.length() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
            try {
                Project _project = projectRepository
                        .save(new Project(projectId, jsonBody));
                return new ResponseEntity<>(_project, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> updateProject(@PathVariable("id") String id, @RequestBody String jsonBody) {
        Optional<Project> projectData = projectRepository.findByProjectId(id);

        if (projectData.isPresent()) {
            Project _project = projectData.get();

            // check the submitted data contains identical project id
            final JSONObject obj = new JSONObject(jsonBody);
            final String projectId = obj.getString("id");
            if (!projectId.equals(_project.getProjectId())) {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
            _project.setData(jsonBody);
            return new ResponseEntity<>(projectRepository.save(_project).getData(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable("id") String id) {
        try {
            Optional<Project> projectData = projectRepository.findByProjectId(id);

            if (projectData.isPresent()) {
                Project _project = projectData.get();
                projectRepository.deleteById(_project.getUuid());
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
