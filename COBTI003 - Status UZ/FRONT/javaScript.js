const btnColeta = document.querySelector(`#btn`);
const btnCriticadas = document.querySelector(`#btn`);
const btnPendentes = document.querySelector(`#btn`);
const btnAtualizadas = document.querySelector(`#btn`); 

const totalNAFILA = document.querySelector(`.numberShopsNAFILA`);
const totalEMFATURAMENTO = document.querySelector(`.numberShopsEMFATURAMENTO`);
const totalAGUARDANDOAUTORIZACAO = document.querySelector(`.numberShopsAGUARDANDOAUTORIZACAO`);
const totalAUTORIZADA = document.querySelector(`.numberShopsAUTORIZADA`);
const totalEnviado = document.querySelector(`.numberShopsEnviado`);
const totalErro = document.querySelector(`.numberShopsErro`);

const tableSelectorNAFILA = document.querySelector(`#table-NAFILA`);
const tableSelectorEMFATURAMENTO = document.querySelector(`#table-EMFATURAMENTO`);
const tableSelectorAGUARDANDOAUTORIZACAO = document.querySelector(`#table-AGUARDANDOAUTORIZACAO`);
const tableSelectorAUTORIZADA = document.querySelector(`#table-AUTORIZADA`);
const tableSelectorEnviado = document.querySelector(`#table-ENVIADO`);
const tableSelectorErro = document.querySelector(`#table-ERRO`);

const tabelas = document.querySelectorAll('.table');

const LINHAS_VISIVEIS = 30;
const ALTURA_LINHA = 20;

async function totaisAdjust(NAFILA, EMFATURAMENTO, AGUARDANDOAUTORIZACAO
    , AUTORIZADA, Enviado, Erro) {
    let data = await fetch(`http://172.16.0.186:6842/totaisAdjust`);
    data = await data.json();

    const objNAFILA = data.find(coluna => coluna.STATUS === "FILA");
    const objEMFATURAMENTO = data.find(coluna => coluna.STATUS === "EM FATURAMENTO");
    const objAGUARDANDOAUTORIZACAO = data.find(coluna => coluna.STATUS === "AGUARDANDO AUTORIZACAO NF");
    const objAUTORIZADA = data.find(coluna => coluna.STATUS === "NF AUTORIZADA");
    const objEnviado = data.find(coluna => coluna.STATUS === "ENVIADA");
    const objErro = data.find(coluna => coluna.STATUS === "ERRO");


    NAFILA.innerText = objNAFILA?.TOTAL || 0;
    EMFATURAMENTO.innerText = objEMFATURAMENTO?.TOTAL || 0;
    AGUARDANDOAUTORIZACAO.innerText = objAGUARDANDOAUTORIZACAO?.TOTAL || 0;
    AUTORIZADA.innerText = objAUTORIZADA?.TOTAL || 0;
    Enviado.innerText = objEnviado?.TOTAL || 0;
    Erro.innerText = objErro?.TOTAL || 0;
}

async function totais(http, total) {
    try {
        let data = await fetch(http);
        data = await data.json();
        total.innerText = data.TOTAL;
    } catch (Error) {
        console.error(`Erro ao carregar o total: `, Error);
    }
}

