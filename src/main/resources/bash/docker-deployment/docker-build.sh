mkdir -p /home/music-box/backend
cd  /home/music-box/backend
# 需要先传输文件(music-box-0.0.1-SNAPSHOT.jar与Dockerfile到/home/music-box/backend)
docker build -t music-box-backend .
# run
docker run -p 8080:8080 -v /home/music-box/backend:/home/music-box/backend --name music-box-backend-main -d music-box-backend