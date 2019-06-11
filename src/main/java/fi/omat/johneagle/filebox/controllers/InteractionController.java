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
     * @return Content of the image.
     */
    @GetMapping("/fileBox/api/images/{id}")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long id) {
        Image image = this.imageService.getImageById(id);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getContentType()));
        headers.setContentLength(image.getContentLength());
        headers.add("Content-Disposition", "attachment; filename=" + image.getFilename());

        return new ResponseEntity<>(image.getContent(), headers, HttpStatus.CREATED);
    }

    @GetMapping("/fileBox/api/files/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        File fo = apiService.getFile(id);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fo.getContentType()));
        headers.setContentLength(fo.getContentLength());
        headers.add("Content-Disposition", "attachment; filename=" + fo.getFilename());

        return new ResponseEntity<>(fo.getContent(), headers, HttpStatus.CREATED);
    }

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
