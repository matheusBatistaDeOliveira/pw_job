using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net.NetworkInformation;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Reflection.Emit;
using System.Reflection;
using System.Diagnostics;
using System.IO;
using System.Data.OleDb;
using static System.Windows.Forms.LinkLabel;
using static System.Windows.Forms.AxHost;
using System.Windows.Forms.VisualStyles;

namespace COBWMS0026_COB_PAINEL_ESTOQUE_CD_2097
{
    public partial class Form1 : Form
    {
        int p_ini = 0;
        int p_fin = 0;
        string dini = String.Empty;
        string dfim = string.Empty;
        string login = "";
        public int acao = 0;
        public int prodclassMerc = 0;
        string filial = string.Empty;
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {


            login = System.Environment.UserName;
            login = login.Replace(".", "");
            login = login.Substring(0, 5);
            login = login.ToUpper();
            Assembly dcomp = Assembly.GetExecutingAssembly();
            string nome = dcomp.GetName(true).Name.ToString();
            string versao = dcomp.GetName().Version.ToString();

            // checkBoxfiltro.Location = new System.Drawing.Point(1156, 3);
            // dataGridViewsldcd.Location = new System.Drawing.Point(3, 25);
            // dataGridViewsldcd.Size = new System.Drawing.Size(1338, 564);

            toolStripStatusLabelversao.Text = nome + " v" + versao;
            textBoxfilial.Text = "2097";



        }


        //Funções
        private void Saldo_Estoque_csv()
        {
            if (dataGridViewsldcd.RowCount == 0)
            {
                MessageBox.Show("Não Há dados para exportar");
                return;
            }


            if (folderBrowserDialog1.ShowDialog() == DialogResult.Cancel)
            {
                return;
            }


            string caminho = folderBrowserDialog1.SelectedPath;

            File.Delete(caminho + "\\REL SALDO ESTOQUE.csv");

            int cont = dataGridViewsldcd.Rows.Count;
            int numcol = dataGridViewsldcd.Columns.Count;
            int columcont = 0;
            string Data = string.Empty;


            StreamWriter arqcsv = new StreamWriter(caminho + "\\REL SALDO ESTOQUE.csv", true);


            while (columcont < numcol)
            {
                if (columcont == numcol - 1)
                {
                    arqcsv.Write(dataGridViewsldcd.Columns[columcont].HeaderText + ";\n");
                }
                else if (columcont < numcol - 1)
                {
                    arqcsv.Write(dataGridViewsldcd.Columns[columcont].HeaderText + ";");
                }

                columcont++;
            }

            for (int i = 0; i < cont; i++)
            {

                columcont = 0;

                while (columcont < numcol)
                {

                    if (columcont == numcol - 1)
                    {
                        arqcsv.Write(dataGridViewsldcd.Rows[i].Cells[columcont].Value.ToString() + ";\n");
                    }
                    else if (columcont < numcol - 1)
                    {

                        if (columcont == 0 || columcont == 1 || columcont == 2)
                        {

                            Data = dataGridViewsldcd.Rows[i].Cells[columcont].Value.ToString();
                            Data = Data.Replace("00:00:00", "");



                            arqcsv.Write(Data + ";");
                        }

                        else if (columcont != 0 || columcont != 1 || columcont != 2)
                        {
                            arqcsv.Write(dataGridViewsldcd.Rows[i].Cells[columcont].Value.ToString() + ";");
                        }


                    }

                    columcont++;
                }

            }

            arqcsv.Close();

            MessageBox.Show("Arquivo gerado" + "   " + caminho + "\\REL SALDO ESTOQUE.csv");
            Process.Start(caminho + "\\REL SALDO ESTOQUE.csv");


        }

