kill -9 $(ps -ef | grep "music-box-0.0.1-SNAPSHOT.jar" | grep -v grep | awk '{print $2}')

rm -f /home/music-box/music-box-0.0.1-SNAPSHOT.jar
rm -f /home/music-box/nohup.out