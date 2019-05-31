package fi.omat.johneagle.filebox.controllers;

import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.domain.validationmodels.ImageModel;
import fi.omat.johneagle.filebox.domain.validationmodels.PersonInfoModel;
import fi.omat.johneagle.filebox.services.ProfileService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

/**
 * Controller to handle routes related to profile actions that user can do after authentication which aren't called by javascript.
 * For REST API endpoints there is own specific controller to handle javascript related actions.
 */
@Controller
public class ProfileController {
    @Autowired
    private ProfileService profileService;


    @GetMapping("/fileBox/{nickname}")
    public String mainPage(Model model, @PathVariable String nickname) {
        Account owner = this.profileService.findByNickname(nickname);
        String name;

        // avoiding non fatal null pointer exception that is caused by 'fast access into lazy object'.
        // basically disabling the messages that are being spammed by this in the log otherwise.
        try {
            name = owner.getFullName();
        } catch (Exception e) {
            name = "";
        }

        model.addAttribute("profileName", name);
        return "main-page";
    }

    /**
     * Personal page.
     * This router is also preAuthorized by security to make sure user has authorization to do this.
     *
     * @see fi.omat.johneagle.filebox.security.CustomPermissionEvaluator
     *
     * @param model model object
     * @param nickname nickname of the person whose page
     *
     * @return template name.
     */
    @PreAuthorize("hasPermission('owner', #nickname)")
    @GetMapping("/fileBox/{nickname}/personal")
    public String infoPage(Model model, @PathVariable String nickname) {
        if (!model.containsAttribute("imageModel")) {
            model.addAttribute("imageModel", new ImageModel());
        }

        if (!model.containsAttribute("personInfoModel")) {
            Account owner = this.profileService.findByNickname(nickname);
            PersonInfoModel validationModel = new PersonInfoModel();

            try {
                BeanUtils.copyProperties(validationModel, owner);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            model.addAttribute("personInfoModel", validationModel);
        }

        return "personal-page";
    }


    @PreAuthorize("hasPermission('owner', #nickname)")
    @PostMapping("/fileBox/{nickname}/personal")
    public String updateInfo(@PathVariable String nickname, @Valid @ModelAttribute PersonInfoModel personInfoModel,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.personInfoModel", bindingResult);
            redirectAttributes.addFlashAttribute("personInfoModel", personInfoModel);
        } else {
            this.profileService.updateProfile(personInfoModel);
        }

        return "redirect:/fileBox/" + nickname + "/personal";
    }

    /**
     * Returns search page showing there the result that was gotten by the parameter user gave.
     * In this situations the list of persons whose first and/or last name withs the search parameter.
     *
     * @param searchField search parameter
     *
     * @return template name.
     */
    @PostMapping("/fileBox/search")
    public String search(@RequestParam String searchField) {
        return "redirect:/fileBox/search/" + searchField;
    }

    /**
     * Returns search page showing there the result that was gotten by the parameter user gave.
     * In this situations the list of persons whose first and/or last name withs the search parameter.
     *
     * @param model model object
     *
     * @return template name.
     */
    @GetMapping("/fileBox/search/{searchField}")
    public String search(Model model, @PathVariable String searchField) {
        model.addAttribute("result", this.profileService.findPeopleWithParam(searchField));
        return "search-page";
    }

    /**
     * Sets the image indicated by the id to be new profile picture of users account.
     * Returning to the main page then.
     * This router is also preAuthorized by security to make sure user has authorization to do this.
     *
     * @see fi.omat.johneagle.filebox.security.CustomPermissionEvaluator
     *
     * @param nickname nickname of the user who owns the picture.
     * @param imageModel validation model
     * @param bindingResult wrapper for validation errors
     * @param redirectAttributes object to enable attributes in redirection
     *
     * @return redirection back to the main page.
     */
    @PreAuthorize("hasPermission('owner', #nickname)")
    @PostMapping("/fileBox/{nickname}/setProfilePicture")
    public String setAsProfilePicture(@PathVariable String nickname, @Valid @ModelAttribute ImageModel imageModel,
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.imageModel", bindingResult);
            redirectAttributes.addFlashAttribute("imageModel", imageModel);
        } else {
            this.profileService.setProfilePicture(imageModel.getFile(), "profile picture.");
        }

        return "redirect:/fileBox/" + nickname + "/personal";
    }
}
