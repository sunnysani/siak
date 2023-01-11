package com.ppl.siakngnewbe.pembayaran;

import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PembayaranRepository extends JpaRepository<Pembayaran, String> {
    Pembayaran findFirstByMahasiswaOrderBySemesterDesc(Mahasiswa mahasiswa);
    List<Pembayaran> findByMahasiswaOrderBySemesterDesc(Mahasiswa mahasiswa);
    List<Pembayaran> findByMahasiswaOrderBySemester(Mahasiswa mahasiswa);
    List<Pembayaran> findAll();
}
