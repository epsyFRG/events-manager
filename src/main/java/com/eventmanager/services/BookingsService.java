package com.eventmanager.services;

import com.eventmanager.entities.Booking;
import com.eventmanager.entities.Event;
import com.eventmanager.entities.User;
import com.eventmanager.exceptions.BadRequestException;
import com.eventmanager.exceptions.NotFoundException;
import com.eventmanager.repositories.BookingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class BookingsService {

    @Autowired
    private BookingsRepository bookingsRepository;
    @Autowired
    private EventsService eventsService;

    public Booking save(UUID eventId, User user) {
        Event event = eventsService.findById(eventId);

        if (event.getAvailableSeats() <= 0) {
            throw new BadRequestException("non ci sono più posti disponibili per questo evento");
        }

        this.bookingsRepository.findByUserAndEvent(user, event).ifPresent(booking -> {
            throw new BadRequestException("hai già prenotato un posto per questo evento");
        });

        event.setAvailableSeats(event.getAvailableSeats() - 1);

        Booking newBooking = new Booking(user, event);
        Booking savedBooking = this.bookingsRepository.save(newBooking);
        log.info("prenotazione con id: " + savedBooking.getId() + " creata correttamente");

        return savedBooking;
    }

    public List<Booking> findByUser(User user) {
        return this.bookingsRepository.findByUser(user);
    }

    public void delete(UUID bookingId, User currentUser) {
        Booking found = this.bookingsRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(bookingId));

        if (!found.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("non puoi cancellare una prenotazione che non ti appartiene");
        }

        Event event = found.getEvent();
        event.setAvailableSeats(event.getAvailableSeats() + 1);

        this.bookingsRepository.delete(found);
        log.info("p" +
                "prenotazione con id " + bookingId + " cancellata correttamente");
    }

    public Booking findById(UUID bookingId) {
        return this.bookingsRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(bookingId));
    }
}