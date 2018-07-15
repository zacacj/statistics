package n26.statistics.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import n26.statistics.application.ApplicationConfig;
import n26.statistics.application.representation.TransactionRepresentation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
public class StatisticRestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private StatisticRestController statisticRestControllerMock;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void addTransaction() throws Exception {
        LocalDateTime datetime1 = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of("UTC");
        long epoch1 = datetime1.atZone(zoneId).toInstant().toEpochMilli();
        TransactionRepresentation transactionRepresentation = new TransactionRepresentation(
                20.1,
                epoch1
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                .content(new ObjectMapper().writeValueAsString(transactionRepresentation))
                .contentType(MediaType.APPLICATION_JSON_UTF8)

        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void addTransactionOlderThanAMinute() throws Exception {
        LocalDateTime datetime1 = LocalDateTime.now().minusMinutes(2);
        ZoneId zoneId = ZoneId.of("UTC");
        long epoch1 = datetime1.atZone(zoneId).toInstant().toEpochMilli();
        TransactionRepresentation transactionRepresentation = new TransactionRepresentation(
                20.1,
                epoch1
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                .content(new ObjectMapper().writeValueAsString(transactionRepresentation))
                .contentType(MediaType.APPLICATION_JSON_UTF8)

        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void getStatistics() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/statistics")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sum").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.avg").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.max").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.min").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").isNotEmpty());
    }
}
