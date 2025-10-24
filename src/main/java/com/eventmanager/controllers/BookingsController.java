package com.eventmanager.controllers;

import com.eventmanager.entities.Booking;
import com.eventmanager.entities.User;
import com.eventmanager.exceptions.ValidationException;
import com.eventmanager.payload.NewBookingDTO;
import com.eventmanager.services.BookingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingsController {
    @Autowired
    private BookingsService bookingsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking createBooking(@RequestBody @Validated NewBookingDTO payload,
                                 BindingResult validationResult,
                                 @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.bookingsService.save(payload.eventId(), currentUser);
    }

    @GetMapping("/my-bookings")
    public List<Booking> getMyBookings(@AuthenticationPrincipal User currentUser) {
        return this.bookingsService.findByUser(currentUser);
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable UUID bookingId, @AuthenticationPrincipal User currentUser) {
        this.bookingsService.delete(bookingId, currentUser);
    }
}