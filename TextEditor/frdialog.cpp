#include "frdialog.h"
#include "ui_frdialog.h"
#include <QtWidgets>
FRDialog::FRDialog(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::FRDialog)
{
    ui->setupUi(this);
}

void FRDialog::closeEvent(QCloseEvent *event)
{
    emit unhighlight();
    event->accept();
}

FRDialog::~FRDialog()
{
    delete ui;
}

void FRDialog::on_findButton_clicked()
{
    emit unhighlight();
    emit find(ui->findEdit->text(), ui->matchCase->isChecked(), ui->searchBack->isChecked());
}

void FRDialog::on_findEdit_textChanged(const QString &arg1)
{
    ui->findButton->setEnabled(!arg1.isEmpty());
    ui->replaceAllButton->setEnabled(!arg1.isEmpty());
    emit unhighlight();
}

void FRDialog::on_replaceButton_clicked()
{
    emit replace(ui->findEdit->text(), ui->replaceEdit->text(), ui->matchCase->isChecked(), ui->searchBack->isChecked());
}

void FRDialog::on_replaceAllButton_clicked()
{
    emit replaceAll(ui->findEdit->text(), ui->replaceEdit->text(), ui->matchCase->isChecked(), ui->searchBack->isChecked());
    emit unhighlight();
}
