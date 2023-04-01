package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.User;

/**
 * Non-authenticated requests only.
 */
@Controller
public class RootController {

    private static final Logger log = LogManager.getLogger(RootController.class);

    // Added

    @GetMapping("/signUp")
    public String signIn(Model model) {
        model.addAttribute("User", new User());
        return "signUp";
    }

    // @GetMapping("/search")
    // public String search(Model model) {
    // return "search";
    // }

    @GetMapping("/formRateEvent")
    public String formRateEvent(Model model) {
        return "formRateEvent";
    }

    @GetMapping("/formAddEvent")
    public String formAddEvent(Model model) {
        model.addAttribute("Event", new Event());
        return "formAddEvent";
    }

    // @GetMapping("/event")
    // public String event(Model model) {
    // return "event";
    // }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
}
