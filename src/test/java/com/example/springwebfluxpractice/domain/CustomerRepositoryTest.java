package com.example.springwebfluxpractice.domain;

import com.example.springwebfluxpractice.DBInit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.util.Objects;

@DataR2dbcTest
@Import(DBInit.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    public void 한건찾기() {
        StepVerifier.create(repository.findById(1L))
                .expectNextMatches(customer -> Objects.equals(customer.getFirstName(), "Jack"))
                .expectComplete()
                .verify();
    }

}