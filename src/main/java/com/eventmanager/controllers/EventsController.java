package com.eventmanager.controllers;

import com.eventmanager.entities.Event;
import com.eventmanager.entities.User;
import com.eventmanager.exceptions.ValidationException;
import com.eventmanager.payload.NewEventDTO;
import com.eventmanager.services.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventsController {
    @Autowired
    private EventsService eventsService;

    @GetMapping
    public Page<Event> findAll(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "date") String sortBy) {
        return this.eventsService.findAll(page, size, sortBy);
    }

    @GetMapping("/my-events")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public Page<Event> findMyEvents(@AuthenticationPrincipal User currentUser,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "date") String sortBy) {
        return this.eventsService.findByOrganizer(currentUser, page, size, sortBy);
    }

    @GetMapping("/{eventId}")
    public Event findById(@PathVariable UUID eventId) {
        return this.eventsService.findById(eventId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ORGANIZER')")
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody @Validated NewEventDTO payload,
                             BindingResult validationResult,
                             @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.eventsService.save(payload, currentUser);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public Event updateEvent(@PathVariable UUID eventId,
                             @RequestBody @Validated NewEventDTO payload,
                             BindingResult validationResult,
                             @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.eventsService.findByIdAndUpdate(eventId, payload, currentUser);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable UUID eventId, @AuthenticationPrincipal User currentUser) {
        this.eventsService.findByIdAndDelete(eventId, currentUser);
    }
}