        private void Saldo_Estoque_excell()
        {

            try
            {



                if (dataGridViewsldcd.RowCount == 0)
                {
                    MessageBox.Show("Não Há dados para exportar");
                    return;
                }
                string Data = string.Empty;
                this.Cursor = Cursors.WaitCursor;

                Microsoft.Office.Interop.Excel.Application app = new Microsoft.Office.Interop.Excel.Application();

                app.Application.Workbooks.Add(Type.Missing);

                for (int i = 1; i < dataGridViewsldcd.Columns.Count + 1; i++)
                {
                    app.Cells[1, i] = dataGridViewsldcd.Columns[i - 1].HeaderText;
                }



                for (int i = 0; i < dataGridViewsldcd.Rows.Count; i++)
                {
                    for (int j = 0; j < dataGridViewsldcd.Columns.Count; j++)
                    {



                        if (dataGridViewsldcd.Rows[i].Visible == true)
                        {
                            if (j == 0 || j == 1 || j == 2)
                            {
                                Data = dataGridViewsldcd.Rows[i].Cells[j].Value.ToString();
                                Data = Data.Replace("00:00:00", "");

                                app.Cells[i + 2, j + 1] = "'" + Data;
                            }
                            else
                            {

                                app.Cells[i + 2, j + 1] = dataGridViewsldcd.Rows[i].Cells[j].Value.ToString();
                            }


                        }


                    }
                }
                app.Columns.AutoFit();
                app.Visible = true;

                app.ActiveWorkbook.Saved = false;

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                this.Cursor = Cursors.Default;
            }
            this.Cursor = Cursors.Default;

        }

        //Função gera estoque
        private void gera_saldoestoque()
        {
            string linha = string.Empty;
            string depto = string.Empty;
            string secao = string.Empty;
            string grupo = string.Empty;
            string subgrupo = string.Empty;
            string sku = string.Empty;
            string forn = string.Empty;
            string tpoprod = string.Empty;
            string stat = string.Empty;
            string estq = string.Empty;
            string estqc = string.Empty;
            string estqw = string.Empty;

            if (textBoxdepto.Text != string.Empty)
            {
                depto = " in (" + textBoxdepto.Text + ") ";
            }
            else
            {
                depto = " >=0 ";
            }

            if (textBoxlinha.Text != string.Empty)
            {
                linha = " in (" + textBoxlinha.Text + ") ";
            }
            else
            {
                linha = " >=0 ";
            }

            if (textBoxsec.Text != string.Empty)
            {
                secao = " in (" + textBoxsec.Text + ") ";
            }
            else
            {
                secao = " >=0 ";
            }

            if (textBoxgrupo.Text != string.Empty)
            {
                grupo = " in (" + textBoxgrupo.Text + ") ";
            }
            else
            {
                grupo = " >=0 ";
            }

            if (textBoxsubgrupo.Text != string.Empty)
            {
                subgrupo = " in (" + textBoxsubgrupo.Text + ") ";
            }
            else
            {
                subgrupo = " >=0 ";
            }

            if (textBoxproduto.Text != string.Empty)
            {
                sku = " in (" + textBoxproduto.Text + ") ";
            }
            else
            {
                sku = " >=0 ";
            }
            if (textBoxforn.Text != string.Empty)
            {
                forn = "in (" + textBoxforn.Text + ") ";
            }
            else
            {
                forn = " >=0 ";
            }

            if (textBoxfilial.Text != string.Empty)
            {
                filial = textBoxfilial.Text;
            }
            else
            {
                filial = "0";
            }

            if (textBoxtpoprod.Text != string.Empty)
            {
                tpoprod = " in (" + textBoxtpoprod.Text + ") ";
            }
            else
            {
                tpoprod = " >=0 ";
            }

            if (checkBoxemlinha.Checked == true & checkBoxforadelinha.Checked == false)
            {

                stat = " =0 ";

            }
            else if (checkBoxemlinha.Checked == false & checkBoxforadelinha.Checked == true)
            {
                stat = " >0 ";


            }
            else if (checkBoxemlinha.Checked == true & checkBoxforadelinha.Checked == true)
            {

                stat = " >=0 ";

            }

            if(radioButtonestqgeral.Checked == false & radioButtoncestq.Checked == true)
            {
                estq = " >0 ";

            }
            if (radioButtonestqgeral.Checked == false & radioButtonsestq.Checked == true)
            {
                estq = " =0 ";

            }

            if (radioButtonestqgeral.Checked == true )
            {
                estq = " >=0 ";

            }

            // if (checkBoxestqcont.Checked == true & radioButtonestqgeral.Checked == false)
            // {
            //     estqc = " >0 ";

            // }
            // else if (checkBoxestqcont.Checked == false & radioButtonestqgeral.Checked == false)
            // {
            //     estqc = " = 0";
            // }

            // if (checkBoxestqwms.Checked == true & radioButtonestqgeral.Checked == false)
            // {
            //    estqw = " >0 ";

            // }
            // else if (checkBoxestqwms.Checked == true & radioButtonestqgeral.Checked == false)
            // {
            //      estqw = " =0 ";
            // }

            //  if (checkBoxestqwms.Checked == false & checkBoxestqcont.Checked == false & radioButtonestqgeral.Checked == true)
            //  {
            estqw = " >=0 ";
            estqc = " >=0 ";

            //}


            this.Cursor = Cursors.WaitCursor;
            string sql = "COB_PROC_PAINELESTQ";
            OleDbConnection con = new OleDbConnection(ClassCon.con);

            OleDbCommand cmd = new OleDbCommand(sql, con);
            con.Open();
            // "in (select git_cod_item||git_digito from aa3citem where git_depto = 90 )";

            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add("linha", OleDbType.VarChar, 600).Value = linha;
            cmd.Parameters.Add("depto", OleDbType.VarChar, 50).Value = depto;
            cmd.Parameters.Add("secao", OleDbType.VarChar, 50).Value = secao;
            cmd.Parameters.Add("grupo", OleDbType.VarChar, 600).Value = grupo;
            cmd.Parameters.Add("subgrupo", OleDbType.VarChar, 600).Value = subgrupo;
            cmd.Parameters.Add("tipoprod", OleDbType.VarChar, 600).Value = tpoprod;
            cmd.Parameters.Add("sku", OleDbType.VarChar, 600).Value = sku;
            cmd.Parameters.Add("forn", OleDbType.VarChar, 600).Value = forn;
            cmd.Parameters.Add("filial", OleDbType.VarChar, 600).Value = filial;
            cmd.Parameters.Add("slinha", OleDbType.VarChar, 100).Value = stat;
            cmd.Parameters.Add("estqc", OleDbType.VarChar, 100).Value = estqc;
            cmd.Parameters.Add("estqw", OleDbType.VarChar, 100).Value = estqw;
            cmd.Parameters.Add("estq", OleDbType.VarChar, 100).Value = estq;
            cmd.Parameters.Add("login", OleDbType.VarChar, 100).Value = login;
            cmd.ExecuteNonQuery();
            con.Close();
            this.Cursor = Cursors.Default;






        }

