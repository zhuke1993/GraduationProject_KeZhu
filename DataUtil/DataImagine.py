__author__ = 'ZHUKE'
# coding=utf-8
import matplotlib.pyplot
from numpy import *

if __name__ == "__main__":
    fo = open("action.txt", "r")
    lines = fo.readlines()
    actions = []
    ax = []
    ay = []
    az = []
    oy = []
    oz = []
    gx = []
    gy = []
    gz = []
    for i in lines:
        sp = i.split(",")
        actions.append(sp)
    for i in actions:
        ax.append(float(i[0]))
        ay.append(float(i[1]))
        az.append(float(i[2]))
        oy.append(float(i[3]))
        oz.append(float(i[4]))
        gx.append(float(i[5]))
        gy.append(float(i[6]))
        gz.append(float(i[7]))

    X = random.normal(0, 1, len(lines))
    matplotlib.pyplot.scatter(az, X)
    matplotlib.pyplot.show()
