package org.deb.simple.wallet.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.deb.simple.wallet.dto.CreateWalletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {

  @Autowired private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void createWallet() throws Exception {
    Integer[] coins = {2, 3, 1, 2, 1};
    CreateWalletRequest createWalletRequest = new CreateWalletRequest();
    createWalletRequest.setCoins(coins);

    String requestJson = objectMapper.writeValueAsString(createWalletRequest);
    MockHttpServletResponse response =
        mockMvc
            .perform(
                post("/api/wallet/v0/create").contentType("application/json").content(requestJson))
            .andDo(print())
            .andExpect(jsonPath("$.message").value("Success"))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse();
  }
}
