package java8.ex04;


import org.junit.Test;

import java8.data.Data;
import java8.data.domain.Customer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Exercice 04 - Stream
 */
public class Stream_04_Test {

    @Test
    public void test_of() throws Exception {
        // Construire un stream permettant de rendre le test passant
    	List<Customer> customers = new Data().getCustomers();
        Stream<String> result = customers.stream().map(c -> c.getFirstname()).sorted(String::compareTo);

        assertThat(result.toArray(), arrayContaining("Alexandra", "Cyril", "Johnny", "Marion", "Sophie"));
    }

    @Test
    public void test_builder() throws Exception {

        // TODO compléter pour rendre le test passant
        // TODO utiliser la méthode "add"
    	List<Customer> customers = new Data().getCustomers();
    	/* 1. Sort list of customers*/
    	Collections.sort(customers, Comparator.comparing(Customer::getFirstname));
    	/* 2. create builder */
    	Stream.Builder<Object> builder = Stream.builder();
    	/* 3. add firstname of each customer */
    	for(Customer c: customers) {
    		builder.add(c.getFirstname());
    	}
    	/* 4. build the stream object from builder */
    	 Stream<Object> result = builder.build();
        assertThat(result.toArray(), arrayContaining("Alexandra", "Cyril", "Johnny", "Marion", "Sophie"));
    }

    @Test
    public void test_concat() throws Exception {
        Stream<String> s1 = Stream.of("Alexandra", "Cyril");
        Stream<String> s2 = Stream.of("Johnny", "Marion", "Sophie");

        // TODO concatener les deux streams s1 et s2
        Stream<String> result = Stream.concat(s1, s2);

        assertThat(result.toArray(), arrayContaining("Alexandra", "Cyril", "Johnny", "Marion", "Sophie"));
    }

    @Test
    public void test_iterate() throws Exception {
        // TODO utiliser la méthode "iterate" de Stream afin de rendre le test passant
    	/* methos iterate(start,incrementation).limit(size)*/
        Stream<Integer> result1 = Stream.iterate(1,n->n+0).limit(5);
        Stream<Integer> result2 = Stream.iterate(1,n->n+1).limit(5);
        assertThat(result1.toArray(), arrayContaining(1,1,1,1,1));
        assertThat(result2.toArray(), arrayContaining(1,2,3,4,5));
    }
}
