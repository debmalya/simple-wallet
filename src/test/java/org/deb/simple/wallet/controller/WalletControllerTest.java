package org.deb.simple.wallet.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.util.UUID;
import org.deb.simple.wallet.dto.CreateWalletRequest;
import org.deb.simple.wallet.dto.GetWalletRequest;
import org.deb.simple.wallet.dto.PayRequest;
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
    Integer[] cash = {2, 3, 1, 2, 1};
    String walletId = createWallet(cash);
    assertNotNull(walletId);
    UUID.fromString(walletId);
  }

  @Test
  void getExistingWallet() throws Exception {
    Integer[] cash = {2, 3, 1, 2, 1};
    String walletId = createWallet(cash);
    GetWalletRequest getWalletRequest = new GetWalletRequest();
    getWalletRequest.setWalletId(UUID.fromString(walletId));

    String requestJson = objectMapper.writeValueAsString(getWalletRequest);
    MockHttpServletResponse response =
        mockMvc
            .perform(get(String.format("/api/wallet/v0/get/%s",UUID.fromString(walletId))).contentType("application/json").content(requestJson))
            .andDo(print())
            .andExpect(jsonPath("$.message").value("current coins are [1, 1, 2, 2, 3]"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
  }

  @Test
  void payFromWallet() throws Exception {
    Integer[] cash = {2, 3, 1, 2, 1};
    String walletId = createWallet(cash);
    PayRequest payRequest = new PayRequest();
    payRequest.setWalletId(UUID.fromString(walletId));
    payRequest.setAmount(1);

    String requestJson = objectMapper.writeValueAsString(payRequest);
    MockHttpServletResponse response =
        mockMvc
            .perform(put("/api/wallet/v0/pay").contentType("application/json").content(requestJson))
            .andDo(print())
            .andExpect(
                jsonPath("$.message").value("Successfully paid 1 current coins are [1, 2, 2, 3]"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
  }

  @Test
  void getNonExistingWallet() throws Exception {
    GetWalletRequest getWalletRequest = new GetWalletRequest();
    getWalletRequest.setWalletId(UUID.fromString("58de63d9-6379-4125-9fef-8d70f7383a4e"));

    mockMvc
        .perform(
            get("/api/wallet/v0/get/58de63d9-6379-4125-9fef-8d70f7383a4e")
                .contentType("application/json"))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse();
  }

  private String createWallet(Integer[] cash) throws Exception {
    CreateWalletRequest createWalletRequest = new CreateWalletRequest();
    createWalletRequest.setCash(cash);

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
    String walletId = JsonPath.parse(response.getContentAsString()).read("$.walletId");
    return walletId;
  }
}
