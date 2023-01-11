package com.ppl.siakngnewbe.security;

import com.ppl.siakngnewbe.security.utils.JWTAuthResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JWTAuthResponseTests {
    private JWTAuthResponse jwtAuthResponse = new JWTAuthResponse(
            "randomTokenString"
    );

    @Test
    void getTokenShoulReturnRandomAssignedString() {
        assertEquals("randomTokenString", jwtAuthResponse.getToken());
    }

    @Test
    void toStringShouldReturnJSONlikeFormat() {
        assertEquals(
                String.format(
                        "{ \"token\": \"%s\"}",
                        jwtAuthResponse.getToken()
                ),
                jwtAuthResponse.toString()
        );
    }
}

