# Bloom-Filter-App
-- Implementaion of `Bloom Filter` datastructure.  
-- Use of bloomfilter to perform lookup on a set of elements (present in a file).

## About Bloomfilter
Bloom filter is a probabilistic datastructure which gives only one piece of information, True or False, based on whether the key is member of set or not. It’s an incredibly space efficient datastrucuture that is often used as a first line of defense in high performance caches. 

Bloom filter takes up very small piece of memory compared to other datastructure used for identifying member, as instead of storing each value, a bloom filter is simply an array of bits i.e, BitSet, using which it identifies the presence of the key in the filter.

There is also possibility of collision when two keys may map to same index in the bit array, this makes `Bloom Filter` a `probabilistic` datastructure where `False Positives` are possible. Which means, when bloomfilter returns 'True' for the presence of an element it may or may not be present in the set but, if it returns 'False', the element is for sure not the member of the set.

When using bloom filter, this probability of False positivity has to be deal with. Based on the usecase, the occurance of false positivity can be optimized by tuning bloomfilter at the time of initialization where, speed(fast result) is directly propertional to error rate. Which means, faster we make bloomfilter, more the chance of false positive.

## Maths Behind Tuning

The major challenge is to reduce the collision in the bit array(BitSet). To overcome this problem, bloom filter often use multiple hash function for each keys and hence, set multiple bit entry corresponding to each of the hash values generated by those hash function for each key.
Also, the size of BitSet should be optimal as, having small size BitSet can increase the chance of collision.

Based on the 'number of elements' going to be in bloomfilter and the 'desired false positivity', the required 'number of hash function' and 'size of BitSet' can be estimated.

Assuming we know the values of, 
n - number of elements, 

p - desired false positivity

We can determine,
```
Size of Bitset, m = (-n * logp) / (log2)^2

Number of hash functions, k = m//n (log2)
```
Using these we can find the optimal size of BitSet and the number of hash functions at the time of initializing bloomfilter.

## Hash Functions

Sometimes hashing can be time consuming. So we need to choose hash functions which takes not much time but gives effective value as it can affect performance of bloomfilter. Also, we need to generate 'k' different hash functions where 'k' depends on entry size and false probability and hence can vary for each usecase.
To make it gereric, i have generated hash functiion with the help of paper `Less Hashing, Same Performance : Building a Better Bloom Filter` where, it has been described how 'two hash functions' can be used to genereate 'k' different hash functions.
 ```
        gi(x) = h1(x) + ih2(x);                 
            where i = 1...k,
                  h1(x) and h2(x) are two hash functions and,
                  gi(x) is the generated hash function.
 ```       
 
 ## Pre-requisite for running this project
 1. Java-11
 2. Gradle
 
 # Initialize, Build and Run
 1. Checkout repo.
 2. There is already a sample 'data.txt' file present with 40 entries in it in resource. You can modify this file and add or remove         entries.
 3. Based on the number of entries in the data.txt file and your desired false positivity, 
      - modify properties(`bloomfilter.expectedEntries and bloomfilter.falsePositiveProbability`) in `application.properties` which will         be used to initialize bloomfilter on startup of application.
 4. run command `sh run-app.sh` which will start the application.
 5. Once the application starts, bits in BitSet corresponding to genrated hash values of all the entries in 'data.txt' will be set in   
    bloomfilter and post which, lookup can be made for to verify any entry in member of bloomfilter or not.

 # Verify BloomFilter
 Once, the application starts, you can make CURL request to verify if the entry is present or not in the data.txt file. 
 Instead of performing lookup from data.txt file, application will use already initialized bloomfilter to perform lookup and respond 
 back.

```
      curl -X GET http://localhost:8081/bloomfilter/verify/{entry}  
           (replace 'entry' with the lookup value)
```

## Class file 
Implementation class of bloom filter data structure, 
   `BloomFilter.java` present at path `src/main/java/com/satyy/bloomfilter/datastructure`
 

