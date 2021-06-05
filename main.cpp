#include "texteditor.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
//    QApplication::setStyle(QWindowsStyle);
    TextEditor w;
    w.show();

    return a.exec();
}
