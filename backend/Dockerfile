FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q dependency:go-offline
COPY src src
RUN ./mvnw -q package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd -r app && mkdir /diag && chown app /diag
USER app
COPY --from=build /app/target/*.jar app.jar
# Size the heap from the container limit, and leave evidence if it dies:
# a heap dump on OOM, rolling GC logs, then exit so the platform restarts it.
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/diag -XX:+ExitOnOutOfMemoryError -Xlog:gc*:file=/diag/gc.log:time,uptime:filecount=5,filesize=10m"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
