package org.triBhaskar.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.triBhaskar.auth.entity.CoinUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CoinUser, Long> {
    Optional<CoinUser> findByEmail(String email);
    Optional<CoinUser> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
