# Multi Publish Subscribe Service

_MPSS_ (Multi Publish Subscribe Service), is a service that can be used where a distributed architecture is in place.
A subscriber can subscribe to multiple subjects, and a publisher can likewise publish to various subjects.
This can be very useful in [multi agent](https://en.wikipedia.org/wiki/Multi-agent_system), and autonomous system. There are key features the make __MPSS__ stand out.

> - Ability to publish and subscribe to different subjects at the same time with the same node
> - No strict [Interface Description Language (IDL)](https://en.wikipedia.org/wiki/Interface_description_language) declaration. (Flexibility of data to be sent and received).
> - Data is passed around using [JSON](https://www.json.org/json-en.html).

## Software Architecture

![alt=Software Architecture](./software_architecture.png)