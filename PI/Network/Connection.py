import requests
import os
import sys
import json
import uuid
import time
import base64
import threading
import TimeCollating as correct
import ReadConfig as config

# class for network connection
class Connection(object):
    _instance_lock = threading.Lock()
    
    half_url = None
    port = None
    my_id = 0
    found = False
    pause = False
    url_found = "connect failed"
    max_time_diff = 20

    # get the MAC address of the raspberryPI
    mac = uuid.UUID(int=uuid.getnode()).hex[-12:]
    data = {'mac': mac}
    print(data)
    
    def __init__(self):
        pass
    
    def __new__(cls, *args, **kwargs):
        if not hasattr(Connection, "_instance"):
            with Connection._instance_lock:
                if not hasattr(Connection, "_instance"):
                    Connection._instance = object.__new__(cls)
        return Connection._instance
    
    # init the url according to the config file
    def initUrl(self, url, port):
        self.half_url = url
        self.port = str(port)

    # function for scanning server address (if not certain)
    def scanServer(self):
        record = 0
        for i in range(256):
            url = self.half_url + str(i) + ':' + self.port + '/server/pi/hi'
            # print(url)
            try:
                response = requests.post(url, data=self.data, timeout=0.1)
                print(response.json())
                
                # if server connected -> set my_id
                if response.json()['id'] is not None:
                    self.my_id = int(response.json()['id'])
                    print('my id: ' + str(self.my_id))
                    print('100%')
                    record = i
                    self.found = True
                    break

            except:
                pass
            # print the scaning process
            if i % 20 == 0:
                print(str(i / 255) + '%')

        # server address found
        if self.found:
            self.url_found = self.half_url + str(record) + ':' + self.port
            return True
        
        return False

    # function for heart beat testing
    def heartBeatTest(self):
        # cycle for heart beat test for every 5 seconds
        while True:
            # get the time stamp
            now = int(time.time())
            heart_beat = {'id': str(self.my_id), 'time': str(now)}
            
            try:
                print(self.url_found + '/server/pi/beat')
                res = requests.post(self.url_found + '/server/pi/beat', data=heart_beat, timeout=0.5)
                print('heart beat connect')
                
                # if server online -> get the operation codes
                if res.json()['op'] is not None and res.json()['time'] is not None:
                    
                # time:
                    server_time = int(res.json()['time'])
                    print(server_time)
                    # calculate the time difference between PI and server
                    timediff = abs(server_time - int(time.time()))
                    # if the time difference is too large -> correct time
                    if timediff > self.max_time_diff:
                        correct.correctTime(server_time)
                        
                # operation:
                    op = int(res.json()['op'])
                    print(op)
                    
                    # operation the PI need to do according to the operation codes
                    if op == 0:
                        # shutdown
                        print('starting shutdown')
                        os.system('shutdown -t 5 now')
                        sys.exit()
                    elif op == 1:
                        # pause
                        self.pause = True
                        print('pause')
                    elif op == 2:
                        # reuse
                        self.pause = False
                        print('reuse')
            except Exception:
                print('heart beat none')
                pass

            time.sleep(5)

    # function for sending photos
    def sendPhoto(self, path, people=False, cat=False):
        # if pause -> do not send
        if self.pause:
            return 0

        # judge the picture type
        # 0 : people
        # 1 : cat
        # 2 : others
        type = 2
        if people:
            type = 0
        elif cat:
            type = 1

        # read the photo and cast to base64 encoding method
        file = open(path, 'rb')
        photo_data = {'id': self.my_id, 'type': type}
        image_file = {'data': file}
        
        try:
            # post the image data to server
            # requests.post(self.url_found + '/server/pi/upload', data=photo_data, timeout=0.5)
            requests.post(self.url_found + '/server/pi/upload', data=photo_data, files=image_file, timeout=1)
            
        except:
            print('error')
            pass
        
        file.close()

