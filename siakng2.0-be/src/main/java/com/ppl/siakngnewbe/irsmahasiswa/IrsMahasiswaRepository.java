package com.ppl.siakngnewbe.irsmahasiswa;

import java.util.List;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IrsMahasiswaRepository extends JpaRepository<IrsMahasiswa, String>{
    IrsMahasiswa findFirstByMahasiswaOrderBySemesterDesc(Mahasiswa mahasiswa);
    List<IrsMahasiswa> findByMahasiswaOrderBySemesterDesc(Mahasiswa mahasiswa);
    List<IrsMahasiswa> findByMahasiswaOrderBySemester(Mahasiswa mahasiswa);
    List<IrsMahasiswa> findAll();
}
