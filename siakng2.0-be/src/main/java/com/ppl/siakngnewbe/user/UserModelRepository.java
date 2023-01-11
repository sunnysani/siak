package com.ppl.siakngnewbe.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserModelRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);
}
