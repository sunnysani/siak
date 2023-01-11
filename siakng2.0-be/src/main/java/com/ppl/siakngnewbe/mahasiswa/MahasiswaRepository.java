package com.ppl.siakngnewbe.mahasiswa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Long> {
    public Mahasiswa findByNpm(String npm);
}
