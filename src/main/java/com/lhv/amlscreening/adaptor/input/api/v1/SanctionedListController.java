package com.lhv.amlscreening.adaptor.input.api.v1;

import com.lhv.amlscreening.application.dto.SanctionedListMatchResponse;
import com.lhv.amlscreening.application.dto.SanctionedListNameRequest;
import com.lhv.amlscreening.application.dto.SanctionedListNameResponse;
import com.lhv.amlscreening.domain.service.NameMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sanctioned-list")
@RequiredArgsConstructor
public class SanctionedListController {

  private final NameMatchingService nameMatchingService;

  // TODO: add that name cannot be null
  @PostMapping("/check")
  public SanctionedListMatchResponse checkName(@RequestBody SanctionedListNameRequest request) {
    return nameMatchingService.checkName(request.fullName());
  }

  @PostMapping("/add-name")
  public SanctionedListNameResponse addName(@RequestBody SanctionedListNameRequest request) {
    return nameMatchingService.addName(request.fullName());
  }
}
