package org.dmc.vottdotserver.controller;

import org.dmc.vottdotserver.model.Json;
import org.dmc.vottdotserver.repository.JsonRepository;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jsons")
public class JsonController {
    @Autowired
    JsonRepository jsonRepository;

    @GetMapping("")
    public ResponseEntity<List<Json>> getAllMetadatas() {
        try {
            List<Json> metadata = new ArrayList<Json>();

            jsonRepository.findAll().forEach(metadata::add);

            if (metadata.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(metadata, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{fileName}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getMetadataById(@PathVariable("fileName") String fileName) {
        Optional<Json> metadatum = jsonRepository.findByFileName(fileName);

        if (metadatum.isPresent()) {
            return new ResponseEntity<>(metadatum.get().getData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{fileName}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Json> createMetadata(@PathVariable("fileName") String fileName, @RequestBody String jsonBody) {
        try {
            final JSONObject obj = new JSONObject(jsonBody);
            final String metadatumId = fileName;
            if (metadatumId.length() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
            try {
                Json _metadatum = jsonRepository
                        .save(new Json(metadatumId, jsonBody));
                return new ResponseEntity<>(_metadatum, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "/{fileName}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> updateMetadata(@PathVariable("fileName") String fileName, @RequestBody String jsonBody) {
        Optional<Json> metadatum = jsonRepository.findByFileName(fileName);

        if (jsonBody.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        if (metadatum.isPresent()) {
            Json _metadatum = metadatum.get();
            _metadatum.setData(jsonBody);
            return new ResponseEntity<>(jsonRepository.save(_metadatum).getData(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(jsonRepository.save(new Json(fileName, jsonBody)).getData(), HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<HttpStatus> deleteMetadata(@PathVariable("fileName") String fileName) {
        try {
            Optional<Json> metadatum = jsonRepository.findByFileName(fileName);

            if (metadatum.isPresent()) {
                Json _metadatum = metadatum.get();
                jsonRepository.deleteById(_metadatum.getUuid());
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
