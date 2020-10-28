package com.co.spring.boot.backend.apirest.models.services;

import com.co.spring.boot.backend.apirest.models.entity.Region;

import java.util.List;

public interface IRegionService {
    public List<Region> buscarRegionesTodas();
}