async function tables(http, table) {
    try {
        let linhas = ``;
        let data = await fetch(http);
        data = await data.json();

        if (data.length < 1) {
            table.innerHTML = `<tr><td>Sem dados a carregar...</td></tr>`;
            return;
        }

        data.forEach(func => {

            if (linhas === ``) {
                if (func.SITUACAO != null && table != tableSelectorErro) {

                    linhas +=

                    `<tr id="topo">
                        <th>ID</th>
                        <th>ORIGEM</th>
                        <th>ORDER_KORBER</th>
                        <th>IMPRESSORA</th>
                        <th>USUARIO</th>
                        <th>UZ</th>
                        <th>GS_STATUS</th>
                        <th>GS_STATUS_MSG</th>
                        <th>TRANSACTIONCODE</th>
                        <th>NOTA</th>
                        <th>SITUAÇÃO</th>
                        <th>GS_DATA_RETORNO_WMS</th>
                        <th>MAPA</th>
                        <th>DESTINO</th>
                    </tr>`;
                } else if (func.SITUACAO != null && table === tableSelectorErro) {
                    linhas +=

                   `<tr id="topo">
                       <th>ID</th>
                       <th>ORIGEM</th>
                       <th>ORDER_KORBER</th>
                       <th>IMPRESSORA</th>
                       <th>USUARIO</th>
                       <th>UZ</th>
                       <th>GS_STATUS</th>
                       <th id="th_gsMsgErro">GS_STATUS_MSG</th>
                       <th>TRANSACTIONCODE</th>
                       <th>NOTA</th>
                       <th>SITUAÇÃO</th>
                       <th>GS_DATA_RETORNO_WMS</th>
                       <th>MAPA</th>
                       <th>DESTINO</th>
                   </tr>`;
                } else if (table != tableSelectorErro) {
                    linhas +=
                   `<tr id="topo">
                        <th>ID</th>
                        <th>ORIGEM</th>
                        <th>ORDER_KORBER</th>
                        <th>IMPRESSORA</th>
                        <th>USUARIO</th>
                        <th>UZ</th>
                        <th>GS_STATUS</th>
                        <th>GS_STATUS_MSG</th>
                        <th>TRANSACTIONCODE</th>
                        <th>NOTA</th>
                        <th>GS_DATA_RETORNO_WMS</th>
                        <th>MAPA</th>
                        <th>DESTINO</th>
                    </tr>`;
                } else {
                    linhas +=

                   `<tr id="topo">
                        <th>ID</th>
                        <th>ORIGEM</th>
                        <th>ORDER_KORBER</th>
                        <th>IMPRESSORA</th>
                        <th>USUARIO</th>
                        <th>UZ</th>
                        <th>GS_STATUS</th>
                        <th id="th_gsMsgErro">GS_STATUS_MSG</th>
                        <th>TRANSACTIONCODE</th>
                        <th>NOTA</th>
                        <th>GS_DATA_RETORNO_WMS</th>
                        <th>MAPA</th>
                        <th>DESTINO</th>
                    </tr>`;
                }
            }

            let dataFormatada = func.GS_DATA_RETORNO_WMS?.split("T")[0] || "";
            let horaFormatada = func.GS_DATA_RETORNO_WMS?.split("T")[1]?.split(".")[0] || "";
            let statusMsg = func.GS_STATUS_MSG;

            if (func.SITUACAO != null && (statusMsg == null || statusMsg.length <= 16)) {

                linhas +=

             `<tr>
                <td id="id">${func.ID}</td>
                <td>${func.ORIGEM}</td>
                <td>${func.ORDER_KORBER}</td>
                <td>${func.IMPRESSORA}</td>
                <td>${func.USUARIO}</td>
                <td>${func.UZ}</td>
                <td>${func.GS_STATUS}</td>
                <td>${statusMsg}</td>
                <td>${func.TRANSACTIONCODE}</td>
                <td>${func.NOTA}</td>
                <td>${func.SITUACAO?.split(" ")[0]} </br> ${func.SITUACAO?.split(" ")[1]}</td>
                <td>${dataFormatada} às ${horaFormatada}</td>
                <td>${func.MAPA}</td>
                <td>${func.DESTINO}</td>
             </tr>`;
           } else if (func.SITUACAO != null && (statusMsg != null && statusMsg.length > 16)) {

               linhas +=

             `<tr>
                <td id="id">${func.ID}</td>
                <td>${func.ORIGEM}</td>
                <td>${func.ORDER_KORBER}</td>
                <td>${func.IMPRESSORA}</td>
                <td>${func.USUARIO}</td>
                <td>${func.UZ}</td>
                <td>${func.GS_STATUS}</td>
                <td class="gsMsg-tableErro">${statusMsg}</td>
                <td>${func.TRANSACTIONCODE}</td>
                <td>${func.NOTA}</td>
                <td>${func.SITUACAO?.split(" ")[0]} </br> ${func.SITUACAO?.split(" ")[1]}</td>
                <td>${dataFormatada} às ${horaFormatada}</td>
                <td>${func.MAPA}</td>
                <td>${func.DESTINO}</td>
             </tr>`;
           } else if (statusMsg == null || statusMsg.length <= 16) {
                linhas +=
             `<tr>
                <td id="id">${func.ID}</td>
                <td>${func.ORIGEM}</td>
                <td>${func.ORDER_KORBER}</td>
                <td>${func.IMPRESSORA}</td>
                <td>${func.USUARIO}</td>
                <td>${func.UZ}</td>
                <td>${func.GS_STATUS}</td>
                <td>${statusMsg}</td>
                <td>${func.TRANSACTIONCODE}</td>
                <td>${func.NOTA}</td>
                <td>${dataFormatada} às ${horaFormatada}</td>
                <td>${func.MAPA}</td>
                <td>${func.DESTINO}</td>
            </tr>`;
            } else {

                linhas +=

                `<tr>
                    <td id="id">${func.ID}</td>
                    <td>${func.ORIGEM}</td>
                    <td>${func.ORDER_KORBER}</td>
                    <td>${func.IMPRESSORA}</td>
                    <td>${func.USUARIO}</td>
                    <td>${func.UZ}</td>
                    <td>${func.GS_STATUS}</td>
                    <td class="gsMsg-tableErro">${statusMsg}</td>
                    <td>${func.TRANSACTIONCODE}</td>
                    <td>${func.NOTA}</td>
                    <td>${dataFormatada} às ${horaFormatada}</td>
                    <td>${func.MAPA}</td>
                    <td>${func.DESTINO}</td>
                </tr>`;
            }
        });

        table.innerHTML = linhas;

    } catch (Error) {
        console.error(`Erro ao carregar a tabela: `, Error);
        table.innerHTML = `<tr><td>Erro ao carregar dados...</td></tr>`;
    }
}

