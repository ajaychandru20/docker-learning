FROM bellsoft/liberica-openjdk-alpine:21

#create Workdir on the docker container
WORKDIR home/selenium-docker

#Add the Curl and Jq for check the grid ststus
RUN apk add curl jq

#Add the local machine files to that docker container
ADD target/docker-resources     ./

#Add runner.sh for the grid condition check

ADD runner.sh                   runner.sh

# Start the runner.sh
ENTRYPOINT sh runner.sh

