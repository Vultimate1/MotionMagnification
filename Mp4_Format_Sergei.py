# -*- coding: utf-8 -*-
"""
Created on Wed Mar 21 10:11:19 2018

@author: Katie Kosak
## CFSA, Warwick University
### Uses Dr. Sergei Anfinogentov's Motion Magnification Code
### This code enables the use of MP4 or AVI formats with the code
"""
from magnify import magnify_motions_2d

print("before everything else")
from magnify import *
import cv2 as cv2
from matplotlib import pyplot as plt
import numpy as np
from matplotlib import animation
import ffmpeg
## Animation Settings ############
#animation.ffmpeg_path = "C:\\ffmpeg-full\\ffmpeg-5.0-full_build\\bin\\ffmpeg.exe"
#plt.rcParams['animation.ffmpeg_path'] = 'C:\ffmpeg-full\ffmpeg-5.0-full_build\bin\ffmpeg.exe'
animation.ffmpeg_path = 'C:\ffmpeg-full\ffmpeg-5.0-full_build\bin\ffmpeg.exe'

#print("hey ffmpeg")
#Writer = animation.writers["ffmpeg"]
#Writer = animation.FFMpegWriter()
#Writer = animation.writers['C:\\ffmpeg-full\\ffmpeg-5.0-full_build\\bin\\ffmpeg']
#Writer = animation.MovieWriter(fps=5, codec=None, bitrate=None, extra_args=None, metadata=None)#
#writer = Writer(fps=10, metadata=dict(artist='Me'), bitrate=180)
#vidpath = 'C:\\Users\\srira\\Documents\\MATLAB\\Matlab\\loaded_cart.mp4'
#metadata = dict(title=vidpath, artist='Matplotlib', comment='')
#writer = Writer(fps=10, metadata=metadata, bitrate=180)
#Writer = animation.FFMpegWriter(fps=10, metadata=metadata, bitrate=180)
Writer = animation.FFMpegWriter(fps=10, metadata=dict(artist='Me'), bitrate=180)

#print("ffmpeg writer")
# Have  a mp4 file converted into a data cube ##################
#vidpath = 'C:\\Users\\srira\\Documents\\MATLAB\\Matlab\\loaded_cart.mp4'
#print("before videocapture in mp4 format sergei")
#cap = cv2.VideoCapture(vidpath)
#print("line 38")
#frameCount = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
#frameWidth = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
#frameHeight = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))

#input_data = np.empty((frameCount, frameHeight, frameWidth,3), np.dtype('double'))
#fc = 0
#ret = True

#while (fc < frameCount  and ret):
#    ret, input_data[fc] = cap.read()
#    fc += 1

#cap.release()

############### Convert RGB Data to 1 Color Stream ################
#input_data=np.sum(input_data,axis=3)
###################### MOtion Magnification ###########################
#k=5 #Magnification
#width= 80 # width
print("before magnify")

#frameCount, frameHeight, frameWidth, result = magnify_motions_2d(vidpath, k, width)



################# Save the Movie as mp4 ##################

print("before create animation defined")
def Create_Animation(frameCount, frameWidth, frameHeight, width, k, title):
    ## Create an Array of pictures from a Data Cube
    print("right before magnify motions 2d")
    print(title)
    result = magnify_motions_2d(frameCount, frameWidth, frameHeight, title, k, width)
    images=[]
    fig=plt.figure()
    for i in range(frameCount):
        img_plot=plt.imshow(result[i])
        images.append([img_plot])
    ani = animation.ArtistAnimation(fig, images, interval=100, blit=True)
    ani.save(title+"_magnified.mp4", writer=Writer)
    return title+"_magnified.mp4"

#images=Create_Animation(width, k, vidpath)

