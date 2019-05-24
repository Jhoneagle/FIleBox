package fi.omat.johneagle.filebox.services;

import fi.omat.johneagle.filebox.domain.entities.Image;
import fi.omat.johneagle.filebox.repository.AccountRepository;
import fi.omat.johneagle.filebox.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for actions related to images.
 */
@Service
public class ImageService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ImageRepository imageRepository;

    /**
     * Finds image by its id.
     *
     * @param id id of an image.
     *
     * @return image that represents the id.
     */
    public Image getImageById(Long id) {
        return this.imageRepository.getOne(id);
    }
}
