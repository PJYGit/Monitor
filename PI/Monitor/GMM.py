import cv2
import os
import json
import time
import threading
import numpy as np
import PicSimilarity as similarity
import PicDetect as detect
from picamera import PiCamera
from picamera.array import PiRGBArray
from datetime import datetime

# class for catching moving objects
class CatchMO(object):
    _instance_lock = threading.Lock()

    config = None
    minMF = None

    def __init__(self):
        pass

    def __new__(cls, *args, **kwargs):
        if not hasattr(CatchMO, "_instance"):
            with CatchMO._instance_lock:
                if not hasattr(CatchMO, "_instance"):
                    CatchMO._instance = object.__new__(cls)
        return CatchMO._instance
    
    # set the config variables
    def setConfig(self, con):
        self.config = con
        self.minMF = con['min_motion_frames']

    # function for starting the camera
    def startMonitor(self):
        # get and set the camera
        cap = PiCamera()
        cap.resolution = self.config['resolution']
        cap.framerate = self.config['fps']

        # cast the video frame to array type
        rawCapture = PiRGBArray(cap, size=(640, 480))
        
        # warm up the camera
        time.sleep(self.config['camera_warmup_time'])
        
        pre_frame = None
        notFirstPic = False
        frame_count = 0

        # cycle for video frame capturing
        for f in cap.capture_continuous(rawCapture, format="bgr", use_video_port=True):
            # get the view
            frame = f.array
            
            # show the original video or not
            if self.config['show_video']:
                cv2.imshow("original views", frame)

            # cast to gray picture
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            gray = cv2.GaussianBlur(gray, (21, 21), 0)
            
            # first background
            if pre_frame is None:
                pre_frame = gray
                rawCapture.truncate(0)
                cv2.waitKey(1)
                frame_count = frame_count + 1
                continue
            
            # ignore too fast moving objects
            if frame_count < self.minMF:
                rawCapture.truncate(0)
                cv2.waitKey(1)
                frame_count = frame_count + 1
                continue
            

            # corrosion and expansion
            diff = cv2.absdiff(pre_frame, gray)
            diff = cv2.threshold(diff, self.config['delta_thresh'], 255, cv2.THRESH_BINARY)[1]
            diff = cv2.dilate(diff, None, iterations=2)

            # find the moving objects
            tmp_, contours, _ = cv2.findContours(diff.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

            object_count = 0

            # cycle for all moving objects, both small and large ones
            for c in contours:
                # ignore the small ones
                if cv2.contourArea(c) > self.config['min_area']:
                    object_count = object_count + 1
                    x, y, w, h = cv2.boundingRect(c)
                    # marking the moving objects
                    cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)
                    # text = 'Occuppied'

            # show text on the frame
            ts = str(time.time())
            # cv2.putText(frame, 'Room Status:{}'.format(text), (10, 20), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
            cv2.putText(frame, ts, (10, frame.shape[0] - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.35, (0, 0, 255), 1)
            
            # show the marked video or not
            if self.config['show_video']:
                cv2.imshow("marked views", frame)

            # if there is something moving and use_imagefile is True
            if object_count > 0 and self.config['use_imagefile']:
                # save image, using time to name the file
                now = datetime.now().strftime('%Y%m%d%H%M%S%f')
                new_name = '/home/pi/pictures/' + str(now) + '.jpg'
                cv2.imwrite(new_name, frame)
                print('save ' + new_name)

                # if first pic -> detect
                if notFirstPic is False:
                    print('first move')
                    t1 = threading.Thread(target=detect.Detect().detectAny, args=(new_name,))
                    t1.start()

                # if not first pic
                if notFirstPic:
                    # if similarity < 0.80 : face and cat detect -> send
                    # else : delete new file
                    sim_persentage = similarity.getPicSimilarity(new_name)
                    print(sim_persentage)

                    if sim_persentage > 0.88:
                        os.remove(new_name)
                        print('delete ' + new_name)
                    else:
                        t2 = threading.Thread(target=detect.Detect().detectAny, args=(new_name,))
                        t2.start()

                notFirstPic = True
            # reset the background
            pre_frame = gray
            frame_count = 0

            rawCapture.truncate(0)
            cv2.waitKey(1)

        cap.release()
        cv2.destoryAllWindows()
