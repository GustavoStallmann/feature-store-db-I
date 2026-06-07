package com.bd_i.feature_store.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dataset")
public class DatasetController {
    public DatasetController() {
    }

    @PostMapping()
    public String postDataset() {
        return "a";
    }
}
