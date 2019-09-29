# 算法效果不太好，备用

import cv2
import numpy as np
from picamera import PiCamera
from picamera.array import PiRGBArray



camera = PiCamera()
camera.resolution = (640, 480)
camera.framerate = 30

rawCapture = PiRGBArray( camera, size=( 640, 480 ) )

es = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (9, 4))
count = 0

framearray = [-1]
n = 24
while n >= 1:
    framearray.append(-1)
    n = n - 1

backSub = cv2.createBackgroundSubtractorMOG2()

for f in camera.capture_continuous( rawCapture, format="bgr", use_video_port=True ):

    frame_lwpCV = f.array
    
    mask = backSub.apply(frame_lwpCV)
    
    gray_lwpCV = cv2.cvtColor(frame_lwpCV, cv2.COLOR_BGR2GRAY)
    gray_lwpCV = cv2.GaussianBlur(gray_lwpCV, (21, 21), 0)

    temp = count % 25
    
    
    if count >= 25:
        diff = cv2.absdiff(framearray[temp], gray_lwpCV)
        diff = cv2.threshold(diff, 25, 255, cv2.THRESH_BINARY)[1]  
        diff = cv2.dilate(diff, es, iterations=2) 

        t_bak ,contours, hierarchy = cv2.findContours(diff.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        for c in contours:
            if cv2.contourArea(c) < 2000:  
                continue
            (x, y, w, h) = cv2.boundingRect(c)
            cv2.rectangle(frame_lwpCV, (x, y), (x + w, y + h), (0, 255, 0), 2)	
        	
        

        cv2.imshow('movers', frame_lwpCV)
        cv2.imshow('no back', mask)
    

    rawCapture.truncate( 0 )
    count = count + 1
    if count > 100:
        count = count - 25
    cv2.waitKey(1)
    framearray[temp] = gray_lwpCV
# When everything done, release the capture
camera.release()
cv2.destroyAllWindows()