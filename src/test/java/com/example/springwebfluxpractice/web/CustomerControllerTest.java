package com.example.springwebfluxpractice.web;

import com.example.springwebfluxpractice.domain.Customer;
import com.example.springwebfluxpractice.domain.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest
@AutoConfigureWebTestClient
class CustomerControllerTest {


    @MockBean
    private CustomerRepository repository;

    @Autowired
    private WebTestClient webTestClient; // 비동기로 http 요청

    @Test
    public void 한건찾기_테스트() {
        Mockito.when(repository.findById(1L)).thenReturn(Mono.just(new Customer("Jack", "Bauer")));

        webTestClient.get().uri("/customer/{id}", 1L).exchange()
                .expectBody()
                .jsonPath("firstName").isEqualTo("Jack")
                .jsonPath("lastName").isEqualTo("Bauer");
    }
}