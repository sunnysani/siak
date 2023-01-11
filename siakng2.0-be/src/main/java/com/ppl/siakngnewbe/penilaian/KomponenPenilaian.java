package com.ppl.siakngnewbe.penilaian;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ppl.siakngnewbe.kelas.Kelas;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "komponenPenilaian")
@Getter
@Setter
@NoArgsConstructor
public class KomponenPenilaian {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String nama;

    @Column
    private Double bobot;

    @ManyToOne
    @JoinColumn(name = "kelas")
    @JsonBackReference
    private Kelas kelas;

    @OneToMany(mappedBy = "komponenPenilaian")
    @JsonManagedReference
    private Set<Nilai> nilaiSet;

}
