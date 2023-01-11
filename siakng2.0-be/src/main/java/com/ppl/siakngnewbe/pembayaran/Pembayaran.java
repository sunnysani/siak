package com.ppl.siakngnewbe.pembayaran;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;

import com.ppl.siakngnewbe.tahunajaran.TahunAjaran;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "pembayaran")
@JsonIgnoreProperties(value = {"mahasiswa"})
public class Pembayaran implements Serializable {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "mahasiswa")
    private Mahasiswa mahasiswa;

    @Column
    private PembayaranStatus status;

    @Column
    private int tagihan;

    @Column
    private int totalDibayar;

    @Column
    private int denda;

    @Column
    private int semester;

    @Column
    private Calendar deadline;

    @Column
    private int tunggakan;

    @Column
    private String mataUang;

    @ManyToOne
    @JoinColumn(name = "tahunAjaran")
    private TahunAjaran tahunAjaran;
}