        //Função lista saldo estoque
        private void func_listasldcd()
        {
            this.Cursor = Cursors.WaitCursor;
            try
            {
                dataGridViewsldcd.DataSource = null;
                string sql = "select * from COB_PROC_PAINELESTQ_" + login;
                OleDbConnection con = new OleDbConnection(ClassCon.con);
                OleDbCommand cmd = new OleDbCommand(sql, con);
                con.Open();
                cmd.CommandType = CommandType.Text;
                OleDbDataAdapter da = new OleDbDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);

                dataGridViewsldcd.DataSource = dt;
                dataGridViewsldcd.Columns[0].Width = 80;
                dataGridViewsldcd.Columns[1].Width = 230;

                con.Close();


            }
            catch (Exception ex)
            {
                // MessageBox.Show("Produto não encontrado");

            }
            this.Cursor = Cursors.Default;

        }

        //Lista Função soma saldo estq
        private void Func_estqsld()
        {

            int totsku = 0;
            decimal total = 0;
            foreach (DataGridViewRow row in dataGridViewsldcd.Rows)
            {
                // Certifique-se de que a linha não é uma linha de cabeçalho ou nova.
                if (!row.IsNewRow)
                {
                    // Tenta converter o valor da célula para decimal. Se não for possível, ignora a linha.
                    if (row.Cells["ESTQ_TOTAL_UN"].Value != null && decimal.TryParse(row.Cells["ESTQ_TOTAL_UN"].Value.ToString(), out decimal valor))
                    {
                        total += valor;
                    }
                }
            }
            // Aqui você pode exibir o valor total em algum controle, como um TextBox.
            textBoxsld2097.Text = total.ToString();
            textBoxtotalsku.Text =dataGridViewsldcd.RowCount.ToString();
            






        }

