package fi.omat.johneagle.filebox.controllers;

import fi.omat.johneagle.filebox.domain.entities.Image;
import fi.omat.johneagle.filebox.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller to handle API calls that relate to interacting with people or system.
 */
@RestController
public class InteractionController {
    @Autowired
    private ImageService imageService;

    /**
     * Used to show images in the application without taking their byte arrays into template.
     * Can be used through image tags src attribute as if this was actual image file when in fact its gotten from database.
     *
     * @param id id of the image to be shown.
     * @return Content of the image.
     */
    @GetMapping("/fileBox/api/files/{id}")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long id) {
        Image image = this.imageService.getImageById(id);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getContentType()));
        headers.setContentLength(image.getContentLength());
        headers.add("Content-Disposition", "attachment; filename=" + image.getFilename());

        return new ResponseEntity<>(image.getContent(), headers, HttpStatus.CREATED);
    }
}
