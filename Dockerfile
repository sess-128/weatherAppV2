FROM tomcat:11.0.2-jdk17

# Удаляем стандартные приложения
RUN rm -rf /usr/local/tomcat/webapps/*

# Копируем собранный WAR-файл
COPY target/weatherAppV2-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Оптимизация запуска
ENV CATALINA_OPTS="-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080
