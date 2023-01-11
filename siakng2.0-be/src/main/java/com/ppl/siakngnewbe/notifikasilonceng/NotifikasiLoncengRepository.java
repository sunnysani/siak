package com.ppl.siakngnewbe.notifikasilonceng;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface NotifikasiLoncengRepository extends JpaRepository<NotifikasiLonceng, Long> {
    @Query("SELECT n FROM NotifikasiLonceng n where notifikasi_mahasiswa= :idMahasiswa or notifikasi_mahasiswa is null ORDER BY id DESC")
    List<NotifikasiLonceng> getAllNotifikasiMahasiswa(@Param("idMahasiswa") Long idMahasiswa);
}