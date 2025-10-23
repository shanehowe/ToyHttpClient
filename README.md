# ToyHttpClient

**ToyHttpClient** is, in the grand scheme of things, a fairly simple HTTP client built in **Java** using **raw sockets**.  
It was developed as a **learning exercise** while reading *Computer Networking: A Top-Down Approach*.

Initially, it was meant to do just one thing — send a single HTTP GET request and print the response.  
But, as these projects tend to go, curiosity took over… and it evolved into a HTTP/1.1 client that can:
- Send HTTP **GET** requests  
- Parse **status lines**, **headers**, and **bodies**  
- Handle **chunked transfer encoding**  
- Decode **gzip-compressed** content  
- Manage **HTTP vs HTTPS** via separate transports  
- Cleanly separate concerns using an extensible, object-oriented design  

---
## Features

- **HTTP/1.1 compliance (basic)**  
  Properly formats requests and parses responses according to the RFC.  
- **Chunked transfer decoding**  
  Handles responses with `Transfer-Encoding: chunked`.  
- **GZIP content decoding**  
  Automatically decompresses `Content-Encoding: gzip` bodies.  
- **HTTPS support**  
  Uses Java’s `SSLSocketFactory` to communicate over TLS.  
- **Extensible design**  
  Easy to add new decoders or transports (e.g., deflate, brotli).  

---
##  Design Overview

ToyHttpClient is designed around clean abstractions:

| Layer                    | Class                                                               | Responsibility                              |
| ------------------------ | ------------------------------------------------------------------- | ------------------------------------------- |
| **Request / Response**   | `ToyHttpRequest`, `ToyHttpResponse`                                 | Represent the HTTP message model            |
| **Parsing / Formatting** | `ToyHttpResponseParser`, `HttpLineWriter`                           | Handle wire-level I/O                       |
| **Transports**           | `PlainHttpTransport`, `SecureHttpTransport`, `DynamicHttpTransport` | Handle HTTP vs HTTPS                        |
| **Decoding**             | `ContentDecoder`, `GzipContentDecoder`, `IdentityContentDecoder`    | Decode body content                         |
| **Client API**           | `ToyHttpClient`                                                     | High-level entry point for sending requests |

---
## Limitations

- Only supports **GET** requests  
- Does not support **binary** or **streaming** response bodies  
- No **keep-alive** connection reuse  
- No **redirect handling**  
- No **proxy** support  

---
## Purpose

The goal of ToyHttpClient isn’t to replace production libraries like `HttpClient` or `OkHttp`.  
It’s to **understand how HTTP actually works beneath the abstractions**.

---
## Example

```java
URI uri = URI.create("https://example.com/");
ToyHttpRequest request = ToyHttpRequest.newBuilder()
    .GET()
    .uri(uri)
    .build();

ToyHttpClient client = new ToyHttpClient();
ToyHttpResponse response = client.send(request);

System.out.println(response.statusCode());
System.out.println(response.body());
```
