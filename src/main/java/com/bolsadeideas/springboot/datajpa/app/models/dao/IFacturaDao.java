package com.bolsadeideas.springboot.datajpa.app.models.dao;

import com.bolsadeideas.springboot.datajpa.app.models.entity.Factura;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long> {

    @Query("SELECT f FROM Factura f " +
            "JOIN f.cliente c " +
            "JOIN f.items l " +
            "JOIN l.producto WHERE f.id=:id")
    Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id);

}
