# -*- coding: utf-8 -*-
import pylab

from matplot_area import *
import matplotlib.dates as mdate
class MatplotWindow(QWidget):
    def __init__(self, data=None,datamaps=None,parent=None):
        super(MatplotWindow,self).__init__(parent)
        mainlayout=QHBoxLayout()
        vboxlayout =QVBoxLayout()
        self.view=MatplotArea(self)
        self.view.setVisible(True)
#        self.view.setGeometry(0,0,self.width(),self.height())
        self.data=data
        self.datamaps=datamaps
        print self.data,self.datamaps
        self.selected_text=data
        self.resize(QSize(800,600))
        self.setWindowTitle(u'日志可视化')


        self.pListWidget =QListWidget(self)
        self.redrawBtn =QPushButton(self)
        self.redrawBtn.setText(u"重绘")
        self.redrawBtn.clicked.connect(self.drawPlot)

        for i,data in enumerate(data):
            pItem = QListWidgetItem(self.pListWidget)
            self.pListWidget.addItem(pItem)
            pItem.setData(Qt.UserRole, i)
            pCheckBox = QCheckBox(self)
            if isinstance(data, str):
                pCheckBox.setText(data)
            else:
                pCheckBox.setText(str(data))
            pCheckBox.setChecked(True)      
            self.pListWidget.addItem(pItem)
            self.pListWidget.setItemWidget(pItem, pCheckBox)
            self.connect(pCheckBox, SIGNAL('stateChanged(int)'), self.stateChanged)
        vboxlayout.addWidget(QLabel(u'选择监控参数'))
        vboxlayout.addWidget(self.redrawBtn)
        vboxlayout.addWidget(self.pListWidget)
        mainlayout.addLayout(vboxlayout)
        mainlayout.addWidget(self.view)
        mainlayout.setStretch(0,1)
        mainlayout.setStretch(1,4)
        self.setLayout(mainlayout)
#        self.on_clicked()
    def stateChanged(self,state):
        object =self.sender()
        if state==2:
            self.selected_text.append(str(object.text()))
        elif state == 0:
            self.selected_text.remove(str(object.text()))
    def set_data(self,data):
        self.data=data
    def sizeHint(self):
        return QSize(1024,768)
    def drawPlot(self):
        #图例样式表
        style_sheet= ['o-','o--', 'p-','p--','s-','s--','*-','*--', 'x-','x--']
        fig=self.view.figure
        fig.clear()
        ax=fig.add_subplot(111)
        ax.set_xlabel('Time')
        ax.xaxis.set_major_formatter(mdate.DateFormatter('%H:%M:%S'))#设置时间标签显示格式
        plt.xticks(rotation=90)
        # time = getattr(dataListener,"time")
        for index,name in enumerate(self.selected_text) :
            data_item = self.datamaps.get(name)
            ax.plot_date(pylab.date2num(data_item.getTimes()),data_item.getData(), style_sheet[index%10],label=data_item.getName())
            ax.legend()
        self.view.canvas.draw()
