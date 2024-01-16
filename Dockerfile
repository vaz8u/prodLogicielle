# Utilisez une image Java
FROM openjdk:11-jdk

# Créez le répertoire de l'application
WORKDIR /usr/src/app

# Copiez le fichier .jar de votre application
COPY /target/MySurvey-0.0.1-SNAPSHOT.jar app.jar

# Exposez le port sur lequel votre application s'exécute
EXPOSE 8080

# Démarrez votre application
ENTRYPOINT ["java","-jar","/usr/src/app/app.jar"]


