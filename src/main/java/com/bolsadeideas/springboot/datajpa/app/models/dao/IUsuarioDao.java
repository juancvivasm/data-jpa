package com.bolsadeideas.springboot.datajpa.app.models.dao;

import com.bolsadeideas.springboot.datajpa.app.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    public Usuario findByUsername(String usernname);

}
