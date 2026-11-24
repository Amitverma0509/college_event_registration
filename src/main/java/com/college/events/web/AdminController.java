package com.college.events.web;

import com.college.events.model.Event;
import com.college.events.model.Registration;
import com.college.events.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final EventService service;

    public AdminController(EventService service) { this.service = service; }

    @GetMapping
    String dashboard(Model m) {
        m.addAttribute("events", service.all());
        return "admin/dashboard";
    }

    @GetMapping("/events/new")
    String newForm(Model m) {
        m.addAttribute("event", new Event());
        return "admin/event-form";
    }

    @GetMapping("/events/{id}/edit")
    String edit(@PathVariable Long id, Model m) {
        m.addAttribute("event", service.get(id));
        return "admin/event-form";
    }

    @PostMapping("/events")
    String save(@Valid @ModelAttribute("event") Event event, BindingResult br) {
        if (br.hasErrors()) return "admin/event-form";
        service.save(event);
        return "redirect:/admin";
    }

    @PostMapping("/events/{id}/delete")
    String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/events/{id}/registrations")
    String registrations(@PathVariable Long id, Model m) {
        m.addAttribute("event", service.get(id));
        m.addAttribute("registrations", service.registrationsFor(id));
        return "admin/registrations";
    }

    @PostMapping("/registrations/{id}/delete")
    String cancel(@PathVariable Long id) {
        Long eventId = service.cancel(id);
        return "redirect:/admin/events/" + eventId + "/registrations";
    }

    @GetMapping("/events/{id}/registrations.csv")
    ResponseEntity<String> csv(@PathVariable Long id) {
        StringBuilder sb = new StringBuilder("Name,Email,Roll No,Branch,Registered At\n");
        for (Registration r : service.registrationsFor(id)) {
            sb.append(esc(r.getStudentName())).append(',').append(esc(r.getEmail())).append(',')
              .append(esc(r.getRollNo())).append(',').append(esc(r.getBranch())).append(',')
              .append(r.getRegisteredAt()).append('\n');
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=registrations-" + id + ".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(sb.toString());
    }

    private String esc(String s) { return "\"" + (s == null ? "" : s.replace("\"", "\"\"")) + "\""; }
}
