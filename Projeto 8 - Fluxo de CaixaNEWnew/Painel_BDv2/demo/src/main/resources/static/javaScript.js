    let tabelaOrigin = document.getElementById('table-original');

    document.addEventListener('DOMContentLoaded', function() {
    const botoes = document.querySelectorAll('input[type="button"]');
    const tabelas = document.querySelectorAll('.table');

    tabelas.forEach(tabela => {
            tabela.classList.add('table-hidden');
    });

    botoes.forEach(botao => {
        botao.addEventListener('click', function() {
            const targetId = this.dataset.target;
            const tabelaAlvo = document.getElementById(targetId);

            tabelas.forEach(tabela => {
                if (tabela !== tabelaAlvo) {
                    tabela.classList.add('table-hidden');
                }
            });

            if (tabelaAlvo) {
                tabelaAlvo.classList.toggle('table-hidden');
            }
        });
    });
});

     HTMLTableElement.prototype.exportCSV = function () {
    let csv = '';

    this.querySelectorAll('tr').forEach(tr => {
        const cols = tr.querySelectorAll('th, td');
        csv += [...cols].map(td => `"${td.innerText}"`).join(',') + '\n';
    });

    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');

    link.href = URL.createObjectURL(blob);
    link.download = 'tabela.csv';
    link.click();
};

exp.addEventListener('click', () => {
    try{
const tableTarget = document.querySelector('.table:not(.table-hidden)');
tableTarget.exportCSV();
    } catch (error) {
        alert(`abra uma tabela antes de exportar`);
    }
});

const tableColeta = document.querySelector('#table-coleta');
const tablePendentes = document.querySelector('#table-pendente');
const tableCriticadas = document.querySelector('#table-criticada');
const tableAtualizadas = document.querySelector('#table-atualizada');
const tableLOJAS = document.querySelector('#table-original');

const btnColeta = document.querySelector('#numberColeta');
const btnCriticadas = document.querySelector('#numberCriticadas');
const btnPendentes = document.querySelector('#numberPendentes');
const btnAtualizadas = document.querySelector('#numberAtualizadas');

function parseDataBR(dataStr) {
  if (!dataStr) return null;
  const [dia, mes, ano] = dataStr.split('/');
  return new Date(`${ano}/${mes}/${dia}`);
}

async function findTableLojas(url, tabela, campoData, seletorData) {
  try {
    tabela.innerHTML = `
      <tr>
        <th>Nome</th>
        <th>Filial</th>
        <th>UF</th>
        <th>Data</th>
        <th>Mapa</th>
      </tr>
    `;

    const response = await fetch(url);
    const data = await response.json();

    if (!Array.isArray(data) || data.length === 0) {
      tabela.innerHTML += `<tr><td colspan="5">Nenhum registro</td></tr>`;
      const pData = document.querySelector(seletorData);
      pData.textContent = 'N/A';
      return;
    }

    let linhas = "";
    let ultimaData = null;

    data.forEach(loja => {
      linhas += `
        <tr>
          <td class="nomes">${loja.NOME}</td>
          <td>${loja.FILIAL}</td>
          <td>${loja.UF}</td>
          <td>${loja[campoData]}</td>
          <td>${loja.MAPA}</td>
        </tr>
      `;

        if (loja[campoData]) {
            const dataAtual = parseDataBR(loja[campoData]);
            if (dataAtual && (!ultimaData || dataAtual > ultimaData)) {
                ultimaData = dataAtual;
            }
        }

    });

    tabela.innerHTML += linhas;
    const pData = document.querySelector(seletorData);

    if (pData !== null) {
      if (ultimaData && seletorData) {
        if (pData) {
          pData.textContent = ultimaData.toLocaleDateString('pt-BR');
        }
      }

      const hoje = new Date();
      const hojeError = new Date();
      const hojeCaution = new Date();
      const hojeTeste = new Date();

      //teste
      //ultimaData = hojeTeste.setDate(hoje.getDate()-5);

      if(ultimaData <= hojeError.setDate(hoje.getDate()-5)){
        pData.classList.add('error')
      }
      if(ultimaData < hojeCaution.setDate(hoje.getDate()-1) && ultimaData >= hojeCaution.setDate(hoje.getDate()-5)){
        pData.classList.add('caution')
      };
    }
  } catch (error) {
    console.error(error);
    tabela.innerHTML = `<tr><td colspan="5">Erro ao carregar dados</td></tr>`;
  }
}





