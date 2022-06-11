import cv2
def main(vidpath):
    objects = []
    minDistBetweenPts = 0.1
    cap = cv2.VideoCapture(vidpath)
    vid_w, vid_h = int(cap.get(3)), int(cap.get(4))
    detector = cv2.SimpleBlobDetector()
    ret, frame = cap.read()
    while ret:
        keypoints = detector.detect(frame)
        ret = cap.read()
