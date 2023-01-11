package com.ppl.siakngnewbe.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ppl.siakngnewbe.auth.AuthController;
import com.ppl.siakngnewbe.auth.AuthServiceImpl;
import com.ppl.siakngnewbe.security.config.JWTAuthenticationFilter;
import com.ppl.siakngnewbe.user.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = {AuthController.class, JWTAuthenticationFilter.class})
class JWTAuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserModelRepository userModelRepository;

    @MockBean
    private AuthServiceImpl authService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Test
    void testAuthWithJWTAndReturnUnauthorized() throws JsonProcessingException {
        Map<String,String> credentials = new HashMap<>();
        credentials.put("password", "dummy_password");
        credentials.put("username", "eren.yeager");
        String jsonReq = new ObjectMapper().writeValueAsString(credentials);


        try {
            mockMvc.perform(
                    post("/auth")
                            .content(jsonReq)
            ).andExpect(status().isUnauthorized());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
