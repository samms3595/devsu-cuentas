# Etapa 1: Construcción
# Usa una imagen base con Maven y JDK 17 para compilar el proyecto
FROM maven:3.8.4-openjdk-17 AS build

# Copia los archivos del proyecto
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

# Establece el directorio de trabajo
WORKDIR /usr/src/app

# Compila y empaqueta la aplicación, saltándose las pruebas unitarias
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

# Etapa 2: Creación de la imagen final
# Usa una imagen base de OpenJDK 17 para ejecución
FROM openjdk:17-jdk

# Crea un directorio en el contenedor para tu aplicación
WORKDIR /app

# Copia el JAR de la etapa de construcción
COPY --from=build /usr/src/app/target/*.jar /app/app.jar

# Expone el puerto que usará tu aplicación (cámbialo según tus necesidades)
EXPOSE 9092

# Define el comando para ejecutar tu aplicación
CMD ["java", "-jar", "/app/app.jar"]