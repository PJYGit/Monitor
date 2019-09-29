import cv2
import os

# calculate the similarity between the two pictures
def getPicSimilarity(newPic):
    oldPic = lastPic()
    img1 = cv2.imread(oldPic)
    img2 = cv2.imread(newPic)

    # calculate the histogram for img1 
    H1 = cv2.calcHist([img1], [1], None, [256], [0, 256])
    # normalize img1
    H1 = cv2.normalize(H1, H1, 0, 1, cv2.NORM_MINMAX, -1)

    # calculate the histogram for img1 
    H2 = cv2.calcHist([img2], [1], None, [256], [0, 256])
    # normalize img1
    H2 = cv2.normalize(H2, H2, 0, 1, cv2.NORM_MINMAX, -1)
    
    # get the final similarity
    similarity = cv2.compareHist(H1, H2, 0)
    
    return similarity

# get the most recent sent(saved) picture file name
def lastPic():
    directory = '/home/pi/pictures'
    
    lists = os.listdir(directory)
    lists.sort(key = lambda fn:os.path.getmtime(directory+'/'+fn))
    old_file = directory + '/' + lists[-2]
    
    return old_file