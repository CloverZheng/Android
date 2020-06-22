# -*- coding: utf-8 -*-
import os
import time
from urllib.parse import urlencode
import fake_useragent
import requests
from lxml import etree
import csv
import argparse
import sys

location = os.getcwd() + 'headers.csv'
ka = fake_useragent.UserAgent(path=location)
f = open('skinProduct.csv', 'w', encoding = 'utf-8', newline='')
csv_writer = csv.writer(f)
parser = argparse.ArgumentParser()
parser.add_argument('--keywords', type=str, default="4433336", help='make CSV according to it')
args = parser.parse_args()

def get_first(shuru,i):
	page=2*i-1
	s = (i  - 1) * 30 + 1

	he = {
		'accept': '*/*',
		'accept-encoding': 'gzip, deflate, br',
		'accept-language': 'zh-CN,zh;q=0.9',
		'cookie': '__jdu=783078358; areaId=15; ipLoc-djd=15-1213-3038-0; shshshfpa=25d2efdd-812b-00bf-0465-bc601a32664e-1572142042; xtest=7667.cf6b6759; shshshfpb=y%2F%201kbkJW0rrCZxHU6os3WA%3D%3D; user-key=56e092e2-0b10-4615-9bc1-dd211435cb26; cn=0; qrsc=3; unpl=V2_ZzNtbUIDRhRzCBIEexhdUmIBFAhKUBNGJQ1DVikcVFY3CxVcclRCFX0URlVnGlQUZwcZXUJcRhxFCEdkeB5fA2AFEFlBZxBFLV0CFi9JH1c%2bbRJcRV5CE3cPRVB7Gmw1ZAMiXUNnRRx3CUBdeR1VNVcEIm1yUUATcAtCZHopXTUlV05eRV5LFXFFQF15GFoMZQcbbUNnQA%3d%3d; __jdv=76161171|baidu-search|t_262767352_baidusearch|cpc|106807362512_0_1e4071ea100f437d96aba443c49ba960|1572333108335; __jda=122270672.783078358.1564988642.1572328729.1572333108.5; __jdc=122270672; __jdb=122270672.3.783078358|5.1572333108; shshshfp=f71d3f04ca730a97469ed0ded5889260; shshshsID=3394e8dd5a61826ebe3e3b39c51a7b35_2_1572333115068; rkv=V0000; 3AB9D23F7A4B3C9B=JDBODRIZ2EQH56E43CTTKRIEK74SLK6SDCZN2MDTPNMDAHNJBPOF7RIORAQB4F75VW3UNR635OBECG4L3P24AWIE2U',
		'referer': 'https://search.jd.com/Search?keyword=%E7%AC%94%E8%AE%B0%E6%9C%AC&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=%E7%AC%94%E8%AE%B0%E6%9C%AC&page='+str(page)+'&s='+str(s)+'&click=0',
		'sec-fetch-mode': 'cors',
		'sec-fetch-site': 'same-origin',
		'x-requested-with': 'XMLHttpRequest',
		'user-agent': ka.random

	}

	data = {
		'keyword': shuru,
		'enc': 'utf-8',
		'qrst': 1,
		'rt': 1,
		'stop': 1,
		'vt': 2,
		'wq': shuru,
		'page': page,
		's': s,
		'click':0
	}
	url = 'https://search.jd.com/s_new.php?'
	res = requests.get(url + urlencode(data), headers=he,timeout=5)
	res.encoding='utf-8'
	source = etree.HTML(res.text)
	title_list=source.xpath('//li[@class="gl-item"]')
	m=0
	for title in title_list:
		tt=title.xpath('./div[@class="gl-i-wrap"]//div[@class="p-name p-name-type-2"]/a/em/text()')
		product="".join(tt)
		product=product.replace(',','').replace('\n', '').strip()
		urls = title.xpath('//div[@class="p-name p-name-type-2"]/a[@target="_blank"]/@href')
		url=urls[m]
		m=m+1
		urlstring="".join(url)
		csv_writer.writerow([product, urlstring])
		print(product)


