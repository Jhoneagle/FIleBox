package fi.omat.johneagle.filebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import fi.omat.johneagle.filebox.domain.entities.File;
import fi.omat.johneagle.filebox.domain.entities.Image;
import fi.omat.johneagle.filebox.domain.jsonmodels.VisibilityUpdate;
import fi.omat.johneagle.filebox.services.ApiService;
import fi.omat.johneagle.filebox.services.ImageService;

/**
 * Rest controller to handle API calls that relate to interacting with people or system.
 */
@RestController
public class InteractionController {
    @Autowired
    private ImageService imageService;

    @Autowired
    private ApiService apiService;

    /**
     * Used to show images in the application without taking their byte arrays into template.
     * Can be used through image tags src attribute as if this was actual image file when in fact its gotten from database.
     *
     * @param id id of the image to be shown.
     *
     * @return Content of the image.
     */
    @GetMapping("/fileBox/api/images/{id}")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long id) {
        Image image = this.imageService.getImageById(id);
        return getResponseEntity(image.getContentType(), image.getContentLength(), image.getFilename(), image.getContent());
    }

    /**
     * Used to download files in the application without taking their byte arrays into template.
     * Can be used through as a link.
     *
     * @param id id of the file to be download.
     *
     * @return Content of the file.
     */
    @GetMapping("/fileBox/api/files/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        File fo = apiService.getFile(id);
        return getResponseEntity(fo.getContentType(), fo.getContentLength(), fo.getFilename(), fo.getContent());
    }

    private ResponseEntity<byte[]> getResponseEntity(String contentType, Long contentLength, String filename, byte[] content) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(contentLength);
        headers.add("Content-Disposition", "attachment; filename=" + filename);

        return new ResponseEntity<>(content, headers, HttpStatus.CREATED);
    }

    /**
     * API endpoint to update files visibility.
     *
     * @param update new visibility.
     *
     * @return string to indicate if operation succeed or not.
     */
    @PostMapping("/fileBox/api/files/access")
    public String updateVisibility(@Valid @RequestBody VisibilityUpdate update) {
        boolean success = this.apiService.updateFIleVisibility(update);

        if (success) {
            return "Success!";
        } else {
            return "Access denied!";
        }
    }
}
