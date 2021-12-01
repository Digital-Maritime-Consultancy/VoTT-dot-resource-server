package org.dmc.imgmockserver.controller;

import org.dmc.imgmockserver.model.Metadata;
import org.dmc.imgmockserver.repository.MetadataRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/metadata")
public class MetadataController {
    @Autowired
    MetadataRepository metadataRepository;

    @GetMapping("")
    public ResponseEntity<List<Metadata>> getAllMetadatas() {
        try {
            List<Metadata> metadata = new ArrayList<Metadata>();

            metadataRepository.findAll().forEach(metadata::add);

            if (metadata.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(metadata, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getMetadataById(@PathVariable("id") String id) {
        Optional<Metadata> metadatum = metadataRepository.findByAssetId(id);

        if (metadatum.isPresent()) {
            return new ResponseEntity<>(metadatum.get().getData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<Metadata> createMetadata(@RequestBody String jsonBody) {
        try {
            final JSONObject obj = new JSONObject(jsonBody);
            final String metadatumId = obj.getJSONObject("asset").getString("id");
            if (metadatumId.length() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
            try {
                Metadata _metadatum = metadataRepository
                        .save(new Metadata(metadatumId, jsonBody));
                return new ResponseEntity<>(_metadatum, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> updateMetadata(@PathVariable("id") String id, @RequestBody String jsonBody) {
        Optional<Metadata> metadatum = metadataRepository.findByAssetId(id);

        if (metadatum.isPresent()) {
            Metadata _metadatum = metadatum.get();

            // check the submitted data contains identical asset id
            final JSONObject obj = new JSONObject(jsonBody);
            final String metadatumId = obj.getJSONObject("asset").getString("id");
            if (!metadatumId.equals(_metadatum.getAssetId())) {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
            _metadatum.setData(jsonBody);
            return new ResponseEntity<>(metadataRepository.save(_metadatum).getData(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMetadata(@PathVariable("id") String id) {
        try {
            Optional<Metadata> metadatum = metadataRepository.findByAssetId(id);

            if (metadatum.isPresent()) {
                Metadata _metadatum = metadatum.get();
                metadataRepository.deleteById(_metadatum.getUuid());
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
