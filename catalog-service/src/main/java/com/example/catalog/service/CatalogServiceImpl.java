package com.example.catalog.service;

import com.example.catalog.jpa.CatalogEntity;
import com.example.catalog.jpa.CatalogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    public CatalogServiceImpl(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Override
    public Iterable<CatalogEntity> getAllCatalog() {
        return catalogRepository.findAll();
    }
}
