#ifndef TEXTEDITOR_H
#define TEXTEDITOR_H

#include <QMainWindow>
#include <QTextEdit>

class FRDialog;


namespace Ui {
class TextEditor;
}

class TextEditor : public QMainWindow
{
    Q_OBJECT

public:
    explicit TextEditor(QWidget *parent = 0);
    ~TextEditor();

public slots:
    void textModified();

protected:
    void closeEvent(QCloseEvent *event);

private slots:
    void unhighlight();
    void processPaste();
    void processDelete();

    void find(const QString &str, bool cs, bool backwards);
    void replace(const QString &str, const QString &rplcStr, bool cs, bool backwards);
    void replaceAll(const QString &str, const QString rplcStr, bool cs, bool backwards);

    void on_actionNew_triggered();

    void on_actionOpen_triggered();

    bool on_actionSave_triggered();

    bool on_actionSave_As_triggered();

    void on_actionDelete_triggered();

    void on_actionFind_Replace_triggered();

   // void on_actionAbout_Text_triggered();

private:
    bool okToContinue();
    bool saveFile(const QString &fileName);
    void setCurrentFile(const QString &fileName);
    void highlightText(const QString &str, const QTextDocument::FindFlags &flags);

    void readSettings();
    void writeSettings();

    QString curFile;
    FRDialog *frdialog;

    QList<QTextEdit::ExtraSelection> extraSelections;

    Ui::TextEditor *ui;
};

#endif // TEXTEDITOR_H
