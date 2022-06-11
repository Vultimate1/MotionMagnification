#import dtcwt #C:\Python39\Lib\site-packages\dtcwt
#import time
import scipy
import dtcwt
from scipy import ndimage
from scipy import signal
from matplotlib import pyplot as plt
import numpy as np
from matplotlib import animation
#import sys
from magnify import *
import numpy as np
import cv2

def phase(x):
    ind = np.where(np.abs(x) > 1e-20)
    ph = np.ones_like(x)
    ph[ind] = x[ind] / np.abs(x[ind])
    return ph


def get_phases(pyramids, level):
    sz = pyramids[0].highpasses[level].size
    length = len(pyramids)
    ph = np.empty((length, sz), pyramids[0].highpasses[level].dtype)
    ph[0, :] = phase(pyramids[0].highpasses[level].flatten())
    ph_prev = ph[0, :]
    for i in range(1, length):
        ph_cur = phase(pyramids[i].highpasses[level].flatten())
        ph[i, :] = ph_cur / ph_prev
        ph_prev = ph_cur
    ang = np.angle(ph)
    ang = np.cumsum(ang, axis=0)
    return ang


def flattop_filter1d(data, width, axis=0, mode='reflect'):
    window_size = round(width / 0.2327)
    window = signal.flattop(window_size)
    window = window / np.sum(window)
    result = ndimage.convolve1d(data, window, axis=axis, mode='reflect')
    return (result)




def magnify_motions_2d(frameCount, frameWidth, frameHeight, vidpath, k, width):
    print("before videocapture "+vidpath)
    cap = cv2.VideoCapture(vidpath)
    print("line 43")
    #frameWidth = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    #frameHeight = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    print("framewidth is "+str(frameWidth))
    print("frameheight is "+str(frameHeight))
    #frameCount = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))

    data = np.empty((int(frameCount), int(frameHeight/8), int(frameWidth/8),3), dtype='float')
    fc = 0
    ret = True

    while (fc < frameCount  and ret):
        ret, data[fc] = cap.read()
        fc += 1
    cap.release()
    data=np.sum(data,axis=3)

    nlevels = 8
    tr = dtcwt.Transform2d()
    pyramids = list()
    n = np.shape(data)[0]
    print('Forward DTCWT...')  # , end =" ", flush = 1)
    for i in range(0, n):
        pyramids.append(tr.forward(data[i, :, :], nlevels=nlevels))
    print('DONE')
    print('Modifying phase...')  # , end = ' ', flush = 1)
    for level in range(0, nlevels):
        phase = get_phases(pyramids, level)
        phase0 = flattop_filter1d(phase, width, axis=0, mode='reflect')
        phase = phase0 + (phase - phase0) * k
        phase = flattop_filter1d(phase, 2.0, axis=0, mode='reflect')
        for i in range(0, n):
            h = pyramids[i].highpasses[level]
            abs_value = np.abs(h).flatten()
            h = abs_value * np.exp(1j * phase[i, :])
            pyramids[i].highpasses[level][:] = np.reshape(h, np.shape(pyramids[i].highpasses[level][:]))
    result = np.empty_like(data)
    print('DONE')
    print('Inverse DTCWT...')  # , end = ' ', flush = 1)
    for i in range(1, n):
        result[i, :, :] = tr.inverse(pyramids[i])
    print('DONE')
    return result


def load_cube(file):
    f = open(file, 'rb')
    dim = np.fromfile(f, np.int32, 3)
    data = np.fromfile(f, np.float64, np.product(dim))
    data = data.reshape((dim[2], dim[1], dim[0]))
    return (data)


def save_cube(file, cube):
    f = open(file, 'wb')
    dim = np.flipud(cube.shape).astype(np.int32)
    dim.tofile(f)
    cube.tofile(f)
