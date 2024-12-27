package org.triBhaskar.coin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.triBhaskar.coin.entity.Coin;

import java.util.Optional;

@Repository
public interface CoinRepository extends JpaRepository<Coin, String> {
    Optional<Coin> findBySymbol(String symbol);
    Optional<Coin> findByName(String name);
    boolean existsBySymbol(String symbol);
    boolean existsByName(String name);
}
