package com.ppl.siakngnewbe.security.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JWTAuthResponse {
    final String token;

    @Override
    public String toString() {
        return String.format(
                "{ \"token\": \"%s\"}",
                this.getToken()
        );
    }
}
