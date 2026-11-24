package com.college.events.web;

import com.college.events.model.Event;
import com.college.events.model.Registration;
import com.college.events.service.EventService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PublicController {
    private final EventService service;

    public PublicController(EventService service) { this.service = service; }

    @GetMapping("/")
    String home(Model m) {
        m.addAttribute("events", service.upcoming());
        return "index";
    }

    @GetMapping("/login")
    String login() { return "login"; }

    @GetMapping("/events/{id}/register")
    String form(@PathVariable Long id, Model m) {
        m.addAttribute("event", service.get(id));
        m.addAttribute("registration", new Registration());
        return "register";
    }

    @PostMapping("/events/{id}/register")
    String submit(@PathVariable Long id, @Valid @ModelAttribute("registration") Registration r,
                  BindingResult br, Model m, RedirectAttributes ra) {
        Event e = service.get(id);
        if (!br.hasErrors()) {
            try {
                service.register(id, r);
                ra.addFlashAttribute("success", "You are registered for " + e.getTitle() + ".");
                return "redirect:/";
            } catch (IllegalStateException ex) {
                br.reject("registration.failed", ex.getMessage());
            }
        }
        m.addAttribute("event", e);
        return "register";
    }
}
