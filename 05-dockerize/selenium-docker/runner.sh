#!/bin/bash

#-------------------------------------------------------------------
#  This script expects the following environment variables
#     SELENIUM_HUB_FORMAT
#     BROWSER
#     THREAD_COUNT
#     TEST_XML_FILE
#-------------------------------------------------------------------

# Let's print what we have received
echo "-------------------------------------------"
echo "SELENIUM_HUB_FORMAT      : ${SELENIUM_HUB_FORMAT:-hub}"
echo "BROWSER       : ${BROWSER:-chrome}"
echo "THREAD_COUNT  : ${THREAD_COUNT:-1}"
echo "TEST_XML_FILE    : ${TEST_XML_FILE}"
echo "-------------------------------------------"

# Do not start the tests immediately. Hub has to be ready with browser nodes
echo "Checking if hub is ready..!"
count=0
while [ "$( curl -s http://${SELENIUM_HUB_FORMAT:-hub}:4444/status | jq -r .value.ready )" != "true" ]
do
  count=$((count+1))
  echo "Attempt: ${count}"
  if [ "$count" -ge 30 ]
  then
      echo "**** HUB IS NOT READY WITHIN 30 SECONDS ****"
      exit 1
  fi
  sleep 1
done

# At this point, selenium grid should be up!
echo "Selenium Grid is up and running. Running the test...."

# Start the java command
java -cp 'libs/*' \
     -Dselenium.grid.enabled=true \
     -Dselenium.grid.hubHost="${SELENIUM_HUB_FORMAT:-hub}" \
     -Dbrowser="${BROWSER:-chrome}" \
     org.testng.TestNG \
     -threadcount "${THREAD_COUNT:-1}" \
     testng-suite-xml/"${TEST_XML_FILE}"