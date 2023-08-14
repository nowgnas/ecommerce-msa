package com.example.catalog.controller;

import com.example.catalog.jpa.CatalogEntity;
import com.example.catalog.service.CatalogService;
import com.example.catalog.vo.ResponseCatalog;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("catalog-service")
public class CatalogController {

    private final Environment env;
    private final CatalogService catalogService;

    public CatalogController(Environment env, CatalogService catalogService) {
        this.env = env;
        this.catalogService = catalogService;
    }

    @GetMapping("health-check")
    public String status() {
        return String.format("it's working catalog service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @GetMapping("catalog")
    public ResponseEntity<List<ResponseCatalog>> getUsers() {
        Iterable<CatalogEntity> catalogAll = catalogService.getAllCatalog();
        List<ResponseCatalog> result = new ArrayList<>();

        catalogAll.forEach(
                v -> {
                    result.add(new ModelMapper().map(v, ResponseCatalog.class));
                });
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
