package com.ppl.siakngnewbe.user;


import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserModelDetailsTest {

    private final UserModel userDetails = new Mahasiswa();

    @BeforeEach
    public void setUp() {
        userDetails.setId(1L);
        userDetails.setUsername("patrick.star");
        userDetails.setPassword("admin123");
        userDetails.setUserRole(UserModelRole.MAHASISWA);
    }

    @Test
    void idShouldReturnLongIdEntity() {assertEquals(1L, userDetails.getId());
    }

    @Test
    void usernameShouldReturnString() {
        assertNotNull(userDetails.getUsername());
    }

    @Test
    void getPasswordShouldReturnAString() {
        assertNotNull(userDetails.getPassword());
    }

    @Test
    void getPasswordShouldReturnUserDetailsPassword(){
        assertEquals("admin123", userDetails.getPassword());
    }

    @Test
    void userRoleShouldHaveEnum() {
        assertNotNull(userDetails.getUserRole());
    }

    @Test
    void userRoleShouldHasMahasiswaEnum() {
        assertEquals(UserModelRole.MAHASISWA, userDetails.getUserRole());
    }

    @Test
    void getUsernameShouldNotReturnNull() {
        assertNotNull(userDetails.getUsername());
    }

    @Test
    void isAccountNotExpiredSHouldReturnTrue(){
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNotLockedShouldReturnTrue() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNotExpiredShouldReturnTrue() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnableShouldReturnTrue() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void isReturningAuthority() {
        assertEquals(userDetails.getAuthorities(), userDetails.getAuthorities());
    }

}

