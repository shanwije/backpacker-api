#Backpacker

sudo docker-compose up --remove-orphans --build


#http://localhost:8080/swagger-resources
#http://localhost:8080/v2/api-docs
#http://localhost:8080/swagger-ui/index.html

for access logging
mvn spring-boot:run -Dreactor.netty.http.server.accessLogEnabled=true


# Build Project

- `docker run --rm -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle:5.4.1-jdk11 gradle build`: Build the project using a Gradle Docker and generate the JAR file at build/libs/project.jar

# Run Project

- `docker-compose up -d`: Create and Start Docker Instances;
- `docker ps`: Check docker instances, there are one instance **spring-app** and other **spring-mongodb**;
- `http://localhost:8080/hello`: Test the Hello World!!!

# Rebuild the Project after changes
- `docker-compose stop`: Stop all Instances from docker-compose.yml file;
- `docker rm spring-app`: Delete the App Docker Instance;
- `docker rmi springboot-webflux-docker_springboot`: Delete The App Docker Image;
- `Build Project`;
- `Run Project`.


# Dockers

### Docker Images
- `gradle`: Image with Gradle;
- `openjdk`: Image with Java JDK 11;
- `demo_springboot`: Image based on `openjdk` that contain the JAR file;
- `mongo`: Image with MongoDB.

### Docker Instances
- `spring-app`: Running the `demo_springboot` image. There are a service with Netty Server running the JAR file at port 8080;
- `spring-mongo`: Running the `mongo` image. There are a service with Mongo DB Instance at port 27017.

### Docker Commands
- `docker ps`: show all Docker Instances running;
- `docker ps -a`: show all Docker Instances;
- `docker images`: show all Images;
- `docker-compose stop`: Stop all Instances from docker-compose.yml file;



