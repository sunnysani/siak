package com.ppl.siakngnewbe.dosen;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DosenModelRepository extends JpaRepository<Dosen, String> {
    Dosen findByNip(String nip);
    List<Dosen> findAll();
}
