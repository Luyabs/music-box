import requests
# 爬虫: 酷我音乐飙升榜 http://www.kuwo.cn/rankList

if __name__ == '__main__':
    headers = {'Accept': ',application/json, text/plain, */*',
               'Accept-Encoding': 'gzip, deflate',
               'Accept-Language': 'zh-CN,zh;q=0.9',
               'Connection': 'keep-alive',
               'Cookie': 'kw_token=XM5GXCP8M5',
               'csrf': 'XM5GXCP8M5',
               }
    rank_list_url = 'http://www.kuwo.cn/api/www/bang/bang/musicList?bangId=93&pn={}&rn=20'
    music_url = 'http://www.kuwo.cn/api/v1/www/music/playUrl?mid={}' \
                '&type=music&httpsStatus=1&reqId=d4578181-e1dd-11ed-92a0-9d4901615d88 '

    for page_number in range(1, 31):
        res = requests.get(rank_list_url.format(page_number), headers=headers)
        music_list = res.json()['data']['musicList']
        for music in music_list:
            try:
                mp3 = requests.get(music_url.format(music['rid']), headers=headers)
                print(music['albumpic'], music['name'], music['artist'], music['album'], music['releaseDate'],
                      mp3.json()['data']['url'])
            except:
                print('收费歌曲', music['name'])

