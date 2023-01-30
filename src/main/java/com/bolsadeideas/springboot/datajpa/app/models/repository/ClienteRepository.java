package com.bolsadeideas.springboot.datajpa.app.models.repository;

import com.bolsadeideas.springboot.datajpa.app.models.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
