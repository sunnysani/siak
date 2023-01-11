package com.ppl.siakngnewbe.mahasiswa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MahasiswaModelRepository extends JpaRepository<Mahasiswa, String> {
    Optional<Mahasiswa> findByUsername(String username);
    Mahasiswa findByNpm(String npm);
}
