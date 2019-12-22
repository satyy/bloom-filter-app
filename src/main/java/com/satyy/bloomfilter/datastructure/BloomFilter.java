package com.satyy.bloomfilter.datastructure;


import java.util.BitSet;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Satyam Singh
 */
public class BloomFilter<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BloomFilter.class);

    private final BitSet bitSet;
    private final int numHashFunc;
    private final int bitSetSize;

    /**
     * Two different hash functions are used to create 'n' hashfunctions where, n = numHashFunc,
     * calculated during bloomfilter object creation.
     */
    private HashFunction<T> hashFunction1 = new HashFunction<>(31);
    private HashFunction<T> hashFunction2 = new HashFunction<>(61);

    /**
     * Create bloomfilter with the following required properties.
     * @param expectedEntries expected number of entries in the bloomfilter
     * @param falsePositiveProb desired false probability
     */
    public BloomFilter(final int expectedEntries, final double falsePositiveProb) {
        this.bitSetSize = numberOfBits(expectedEntries, falsePositiveProb);
        this.bitSet = new BitSet(bitSetSize);
        this.numHashFunc = numberOfHashFunction(expectedEntries);
        LOGGER.info("Creating Bloomfilter with Hashfunctions - {} and, BitSet size - {}",
                numHashFunc, bitSetSize);
    }

    /**
     * //finds out number of bits needed during the bloomfilter creation.
     * @param expectedEntries expected number of entries in the bloomfilter
     * @param falsePositiveProb desired false probability
     * @return number of bits needed for the bloomfilter, based on configuration
     */
    private int numberOfBits(final int expectedEntries, final double falsePositiveProb) {
        return (int) (-expectedEntries * Math.log(falsePositiveProb) / (Math.log(2) * Math.log(2)));
    }

    /**
     * Calculates number of hash functions needed for the bloomfilter.
     * @param expectedEntries expected number of entries in the bloomfilter
     * @return number of hash functions
     */
    private int numberOfHashFunction(final int expectedEntries){
        return (int) ((bitSetSize / expectedEntries) * Math.log(2));
    }

    /**
     * insert an element to the bloomfilter. Calculates the bits to
     * be initialized for the element and initialize all those bits in the bitset.
     * @param object object to be inserted.
     */
    public void put(T object) {
        if (object == null){
            throw new NullPointerException("Can not insert null element");
        }

        int hashValue;
        for (int i = 1; i <= numHashFunc; i++){
            hashValue = Math.abs(hashFunction1.getHashValue(object) + (i * hashFunction2.getHashValue(object)));
            int index = Math.abs(hashValue % bitSetSize);
            bitSet.set(index);
        }
    }

    /**
     * Insert collection of elements to be inserted in the bloomfilter. Calculates the bits to
     * be initialized for each of the element and initialize all those bits in the bitset.
     * @param objects collection to initialize into bloomfilter
     */
    public void putAll(Collection<T> objects){
        objects.forEach(this::put);
    }

    /**
     * Verify if the object is present or not in the bloomfilter.
     * @param object object to verify
     * @return false (with 100% probability) if the object is not present, true if the object is
     * present (With false probabilty of falsePositiveProb, with which bloomfilter was
     * initialized)
     */
    public boolean isMightPresent(T object){
        int hashValue;
        for (int i = 1; i <= numHashFunc; i++){
            hashValue = hashFunction1.getHashValue(object) + (i * hashFunction2.getHashValue(object));
            int index = Math.abs(hashValue % bitSetSize);
            if (!bitSet.get(index))
                return false;
        }
        return true;
    }

    /**
     * Generates a hashcode for the given object. Generates different hashcode for the same
     * object based on the prime number provided.
     * @param <T> Object type.
     */
    private static class HashFunction<T> {

        private final int primeNumber;

        HashFunction(final int primeNumber) {
            this.primeNumber = primeNumber;
        }

        private int getHashValue(final T object) {
            final int hashcode = Math.abs(object.hashCode());
            return  hashcode * 59 * 43 * primeNumber;

        }
    }
}
