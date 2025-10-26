package com.lhv.amlscreening.application.dto;

import java.util.List;

public record SanctionedListMatchResponse(
    boolean isMatch,
    List<SanctionedListNameResponse> matchedNames,
    String matchConfidence,
    String explanation) {}
