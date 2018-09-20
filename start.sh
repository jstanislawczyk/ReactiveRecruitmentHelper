#!/usr/bin/env bash
clear
printf "####################################\n"
printf "STOP AND REMOVE CONTAINERS         #"
printf "\n####################################\n\n"

printf "\nStop and remove spring app\n"
docker stop reactive-recruitment-helper
docker rm reactive-recruitment-helper

printf "\nstop and remove mongo database\n"
docker stop reactive-mongo
docker rm reactive-mongo

printf "\n\n####################################\n"
printf "REMOVE IMAGES                      #"
printf "\n####################################\n\n"

printf "remove spring app image\n"
docker rmi $(docker images --format '{{.Repository}}' | grep 'reactive-recruitment-helper')

printf "\nremove mongo image\n"
docker rmi $(docker images --format '{{.Repository}}' | grep 'reactive-mongo')

printf "\nremove openjdk image\n"
docker rmi $(docker images --format '{{.Repository}}' | grep 'openjdk')

printf "\n\n####################################\n"
printf "PULL IMAGES                        #"
printf "\n####################################\n\n"

printf "\npull openjdk\n"
docker pull openjdk:10

printf "\npull mongo\n"
docker pull mongo

printf "\n\n####################################\n"
printf "BUILD FROM DOCKERFILE              #"
printf "\n####################################\n\n"
docker build -f Dockerfile -t reactive-recruitment-helper .

printf "\n\n####################################\n"
printf "RUN CONTAINERS                     #"
printf "\n####################################\n\n"

printf "\nrun mongo\n"
docker run --name reactive-mongo -p 27018:27017 -d mongo

printf "\nrun spring app\n"
docker run --name reactive-recruitment-helper -p 8090:8090 reactive-recruitment-helper