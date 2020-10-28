package com.co.spring.boot.backend.apirest.models.dao;

import com.co.spring.boot.backend.apirest.models.entity.Cliente;
import com.co.spring.boot.backend.apirest.models.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IRegionesDao extends JpaRepository<Region,Long> {

    @Query(value = "SELECT * FROM regiones m ", nativeQuery = true)
    List<Region> buscarRegionesTodas();

}
