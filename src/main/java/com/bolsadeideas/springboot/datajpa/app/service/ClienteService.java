package com.bolsadeideas.springboot.datajpa.app.service;

import com.bolsadeideas.springboot.datajpa.app.models.dao.IProductoDao;
import com.bolsadeideas.springboot.datajpa.app.models.entity.Cliente;
import com.bolsadeideas.springboot.datajpa.app.models.entity.Producto;
import com.bolsadeideas.springboot.datajpa.app.models.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private IProductoDao productoDao;

    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    @Transactional
    public void save(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {
        return clienteRepository.findById(id).get();
    }

    @Transactional
    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Producto> findByNombre(String term){
        //return productoDao.findByNombre(term);
        return productoDao.findByNombreLikeIgnoreCase("%"+term+"%");
    }
}
