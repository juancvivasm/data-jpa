package com.bolsadeideas.springboot.datajpa.app.models.repository;

import com.bolsadeideas.springboot.datajpa.app.models.entity.Cliente;
import com.bolsadeideas.springboot.datajpa.app.models.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c " +
            "LEFT OUTER JOIN c.facturas f " +
            "WHERE c.id=:id")
    Cliente fetchByIdWithFacturas(Long id);

}
