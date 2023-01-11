package com.ppl.siakngnewbe.pengumuman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "pengumuman")
public class PengumumanModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "judul")
    private String judul;

    @Column(name = "isi", columnDefinition = "TEXT")
    private String isi;

    @Column(name = "penulis")
    private String penulis;

    @Column(name = "waktu")
    private Date waktu;
}
