package com.ppl.siakngnewbe.tahunajaran;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "TahunAjaran")
@JsonIgnoreProperties(value = { "id", "irsSet", "mataKuliahSet" })
public class TahunAjaran implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String nama;

    @Column
    private int term;

    @Column
    private int active;

    @Enumerated(EnumType.STRING)
    private TahunAjaranStatus status;

    @OneToMany(mappedBy = "tahunAjaran")
    @JsonManagedReference
    private Set<IrsMahasiswa> irsSet;

    @OneToMany(mappedBy = "tahunAjaran")
    @JsonManagedReference
    private Set<MataKuliah> mataKuliahSet;

    @Column
    private Calendar isiIRSStart;

    @Column
    private Calendar isiIRSEnd;

    @Column
    private Calendar addDropIRSStart;

    @Column
    private Calendar addDropIRSEnd;

    @Column
    private Calendar pembayaranStart;

    @Column
    private Calendar pembayaranEnd;

    @Column
    private Calendar perkuliahanStart;

    @Column
    private Calendar perkuliahanEnd;

    @Column
    private Calendar isiNilaiDosenStart;

    @Column
    private Calendar isiNilaiDosenEnd;

    @Column
    private Calendar selesaiDate;
}