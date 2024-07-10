# Dependencias del Proyecto

Este proyecto requiere varias librerías externas. A continuación se presentan los pasos para instalar estas dependencias usando Maven.

## Prerrequisitos

- Kit de Desarrollo de Java (JDK) 21 o superior
- Apache Maven 3.6.3 o superior

## Instalación

1. **Instalar JDK 21**

   Descarga e instala JDK 21 desde el [sitio web de Oracle](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html) o usa tu gestor de paquetes si estás en Linux.

2. **Instalar Apache Maven**

   Descarga e instala Apache Maven desde el [sitio web de Maven](https://maven.apache.org/download.cgi).

3. **Agregar Dependencias a `pom.xml`**

   Agrega las siguientes dependencias a tu archivo `pom.xml`:

   ```xml
   <dependencies>
       <dependency>
           <groupId>com.fasterxml</groupId>
           <artifactId>classmate</artifactId>
           <version>1.5.1</version>
       </dependency>
       <dependency>
           <groupId>com.google.protobuf</groupId>
           <artifactId>protobuf-java</artifactId>
           <version>3.19.4</version>
       </dependency>
       <dependency>
           <groupId>com.sun.activation</groupId>
           <artifactId>jakarta.activation</artifactId>
           <version>2.0.1</version>
       </dependency>
       <dependency>
           <groupId>com.sun.istack</groupId>
           <artifactId>istack-commons-runtime</artifactId>
           <version>4.0.1</version>
       </dependency>
       <dependency>
           <groupId>jakarta.inject</groupId>
           <artifactId>jakarta.inject-api</artifactId>
           <version>2.0.0</version>
       </dependency>
       <dependency>
           <groupId>jakarta.persistence</groupId>
           <artifactId>jakarta.persistence-api</artifactId>
           <version>3.0.0</version>
       </dependency>
       <dependency>
           <groupId>jakarta.servlet</groupId>
           <artifactId>jakarta.servlet-api</artifactId>
           <version>5.0.0</version>
       </dependency>
       <dependency>
           <groupId>jakarta.transaction</groupId>
           <artifactId>jakarta.transaction-api</artifactId>
           <version>2.0.0</version>
       </dependency>
       <dependency>
           <groupId>jakarta.xml.bind</groupId>
           <artifactId>jakarta.xml.bind-api</artifactId>
           <version>3.0.1</version>
       </dependency>
       <dependency>
           <groupId>mysql</groupId>
           <artifactId>mysql-connector-java</artifactId>
           <version>8.0.30</version>
       </dependency>
       <dependency>
           <groupId>net.bytebuddy</groupId>
           <artifactId>byte-buddy</artifactId>
           <version>1.12.9</version>
       </dependency>
       <dependency>
           <groupId>org.antlr</groupId>
           <artifactId>antlr4-runtime</artifactId>
           <version>4.10</version>
       </dependency>
       <dependency>
           <groupId>org.apiguardian</groupId>
           <artifactId>apiguardian-api</artifactId>
           <version>1.1.2</version>
       </dependency>
       <dependency>
           <groupId>org.glassfish.jaxb</groupId>
           <artifactId>jaxb-core</artifactId>
           <version>3.0.2</version>
       </dependency>
       <dependency>
           <groupId>org.glassfish.jaxb</groupId>
           <artifactId>jaxb-runtime</artifactId>
           <version>3.0.2</version>
       </dependency>
       <dependency>
           <groupId>org.glassfish.jaxb</groupId>
           <artifactId>txw2</artifactId>
           <version>3.0.2</version>
       </dependency>
       <dependency>
           <groupId>org.hibernate.common</groupId>
           <artifactId>hibernate-commons-annotations</artifactId>
           <version>6.0.1.Final</version>
       </dependency>
       <dependency>
           <groupId>org.hibernate.orm</groupId>
           <artifactId>hibernate-core</artifactId>
           <version>6.0.2.Final</version>
       </dependency>
       <dependency>
           <groupId>org.jboss.logging</groupId>
           <artifactId>jboss-logging</artifactId>
           <version>3.4.3.Final</version>
       </dependency>
       <dependency>
           <groupId>org.jboss</groupId>
           <artifactId>jandex</artifactId>
           <version>2.4.2.Final</version>
       </dependency>
       <dependency>
           <groupId>org.json</groupId>
           <artifactId>json</artifactId>
           <version>20200518</version>
       </dependency>
       <dependency>
           <groupId>org.junit.jupiter</groupId>
           <artifactId>junit-jupiter-api</artifactId>
           <version>5.9.2</version>
       </dependency>
       <dependency>
           <groupId>org.junit.jupiter</groupId>
           <artifactId>junit-jupiter-engine</artifactId>
           <version>5.9.2</version>
       </dependency>
       <dependency>
           <groupId>org.junit.platform</groupId>
           <artifactId>junit-platform-commons</artifactId>
           <version>1.9.2</version>
       </dependency>
       <dependency>
           <groupId>org.junit.platform</groupId>
           <artifactId>junit-platform-engine</artifactId>
           <version>1.9.2</version>
       </dependency>
       <dependency>
           <groupId>org.opentest4j</groupId>
           <artifactId>opentest4j</artifactId>
           <version>1.2.0</version>
       </dependency>
   </dependencies>
3. **Build the Project **

   Abra una terminal, navegue hasta el directorio raíz de su proyecto y ejecute el siguiente comando para compilar el proyecto y descargar las dependencias:

```sh
mvn clean install