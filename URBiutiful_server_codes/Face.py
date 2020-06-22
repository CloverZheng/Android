# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""

import cv2
import sys
import dlib
from matplotlib import pyplot as plt 

#最后剪裁的图片大小
class face():
    def __init__(self,filename):
        self.filename=filename
        self.image=cv2.imread(filename)
        self.facecut=cv2.imread(filename)
    def getface(self):
        size_m = 1024
        size_n =1024
        cascade = cv2.CascadeClassifier(r"/root/HF/code/haarcascade_frontalface_alt2.xml")
        img = cv2.imread(self.filename)
        dst=img
        rects = cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=4, minSize=(30, 30),flags=cv2.CASCADE_SCALE_IMAGE)
        if len(rects) == 0:
            return 'no face'
        rects[:, 2:] += rects[:, :2]
        for x1,y1,x2,y2 in rects:
            #调整人脸截取的大小。横向为x,纵向为y
            roi = dst[y1:y2, x1 :x2 ]
            img_roi = roi
            self.facecut = cv2.resize(img_roi, (size_m, size_n))
        return 'a face'
    def getskin(self):
        """YCrCb颜色空间的Cr分量+Otsu阈值分割
        :param image: 图片路径
        :return: None
        """
        img = self.facecut
        ycrcb = cv2.cvtColor(img, cv2.COLOR_BGR2YCR_CB)
        
        (y, cr, cb) = cv2.split(ycrcb)
        cr1 = cv2.GaussianBlur(cr, (5, 5), 0)
        _, skin = cv2.threshold(cr1,0,255,cv2.THRESH_BINARY+cv2.THRESH_OTSU)
        self.faceskin = cv2.bitwise_and(img, img, mask=skin)
    
    def getfacepart(self):
        color_image = self.facecut
        gray_image = cv2.cvtColor(color_image, cv2.COLOR_BGR2GRAY)  # 将彩色图片转换为灰色图片，提高人脸检测的准确率
        
        # 也可以不用转换灰色图像，但是BGR要转换为RGB
        # color_image = cv2.imread(file_path, cv2.IMREAD_COLOR)
        # B, G, R = cv2.split(color_image)   # 分离三个颜色通道
        # color_image = cv2.merge([R, G, B]) # 融合三个颜色通道生成新图片
        
        # 人脸检测器
        detector = dlib.get_frontal_face_detector()
        # 特征点检测器
        predictor = dlib.shape_predictor(r"/root/HF/code/shape_predictor_68_face_landmarks.dat")
        # 检测人脸
        # The 1 in the second argument indicates that we should upsample the image 1 time.
        faces = detector(gray_image, 1)  
        
        # 调用训练好的卷积神经网络（cnn）模型进行人脸检测
        # cnn_face_detector = dlib.cnn_face_detection_model_v1('model/mmod_human_face_detector.dat')
        # faces = cnn_face_detector(gray_image, 1)
        
        for face in faces:
            # 寻找人脸的68个标定点
            shape = predictor(color_image, face)
        self.face68=color_image
        img=self.facecut
        #for blackcircle
        self.lefteyebelow=img[shape.part(40).y+35:shape.part(40).y+115,shape.part(36).x:shape.part(39).x]
        self.righteyebelow=img[shape.part(47).y+35:shape.part(14).y+115,shape.part(42).x:shape.part(45).x]
        #for wrinkle
        self.forehead=img[shape.part(25).y//3:shape.part(25).y,shape.part(18).x:shape.part(25).x]
        #for roughness
        self.leftfacerough=img[(shape.part(1).y+shape.part(2).y)//2:(shape.part(2).y+shape.part(3).y)//2,shape.part(17).x:shape.part(19).x]
        self.rightfacerough=img[(shape.part(15).y+shape.part(14).y)//2:(shape.part(14).y+shape.part(13).y)//2,shape.part(24).x:shape.part(26).x]
        #for poreslevel and oil
        self.leftnose=img[(shape.part(1).y+shape.part(2).y)//2:shape.part(3).y,shape.part(41).x:(shape.part(40).x+shape.part(39).x)//2]
        self.rightnose=img[(shape.part(14).y+shape.part(15).y)//2:shape.part(13).y,(shape.part(42).x+shape.part(47).x)//2:shape.part(46).x]
        self.foreheadpore=img[shape.part(25).y-80:shape.part(25).y,shape.part(21).x:shape.part(22).x]
        self.nosepore=img[shape.part(29).y:shape.part(30).y+5,shape.part(31).x:shape.part(35).x]
