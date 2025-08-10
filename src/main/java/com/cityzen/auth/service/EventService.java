package com.cityzen.auth.service;

import com.cityzen.auth.entity.AdminEvents;
import com.cityzen.auth.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public AdminEvents createEvent(AdminEvents event) {
        if (event.getTitle() == null || event.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Event title cannot be empty");
        }
        if (event.getDate() == null) {
            throw new IllegalArgumentException("Event date cannot be empty");
        }
        return eventRepository.save(event);
    }

    public List<AdminEvents> getEventsByAdmin(String adminId) {
        return eventRepository.findByAdminId(adminId);
    }

    public Optional<AdminEvents> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public AdminEvents updateEvent(Long id, AdminEvents eventDetails) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    if (eventDetails.getTitle() != null) {
                        existingEvent.setTitle(eventDetails.getTitle());
                    }
                    if (eventDetails.getDate() != null) {
                        existingEvent.setDate(eventDetails.getDate());
                    }
                    if (eventDetails.getStartTime() != null) {
                        existingEvent.setStartTime(eventDetails.getStartTime());
                    }
                    if (eventDetails.getEndTime() != null) {
                        existingEvent.setEndTime(eventDetails.getEndTime());
                    }
                    if (eventDetails.getLocation() != null) {
                        existingEvent.setLocation(eventDetails.getLocation());
                    }
                    if (eventDetails.getDescription() != null) {
                        existingEvent.setDescription(eventDetails.getDescription());
                    }
                    return eventRepository.save(existingEvent);
                })
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }
}