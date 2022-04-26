FROM openjdk:8-slim

ENV TZ=Asia/Shanghai env=dev
RUN set -eux; \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime; \
    echo $TZ > /etc/timezone

WORKDIR /app

COPY sre-test-foo-0.0.1-SNAPSHOT.jar sre-test-foo-0.0.1-SNAPSHOT.jar

EXPOSE 8900
ENTRYPOINT ["java", "-Xdebug ", "-jar", "sre-test-foo-0.0.1-SNAPSHOT.jar"]