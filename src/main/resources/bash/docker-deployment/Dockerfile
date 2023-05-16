FROM larrypu/jdk17:17.0.7
MAINTAINER luyabs <https://github.com/Luyabs>
ADD music-box-0.0.1-SNAPSHOT.jar /home/music-box/music-box.jar
WORKDIR /home/music-box
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/music-box/music-box.jar"]
