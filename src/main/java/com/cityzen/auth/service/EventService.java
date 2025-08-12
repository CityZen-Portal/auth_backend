package com.cityzen.auth.service;

import com.cityzen.auth.entity.AdminEvents;
import com.cityzen.auth.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cityzen.auth.entity.User;
import com.cityzen.auth.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public AdminEvents createEvent(AdminEvents event) {
        if (event.getTitle() == null || event.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Event title cannot be empty");
        }
        if (event.getDate() == null) {
            throw new IllegalArgumentException("Event date cannot be empty");
        }
        AdminEvents savedEvent = eventRepository.save(event);
        notifyUsersAndStaff(savedEvent, "New Event Created");
        return savedEvent;
    }

    public List<AdminEvents> getEventsByAdmin(String adminId) {
        return eventRepository.findByAdminId(adminId);
    }

    public Optional<AdminEvents> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public AdminEvents updateEvent(Long id, AdminEvents eventDetails) {
        AdminEvents updatedEvent = eventRepository.findById(id)
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
        notifyUsersAndStaff(updatedEvent, "Event Updated");
        return updatedEvent;
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    private void notifyUsersAndStaff(AdminEvents event, String subject) {
        List<User> users = userRepository.findAll();
        String message = "Event: " + event.getTitle() +
                "\nDate: " + event.getDate() +
                "\nLocation: " + event.getLocation() +
                "\nDescription: " + event.getDescription();
        for (User user : users) {
            emailService.sendEmail(user.getEmail(), subject, message);
        }
    }
}