        //Função fornecedor
        public string forn(string forn)
        {
            try
            {
                string sql = "";


                sql = "select tip_nome_fantasia  from AA2CTIPO where AA2CTIPO.TIP_CODIGO||AA2CTIPO.Tip_Digito =" + forn + "";

                OleDbConnection con = new OleDbConnection(ClassCon.con);
                OleDbCommand cmd = new OleDbCommand(sql, con);
                con.Open();
                cmd.CommandType = CommandType.Text;
                OleDbDataAdapter da = new OleDbDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);


                forn = dt.Rows[0][0].ToString();


                con.Close();


            }
            catch (Exception ex)
            {
                forn = "0";

            }

            return forn;
        }

        //Produto
        public string prod(string item)
        {
            try
            {
                string sql = "";


                sql = " select  git_desc_reduz  from aa3citem where git_cod_item||git_digito  =" + item + "";




                OleDbConnection con = new OleDbConnection(ClassCon.con);
                OleDbCommand cmd = new OleDbCommand(sql, con);
                con.Open();
                cmd.CommandType = CommandType.Text;
                OleDbDataAdapter da = new OleDbDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);


                item = dt.Rows[0][0].ToString();


                con.Close();


            }
            catch (Exception ex)
            {
                item = "0";

            }

            return item;
        }

        private void buttopesquisar_Click(object sender, EventArgs e)
        {
            if (checkBoxemlinha.Checked == false & checkBoxforadelinha.Checked == false)
            {
                MessageBox.Show("Por favor selecionar se o item está em linha ou fora de linha ou marque os dois para trazer todos ");
                return;
            }

           // if (radioButtonsestq.Checked == true & checkBoxestqcont.Checked == false & checkBoxestqwms.Checked == false)
           // {
           //     MessageBox.Show("Por favor selecionar Estoque WMS ou Contábil ");
            //    return;
           // }
           // else if (radioButtoncestq.Checked == true & checkBoxestqcont.Checked == false & checkBoxestqwms.Checked == false)
           // {
            //    MessageBox.Show("Por favor selecionar Estoque WMS ou Contábil ");
            //    return;

           // }
            this.Cursor = Cursors.WaitCursor;
            gera_saldoestoque();
            func_listasldcd();
            Func_estqsld();
            this.Cursor = Cursors.Default;
        }

        private void buttonexpcsv_Click(object sender, EventArgs e)
        {
            this.Cursor = Cursors.WaitCursor;
            Saldo_Estoque_csv();
            this.Cursor = Cursors.Default;
        }

        private void radioButtonestqgeral_CheckedChanged(object sender, EventArgs e)
        {
            checkBoxestqcont.Visible = false;
            checkBoxestqwms.Visible = false;
            checkBoxestqcont.Checked = false;
            checkBoxestqwms.Checked = false;


        }

        private void radioButtonestq_CheckedChanged(object sender, EventArgs e)
        {
            checkBoxestqcont.Visible = true;
            checkBoxestqwms.Visible = true;
            checkBoxestqcont.Checked = true;
            checkBoxestqwms.Checked = true;
        }

        private void textBoxfilial_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == 13)
            {

                if (textBoxfilial.Text == "")
                {
                    labeldesccdfilial.Text = "";
                }
                else
                {

                    labeldesccdfilial.Text = forn(textBoxfilial.Text);
                }

            }
        }
        private void textBoxforn_KeyPress_1(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == 13)
            {


                if (textBoxforn.Text == "")
                {
                    labelfornecedor.Text = "";
                }
                else
                {

                    labelfornecedor.Text = forn(textBoxforn.Text);
                }



            }
        }

        private void textBoxproduto_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == 13)
            {


                if (textBoxproduto.Text == "")
                {
                    labelprod.Text = "";
                }
                else
                {

                    labelprod.Text = prod(textBoxproduto.Text);
                }



            }
        }

        private void checkBoxfiltro_CheckedChanged(object sender, EventArgs e)
        {
            if (checkBoxfiltro.Checked == true)
            {
                panelsldestoque.Visible = true;
                buttonpesquisar.Visible = false;

                checkBoxfiltro.Location = new System.Drawing.Point(1250, 58);
                dataGridViewsldcd.Location = new System.Drawing.Point(3, 100);
                dataGridViewsldcd.Size = new System.Drawing.Size(1338, 490);


            }
            else if (checkBoxfiltro.Checked == false)
            {
                checkBoxfiltro.Location = new System.Drawing.Point(1156, 3);
                dataGridViewsldcd.Location = new System.Drawing.Point(3, 25);
                dataGridViewsldcd.Size = new System.Drawing.Size(1338, 564);
                panelsldestoque.Visible = false;
                buttonpesquisar.Visible = true;

                textBoxlinha.Text = string.Empty;
                textBoxdepto.Text = string.Empty;
                textBoxgrupo.Text = string.Empty;
                textBoxsubgrupo.Text = string.Empty;
                textBoxsec.Text = string.Empty;
                textBoxforn.Text = string.Empty;
                textBoxproduto.Text = string.Empty;
                textBoxfilial.Text = string.Empty;

            }
        }


        private void buttongerar_Click(object sender, EventArgs e)
        {
            if (checkBoxemlinha.Checked == false & checkBoxforadelinha.Checked == false)
            {
                MessageBox.Show("Favor selecionar se o item está em linha ou fora de linha ou marque os dois para trazer todos ");
                return;
            }

            if (radioButtonsestq.Checked == true & checkBoxestqcont.Checked == false & checkBoxestqwms.Checked == false)
            {
                MessageBox.Show("Por favor selecionar Estoque WMS ou Contábil ");
                return;
            }
            else if (radioButtoncestq.Checked == true & checkBoxestqcont.Checked == false & checkBoxestqwms.Checked == false)
            {
                MessageBox.Show("Por favor selecionar Estoque WMS ou Contábil ");
                return;

            }



            this.Cursor = Cursors.WaitCursor;
            gera_saldoestoque();
            func_listasldcd();
            Func_estqsld();
            this.Cursor = Cursors.Default;

        }

        private void buttonfiltar_Click(object sender, EventArgs e)
        {
            if (checkBoxemlinha.Checked == false & checkBoxforadelinha.Checked == false)
            {
                MessageBox.Show("Por favor selecionar se o item está em linha ou fora de linha ou marque os dois para trazer todos ");
                return;
            }

            if (radioButtonsestq.Checked == true & checkBoxestqcont.Checked == false & checkBoxestqwms.Checked == false)
            {
                MessageBox.Show("Por favor selecionar Estoque WMS ou Contábil ");
                return;
            }
            else if (radioButtoncestq.Checked == true & checkBoxestqcont.Checked == false & checkBoxestqwms.Checked == false)
            {
                MessageBox.Show("Por favor selecionar Estoque WMS ou Contábil ");
                return;

            }
            this.Cursor = Cursors.WaitCursor;
            gera_saldoestoque();
            func_listasldcd();
            Func_estqsld();
            this.Cursor = Cursors.Default;

        }

        private void buttonpesquisar_Click(object sender, EventArgs e)
        {
            if (checkBoxemlinha.Checked == false & checkBoxforadelinha.Checked == false)
            {
                MessageBox.Show("Por favor selecionar se o item está em linha ou fora de linha ou marque os dois para trazer todos ");
                return;
            }

            //if (radioButtonsestq.Checked == true & checkBoxestqcont.Checked == false & checkBoxestqwms.Checked == false)
            //{
            //    MessageBox.Show("Por favor selecionar Estoque WMS ou Contábil ");
            //    return;
           // }
           // else if (radioButtoncestq.Checked == true & checkBoxestqcont.Checked == false & checkBoxestqwms.Checked == false)
           // {
            //    MessageBox.Show("Por favor selecionar Estoque WMS ou Contábil ");
             //   return;

           // }
            this.Cursor = Cursors.WaitCursor;
            gera_saldoestoque();
            func_listasldcd();
            Func_estqsld();
            this.Cursor = Cursors.Default;
        }

        private void radioButtoncestq_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void radioButtonestqgeral_CheckedChanged_1(object sender, EventArgs e)
        {

        }

        private void radioButtonsestq_CheckedChanged(object sender, EventArgs e)
        {

        }
    }
}
