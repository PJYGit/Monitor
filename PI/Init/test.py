import Connection as connect
import GMM as opencamera
import PicDetect as det
import ReadConfig as conf
import threading as thr
import time
import os
import socket

# init the config and network connection
def loading():
    time.sleep(5)
    # read the config file
    now_config = conf.Config().readConfig()
    
    # first run
    if os.path.exists('/home/pi/Factorynew.conf') is False:
        tmpfile = open('/home/pi/Factorynew.conf', 'w')
        tmpfile.close()
        WIFI(now_config['wifi_account'], now_config['wifi_password'])
        
    opencamera.CatchMO().setConfig(now_config)
    det.Detect().setMinTime(now_config['min_upload_seconds'])
    connect.Connection().initUrl(getIP(), now_config['server_port'])
    
    # init the network
    checknet = connect.Connection().scanServer()
    if not checknet:
        checkagain = connect.Connection().scanServer()
        if not checkagain:
            print('Network connection failed')
            return False
    # network checked -> start heart beat test
    t = thr.Thread(target=connect.Connection().heartBeatTest, args=())
    t.start()
    
    print('Init finished')
    return True

def WIFI(ac, code):
    data = '\nnetwork={\n\tssid="' + str(ac) + '"\n\tpsk="' + str(code) + '"\n}'
    os.system('sudo chmod 777 /etc/wpa_supplicant/wpa_supplicant.conf')
    file = open('/etc/wpa_supplicant/wpa_supplicant.conf', 'a')
    file.write(data)
    file.close()
    print('wifi init')
    time.sleep(2)
    os.system('sudo reboot')
    
def getIP():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(('8.8.8.8', 80))
    ip = s.getsockname()[0]
    pos = ip.rfind('.') + 1
    address = 'http://' + ip[:pos]
    return address
    
if __name__ == '__main__':
    # load everything
    checkload = loading()
    # init failed -> quit
    if not checkload:
        print('Too silly to use our PI')
        os.system('sudo reboot')
    # init succeed -> open camera
    else:
        opencamera.CatchMO().startMonitor()
        
