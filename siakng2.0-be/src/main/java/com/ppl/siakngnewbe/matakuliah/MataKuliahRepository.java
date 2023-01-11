package com.ppl.siakngnewbe.matakuliah;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MataKuliahRepository extends JpaRepository<MataKuliah, String>{
    List<MataKuliah> findAll();
    List<MataKuliah> findAllByOrderByNamaAsc();
}