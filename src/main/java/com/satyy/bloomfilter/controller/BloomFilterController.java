package com.satyy.bloomfilter.controller;


import com.satyy.bloomfilter.facade.BloomFilterFacade;
import com.satyy.bloomfilter.model.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bloomfilter")
public class BloomFilterController {

    @Autowired
    private BloomFilterFacade facade;

    @GetMapping("/verify/{data}")
    public Response checkIfPresent(@PathVariable("data") final String value){
        return facade.isMightPresent(value);
    }
}
