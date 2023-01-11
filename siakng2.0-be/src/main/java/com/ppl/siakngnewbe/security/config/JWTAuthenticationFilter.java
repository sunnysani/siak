package com.ppl.siakngnewbe.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.user.UserModel;
import com.ppl.siakngnewbe.security.utils.JWTAuthResponse;
import com.ppl.siakngnewbe.security.utils.UsernamePasswordAuthRequest;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.EXPIRATION_TIME;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.SECRET;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/auth");
    }


    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        UsernamePasswordAuthRequest creds = new ObjectMapper().readValue(
                req.getInputStream(),
                UsernamePasswordAuthRequest.class
        );
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.getUsername(),
                        creds.getPassword()
                )
        );

    }

    @Override
    public void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis()+EXPIRATION_TIME);

        String token;
        if (((UserModel) auth.getPrincipal()).getUserRole().name().equals("MAHASISWA")) {
            token = JWT.create().withSubject(((UserModel) auth.getPrincipal()).getUsername()).withClaim("role",((UserModel) auth.getPrincipal()).getUserRole().name()).withClaim("npm",((Mahasiswa) auth.getPrincipal()).getNpm()).withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC512(SECRET.getBytes()));
        } else {
            token = JWT.create().withSubject(((UserModel) auth.getPrincipal()).getUsername()).withClaim("role",((UserModel) auth.getPrincipal()).getUserRole().name()).withClaim("nip",((Dosen) auth.getPrincipal()).getNip()).withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC512(SECRET.getBytes()));
        }

        var jwtAuthResponse = new JWTAuthResponse(token);

        res.addHeader("Access-Control-Expose-Headers", "*");
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(jwtAuthResponse.toString());
        res.getWriter().flush();
    }

}
