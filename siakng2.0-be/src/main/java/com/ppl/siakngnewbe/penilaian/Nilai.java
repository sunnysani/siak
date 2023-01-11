package com.ppl.siakngnewbe.penilaian;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "nilai")
@Setter
@Getter
@NoArgsConstructor
public class Nilai {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Double angka;

    @ManyToOne
    @JoinColumn(name = "komponenPenilaian")
    @JsonBackReference
    private KomponenPenilaian komponenPenilaian;

    @ManyToOne
    @JoinColumn(name = "kelasIrs")
    @JsonBackReference
    private KelasIrs kelasIrs;

}
