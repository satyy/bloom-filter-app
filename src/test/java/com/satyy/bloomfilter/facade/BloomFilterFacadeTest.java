package com.satyy.bloomfilter.facade;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BloomFilterFacadeTest {

    @Value("${resource.file.path}")
    private Resource resource;

    @Autowired
    private BloomFilterFacade bloomFilterFacade;

    @Test
    public void verifyIsMightPresent() {

        try (final Stream<String> stream = Files.lines(Paths.get(resource.getURI()))) {
            stream.forEach( word -> {
                Assert.assertEquals(word + " might present",
                        bloomFilterFacade.isMightPresent(word).getLookupResponse());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}