FROM gradle:latest

RUN mkdir /sd-language
COPY --chown=gradle:gradle . /sd-language
WORKDIR /sd-language

ENTRYPOINT ["./gradlew", "build"]
