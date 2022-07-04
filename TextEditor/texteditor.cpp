#include "texteditor.h"
#include "ui_texteditor.h"
#include "ui_frdialog.h"
#include "frdialog.h"
#include <QtWidgets>

TextEditor::TextEditor(QWidget *parent) :
    QMainWindow(parent),
    frdialog(NULL),
    ui(new Ui::TextEditor)
{
    ui->setupUi(this);
    readSettings();

    processPaste();

    connect(ui->textEdit, SIGNAL(textChanged()), this, SLOT(textModified()));
    connect(ui->actionQuit, SIGNAL(triggered()), this, SLOT(close()));
    connect(ui->textEdit, SIGNAL(undoAvailable(bool)), ui->actionUndo, SLOT(setEnabled(bool)));
    connect(ui->textEdit, SIGNAL(redoAvailable(bool)), ui->actionRedo, SLOT(setEnabled(bool)));
    connect(ui->textEdit, SIGNAL(copyAvailable(bool)), ui->actionCopy, SLOT(setEnabled(bool)));
    connect(ui->textEdit, SIGNAL(copyAvailable(bool)), ui->actionCut, SLOT(setEnabled(bool)));
    connect(ui->menuEdit, SIGNAL(aboutToShow()), this, SLOT(processPaste()));
    connect(ui->textEdit, SIGNAL(selectionChanged()), this, SLOT(processDelete()));

    setCurrentFile("");
}

TextEditor::~TextEditor()
{
    delete ui;
}

void TextEditor::closeEvent(QCloseEvent *event)
{
    if (okToContinue())
        {
            writeSettings();
            event->accept();
        }
        else
            event->ignore();
}


void TextEditor::on_actionNew_triggered()
{
    if (okToContinue())
    {
        ui->textEdit->clear();
        setCurrentFile("");
        ui->textEdit->document()->setModified(false);
    }
}

void TextEditor::on_actionOpen_triggered()
{
    if (okToContinue())
    {
        QString fileName = QFileDialog::getOpenFileName(this, tr("Open Text files"), ".",
                            tr("*"));

        if (!fileName.isEmpty())
        {
            setCurrentFile(fileName);
            QFile file(fileName);

            if(!file.open(QIODevice::ReadOnly))
            {
                ui->statusBar->showMessage(tr("Could not open file"), 2000);
                QMessageBox::critical(this, tr("Error"), tr("Could not open file"));
                return;
            }

            QTextStream in(&file);
            ui->textEdit->setText(in.readAll());//setText is fnx of textEdit
            file.close();
            ui->textEdit->document()->setModified(false);
        }
    }
}

bool TextEditor::on_actionSave_triggered()
{
    if (curFile.isEmpty())
        return on_actionSave_As_triggered();
    else
        return saveFile(curFile);
}

bool TextEditor::on_actionSave_As_triggered()
{
    QString fileName = QFileDialog::getSaveFileName(this, tr("Save Text File"), "Untitled Document 1",
                                                          tr("Text files (*.txt)"));
    if (fileName.isEmpty())
        return false;

    return saveFile(fileName);
}

void TextEditor::on_actionDelete_triggered()
{
    ui->textEdit->textCursor().removeSelectedText();
}


bool TextEditor::okToContinue()
{
    if (ui->textEdit->document()->isModified())
    {
        int r = QMessageBox::warning(this, tr("TextEditor"),
                                     tr("The document has been modified.\n"
                                        "Do you want to save your changes?"),
                                     QMessageBox::Yes | QMessageBox::No | QMessageBox::Cancel);
        if (r == QMessageBox::Yes)
            return on_actionSave_triggered();
        else if (r == QMessageBox::Cancel)
            return false;
    }
    return true;
}

bool TextEditor::saveFile(const QString &fileName)
{
    QFile file(fileName);

    if (!file.open(QIODevice::WriteOnly))
    {
        ui->statusBar->showMessage(tr("Saving failed"), 2000);
        QMessageBox::critical(this, tr("Error"), tr("Could not save file"));
        return false;
    }

    QTextStream stream(&file);
    stream << ui->textEdit->toPlainText();
    stream.flush();
    file.close();

    setCurrentFile(fileName);
    ui->statusBar->showMessage(tr("Saved"), 2000);

    ui->textEdit->document()->setModified(false);
    return true;
}

