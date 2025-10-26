package com.lhv.amlscreening.application.dto;

import jakarta.validation.constraints.NotBlank;

public record SanctionedListNameRequest(
    @NotBlank(message = "Full name must not be blank") String fullName) {}
