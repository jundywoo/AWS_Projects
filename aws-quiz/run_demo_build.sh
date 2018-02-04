#!/bin/bash

# build a latest image
docker build -t aws-quiz --build-arg JAR_FILE=target/aws-quiz-1.1-SNAPSHOT.jar .

# login to docker
# WARNING! Using --password via the CLI is insecure. Use --password-stdin.
docker login -u jundywoo -p <password>

# tag it
docker tag aws-quiz jundywoo/aws-quiz:1.1-SNAPSHOT

# push to Docker HUB public 
docker push jundywoo/aws-quiz:1.1-SNAPSHOT
