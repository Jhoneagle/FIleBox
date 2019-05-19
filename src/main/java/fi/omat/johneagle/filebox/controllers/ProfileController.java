package fi.omat.johneagle.filebox.controllers;

import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
        } catch(Exception e) {
            name = "";
        }

        model.addAttribute("profileName", name);
        return "main-page";
    }
}
