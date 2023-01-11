package com.ppl.siakngnewbe.user;


import com.ppl.siakngnewbe.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTests {
    @Mock
    private UserModelRepository userModelRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private UserModel userCreated;

    @BeforeEach
    void setUp() {
        userCreated = new UserModel();
        userCreated.setUsername("spongebob.squarepants");
        userCreated.setPassword("sasageyo");
        userCreated.setUserRole(UserModelRole.MAHASISWA);

    }

    @Test
    void loadUserByUsernameShouldThrowsUsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("spongebob.squarepants");
        });
    }

    @Test
    void loadUsernameShouldReturnUserDetails() {
        when(userModelRepository.findByUsername("spongebob.squarepants")).thenReturn(Optional.ofNullable((userCreated)));

        UserDetails result = userDetailsService.loadUserByUsername("spongebob.squarepants");
        assertEquals("spongebob.squarepants", result.getUsername());
    }
}

