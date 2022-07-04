#ifndef FRDIALOG_H
#define FRDIALOG_H

#include <QDialog>

namespace Ui {
class FRDialog;
}

class FRDialog : public QDialog
{
    Q_OBJECT
    friend class TextEditor;
public:
    explicit FRDialog(QWidget *parent = 0);
    ~FRDialog();

signals:
    void unhighlight();

    void find(const QString &str, bool cs, bool backwards);
    void replace(const QString &find, const QString &rplc, bool cs, bool backwards);
    void replaceAll(const QString &find, const QString &rplc, bool cs, bool backwards);

protected:
    void closeEvent(QCloseEvent *event);
private slots:
    void on_findButton_clicked();

    void on_findEdit_textChanged(const QString &arg1);

    void on_replaceButton_clicked();

    void on_replaceAllButton_clicked();

private:
    Ui::FRDialog *ui;
};

#endif // FRDIALOG_H