void TextEditor::setCurrentFile(const QString &fileName)
{
    curFile = fileName;
    QString shownName = QFileInfo(curFile).fileName();
    if (shownName.isEmpty())
        setWindowTitle(tr("Untitled Document 1 [*] - TextEditor"));
    else
        setWindowTitle(shownName + tr(" [*] - TextEditor"));
    setWindowModified(false);
}

void TextEditor::textModified()
{
    setWindowModified(true);
    ui->actionSelectAll->setEnabled(!(ui->textEdit->toPlainText().isEmpty()));
}

void TextEditor::readSettings()
{
    QSettings settings("TDPVista", "Text-Editor");

    restoreGeometry(settings.value("geometry").toByteArray());
    restoreState(settings.value("windowState").toByteArray());
}

void TextEditor::writeSettings()
{
    QSettings settings("TDPVista", "TextEditor");

    settings.setValue("geometry", saveGeometry());
    settings.setValue("windowState", saveState());
}


void TextEditor::on_actionFind_Replace_triggered()
{
    if (!frdialog)
    {
        frdialog = new FRDialog(this);
        connect(frdialog, SIGNAL(find(const QString &, bool, bool)),
                                this, SLOT(find(const QString &, bool, bool)));
        connect(frdialog, SIGNAL(replace(QString,QString,bool,bool)),
                            this, SLOT(replace(const QString, const QString, bool, bool)));
        connect(frdialog, SIGNAL(replaceAll(QString,QString,bool,bool)),
                            this, SLOT(replaceAll(const QString, const QString, bool, bool)));
        connect(frdialog, SIGNAL(unhighlight()), this, SLOT(unhighlight()));
    }

    frdialog->show();
    frdialog->raise();
    frdialog->activateWindow();
}

void TextEditor::processPaste()
{
    if (const QMimeData *md = QApplication::clipboard()->mimeData())
        ui->actionPaste->setEnabled(md->hasText());
}

void TextEditor::processDelete()
{
    ui->actionDelete->setEnabled(!ui->textEdit->textCursor().selectedText().isEmpty());
}


void TextEditor::highlightText(const QString &str, const QTextDocument::FindFlags &flags)
{
    static QTextCursor origCur;
    origCur = ui->textEdit->textCursor();

    static QTextEdit::ExtraSelection extra;

    ui->textEdit->moveCursor(QTextCursor::Start);
    extra.format.setBackground(QColor(Qt::yellow).lighter(140));

    while (ui->textEdit->find(str, flags))
    {
        extra.cursor = ui->textEdit->textCursor();
        extraSelections.append(extra);
    }

    ui->textEdit->setExtraSelections(extraSelections);
    ui->textEdit->setTextCursor(origCur);
}


void TextEditor::find(const QString &str, bool cs, bool backwards)
{   
    QTextDocument::FindFlags flags;
    if (cs)
        flags |= QTextDocument::FindCaseSensitively;

    highlightText(str, flags);

    if (backwards)
        flags |= QTextDocument::FindBackward;

    if (ui->textEdit->find(str, flags))
        frdialog->ui->replaceButton->setEnabled(true);
    else
    {
        QApplication::beep();
        if (backwards)
            ui->textEdit->moveCursor(QTextCursor::End);
        else
            ui->textEdit->moveCursor(QTextCursor::Start);

        if (ui->textEdit->find(str, flags))
            frdialog->ui->replaceButton->setEnabled(true);
    }

}

void TextEditor::replace(const QString &str, const QString &rplcStr, bool cs, bool backwards)
{
    ui->textEdit->insertPlainText(rplcStr);
    find(str, cs, backwards);
}

void TextEditor::replaceAll(const QString &str, const QString rplcStr, bool cs, bool backwards)
{
    ui->textEdit->moveCursor(QTextCursor::Start);

    QTextDocument::FindFlags flags;
    if (cs)
        flags |= QTextDocument::FindCaseSensitively;

    if (backwards)
        flags |= QTextDocument::FindBackward;

    while (ui->textEdit->find(str, flags))
    {
        ui->textEdit->insertPlainText(rplcStr);
    }

    highlightText(rplcStr, flags);
}


void TextEditor::unhighlight()
{
    extraSelections = QList<QTextEdit::ExtraSelection>();
    ui->textEdit->setExtraSelections(extraSelections);
}
