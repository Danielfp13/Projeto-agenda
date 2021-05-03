package com.api.agenda.repositories;

import com.api.agenda.domain.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository extends JpaRepository<Contato,Integer> {

    boolean existsByEmail(String email);

    Contato findByEmail(String email);
}
