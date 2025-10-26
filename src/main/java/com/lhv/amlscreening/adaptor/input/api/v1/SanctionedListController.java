package com.lhv.amlscreening.adaptor.input.api.v1;

import com.lhv.amlscreening.adaptor.input.api.SanctionedListApi;
import com.lhv.amlscreening.application.dto.SanctionedListMatchResponse;
import com.lhv.amlscreening.application.dto.SanctionedListNameRequest;
import com.lhv.amlscreening.application.dto.SanctionedListNameResponse;
import com.lhv.amlscreening.domain.service.NameMatchingService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = SanctionedListApi.ROOT_SANCTIONED_LIST_V1)
@RequiredArgsConstructor
public class SanctionedListController {

  private final NameMatchingService nameMatchingService;

  @PostMapping(SanctionedListApi.PATH_CHECK_NAME)
  public SanctionedListMatchResponse checkName(
      @Valid @RequestBody SanctionedListNameRequest request) {
    return nameMatchingService.checkName(request.fullName());
  }

  @PostMapping(SanctionedListApi.PATH_ADD_NAME)
  public SanctionedListNameResponse addName(@RequestBody SanctionedListNameRequest request) {
    return nameMatchingService.addName(request.fullName());
  }

  @PutMapping(SanctionedListApi.PATH_UPDATE_NAME + "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public SanctionedListNameResponse updateName(
      @PathVariable("id") UUID id, @RequestBody SanctionedListNameRequest request) {
    return nameMatchingService.updateName(id, request.fullName());
  }

  @DeleteMapping(SanctionedListApi.PATH_DELETE_NAME + "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteName(@PathVariable("id") UUID id) {
    nameMatchingService.deleteName(id);
  }
}
