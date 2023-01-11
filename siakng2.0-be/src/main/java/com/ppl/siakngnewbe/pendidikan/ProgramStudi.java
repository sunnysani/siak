package com.ppl.siakngnewbe.pendidikan;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "programStudi")
public class ProgramStudi {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String nama;

    @Enumerated(EnumType.STRING)
    private ProgramPendidikan programPendidikan;

    @ManyToOne
    @JoinColumn(name = "fakultas")
    @JsonBackReference
    private Fakultas fakultas;

    @OneToMany(mappedBy = "programStudi")
    @JsonIgnore
    private Set<Mahasiswa> mahasiswaSet;

    @Override
    public String toString() {
        return nama + ", " + programPendidikan;
    }
}
