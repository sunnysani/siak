package com.ppl.siakngnewbe.kelasirs;

import java.util.List;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KelasIrsRepository extends JpaRepository<KelasIrs, String>{
    List<KelasIrs> findByIrs(IrsMahasiswa irs);
    List<KelasIrs> findByIrsIn(List<IrsMahasiswa> listIrs);
    List<KelasIrs> findAll();
    KelasIrs findById(long id);
}
