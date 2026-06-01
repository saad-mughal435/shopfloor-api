package dev.saadm.shopfloor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JobOrderFlowTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    void loginReturnsAToken() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"manager\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value("MANAGER"));
    }

    @Test
    void wrongPasswordIsUnauthorized() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"manager\",\"password\":\"nope\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpointRejectsAnonymous() throws Exception {
        mvc.perform(get("/api/job-orders")).andExpect(status().isUnauthorized());
    }

    @Test
    void rootServesTheLandingPage() throws Exception {
        mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "operator", roles = "OPERATOR")
    void operatorCannotCreateALine() throws Exception {
        mvc.perform(post("/api/lines").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"LINE-DENY\",\"name\":\"x\",\"ratedUnitsPerHour\":6000}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void runsAJobOrderEndToEndAndComputesOee() throws Exception {
        String line = mvc.perform(post("/api/lines").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"LINE-T1\",\"name\":\"Test Line\",\"ratedUnitsPerHour\":6000}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        long lineId = om.readTree(line).get("id").asLong();

        String job = mvc.perform(post("/api/job-orders").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\":\"JO-T1\",\"lineId\":" + lineId
                                + ",\"product\":\"Test\",\"plannedQty\":5000,\"plannedRuntimeMinutes\":60}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        long jobId = om.readTree(job).get("id").asLong();

        mvc.perform(post("/api/job-orders/" + jobId + "/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RUNNING"));

        mvc.perform(post("/api/job-orders/" + jobId + "/downtime").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"minutes\":6,\"reason\":\"jam\",\"rootCause\":\"chute\"}"))
                .andExpect(status().isOk());

        mvc.perform(post("/api/job-orders/" + jobId + "/close").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"goodUnits\":4500,\"rejectUnits\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"))
                .andExpect(jsonPath("$.downtimeMinutes").value(6))
                .andExpect(jsonPath("$.oee").exists());
    }
}
