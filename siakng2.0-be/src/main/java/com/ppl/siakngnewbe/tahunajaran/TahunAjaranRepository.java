package com.ppl.siakngnewbe.tahunajaran;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TahunAjaranRepository extends JpaRepository<TahunAjaran, Integer>{

    TahunAjaran findTopByOrderByNamaDescTermDesc();
    TahunAjaran findTopByNamaAndTerm(String nama, int term);
}