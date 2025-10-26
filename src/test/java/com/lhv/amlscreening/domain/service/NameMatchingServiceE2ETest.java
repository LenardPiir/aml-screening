package com.lhv.amlscreening.domain.service;

import static com.lhv.amlscreening.testhelpers.fixtures.CommonTestFixtures.A_FULL_NAME_OSAMA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhv.amlscreening.adaptor.input.api.SanctionedListApi;
import com.lhv.amlscreening.application.dto.SanctionedListNameRequest;
import com.lhv.amlscreening.testhelpers.base.ABaseE2ETest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public class NameMatchingServiceE2ETest extends ABaseE2ETest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  public void testCheckName() throws Exception {
    SanctionedListNameRequest request = new SanctionedListNameRequest(A_FULL_NAME_OSAMA);
    String requestJson = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(
            post(SanctionedListApi.ROOT_SANCTIONED_LIST_V1 + SanctionedListApi.PATH_CHECK_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isMatch").value(true))
        .andExpect(jsonPath("$.matchedNames[0].fullName").value("Ben Laden Osama"));
  }
}
