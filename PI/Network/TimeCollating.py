import time
import os

def correctTime(new_time):
    t = time.localtime(new_time + 7 * 60 * 60)
    print(t)
    currenttime = "%u-%02u-%02u %02u:%02u:%02u"%(t.tm_year, t.tm_mon, t.tm_mday, t.tm_hour, t.tm_min, t.tm_sec)
    os.system('sudo date -s "' + currenttime + '"')
    print('time changed')
    now = int(time.time())
    print(now)
