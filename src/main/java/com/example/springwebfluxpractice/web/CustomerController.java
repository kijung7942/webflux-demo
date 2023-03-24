package com.example.springwebfluxpractice.web;

import com.example.springwebfluxpractice.domain.Customer;
import com.example.springwebfluxpractice.domain.CustomerRepository;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
public class CustomerController {

    private final CustomerRepository repository;
    private final Sinks.Many<Customer> sink;


    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
        sink = Sinks.many().multicast().onBackpressureBuffer();   // A요청 나가는 중 B요청이 들어오면 stream을 합쳐서 하나의 스트림으로 merge함.
    }

    @GetMapping(value = "/customer", produces = "application/stream+json")
    public Flux<Customer> findAll(){
        return repository.findAll().delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping("/customer/{id}")
    public Mono<Customer> findById(@PathVariable Long id){
        return repository.findById(id).log();
    }

    @GetMapping(value = "/customer/sse") // 생략 가능 ServerSentEvent로 REsponse를 주기 때문에 produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Customer>> findAllSse(){
        return sink.asFlux().map(c-> ServerSentEvent.builder(c).build()).doOnCancel(()->sink.asFlux().blockLast());
    }

    @PostMapping("/customer")
    public Mono<Customer> save () {
        return repository.save(new Customer("gildong","hong")).doOnNext(sink::tryEmitNext);
    }

    @GetMapping("/flux")
    public Flux<Integer> flux() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/fluxstream", produces = "application/stream+json")
    public Flux<Integer> fluxstream() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }
}
