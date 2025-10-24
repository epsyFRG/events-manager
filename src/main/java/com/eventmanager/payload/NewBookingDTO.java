package com.eventmanager.payload;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NewBookingDTO(
        @NotNull(message = "l'ID dell'evento è obbligatorio")
        UUID eventId) {
}