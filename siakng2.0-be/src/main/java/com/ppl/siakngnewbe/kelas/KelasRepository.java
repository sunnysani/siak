package com.ppl.siakngnewbe.kelas;

import java.util.List;

import com.ppl.siakngnewbe.matakuliah.MataKuliah;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KelasRepository extends JpaRepository<Kelas, String>{
    List<Kelas> findByMataKuliahOrderByNamaAsc(MataKuliah mataKuliah);
}