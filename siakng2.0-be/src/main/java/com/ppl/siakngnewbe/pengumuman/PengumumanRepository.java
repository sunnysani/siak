package com.ppl.siakngnewbe.pengumuman;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PengumumanRepository extends JpaRepository<PengumumanModel, Long> {
    Optional<PengumumanModel> findById(long id);
}
