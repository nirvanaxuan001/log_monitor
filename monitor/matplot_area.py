# -*- coding:utf-8 -*-
import sys
from PyQt4.QtCore import *
from PyQt4.QtGui import *

from matplotlib.backends.backend_qt4agg import FigureCanvasQTAgg as FigureCanvas
import matplotlib as mpl
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import *
import numpy as np
from mpl_toolkits.mplot3d import proj3d
from matplotlib.backends.backend_qt4 import NavigationToolbar2QT as NavigationToolbar

class MatplotArea(QWidget):
    def __init__(self, parent=None):
        # mpl.rc('figure.subplot', wspace=0.3, hspace=0.6)
        mpl.rc('axes', grid=True)
        mpl.rcParams['font.sans-serif']=['SimHei'] #用来正常显示中文标签
        mpl.rcParams['axes.unicode_minus']=False #用来正常显示负号
        # mpl.rc('xtick', labelsize=20) #设置坐标轴刻度显示大小
        # mpl.rc('ytick', labelsize=20)
        super(MatplotArea, self).__init__(parent)

        self.figure = plt.figure()

          
        self.canvas = FigureCanvas(self.figure)
        self.canvas.mpl_connect('motion_notify_event', self.UpdateStatusBar)
        self.toolbar = NavigationToolbar(self.canvas, self)
        self.statusBar = QStatusBar(self)
        self.statusBar.setFixedHeight(10)
        self.layout = QVBoxLayout()
        self.figure.set_facecolor('white')
        self.layout.addWidget(self.toolbar)
        self.layout.addWidget(self.canvas)
        self.layout.addWidget(self.statusBar)
        self.resize(QSize(800,800))
        self.setLayout(self.layout)
        


    def UpdateStatusBar(self, event):
        if event.inaxes:
            x, y= event.xdata, event.ydata

            if hasattr(self.figure.gca(),'M'):
                p = (x, y)
                edges = self.figure.gca().tunit_edges()
            #lines = [proj3d.line2d(p0,p1) for (p0,p1) in edges]
                ldists = [(proj3d.line2d_seg_dist(p0, p1, p), i) for \
                    i, (p0, p1) in enumerate(edges)]
                ldists.sort()
            # nearest edge
                edgei = ldists[0][1]

                p0, p1 = edges[edgei]

            # scale the z value to match
                x0, y0, z0 = p0
                x1, y1, z1 = p1
                d0 = np.hypot(x0-x, y0-y)
                d1 = np.hypot(x1-x, y1-y)
                dt = d0+d1
                z = d1/dt * z0 + d0/dt * z1

                x, y, z = proj3d.inv_transform(x, y, z, self.figure.gca().M)



                self.statusBar.showMessage("x= " + str(x) +
                                               "  y=" +str(y)+"  z="+str(z))
            else:
                self.statusBar.showMessage("x= " + str(x) +
                                           "  y=" +str(y))

    def mouseMoveEvent(self, e):
        if e.buttons() != Qt.LeftButton:
            return
        mimeData = QMimeData()
        # simple string with 'x,y'

        # let's make it fancy. we'll show a "ghost" of the button as we drag
        # grab the button to a pixmap
        pixmap = QPixmap.grabWidget(self.canvas)
        image=QImage(pixmap)
        mimeData.setImageData(pixmap)
        # below makes the pixmap half transparent
        painter = QPainter(pixmap)
        painter.setCompositionMode(painter.CompositionMode_DestinationIn)
        painter.fillRect(pixmap.rect(), QColor(0, 0, 0, 127))
        painter.end()

        # make a QDrag
        drag = QDrag(self.canvas)
        # put our MimeData
        drag.setMimeData(mimeData)
        # set its Pixmap
        drag.setPixmap(pixmap)

        # shift the Pixmap so that it coincides with the cursor position
        drag.setHotSpot(e.pos())

        # start the drag operation
        # exec_ will return the accepted action from dropEvent
        if drag.exec_(Qt.CopyAction |Qt.MoveAction) == Qt.MoveAction:
            print 'moved'
        else:
            print 'copied'
        return QWidget.mouseMoveEvent(self, e)
