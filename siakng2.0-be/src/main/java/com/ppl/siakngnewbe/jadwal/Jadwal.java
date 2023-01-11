package com.ppl.siakngnewbe.jadwal;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ppl.siakngnewbe.kelas.Kelas;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "Jadwal")
public class Jadwal implements Serializable {
    
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "kelas")
    @JsonBackReference
    private Kelas kelas;

    @Column
    private String hari;

    @Column
    private Calendar waktuMulai;

    @Column
    private Calendar waktuSelesai;

    @Column
    private String ruang;

}
