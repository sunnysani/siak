package com.ppl.siakngnewbe.dosen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.user.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "dosen")
public class Dosen extends UserModel implements Serializable {

    @JsonIgnore
    @ManyToMany(mappedBy = "chatSet", cascade = CascadeType.ALL)
    private Set<Mahasiswa> diChatOleh;

    @Column(unique = true)
    private String nip;

    @JsonIgnore
    @OneToMany(mappedBy = "pembimbingAkademik")
    private Set<Mahasiswa> mahasiswaModelSet;
}
