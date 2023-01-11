package com.ppl.siakngnewbe.pengumuman;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PengumumanModelTest {
    private PengumumanModel pengumumanModel;

    @BeforeEach
    void setUp() {
        pengumumanModel = new PengumumanModel();
        pengumumanModel.setId(1L);
        pengumumanModel.setJudul("Test Pengumuman");
        pengumumanModel.setIsi("Test");
        pengumumanModel.setPenulis("Tester");
        pengumumanModel.setWaktu(new Date());
    }

    @Test
    void testGetIdReturnTheRightObject() throws Exception {
        assertEquals(1, pengumumanModel.getId());
        assertTrue(Long.class.isInstance(pengumumanModel.getId()));
    }

    @Test
    void testGetJudulReturnTheRightObject() throws Exception {
        assertEquals("Test Pengumuman", pengumumanModel.getJudul());
        assertTrue(String.class.isInstance(pengumumanModel.getJudul()));
    }

    @Test
    void testGetIsiReturnTheRightObject() throws Exception {
        assertEquals("Test", pengumumanModel.getIsi());
        assertTrue(String.class.isInstance(pengumumanModel.getIsi()));
    }

    @Test
    void testGetPenulisReturnTheRightObject() throws Exception {
        assertEquals("Tester", pengumumanModel.getPenulis());
        assertTrue(String.class.isInstance(pengumumanModel.getPenulis()));
    }

    @Test
    void testGetDateReturnTheRightObject() throws Exception {
        assertTrue(Date.class.isInstance(pengumumanModel.getWaktu()));
    }
}
