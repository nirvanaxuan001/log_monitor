# -*- coding: utf-8 -*-
import re

import pylab
from PyQt4 import QtCore, QtGui
import sys

from xlwt import Workbook

from Re import re_tools
from data_map import DataItem
from matplot_window import MatplotWindow
from preference_widget import PreferenceWindow
import matplotlib.pyplot as plt
import matplotlib.dates as mdate
def parseFile(file_re):
    pattern_map = {}
    datamaps = {}
    timeFormat=file_re.readline()[:-1]
    temp = list(timeFormat)
    temp.insert(1,"^")
    startWithTime="".join(temp)
    for line in file_re.readlines():   #创建解码数据结构
        key,value = line.split()
        pattern_map[key]=startWithTime+value
        datamap = DataItem(key)
        datamaps[key] = datamap
    return timeFormat,pattern_map,datamaps
def logFile(file,timeFormat,pattern_map,datamaps):
    time=timeFormat
    datamap = datamaps
    alllines=file.read()
    # lines = re.split("(\d+-\d+-\d+\s)",alllines)
    lines = re.split(time,alllines)
    line_iter=iter(lines[1::])
    lines=map(lambda x:x[0]+x[1] ,zip(line_iter,line_iter))
    for line in lines:
        object=[]
        for key in pattern_map.keys():
            pattern =pattern_map.get(key)
            object =  re_tools(pattern,line)
            if object!=None and len(object)==2:
                datamap[key].update(object)
    return datamap
def drawPlot(window,datamaps):
    #图例样式表
    style_sheet= ['o-','o--', 'p-','p--','s-','s--','*-','*--', 'x-','x--']
    fig=window.view.figure
    ax=fig.add_subplot(111)
    ax.set_xlabel('Time')
    ax.xaxis.set_major_formatter(mdate.DateFormatter('%H:%M:%S'))#设置时间标签显示格式
    plt.xticks(rotation=90)
    # time = getattr(dataListener,"time")
    for index,data_item in enumerate(datamaps.values()) :
        ax.plot_date(pylab.date2num(data_item.getTimes()),data_item.getData(), style_sheet[index%10],label=data_item.getName(),markeredgewidth=0.5)
        ax.legend()
    window.view.canvas.draw()
def outputXLS(parentPath,datamaps):
    w = Workbook()
    ws=w.add_sheet('test',cell_overwrite_ok=True)
    for col,data_name in enumerate(datamaps.keys()):
        data_item=datamaps.get(data_name)
        ws.write(0,col*2,data_name)
        for row,time in enumerate(data_item.getTimes()) :
            ws.write(row+1,col*2,time.strftime("%Y-%m-%d %H:%M:%S"))
        for row,data in enumerate(data_item.getData()) :
            ws.write(row+1,col*2+1,data)
    w.save(parentPath+"/output.xls")
if __name__ == "__main__":
    app=QtGui.QApplication(sys.argv)
    pattern_path,path,ok= PreferenceWindow().getResult()  #获取参数
    file_re = open(pattern_path,"r")  #读解码文件
    timeFormat,pattern_map,datamaps = parseFile(file_re)


    file = open(path,"r")   #读日志文件
    datamaps= logFile(file,timeFormat,pattern_map,datamaps)

    # window =MatplotArea()
    window = MatplotWindow(pattern_map.keys(),datamaps)
    drawPlot(window,datamaps)
    window.show()
    #输出xls
    parentPath =path[:path.rindex("/")]
    outputXLS(parentPath,datamaps)

    file_re.close()
    file.close()
    sys.exit(app.exec_())