package com.welly.simplypay.objects;

public record ErrorMessage (
        int status,
        String message,
        long timestamp,
        String path
)
{}
