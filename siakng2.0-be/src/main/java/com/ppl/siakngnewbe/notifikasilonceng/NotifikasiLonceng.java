package com.ppl.siakngnewbe.notifikasilonceng;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;

import org.springframework.data.annotation.CreatedDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "NotifikasiLonceng")
public class NotifikasiLonceng {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String isiNotifikasi;

    @CreatedDate
    @Column
    private String createdAt;
    
    @Column
    private Boolean read;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "notifikasi_mahasiswa", nullable = true)
    private Mahasiswa notifikasiMahasiswa;

    public NotifikasiLonceng(String isiNotifikasi, Mahasiswa mahasiswa) {
        this.isiNotifikasi = isiNotifikasi;
        this.createdAt = Calendar.getInstance().getTime().toString();
        this.read = false;
        this.notifikasiMahasiswa = mahasiswa;
    }
}
