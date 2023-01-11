package com.ppl.siakngnewbe.pendidikan;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "fakultas")
public class Fakultas {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String nama;

    @OneToMany(mappedBy = "fakultas")
    @JsonManagedReference
    private Set<ProgramStudi> programStudiSet;
}
