import cv2
import threading
import time
import Connection as connect

# class for face and cat detect
class Detect(object):
    _instance_lock = threading.Lock()

    # Load a cascade file for detecting faces and cats
    face_cascade_front = cv2.CascadeClassifier(
        '/home/pi/opencv-3.3.0/data/haarcascades/haarcascade_frontalface_default.xml')
    face_cascade_profile = cv2.CascadeClassifier('/home/pi/opencv-3.3.0/data/haarcascades/haarcascade_profileface.xml')
    face_cascade_upperbody = cv2.CascadeClassifier('/home/pi/opencv-3.3.0/data/haarcascades/haarcascade_upperbody.xml')
    face_cascade_lowerbody = cv2.CascadeClassifier('/home/pi/opencv-3.3.0/data/haarcascades/haarcascade_lowerbody.xml')
    face_cat = cv2.CascadeClassifier('/home/pi/opencv-3.3.0/data/haarcascades/haarcascade_frontalcatface.xml')

    origin_time = 0
    min_time = 0

    def __init__(self):
        pass

    # set min_upload_seconds
    def setMinTime(self, min):
        self.min_time = min

    def __new__(cls, *args, **kwargs):
        if not hasattr(Detect, "_instance"):
            with Detect._instance_lock:
                if not hasattr(Detect, "_instance"):
                    Detect._instance = object.__new__(cls)
        return Detect._instance

    # detect the kind of the moving objects
    def detectAny(self, pic_path):
        # get the time difference for the recent two detection
        now = time.time()
        diff = now - self.origin_time
        # time difference is too small -> give up detection and sending
        if diff < self.min_time:
            return False

        image = cv2.imread(pic_path)

        # use the cascade file loaded to detect faces and cats
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

        faces_front = self.face_cascade_front.detectMultiScale(gray)
        faces_profile = self.face_cascade_profile.detectMultiScale(gray)
        upper_body = self.face_cascade_upperbody.detectMultiScale(gray)
        lower_body = self.face_cascade_lowerbody.detectMultiScale(gray)
        cat = self.face_cat.detectMultiScale(gray)
        
        # get the numbers of faces and cats
        c = len(cat)

        m = len(faces_front)
        n = len(faces_profile)
        p = len(upper_body)
        q = len(lower_body)

        # send the photo according to the kind fo the image
        if m > 0 or n > 0 or p > 0 or q > 0:
            # This is a person picture
            print('There is a person')
            connect.Connection().sendPhoto(pic_path, people=True)
        elif c > 0:
            # This is a cat picture
            print('There is a cat')
            connect.Connection().sendPhoto(pic_path, cat=True)
        else:
            # There is something moving
            print('There is something moving')
            connect.Connection().sendPhoto(pic_path)
            
        self.origin_time = now
