package com.example.cftcbrandtech.Exceptions;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, int status, int code, String message, String error) {
}