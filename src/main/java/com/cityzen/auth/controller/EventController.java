package com.cityzen.auth.controller;

import com.cityzen.auth.entity.AdminEvents;
import com.cityzen.auth.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<AdminEvents> createEvent(@RequestBody AdminEvents event) {
        AdminEvents savedEvent = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<AdminEvents>> getEventsByAdmin(@PathVariable String adminId) {
        List<AdminEvents> events = eventService.getEventsByAdmin(adminId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminEvents> getEventById(@PathVariable Long id) {
        Optional<AdminEvents> event = eventService.getEventById(id);
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminEvents> updateEvent(
            @PathVariable Long id,
            @RequestBody AdminEvents eventDetails) {
        AdminEvents updatedEvent = eventService.updateEvent(id, eventDetails);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}