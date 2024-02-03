package com.example.VirtualFridge;


import com.example.VirtualFridge.dataManagerImpl.PostgresTableManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.example.VirtualFridge.dataManagerImpl.PostgresTableManager.getPostgresTableManager;

@RestController
@RequestMapping("/api/v1.0/createtable")
public class TableMappingController {
    @GetMapping("/storagev2")
    @ResponseStatus(HttpStatus.OK)
    public String createStorageV2Table() {
        final PostgresTableManager postgresTableManager =
                getPostgresTableManager();
        postgresTableManager.createTableFood();
        postgresTableManager.createTableAmount();
        postgresTableManager.createTableUnit();
        postgresTableManager.createTableAttribute();
        postgresTableManager.createTableFoodAttributeRel();
        postgresTableManager.createTableAttrValue();

        return "Database Table created";
    }

}
