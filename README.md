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

## To Fix

* REST Responses to commands malformed

## Aspects to try

- [x] Commands and events
- [x] Projection
- [x] Events and Projection in RDBMS
- [ ] Adding a projection when there's old events
- [ ] Updating the aggregate
- [ ] Updating events (versioning?)
- [ ] Updating a projection (replaying events) - see https://docs.axoniq.io/reference-guide/v/4.0/configuring-infrastructure-components/event-processing/event-processors#replaying-events and https://github.com/mrook/axon-projection-rebuild-demo (looks like it's based on a version of axon without the native support for replay)
- [ ] Approach to deploying updates (blue/green? what to do with queries whilst rebuilding? Flyway? see https://discuss.axoniq.io/t/best-practices-for-zero-downtime-deployments-event-replay-rebuilding-projections/5008)

## Versions

### Version One

Ships can be:

* created (`ShipCreatedEvent`)
* readied for sailing (`ShipReadyForSailingEvent`)
* sailed ('ShipSailedEvent`)
* docked (`ShipDockedEvent`)

Projections:

* SailingProject - Contains sailings which include the source (where they were before), the intended destination (taken from when the sailing in created) and an actual destination (taken from where they docked).
* ShipsProjection - Contains ships, including their name and whether they have been readied for sailing

Queries:

* FindAllShipsQuery (with option to limit to readied ships)
* FindAllSailingsQuery (with option to limit to sailings that did not arrive at the intended destination)

### Version Two

This adds an additional projection, to ensure it catches up on the missed events.

* DestinationsProjection (destination name, how many ships have docked there)

and a query for those destinations

* FindAllDestinationsQuery


## Examples

1.  POST http://localhost:8080/ships { "name": "Jolly Roger" } # Create a ship
2.  GET  http://localhost:8080/ships?onlyReadyForSailing=false # Query for all ships - returns ship
3.  GET  http://localhost:8080/ships?onlyReadyForSailing=true  # Query for all ships ready for sailing - returns no ships
4.  POST http://localhost:8080/shipReadiers { "shipId": "57f48120-fe52-4c50-92ab-d023b598e582" } # Ready ship for sailing
5.  GET  http://localhost:8080/ships?onlyReadyForSailing=true # Query for all ships ready for sailing returns ship
6.  POST http://localhost:8080/sailings { "shipId": "57f48120-fe52-4c50-92ab-d023b598e582" , "destination": "Cardiff" } # Sail the ship towards Cardiff
7.  POST http://localhost:8080/dockers { "shipId": "57f48120-fe52-4c50-92ab-d023b598e582" , "location": "Cardiff" } # Dock the ship in Cardiff
8.  POST http://localhost:8080/sailings { "shipId": "57f48120-fe52-4c50-92ab-d023b598e582" , "destination": "Newport" } # Sail the ship towards Newport
9.  POST http://localhost:8080/dockers { "shipId": "57f48120-fe52-4c50-92ab-d023b598e582" , "location": "Jersey" } # Dock the ship in Jersey
10. GET  http://localhost:8080/sailings # Query for all sailings - returns both sailings
11. GET  http://localhost:8080/sailings?onlyUnintendedDestinations=true # Query for sailings with an unintended destination - returns the sailing to Jersey 



