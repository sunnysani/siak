package com.ppl.siakngnewbe.kelasirs;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.kelas.Kelas;

import com.ppl.siakngnewbe.penilaian.Nilai;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "KelasIrs")
@JsonIgnoreProperties(value = {"id"})
public class KelasIrs implements Serializable {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "irs")
    @JsonBackReference
    private IrsMahasiswa irs;

    @ManyToOne
    @JoinColumn(name = "kelas")
    private Kelas kelas;

    @Column
    private int posisi;

    @Column
    private double nilaiAkhir;

    @Column(columnDefinition = "varchar(255) default 'I'")
    private String nilaiHuruf;

    @JsonIgnore
    @OneToMany(mappedBy = "kelasIrs")
    private List<Nilai> nilaiList;

}
