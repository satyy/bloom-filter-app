package com.satyy.bloomfilter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "bloomfilter")
public class BloomFilterProperties {

    private String falsePositiveProbability;
    private int expectedEntries;

}