async function findTotalLojas() {
  try {
    const response = await fetch('http://172.16.0.185:7872/totalLojas');
    const data = await response.json();
    document.querySelector('#numberShopsBoxTotal p.numberShopsTotal').textContent = data.TOTAL_LOJAS;
  } catch (error) {
    console.log(error);
  }
}
async function findTotalColeta() {
  try {
    const response = await fetch('http://172.16.0.185:7872/totalColeta');
    const data = await response.json();
    document.querySelector('#numberBD-Coleta p.numberShopsColeta').textContent = data.COLETADAS_TOTAL;
  } catch (error) {
    console.log(error);
  }
}
async function findTotalCriticadas() {
  try {
    const response = await fetch('http://172.16.0.185:7872/totalCriticadas');
    const data = await response.json();
    document.querySelector('#numberBD-Criticadas p.numberShopsCriticada').textContent = data.CRITICADAS_TOTAL;
  } catch (error) {
    console.log(error);
  }
}
async function findTotalPendentes() {
  try {
    const response = await fetch('http://172.16.0.185:7872/totalPendente');
    const data = await response.json();
    document.querySelector('#numberBD-Pendentes p.numberShopsPendentes').textContent = data.PENDENTES_TOTAL;
  } catch (error) {
    console.log(error);
  }
}
async function findTotalAtualizadas() {
  try {
    const response = await fetch('http://172.16.0.185:7872/totalAtualizadas');
    const data = await response.json();
    document.querySelector('#numberBD-Atualizadas p.numberShopsAtualizadas').textContent = data.ATUALIZADAS_TOTAL;
  } catch (error) {
    console.log(error);
  }
}

     async function carregarTotais() {
       const dados = await fetch('http://172.16.0.185:7872/totalAjuste').then(r => r.json());

       const atualizadas = dados.find(i => i.STATUS === 'ATUALIZADA')?.TOTAL ?? 0;
       const criticada   = dados.find(i => i.STATUS === 'CRITICADA')?.TOTAL ?? 0;
       const pendente    = dados.find(i => i.STATUS === 'PENDENTE')?.TOTAL ?? 0;
       const coleta      = dados.find(i => i.STATUS === 'COLETA')?.TOTAL ?? 0;

       document.querySelector('#numberBD-Atualizadas p').textContent = atualizadas;
       document.querySelector('#numberBD-Coleta p.numberShopsColeta').textContent = coleta;
       document.querySelector('#numberBD-Pendentes p.numberShopsPendente').textContent = pendente;
       document.querySelector('#numberBD-Criticadas p.numberShopsCriticada').textContent = criticada;
     }

    async function  atualizarTotais() {
        console.log("Atualizando dados...");
        await findTotalLojas(); // Total de lojas gerais
        await carregarTotais()
        await findTableLojas(
        'http://172.16.0.185:7872/tabela_coleta',
        tableColeta,
        'DATA',
        '#dateColeta'
        );
        await findTableLojas(
        'http://172.16.0.185:7872/tabela_pendentes',
        tablePendentes,
        'DATA',
        '#datePendente'
        );
        await findTableLojas(
        'http://172.16.0.185:7872/tabela_criticadas',
        tableCriticadas,
        'DATA',
        '#dateCriticada'
        );
        await findTableLojas(
        'http://172.16.0.185:7872/tabela_atualizadas',
        tableAtualizadas,
        'DATA',
        null
        );
        console.log("atualizados com sucesso!");
    }

    window.onload = function() {
        atualizarTotais();
        setInterval(atualizarTotais, 600000);
    };