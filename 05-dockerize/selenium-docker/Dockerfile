FROM bellsoft/liberica-openjdk-alpine:21

#create Workdir on the docker container
WORKDIR home/selenium-docker

#Add the local machine files to that docker container
ADD target/docker-resources ./

# Environment variables
# BROWSER
# SELENIUM_GRID_ENABLED
# SELENIUM_HUB_FORMAT
# THREAD_COUNT
# TEST_XML_FILE

#Add Entry points
ENTRYPOINT java -cp 'libs/*' \
           -Dselenium.grid.enabled=true \
           -Dbrowser=${BROWSER} \
           -Dselenium.grid.hubHost=${SELENIUM_HUB_FORMAT} \
           org.testng.TestNG \
           -threadcount=${THREAD_COUNT} \
           testng-suite-xml/${TEST_XML_FILE}