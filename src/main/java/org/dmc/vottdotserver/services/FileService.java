package org.dmc.vottdotserver.services;

import lombok.extern.slf4j.Slf4j;
import org.dmc.vottdotserver.exceptions.DataNotFoundException;
import org.dmc.vottdotserver.models.domain.File;
import org.dmc.vottdotserver.models.domain.Task;
import org.dmc.vottdotserver.models.domain.enums.AssetState;
import org.dmc.vottdotserver.repository.FileRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.ServiceUnavailableException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class FileService {
    @Autowired
    FileRepository fileRepository;

    /**
     * Get all the files.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<File> findAll() {
        log.debug("Request to get all files");
        return this.fileRepository.findAll();
    }

    /**
     * Get one file by ID.
     *
     * @param fileName  the name of file
     * @return the entity
     */
    @Transactional(readOnly = true)
    public File findOne(String fileName) throws DataNotFoundException {
        log.debug("Request to get file : {}", fileName);
        return fileRepository.findByFileName(fileName).stream().findFirst()
                .orElseThrow(() -> new DataNotFoundException("No file found for the provided ID", null));
    }

    /**
     * Save a file.
     *
     * @param fileName the name of file
     * @param jsonBody content string
     * @return the persisted entity
     */
    @Transactional
    public File save(String fileName, String jsonBody) throws ServiceUnavailableException {
        log.debug("Request to save file : {}", fileName);
        Optional<File> metadatum = fileRepository.findByFileName(fileName);
        boolean isDisabled = false;

        if (jsonBody.isEmpty()) {
            throw new ServiceUnavailableException("No JSON body");
        }
        else {
            if (fileName.contains(".json")) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(jsonBody);
                } catch (Exception e) {
                    System.out.println("Error in parsing json: \n" +jsonBody);
                }
                if (obj != null && obj.getJSONObject("asset") != null) {
                    isDisabled = obj.getJSONObject("asset").getBoolean("isDisabled");
                }
            }
        }
        if (metadatum.isPresent()) {
            File _metadatum = metadatum.get();
            _metadatum.setIsDisabled(isDisabled);
            _metadatum.setData(jsonBody);
            return fileRepository.save(_metadatum);
        } else {
            return fileRepository.save(new File(fileName, isDisabled, jsonBody));
        }
    }

    /**
     * Delete the file by ID.
     *
     * @param fileName the name of file
     */
    @Transactional(propagation = Propagation.NESTED)
    public void delete(String fileName) throws DataNotFoundException {
        log.debug("Request to delete file : {}", fileName);
        this.fileRepository.findByFileName(fileName)
                .ifPresentOrElse(i -> {
                    this.fileRepository.deleteById(i.getId());
                }, () -> {
                    throw new DataNotFoundException("No file found for the provided ID", null);
                });
    }
}
