#!/bin/bash

docker-compose -f ms_edge/src/main/docker/app.yml up -d
docker-compose -f AccountService/src/main/docker/app.yml up -d 
docker-compose -f AnalyticService/src/main/docker/app.yml up -d
docker-compose -f StatsService/src/main/docker/app.yml up -d  
docker-compose -f TaskService/src/main/docker/app.yml up -d