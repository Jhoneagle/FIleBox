package fi.omat.johneagle.filebox.controllers;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.lang.reflect.InvocationTargetException;
import javax.validation.Valid;
import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.domain.entities.Image;
import fi.omat.johneagle.filebox.domain.validationmodels.ChangePasswordModel;
import fi.omat.johneagle.filebox.domain.validationmodels.DownloadFile;
import fi.omat.johneagle.filebox.domain.validationmodels.ImageModel;
import fi.omat.johneagle.filebox.domain.validationmodels.PersonInfoModel;
import fi.omat.johneagle.filebox.services.ProfileService;

/**
 * Controller to handle routes related to profile actions that user can do after authentication which aren't called by javascript.
 * For REST API endpoints there is own specific controller to handle javascript related actions.
 */
@Controller
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    /**
     * Main page and peoples wall.
     *
     * @param model model object
     * @param nickname nickname of the person whose page
     *
     * @return template name.
     */
    @GetMapping("/fileBox/{nickname}")
    public String mainPage(Model model, @PathVariable String nickname) {
        if (!model.containsAttribute("downloadFile")) {
            model.addAttribute("downloadFile", new DownloadFile());
        }

        Account owner = this.profileService.findByNickname(nickname);
        String name;

        // avoiding non fatal null pointer exception that is caused by 'fast access into lazy object'.
        // basically disabling the messages that are being spammed by this in the log otherwise.
        try {
            name = owner.getFullName();
        } catch (Exception e) {
            name = "";
        }

        Image profileImage = owner.getProfileImage();
        if (profileImage != null) {
            model.addAttribute("picId", profileImage.getId());
        }

        model.addAttribute("following", this.profileService.getFollowing(nickname));
        model.addAttribute("files", this.profileService.getShowableFiles(nickname));
        model.addAttribute("profileName", name);
        model.addAttribute("whoseWall", owner.getNickname());
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
        Account owner = this.profileService.findByNickname(nickname);

        if (!model.containsAttribute("imageModel")) {
            model.addAttribute("imageModel", new ImageModel());
        }

        if (!model.containsAttribute("changePasswordModel")) {
            model.addAttribute("changePasswordModel", new ChangePasswordModel());
        }

        if (!model.containsAttribute("personInfoModel")) {
            PersonInfoModel validationModel = new PersonInfoModel();

            try {
                BeanUtils.copyProperties(validationModel, owner);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            model.addAttribute("personInfoModel", validationModel);
        }

        Image profileImage = owner.getProfileImage();
        if (profileImage != null) {
            model.addAttribute("picId", profileImage.getId());
        }

        model.addAttribute("whoseWall", owner.getNickname());
        return "personal-page";
    }

    /**
     * Updates persons account information if validation accepts it. Only user itself has access thanks to PreAuthorize.
     *
     * @param nickname nickname of the user
     * @param personInfoModel validation model for account update
     * @param bindingResult wrapper for validation errors
     * @param redirectAttributes object to enable attributes in redirection
     *
     * @return redirect back to page came from.
     */
    @PreAuthorize("hasPermission('owner', #nickname)")
    @PostMapping("/fileBox/{nickname}/personal")
    public String updateInfo(@PathVariable String nickname, @Valid @ModelAttribute PersonInfoModel personInfoModel,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.personInfoModel", bindingResult);
            redirectAttributes.addFlashAttribute("personInfoModel", personInfoModel);
        } else {
            this.profileService.updateProfile(personInfoModel);
        }

        return "redirect:/fileBox/" + nickname + "/personal";
    }

    @PreAuthorize("hasPermission('owner', #nickname)")
    @PostMapping("/fileBox/{nickname}/update")
    public String changePassword(@PathVariable String nickname, @Valid @ModelAttribute ChangePasswordModel changePasswordModel,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changePasswordModel", bindingResult);
            redirectAttributes.addFlashAttribute("changePasswordModel", changePasswordModel);
        } else {
            this.profileService.updatePassword(changePasswordModel);
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
    @PostMapping("/fileBox/people/search")
    public String search(Model model, @RequestParam String searchField) {
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
     * @param nickname nickname of the user who owns the picture
     * @param imageModel validation model for image
     * @param bindingResult wrapper for validation errors
     * @param redirectAttributes object to enable attributes in redirection
     *
     * @return redirection back to the main page.
     */
    @PreAuthorize("hasPermission('owner', #nickname)")
    @PostMapping("/fileBox/{nickname}/setProfilePicture")
    public String setAsProfilePicture(@PathVariable String nickname, @Valid @ModelAttribute ImageModel imageModel,
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.imageModel", bindingResult);
            redirectAttributes.addFlashAttribute("imageModel", imageModel);
        } else {
            this.profileService.setProfilePicture(imageModel.getFile(), "profile picture.");
        }

        return "redirect:/fileBox/" + nickname + "/personal";
    }

    /**
     * To create new file if validation allows it.
     * This router is also preAuthorized by security to make sure user has authorization to do this.
     *
     * @param nickname nickname of the user who owns the file
     * @param downloadFile validation model for file
     * @param bindingResult wrapper for validation errors
     * @param redirectAttributes object to enable attributes in redirection
     *
     * @return redirection back to the main page.
     */
    @PreAuthorize("hasPermission('owner', #nickname)")
    @PostMapping("/fileBox/{nickname}/newFIle")
    public String saveFile(@PathVariable String nickname, @Valid @ModelAttribute DownloadFile downloadFile,
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.downloadFile", bindingResult);
            redirectAttributes.addFlashAttribute("downloadFile", downloadFile);
        } else {
            this.profileService.saveFile(downloadFile);
        }

        return "redirect:/fileBox/" + nickname;
    }

    /**
     * Deletes the file if has the access to do it of course.
     * This router is also preAuthorized by security to make sure user has authorization to do this.
     *
     * @param nickname nickname of the user who owns the file
     * @param id of the file
     *
     * @return redirection back to the main page.
     */
    @PreAuthorize("hasPermission('owner', #nickname)")
    @PostMapping("/fileBox/{nickname}/deleteFile/{id}")
    public String deleteFile(@PathVariable String nickname, @PathVariable Long id) {
        this.profileService.deleteFile(id);
        return "redirect:/fileBox/" + nickname;
    }

    /**
     * Changes status of following between user and selected person.
     *
     * @param nickname nickname of the user who is wanted to be followed or unfollowed
     *
     * @return redirection back to the main page.
     */
    @PostMapping("/fileBox/{nickname}/edit/follow")
    public String editFollow(@PathVariable String nickname) {
        this.profileService.follow(nickname);
        return "redirect:/fileBox/" + nickname;
    }

    /**
     * Lists all the followers of the person.
     *
     * @param nickname nickname of the user whose followers wanted
     *
     * @return followers page.
     */
    @GetMapping("/fileBox/{nickname}/follows")
    public String follows(Model model, @PathVariable String nickname) {
        model.addAttribute("followers", this.profileService.getFollowers(nickname));
        return "follows-page";
    }
}
