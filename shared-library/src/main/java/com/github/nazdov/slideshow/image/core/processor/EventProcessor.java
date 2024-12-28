package com.github.nazdov.slideshow.image.core.processor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public abstract class EventProcessor<T, R> {

    public static final int TTL_SECONDS = 60;

    private final Scheduler scheduler;
    private Sinks.Many<T> eventPublisher;
    private final Function<Flux<T>, Flux<R>> transformer;
    private final Consumer<R> consumer;

    /**
     * Creates a generic EventProcessor for asynchronous, queue-like event handling.
     * Will use elastic Scheduler by default
     * <p>
     * Example of simple processor that transforms Integers to strings and prints them:
     * <pre>
     * public class ExampleEventProcessor extends EventProcessor<Integer, String> {
     *
     *     public ExampleEventProcessor() {
     *         super(
     *                 integer$ -> integer$.map(Object::toString),
     *                 string -> System.out.println(string)
     *         );
     *     }
     * }
     * </pre>
     *
     * @param transformer transformations (.map(), .doOnNext()) to apply to the event before consumer consumes
     * @param consumer    function that takes the event off the queue, the final step in processing. f in .subscribe(f)
     */
    public EventProcessor(Function<Flux<T>, Flux<R>> transformer, Consumer<R> consumer) {
        this(transformer, consumer, null);
    }

    public EventProcessor(Function<Flux<T>, Flux<R>> transformer, Consumer<R> consumer, Scheduler scheduler) {
        this.transformer = transformer;
        this.consumer = consumer;
        this.scheduler = scheduler == null ? Schedulers.newBoundedElastic(
                Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE,
                Schedulers.DEFAULT_BOUNDED_ELASTIC_QUEUESIZE,
                this.getClass().getSimpleName(),
                TTL_SECONDS) : scheduler;
        initEventPublisher();
    }

    /**
     * Emits an event onto the flux with a busy loop on
     * {@link reactor.core.publisher.Sinks.EmitResult#FAIL_NON_SERIALIZED} with a default
     * deadline of 1 second
     * @param event to emit
     */
    public void process(T event) {
        Sinks.EmitResult emitResult = process(event, Duration.ofSeconds(1));
        if (emitResult.isFailure()) {
            log.error("Failed to emit event: {}, emitResult: {}", event, emitResult);
        }
    }

    /**
     * Tries to emit event with a busy loop for {
     * @link reactor.core.publisher.Sinks.EmitResult#FAIL_NON_SERIALIZED} results
     * <br/>
     * with a deadline for the event
     * @param event to emit
     * @param duration to wait for the busy loop
     * @return the {@link reactor.core.publisher.Sinks.EmitResult}
     */
    public Sinks.EmitResult process(T event, Duration duration) {
        long deadline = System.nanoTime() + duration.toNanos();

        Sinks.EmitResult emitResult;
        do {
            try {
                emitResult = this.eventPublisher.tryEmitNext(event);
                if (Sinks.EmitResult.FAIL_TERMINATED == emitResult || Sinks.EmitResult.FAIL_CANCELLED == emitResult) {
                    initEventPublisher();
                    emitResult = this.eventPublisher.tryEmitNext(event);
                }
            } catch (Exception e) {
                log.error("Fatal error processing event: {}", event, e);
                // Recreates sink
                initEventPublisher();
                emitResult = this.eventPublisher.tryEmitNext(event);
            }
            if (log.isTraceEnabled()) {
                log.trace("emitResult: {}, event: {}", emitResult, event);
            }
        } while (Sinks.EmitResult.FAIL_NON_SERIALIZED == emitResult && (System.nanoTime() < deadline));

        return emitResult;
    }


    public EventProcessor<T, R> start() {
        this.getFlux().onErrorContinue((throwable, o) -> {
            log.error("error occurred on event processor", throwable);
        }).retry().subscribe(consumer);
        return this;
    }

    public Flux<R> getFlux() {
        return this.eventPublisher.asFlux()
                .publishOn(scheduler)
                .transform(transformer)
                .doOnSubscribe(subscription -> {
                    log.debug("we have subscription: {}", subscription);
                })
                .doOnComplete(() -> {
                    log.debug("subscriber completed");
                })
                .doOnTerminate(() -> {
                    log.debug("subscriber terminated");
                })
                .doOnCancel(() -> {
                    log.debug("subscriber cancelled");
                });
    }

    public Sinks.Many<T> getEventPublisher() {
        return this.eventPublisher;
    }

    public Consumer<R> getConsumer() {
        return this.consumer;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void initEventPublisher() {
        this.eventPublisher = Sinks
                .many()
                .multicast()
                .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }
}

