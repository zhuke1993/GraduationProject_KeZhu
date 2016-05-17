__author__ = 'ZHUKE'
# coding=utf-8
from sklearn import neighbors
from sklearn.cross_validation import train_test_split
import numpy as np

data = []
lables = []

with open("action.txt") as ifile:
    for line in ifile:
        tokens = line.strip().split(" ")
        data.append([float(tk) for tk in tokens[1:]])
        lables.append(tokens[0])
x = np.array(data)
y = np.array(lables)
'''拆分训练数据与测试数据'''
x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2)
'''训练KNN分类器'''
clf = neighbors.KNeighborsClassifier(algorithm="kd_tree")
clf.fit(x_train, y_train)
'''测试结果打印'''
answer = clf.predict(x)
print x
print answer
print y
print np.mean(answer == y)
