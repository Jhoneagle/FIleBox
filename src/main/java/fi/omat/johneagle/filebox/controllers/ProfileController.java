package fi.omat.johneagle.filebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.services.AccountService;

/**
 * Controller to handle routes related to profile actions that user can do after authentication which aren't called by javascript.
 * For REST API endpoints there is own specific controller to handle javascript related actions.
 */
@Controller
public class ProfileController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/fileBox/{nickname}")
    public String mainPage(Model model, @PathVariable String nickname) {
        Account owner = this.accountService.findByNickname(nickname);
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
}
