# -*- coding:utf-8 -*-
import re
from datetime import datetime as dt, datetime

import sys

from enaml.qt import QtGui

from matplot_window import MatplotWindow

if __name__ == '__main__':

    # f =open("E:/NRTC20170728.log","r")
    # l=f.read()
    # a=re.split("(\d+-\d+-\d+)",l)
    # b=iter(a[1::])
    # s=map(lambda x:x[0]+x[1] ,zip(b,b))
    # for i in s :
    #     print i


    # a=datetime.strptime("2017-07-28 10:19:40", "%Y-%m-%d %H:%M:%S")
    # print type(a)
    # data=["aa","bb"]
    # app=QtGui.QApplication(sys.argv)
    # window = MatplotWindow(data)
    # window.show()
    # app.exec_()
    a="12345"
    print a.index("1")
    b=list(a)
    b.insert(1,",")
    print "".join(b)