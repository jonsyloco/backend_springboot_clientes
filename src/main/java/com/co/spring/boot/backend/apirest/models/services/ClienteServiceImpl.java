package com.co.spring.boot.backend.apirest.models.services;

import com.co.spring.boot.backend.apirest.models.dao.IClienteDao;
import com.co.spring.boot.backend.apirest.models.dao.IRegionesDao;
import com.co.spring.boot.backend.apirest.models.entity.Cliente;
import com.co.spring.boot.backend.apirest.models.entity.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService{

    /*inyectando la dependencia del crud repository*/
    @Autowired
    IClienteDao clienteDao;

    @Autowired
    IRegionesDao regionesDao;

    @Override
    public List<Cliente> findAllClient() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    public Page<Cliente> findAllClient(Pageable paginacion) {
        return clienteDao.findAll(paginacion);
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        return clienteDao.save(cliente);
    }

    @Override
    public void eliminarCliente(Long id) {
        clienteDao.deleteById(id);

    }

    @Override
    public Cliente buscarClientePorId(Long id) {
        return clienteDao.buscarClienteId(id);
    }


}
