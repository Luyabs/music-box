import requests
import pandas as pd
import os

# 爬虫: 酷我音乐飙升榜 http://www.kuwo.cn/rankList
# 自己手动添加则推荐使用mp3tag

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

    if not os.path.exists('album_pic'):
        os.makedirs('album_pic')
    if not os.path.exists('mp3'):
        os.makedirs('mp3')

    name_list = []
    artist_list = []
    album_list = []
    release_date_list = []
    music_url_list = []
    album_pic_list = []
    for page_number in range(1, 16):
        res = requests.get(rank_list_url.format(page_number), headers=headers)  # 音乐各项信息
        music_list = res.json()['data']['musicList']
        for music in music_list:
            mp3 = requests.get(music_url.format(music['rid']), headers=headers)  # 音乐实际地址
            try:
                music_url_list.append(mp3.json()['data']['url'])
                name_list.append(music['name'])
                artist_list.append(music['artist'])
                album_list.append(music['album'])
                release_date_list.append(music['releaseDate'])
                album_pic_list.append(music['albumpic'])

                with open('mp3/' + music['name'] + '.mp3', 'wb') as f:
                    res = requests.get(mp3.json()['data']['url'], headers=headers)
                    f.write(res.content)
                with open('album_pic/' + music['album'] + '.jpg', 'wb') as f:
                    res = requests.get(music['albumpic'], headers=headers)
                    f.write(res.content)

                print('已完成下载:', music['name'])

            except Exception as e:
                print('付费歌曲:', music['name'])

    dfData = {  # 用字典设置DataFrame所需数据
        '音乐名': name_list,
        '歌手': artist_list,
        '专辑': album_list,
        '发布时间': release_date_list,
        '音乐下载地址': music_url_list,
        '专辑封面': album_pic_list
    }
    df = pd.DataFrame(dfData)  # 创建DataFrame
    df.to_excel('music.xlsx', index=False)
