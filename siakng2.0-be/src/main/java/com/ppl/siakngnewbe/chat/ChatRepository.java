package com.ppl.siakngnewbe.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    Optional<Chat> findById(long id);

    @Query("SELECT c FROM chat c where nip= :idDosen and npm = :idMahasiswa ORDER BY id ASC ")
    List<Chat> findChatByMahasiswaAndDosen(@Param("idDosen") Long idDosen, @Param("idMahasiswa") Long idMahasiswa);
}
