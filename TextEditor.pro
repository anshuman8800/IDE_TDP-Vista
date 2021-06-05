#-------------------------------------------------
#
# Project created by QtCreator 2015-11-17T21:14:37
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = TextEditor
TEMPLATE = app


SOURCES += main.cpp\
        texteditor.cpp \
        frdialog.cpp

HEADERS  += texteditor.h \
            frdialog.h

FORMS    += texteditor.ui \
            frdialog.ui

RESOURCES += \
    icons.qrc

DISTFILES +=
