package com.ppl.siakngnewbe.chat;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;

import org.springframework.data.annotation.CreatedDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "chat")
public class Chat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "TEXT")
    private String isiChat;
    
    @Column
    private String sender;

    @Column
    private Boolean readByMahasiswa;

    @Column
    private Boolean readByDosen;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private String createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "nip")
    private Dosen dosenModel;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "npm")
    private Mahasiswa mahasiswaModel;

    public Chat(Mahasiswa mahasiswa, Dosen dosen, String chat, String sender, Boolean readByMahasiswa,Boolean readByDosen) {
        this.isiChat = chat;
        this.dosenModel = dosen;
        this.mahasiswaModel = mahasiswa;
        this.sender = sender;
        this.createdAt = Calendar.getInstance().getTime().toString();
        this.readByMahasiswa = readByMahasiswa;
        this.readByDosen = readByDosen;
    }
}
