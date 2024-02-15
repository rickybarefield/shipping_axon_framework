# Axon Demo Project - Shipping domain

A simple usage of Axon Framework based on shipping domain.

## Getting started

This project used devbox - see https://www.jetpack.io/devbox for installation
Once installed execute `devbox shell` to setup a dev environment

## Features

A ship can be created and readied for sailing.

Two queries - one for finding all ships and one for finding all ships ready for sailing.

## Technical Spec

REST interface

Projection which is created and stored in RDBMS using JPA.

Uses RDBMS for storing events due to axon.axonserver.enabled being set to false in application.yml

H2 in memory db server for ease of use

## Aspects to try

- [x] Commands and events
- [x] Projection
- [x] Events and Projection in RDBMS
- [ ] Adding a projection when there's old events
- [ ] Updating the aggregate
- [ ] Updating events (versioning?)
