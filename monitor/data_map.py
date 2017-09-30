from datetime import datetime


class DataItem():

    def __init__(self,name):
        self.time = []
        self.name = name
        self.data = []
    def update(self,list):
        self.time.append(datetime.strptime(list[0], "%Y-%m-%d %H:%M:%S"))
        self.data.append(list[1])
    def getTimes(self):
        return self.time
    def getData(self):
        return self.data
    def getName(self):
        return self.name