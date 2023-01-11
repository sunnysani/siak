package com.ppl.siakngnewbe.irsmahasiswa;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaran;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "IrsMahasiswa")
@JsonIgnoreProperties(value = {"mahasiswa"})
public class IrsMahasiswa implements Serializable {

    @Id
    private String idIrs;

    @ManyToOne
    @JoinColumn(name = "mahasiswa")
    private Mahasiswa mahasiswa;

    @Column
    private int semester;

    @Enumerated(EnumType.STRING)
    private PersetujuanIRSStatus statusPersetujuan;

    @Column
    private boolean nilaiDefined;

    @Column
    private double totalMutu;

    @Column
    private int sksa;

    @Column
    private int sksl;

    @OneToMany(mappedBy = "irs")
    @JsonManagedReference
    private Set<KelasIrs> kelasIrsSet;

    @ManyToOne
    @JoinColumn(name = "tahunAjaran")
    private TahunAjaran tahunAjaran;

    public double getIps() {
        return sksa == 0 ? 0 : totalMutu / sksa;
    }
}