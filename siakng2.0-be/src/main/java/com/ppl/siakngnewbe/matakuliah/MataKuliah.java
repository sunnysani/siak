package com.ppl.siakngnewbe.matakuliah;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaran;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "MataKuliah")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MataKuliah implements Serializable{
    
    @Id
    private String id;

    @Column
    private String nama;

    @Column
    private int sks;

    @Column
    private String term;

    @Column
    private String kurikulum;

    @ManyToMany
    @JoinTable(
        name = "prasyaratMataKuliah", 
        joinColumns = @JoinColumn(name = "mataKuliah", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "prasyarat", referencedColumnName = "id")
    )
    private Set<MataKuliah> prasyaratMataKuliahSet;

    @OneToMany(mappedBy = "mataKuliah")
    @JsonManagedReference
    private List<Kelas> kelasSet;

    @ManyToOne
    @JoinColumn(name = "tahunAjaran")
    private TahunAjaran tahunAjaran;

}
