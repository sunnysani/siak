package com.ppl.siakngnewbe.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)

class ChatModelTest {

    private Chat chatModel = new Chat();
    private  Chat chatModel2;

    @BeforeEach
    public void setUp() throws Exception{
        chatModel.setId(1L);
        chatModel.setIsiChat("Dummy Hello");
        chatModel.setSender("MAHASISWA");
        chatModel.setReadByDosen(false);
        chatModel.setReadByMahasiswa(null);
        chatModel.setCreatedAt("2022-21-12");
        chatModel.setMahasiswaModel(null);
        chatModel.setDosenModel(null);

        chatModel2 = new Chat(null,null,"Halo Dummy","DOSEN",false,null);
    }

    @Test
    void getIDShouldReturnLong() {
        assertEquals(1L, chatModel.getId());
    }

    @Test
    void getChatReturnString() {
        assertEquals("Dummy Hello", chatModel.getIsiChat());
    }

    @Test
    void getPendaftarModelShouldReturnNull() {
        assertEquals(null, chatModel.getMahasiswaModel());
    }

    @Test
    void getRekruitmenModelShouldReturnNull() {
        assertEquals(null, chatModel.getDosenModel());
    }

    @Test
    void getCreatedDateReturnDate() {
        assertEquals("2022-21-12", chatModel.getCreatedAt());
    }

    @Test
    void getSenderReturnMahasiswa() {
        assertEquals("MAHASISWA", chatModel.getSender());
    }

    @Test
    void getReadByDosenReturnFalse() {
        assertFalse(chatModel.getReadByDosen());
    }

    @Test
    void getReadByMahasiswaReturnNull() {
        assertEquals(null, chatModel.getReadByMahasiswa());
    }
}
