using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;
using System.Runtime.InteropServices;

namespace TextEditor  
{
    public partial class EditorScreen : Form
    {
       
        public const string CppFunctionsDLL = @"C:\Users\harig\source\repos\Temp\Debug\Temp.dll";
        [DllImport(CppFunctionsDLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern string changeColor(string inp);



        public EditorScreen()
        {
            InitializeComponent();
        }

        private void toolStrip1_ItemClicked(object sender, ToolStripItemClickedEventArgs e)
        {
            //System.Text.StringBuilder messageBoxCS = new System.Text.StringBuilder();
            //messageBoxCS.AppendFormat("{0} = {1}", "ClickedItem", e.ClickedItem);
            //messageBoxCS.AppendLine();
            //MessageBox.Show(messageBoxCS.ToString(), "ItemClicked Event");
        }

        private void newToolStripButton_Click(object sender, EventArgs e)
        {
            inputText.Clear();
        }

        // this opens a file and reads to richtextbox1
        private void openToolStripButton_Click(object sender, EventArgs e)
        {
            OpenFileDialog openfile = new OpenFileDialog();
            openfile.Title = "My open file dialog";
            if (openfile.ShowDialog() == DialogResult.OK)
            {
                inputText.Clear();
                using (StreamReader sr = new StreamReader(openfile.FileName))
                {
                    inputText.Text = sr.ReadToEnd();
                    sr.Close();
                }
            }
        }

        // this saves richtextbox1 to a file
        private void saveToolStripButton_Click(object sender, EventArgs e)
        {
            SaveFileDialog savefile = new SaveFileDialog();
            savefile.Title = "Save file as..";
            if (savefile.ShowDialog() == DialogResult.OK)
            {
                StreamWriter txtoutput = new StreamWriter(savefile.FileName);
                txtoutput.Write(inputText.Text);
                txtoutput.Close();
            }
        }

        // this cuts all text highlighted
        private void cutToolStripButton_Click(object sender, EventArgs e)
        {
            inputText.Cut();
        }

        // this copies all text highlighted
        private void copyToolStripButton_Click(object sender, EventArgs e)
        {
            inputText.Copy();
        }

        // this pastes the cut or copied text
        private void pasteToolStripButton_Click(object sender, EventArgs e)
        {
            inputText.Paste();
        }

        // this undo's
        private void undoButton_Click(object sender, EventArgs e)
        {
            inputText.Undo();
        }

        // this redo's
        private void redoButton_Click(object sender, EventArgs e)
        {
            inputText.Redo();
        }

        // this highlights all text
        private void selectAllButton_Click(object sender, EventArgs e)
        {
            inputText.SelectAll();
        }

        // this calls the new button
        private void newToolStripMenuItem_Click(object sender, EventArgs e)
        {
            newToolStripButton.PerformClick();
        }

        // this calls the open button
        private void openToolStripMenuItem_Click(object sender, EventArgs e)
        {
            openToolStripButton.PerformClick();
        }

        // this calls the save button
        private void saveAsToolStripMenuItem_Click(object sender, EventArgs e)
        {
            saveToolStripButton.PerformClick();
        }

        // this exits the program
        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        // this calls the undo button
        private void undoToolStripMenuItem_Click(object sender, EventArgs e)
        {
            undoButton.PerformClick();
        }

        // this calls the redo button
        private void redoToolStripMenuItem_Click(object sender, EventArgs e)
        {
            redoButton.PerformClick();
        }

        // this calls the cut button
        private void cutToolStripMenuItem_Click(object sender, EventArgs e)
        {
            cutToolStripButton.PerformClick();
        }

        // this calls the copy button
        private void copyToolStripMenuItem_Click(object sender, EventArgs e)
        {
            copyToolStripButton.PerformClick();
        }

        // this calls the paste button
        private void pasteToolStripMenuItem_Click(object sender, EventArgs e)
        {
            pasteToolStripButton.PerformClick();
        }

        // this calls the select all button
        private void selectAllToolStripMenuItem_Click(object sender, EventArgs e)
        {
            selectAllButton.PerformClick();
        }

        private void colorChangeButton_Click(object sender, EventArgs e)
        {
            string keyword_textFile = "C:\\Users\\harig\\source\\repos\\TextEditor\\TextEditor\\ListOfKeywords.txt";
            string[] keywords = File.ReadAllLines(keyword_textFile);
            if (File.Exists(keyword_textFile))
            {
                foreach (string word in keywords)
                {
                        if (inputText.Text.Contains(word))
                        {
                            inputText.Select(inputText.Text.IndexOf(word), word.Length);
                            inputText.SelectionColor = Color.Red;
                        }
                  
                }
            }
            string preprocessor_textFile = "C:\\Users\\harig\\source\\repos\\TextEditor\\TextEditor\\ListOfPreprocessKeywords.txt";
            string[] preprocessor_keywords = File.ReadAllLines(preprocessor_textFile);
            if (File.Exists(preprocessor_textFile))
            {
                foreach (string word in preprocessor_keywords)
                {
                    if (inputText.Text.Contains(word))
                    {
                        inputText.Select(inputText.Text.IndexOf(word), word.Length);
                        inputText.SelectionColor = Color.Blue;
                    }

                }
            }
            //inputText.Clear();
            //TextRange textRange = newTextRange(rtb.Document.ContentStart, inputText.ContentEnd);
            //string[] RichTextBoxLines = inputText.Lines;
            //inputText.Clear();
            //foreach (string line in RichTextBoxLines)
            //{
              //  string a = changeColor(line);
               // inputText.AppendText(a);
            //}
            
        }

        private void menuStrip1_ItemClicked(object sender, ToolStripItemClickedEventArgs e)
        {

        }

        private void EditorScreen_Load(object sender, EventArgs e)
        {
            //MyStruct ms = new MyStruct();
            //ms.a = 5;
            //ms.b = 10;

            //int output = AddNumbers(ms);
            //MessageBox.Show(output.ToString(), "ItemClicked Event");

            //Console.WriteLine($"Output is: {output}");

        }

        private void richTextBox1_TextChanged(object sender, EventArgs e)
        {
            

        }
    }
}
