package com.ppl.siakngnewbe.kelas;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.jadwal.Jadwal;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;

import com.ppl.siakngnewbe.penilaian.KomponenPenilaian;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Kelas")
@JsonIgnoreProperties(value = {"kelasIrsSet", "hibernateLazyInitializer"})
public class Kelas implements Serializable {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "mataKuliah")
    @JsonBackReference
    private MataKuliah mataKuliah;

    @Column
    private String nama;

    @Column
    private int kapasitasTotal;

    @Column
    private int sks;
    
    @Column
    private int kapasitasSaatIni;

    @Column
    private String status;

    @OneToMany(mappedBy = "kelas")
    @JsonManagedReference
    private Set<Jadwal> jadwalSet;

    @OneToMany(mappedBy = "kelas")
    @JsonManagedReference
    private Set<KelasIrs> kelasIrsSet;

    @ManyToMany
    private Set<Dosen> dosenSet;

    @OneToMany(mappedBy = "kelas")
    @JsonIgnore
    private Set<KomponenPenilaian> komponenPenilaianSet;

}
