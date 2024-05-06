The objective of this project was to develop a movie booking system. The application is organised as a set of three microservices -
user, booking and wallet, each hosting a RESTful service. Each service was developed as separate spring project with an in-memory
H2 database as the data store.

### DOCKER COMMANDS TO RUN THE SERVICES

User Service

### Build the docker image
docker build -t user-service .

### Run the docker image
docker run -p 8080:8080 --rm --name user --add-host=host.docker.internal:host-gateway user-service

### Stop the container
docker stop user

### Remove the docker image
docker image rm user-service


Booking Service

### Build the docker image
docker build -t booking-service .

### Build the docker image
docker run -p 8081:8080 --rm --name booking --add-host=host.docker.internal:host-gateway booking-service

### Stop the container
docker stop booking

### Remove the docker image
docker image rm booking-service


Wallet Service

### Build the docker image
docker build -t wallet-service .

### Build the docker image
docker run -p 8082:8080 --rm --name wallet --add-host=host.docker.internal:host-gateway wallet-service

### Stop the container
docker stop wallet

### Remove the docker image
docker image rm wallet-service
