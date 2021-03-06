package com.co.spring.boot.backend.apirest.models.dao;

import com.co.spring.boot.backend.apirest.models.entity.Cliente;
import com.co.spring.boot.backend.apirest.models.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IClienteDao extends JpaRepository<Cliente,Long> {

    @Query(value = "SELECT * FROM cliente m WHERE m.id = :id", nativeQuery = true)
    Cliente buscarClienteId( @Param("id") Long id);


}
