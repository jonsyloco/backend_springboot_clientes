package com.co.spring.boot.backend.apirest.models.services;

import com.co.spring.boot.backend.apirest.models.dao.IRegionesDao;
import com.co.spring.boot.backend.apirest.models.entity.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RegionServiceImpl implements IRegionService{
    @Autowired
    IRegionesDao regionesDao;


    @Override
    public List<Region> buscarRegionesTodas() {
        return regionesDao.buscarRegionesTodas();
    }

}