async function getTotais() {
    await totaisAdjust
    (
        totalNAFILA, 
        totalEMFATURAMENTO, 
        totalAGUARDANDOAUTORIZACAO, 
        totalAUTORIZADA, 
        totalEnviado, 
        totalErro
    );

    await tables(`http://172.16.0.186:6842/tableNAFILA`, tableSelectorNAFILA);
    await tables(`http://172.16.0.186:6842/tableEMFATURAMENTO`, tableSelectorEMFATURAMENTO);
    await tables(`http://172.16.0.186:6842/tableAGUARDANDOAUTORIZACAO`, tableSelectorAGUARDANDOAUTORIZACAO);
    await tables(`http://172.16.0.186:6842/tableAUTORIZADA`, tableSelectorAUTORIZADA);
    await tables(`http://172.16.0.186:6842/tableErro`, tableSelectorErro);
}

async function atualizarDadosIntroducao() {
    console.log(`atualizando dados...`)
    await getTotais();
    setInterval(atualizarDados, 2*60000);
    console.log(`Dados atualizados`);
}

async function atualizarDados() {
    console.log(`atualizando dados...`)
    await getTotais();
    console.log(`Dados atualizados`);
}

window.reload = atualizarDadosIntroducao();

     document.addEventListener('DOMContentLoaded', function() {
     const botoes = document.querySelectorAll('input[type="button"]');
     const exp = document.getElementById('exp');

     tabelas.forEach(tabela => {
             tabela.classList.add('table-hidden');
     });

    botoes.forEach(botao => {
        botao.addEventListener('click', function() {
        
        const targetId = this.dataset.target;
        const tabelaAlvo = document.getElementById(targetId);
        if(targetId === "table-ENVIADO") {
            tables(`http://172.16.0.186:6842/tableEnviado`, tableSelectorEnviado);
            atualizarDados();
        }else{
            atualizarDados();
        };

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
    link.download = `${this.id}.csv`;
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