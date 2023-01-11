package com.ppl.siakngnewbe.auth;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppl.siakngnewbe.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;


import java.util.HashMap;
import java.util.Map;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthServiceImpl authService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testRegisterMahasiswa() throws JsonProcessingException {

        Map<String,String> credentials = new HashMap<>();
        credentials.put("username", "eren.yeager");
        credentials.put("password", "dummy_password");
        credentials.put("userRole", "MAHASISWA");
        credentials.put("npm", "200612349876");
        credentials.put("status", "AKTIF");

        String jsonReq = mapper.writeValueAsString(credentials);

        try {
            mockMvc.perform(
                    post("/auth/register/mahasiswa")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonReq)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(handler().methodName("registerMahasiswa")).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testRegisterDosen() throws JsonProcessingException {

        Map<String,String> credentials = new HashMap<>();
        credentials.put("username", "erwin.smith");
        credentials.put("password", "dummy_password");
        credentials.put("userRole", "DOSEN");
        credentials.put("NIP", "1901090190910100191");

        String jsonReq = mapper.writeValueAsString(credentials);

        try {
            mockMvc.perform(
                    post("/auth/register/dosen")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonReq)
                            .accept(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isOk())
                    .andExpect(handler().methodName("registerDosen")).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


