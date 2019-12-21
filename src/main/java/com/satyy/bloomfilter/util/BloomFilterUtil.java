package com.satyy.bloomfilter.util;

import com.satyy.bloomfilter.config.BloomFilterProperties;
import com.satyy.bloomfilter.datastructure.BloomFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class BloomFilterUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(BloomFilterUtil.class);

    @Value("${resource.file.path}")
    private Resource resource;

    @Autowired
    private BloomFilterProperties configuration;

    private BloomFilter<String> bloomFilter;

    public BloomFilterUtil() {
    }

    @PostConstruct
    private void initializeFilter() {

        try (final Stream<String> stream = new BufferedReader(new InputStreamReader(resource.getInputStream())).lines()) {
            //create a bloomfilter object with properties, expected number of entries and
            // desired false probability.
            bloomFilter = new BloomFilter<>(configuration.getExpectedEntries(),
                    Double.parseDouble(configuration.getFalsePositiveProbability()));

            //initialize bloomfilter with all the words present in the file.
            stream.forEach(bloomFilter::put);
            LOGGER.info("Bloom filter initialized");
        } catch (IOException e) {
            LOGGER.error("Error while initializing bloomfilter, {}", e.getMessage());
        }
    }

    public Optional<Boolean> isMightPresent(String value){
        Optional<Boolean> result;
        if (bloomFilter != null){
            result = Optional.of(bloomFilter.isMightPresent(value));
        } else {
          result = Optional.empty();
        }
        return result;
    }
}
