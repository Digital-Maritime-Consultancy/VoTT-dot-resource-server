package org.dmc.vottdotserver.controllers;

import org.dmc.vottdotserver.exceptions.DataNotFoundException;
import org.dmc.vottdotserver.models.domain.File;
import org.dmc.vottdotserver.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ServiceUnavailableException;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@Validated
@RequestMapping("/file")
public class FileController {
    @Autowired
    FileService fileService;

    @GetMapping("")
    public ResponseEntity<List<File>> getAllMetadatas() {
        try {
            List<File> files = this.fileService.findAll();

            if (files.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(files, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{fileName}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getMetadataById(@PathVariable("fileName") String fileName) {
        try {
            File file = this.fileService.findOne(fileName);
            return new ResponseEntity<>(file.getData(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{fileName}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> updateMetadata(@PathVariable("fileName") String fileName, @RequestBody String jsonBody) throws ServiceUnavailableException {
        return new ResponseEntity<>(this.fileService.save(fileName, jsonBody).getData(), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<HttpStatus> deleteMetadata(@PathVariable("fileName") String fileName) {
        try {
            this.fileService.delete(fileName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DataNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