def get_laterpage(shuru,i):
	s = (i * 2 - 1) * 30 + 1
	page = i * 2

	he = {
		'accept': '*/*',
		'accept-encoding': 'gzip, deflate, br',
		'accept-language': 'zh-CN,zh;q=0.9',
		'cookie': '__jdu=783078358; areaId=15; ipLoc-djd=15-1213-3038-0; shshshfpa=25d2efdd-812b-00bf-0465-bc601a32664e-1572142042; xtest=7667.cf6b6759; shshshfpb=y%2F%201kbkJW0rrCZxHU6os3WA%3D%3D; user-key=56e092e2-0b10-4615-9bc1-dd211435cb26; cn=0; qrsc=3; unpl=V2_ZzNtbUIDRhRzCBIEexhdUmIBFAhKUBNGJQ1DVikcVFY3CxVcclRCFX0URlVnGlQUZwcZXUJcRhxFCEdkeB5fA2AFEFlBZxBFLV0CFi9JH1c%2bbRJcRV5CE3cPRVB7Gmw1ZAMiXUNnRRx3CUBdeR1VNVcEIm1yUUATcAtCZHopXTUlV05eRV5LFXFFQF15GFoMZQcbbUNnQA%3d%3d; __jdv=76161171|baidu-search|t_262767352_baidusearch|cpc|106807362512_0_1e4071ea100f437d96aba443c49ba960|1572333108335; __jda=122270672.783078358.1564988642.1572328729.1572333108.5; __jdc=122270672; __jdb=122270672.3.783078358|5.1572333108; shshshfp=f71d3f04ca730a97469ed0ded5889260; shshshsID=3394e8dd5a61826ebe3e3b39c51a7b35_2_1572333115068; rkv=V0000; 3AB9D23F7A4B3C9B=JDBODRIZ2EQH56E43CTTKRIEK74SLK6SDCZN2MDTPNMDAHNJBPOF7RIORAQB4F75VW3UNR635OBECG4L3P24AWIE2U',
		'referer': 'https://search.jd.com/Search?keyword=%E7%AC%94%E8%AE%B0%E6%9C%AC&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=%E7%AC%94%E8%AE%B0%E6%9C%AC&page='+str(page)+'&s='+str(s)+'&click=0',
		'sec-fetch-mode': 'cors',
		'sec-fetch-site': 'same-origin',
		'x-requested-with': 'XMLHttpRequest',
		'user-agent': ka.random

	}

	data = {
		'keyword': shuru,
		'enc': 'utf-8',
		'qrst': 1,
		'rt': 1,
		'stop': 1,
		'vt': 2,
		'wq': shuru,
		'page': page,
		's': s,
		'scrolling': 'y',
		'log_id': int(time.time() * 100000) / 100000,
		'tpl': '1_M'
	}
	url = 'https://search.jd.com/s_new.php?'
	res = requests.get(url + urlencode(data), headers=he,timeout=5)
	res.encoding='utf-8'
	source = etree.HTML(res.text)
	title_list=source.xpath('//li[@class="gl-item"]')
	m=0
	for title in title_list:
		tt=title.xpath('./div[@class="gl-i-wrap"]//div[@class="p-name p-name-type-2"]/a/em/text()')
		product="".join(tt)
		product=product.replace(',','').replace('\n','').strip()
		urls = title.xpath('//div[@class="p-name p-name-type-2"]/a[@target="_blank"]/@href')
		url=urls[m]
		m=m+1
		urlstring="".join(url)
		csv_writer.writerow([product, urlstring])
		print(product)


if __name__ == '__main__':
    csv_writer.writerow("testtesttest")
#    最小result为1111111 最大result为4433336
    result=args.keywords
    
    csv_writer.writerow(result)
    result_list = list(result)  #使用list()
    shuru_list=[]
    shuru_list.append('面部 女')
    category=["综合","洁面","面膜","精华","眼霜","防晒"]
    if ((int(result[6])>1)and(int(result[6])<=6)):
        shuru_list.append(' ')
        shuru_list.append(category[int(result[6])-1])
    
#String[] skinColorLevel={"肤色","白","较白","较黑","黑"};
#String[] poreLevel={"毛孔","无","轻度","中度","严重"};
#String[] wrinkleLevel={"皱纹","无","中度","严重"};
#String[] smoothLevel={"光滑度","光滑","中度","粗糙"};
#String[] oilLevel={"水油性","干性","适中","油性"};
#String[] darkCircleLevel={"黑眼圈","无","轻微","严重"};
    
    #若非白皮 且非选择眼霜或防晒（关系不大） 搜索美白
    if (int(result[0])>2 and (int(result[6])!=6) and (int(result[6])!=5)):
        shuru_list.append(' ')
        shuru_list.append('美白')
        
    #若是毛孔存在粗大情况 且非选择眼霜或防晒（关系不大） 搜索毛孔
    if (int(result[1])>1 and (int(result[6])!=6) and (int(result[6])!=5)):
        shuru_list.append(' ')
        shuru_list.append('毛孔')
        
    #若是出现细纹或更严重的纹路情况 且非选择防晒（关系不大） 搜索细纹或淡纹
    if (int(result[2])!=1 and (int(result[6])!=6)):
        shuru_list.append(' ')
        shuru_list.append('纹')
        
    #若是出现老化皱纹或明显皱纹 且非选择防晒（关系不大） 搜索抗皱
    if (int(result[2])==3 and (int(result[6])!=6)):
        shuru_list.append(' ')
        shuru_list.append('抗皱')
        
    #若是皮肤不够细致 且非选择防晒（关系不大） 搜索保湿 补水
    if (int(result[3])!=1 and (int(result[6])!=6)):
        shuru_list.append(' ')
        shuru_list.append('保湿 补水')
        
    #根据肤质水油性 搜索相应关键词
    if int(result[4])==1:
        shuru_list.append(' ')
        shuru_list.append('干皮')
    elif int(result[4])==2:
        shuru_list.append(' ')
        shuru_list.append('中性')
    elif int(result[4])==3:
        shuru_list.append(' ')
        shuru_list.append('油皮')
        
    #若是有黑眼圈 且非选择防晒（关系不大） 搜索黑眼圈
    if (int(result[5])!=1 and (int(result[6])!=6)):
        shuru_list.append(' ')
        shuru_list.append('黑眼圈')
        
    shuru = "".join(shuru_list)
    print(shuru)
    for i in range(1,2):
        get_first(shuru,i)
        print('---------------------------')
        time.sleep(2)
        j=get_laterpage(shuru,i)
        print('第%s页结束'%i)
        time.sleep(2)
    f.close()
