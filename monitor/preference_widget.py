# -*- coding: utf-8 -*-
from PyQt4.QtCore import SIGNAL, SLOT, Qt
from PyQt4.QtGui import QWidget, QLabel, QLineEdit, QFormLayout, QPushButton, QDialog, QDialogButtonBox, QFileDialog, \
    QHBoxLayout, QMessageBox


class PreferenceWindow(QDialog):
    def __init__(self,parent=None):
        super(PreferenceWindow,self).__init__(parent)
        self.initUI()
    def initUI(self):

        mainlayout = QFormLayout()
        lbre = QLabel(u"解码文件路径：",self)
        self.qlere = QLineEdit(self)
        lbpath = QLabel(u"日志文件路径：",self)
        self.qlepath = QLineEdit(self)

        rbtn = QPushButton(u"选择解码文件")
        rbtn.clicked.connect(self.msg)

        fbtn = QPushButton(u"选择日志文件")
        fbtn.clicked.connect(self.msg)

        buttons = QDialogButtonBox(
        QDialogButtonBox.Ok | QDialogButtonBox.Cancel,
        Qt.Horizontal, self)
        buttons.accepted.connect(self.accept)
        buttons.rejected.connect(self.reject)

        parseWidget=QWidget(self)
        parseLayout = QFormLayout()
        parseLayout.addRow(self.qlere,rbtn)
        parseWidget.setLayout(parseLayout)

        fileWidget = QWidget(self)
        fileLayout = QFormLayout()
        fileLayout.addRow(self.qlepath,fbtn)
        fileWidget.setLayout(fileLayout)


        mainlayout.addRow(lbre,parseWidget)
        mainlayout.addRow(lbpath,fileWidget)
        mainlayout.addRow(buttons)
        self.setLayout(mainlayout)

    def msg(self):
        fileName1 = QFileDialog.getOpenFileName(self,
                                              "选取文件",
                                              "C:/",
                                              "All Files (*);;Text Files (*.txt)")   #设置文件扩展名过滤,注意用双分号间隔
        sender = self.sender()
        if sender.text()==u"选择日志文件":
            self.qlepath.setText(fileName1)
        else:
            self.qlere.setText(fileName1)

    def getPattern(self):
        self.pattern = str(self.qlere.text())
        return self.pattern
    def getPath(self):
        self.path = str(self.qlepath.text())
        return self.path
    # def getNameList(self):
    #     names = self.qlename.text()
    #     self.name_list =str(names).split(",")
    #     return self.name_list
    @staticmethod
    def getResult(parent=None):
        dialog = PreferenceWindow(parent)
        result = dialog.exec_()
        return (dialog.getPattern(),dialog.getPath(),result == QDialog.Accepted)
