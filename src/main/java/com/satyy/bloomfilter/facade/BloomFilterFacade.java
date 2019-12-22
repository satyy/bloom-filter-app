package com.satyy.bloomfilter.facade;


import com.satyy.bloomfilter.model.Response;
import com.satyy.bloomfilter.util.BloomFilterUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BloomFilterFacade {

    @Autowired
    private BloomFilterUtil bloomFilterUtil;

    /**
     * Check if the value is present or not and creates the appropriate response.
     * @param value value to be checked/
     * @return response with, lookupValue and response.
     */
    public Response isMightPresent(String value){
        final Response response = new Response();
        response.setLookupKey(value);
        bloomFilterUtil.isMightPresent(value)
                .ifPresentOrElse(result -> {
                    response.setLookupResponse(result ? value + " might present" : value + " is " +
                            "not present");
                    }, () -> {
                        response.setLookupResponse("Error, bloom filter not initialized");
                    }
        );
        return response;
    }
}
