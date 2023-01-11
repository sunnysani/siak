package com.ppl.siakngnewbe.jadwal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JadwalRepository extends JpaRepository<Jadwal, String>{

}