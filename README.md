# Akka Examples

------


Akka is a set of open-source libraries for designing scalable, resilient systems that span processor cores and 
networks. Akka allows you to focus on meeting business needs instead of writing low-level code to provide reliable behavior, fault tolerance, and high performance.

Many common practices and accepted programming models do not address important challenges inherent in designing systems for modern computer architectures. To be successful, distributed systems must cope in an environment where components crash without responding, messages get lost without a trace on the wire, and network latency fluctuates. These problems occur regularly in carefully managed intra-datacenter environments - even more so in virtualized architectures.

To help you deal with these realities, Akka provides:

- Multithreaded behavior without the use of low-level concurrency constructs like atomics or locks — relieving you from even thinking about memory visibility issues.
- Transparent remote communication between systems and their components — relieving you from writing and maintaining difficult networking code.
- A clustered, high-availability architecture that is elastic, scales in or out, on demand — enabling you to deliver a truly reactive system.

## Main topics

- The concepts of actor design pattern
- Scheduling and timers
- Actor lifecycle methods
- Akka interaction patterns
  - The tell and forget and the request-response pattern
  - The ask pattern
- Actor Supervision
  - Watching actors
  - Dealing with actors that crash
  - Shutting down all the child actors
- Ensuring immutable state
- Actors sending messages to themselves
- Stashing messages
- Using routers for simultaneous actor operations

## Links

[Akka documentation](https://akka.io/docs/)
<br/>
[Actor Model Explained](https://www.youtube.com/watch?v=ELwEdb_pD0k)
<br/>
[Actors or Not: Async Event Architectures](https://www.youtube.com/watch?v=FM_wuZj83-8)

## Courses
[Practical Java concurrency with the Akka Actor Model](https://www.udemy.com/course/practical-java-concurrency-with-the-akka-actor-model/)
<br/>
[Practical Akka Http and Microservices](https://www.udemy.com/course/practical-akka-http-and-microservices)
<br/>
[Practical Reactive Streams with Akka and Java](https://www.udemy.com/course/practical-reactive-streams-with-akka-and-java/)


## Version

### 1.0.0

- Akka framework 2.6.17
- Junit 5.8.2
- Java 16