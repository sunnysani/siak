package com.ppl.siakngnewbe.security;


import com.ppl.siakngnewbe.security.utils.UsernamePasswordAuthRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsernamePasswordAuthRequestTests {
    private UsernamePasswordAuthRequest emailPasswordAuthRequest = new UsernamePasswordAuthRequest(
            "hoover.bertholt",
            "RUMBLIIING"
    );

    private UsernamePasswordAuthRequest emptyAuthRequest = new UsernamePasswordAuthRequest();

    @Test
    void getUsernameShouldReturnUsername() {
        assertEquals("hoover.bertholt", emailPasswordAuthRequest.getUsername());
    }

    @Test
    void getPasswordShouldReturnPassword() {
        assertEquals("RUMBLIIING", emailPasswordAuthRequest.getPassword());
    }

    @Test
    void setEmailPasswordEmptyAuthRequestThenGetEmailPassword() {
        emptyAuthRequest.setUsername("hoover.bertholt");
        emptyAuthRequest.setPassword("RUMBLIIING");
        assertEquals("hoover.bertholt", emptyAuthRequest.getUsername());
        assertEquals("RUMBLIIING", emptyAuthRequest.getPassword());
    }
}
