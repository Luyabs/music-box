docker pull abstract1on/music-box-backend:1.0.0
docker run -p 8080:8080 -v /home/music-box/backend:/home/music-box/backend --name music-box-backend-main -d music-box-backend
# 或者这个
docker run -p 8080:8080 -v /home/music-box/backend:/home/music-box/backend --name music-box-backend-main -d abstract1on/music-box-backend:1.0.0