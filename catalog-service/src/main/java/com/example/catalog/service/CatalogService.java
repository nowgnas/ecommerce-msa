package com.example.catalog.service;

import com.example.catalog.jpa.CatalogEntity;

public interface CatalogService {

    Iterable<CatalogEntity> getAllCatalog();
}
