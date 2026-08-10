package com.banking.operaciones.exception;

import java.time.Instant;

public record ApiErrorResponse(String mensaje, Instant fecha) {
}
