package org.dmc.vottdotserver.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import org.dmc.vottdotserver.model.Task;
import org.dmc.vottdotserver.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Value("classpath:jsons/data.json")
    Resource resourceFile;

    @Autowired
    TaskRepository taskRepository;

    @RequestMapping(value = "", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllImages() throws IOException {
        return Files.readString(resourceFile.getFile().toPath());
    }

    @RequestMapping(value = "/{fileName}", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(HttpServletResponse response, @PathVariable("fileName") String fileName) throws IOException {

        var imgFile = new ClassPathResource("images/"+fileName);

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }
}
