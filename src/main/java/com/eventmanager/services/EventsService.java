package com.eventmanager.services;

import com.eventmanager.entities.Event;
import com.eventmanager.entities.User;
import com.eventmanager.exceptions.BadRequestException;
import com.eventmanager.exceptions.NotFoundException;
import com.eventmanager.payload.NewEventDTO;
import com.eventmanager.repositories.EventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    public Page<Event> findAll(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.eventsRepository.findAll(pageable);
    }

    public Event save(NewEventDTO payload, User organizer) {
        Event newEvent = new Event(payload.title(), payload.description(), payload.date(), payload.location(), payload.totalSeats(), organizer);
        Event savedEvent = this.eventsRepository.save(newEvent);
        log.info("l'evento con id: " + savedEvent.getId() + " è stato salvato correttamente");
        return savedEvent;
    }

    public Event findById(UUID eventId) {
        return this.eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
    }

    public Event findByIdAndUpdate(UUID eventId, NewEventDTO payload, User currentUser) {
        Event found = this.findById(eventId);

        if (!found.getOrganizer().getId().equals(currentUser.getId())) {
            throw new BadRequestException("non puoi modificare un evento che non hai creato");
        }

        int bookedSeats = found.getTotalSeats() - found.getAvailableSeats();
        if (payload.totalSeats() < bookedSeats) {
            throw new BadRequestException("non puoi ridurre i posti a meno di quelli già prenotati (" + bookedSeats + ")");
        }

        found.setTitle(payload.title());
        found.setDescription(payload.description());
        found.setDate(payload.date());
        found.setLocation(payload.location());
        found.setAvailableSeats(payload.totalSeats() - bookedSeats);
        found.setTotalSeats(payload.totalSeats());

        Event modifiedEvent = this.eventsRepository.save(found);
        log.info("l'evento con id " + modifiedEvent.getId() + " è stato modificato correttamente");

        return modifiedEvent;
    }

    public void findByIdAndDelete(UUID eventId, User currentUser) {
        Event found = this.findById(eventId);

        if (!found.getOrganizer().getId().equals(currentUser.getId())) {
            throw new BadRequestException("non puoi eliminare un evento che non hai creato");
        }

        this.eventsRepository.delete(found);
        log.info("l'evento con id " + eventId + " è stato eliminato correttamente");
    }

    public Page<Event> findByOrganizer(User organizer, int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.eventsRepository.findByOrganizer(organizer, pageable);
    }
}