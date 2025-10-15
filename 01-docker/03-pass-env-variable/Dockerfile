FROM alpine
RUN apk add --no-cache openjdk8
ENV PATH=$PATH:/usr/lib/jvm/java-1.8-openjdk/bin/
WORKDIR /usr/share/udemy
ADD Test.java Test.java
ENTRYPOINT ["sh", "-c", "javac Test.java && java Test $NUMBER"]