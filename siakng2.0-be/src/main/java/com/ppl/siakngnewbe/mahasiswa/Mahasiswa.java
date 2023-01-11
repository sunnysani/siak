package com.ppl.siakngnewbe.mahasiswa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppl.siakngnewbe.chat.Chat;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.notifikasilonceng.NotifikasiLonceng;
import com.ppl.siakngnewbe.pendidikan.ProgramStudi;
import com.ppl.siakngnewbe.user.UserModel;
import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "mahasiswa")
public class Mahasiswa extends UserModel implements Serializable {

    @JsonIgnore
    @ManyToMany
    private Set<Chat> chatSet;

    @ManyToOne
    @JoinColumn(name = "pembimbing_akademik")
    private Dosen pembimbingAkademik;

    @Column
    private String urlFoto;

    @Column(unique = true)
    private String npm;

    @Column(nullable = false)
    private StatusAkademik status;

    @Column(nullable = false)
    private float ipk;

    @ManyToOne
    @JoinColumn(name = "programStudi")
    private ProgramStudi programStudi;

    @JsonIgnore 
    @OneToMany(mappedBy = "notifikasiMahasiswa")
    private Set<NotifikasiLonceng> notifikasiLoncengSet;
}