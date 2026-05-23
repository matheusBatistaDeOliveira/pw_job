const totalT852 = document.querySelector(`.numberShops852`);
const totalT711 = document.querySelector(`.numberShops711`);
const totalT851 = document.querySelector(`.numberShops851`);
const totalT701 = document.querySelector(`.numberShops701`);
const totalT853 = document.querySelector(`.numberShops853`);
const totalT700 = document.querySelector(`.numberShops700`);
const totalBloqueio = document.querySelector(`.totalBloqueio`);
const totalDesbloqueio = document.querySelector(`.totalDesbloqueio`);
const totalSKUs = document.querySelector(`.totalSKUs`);


const tableSelectorT852 = document.querySelector(`#table-t852`);
const tableSelectorT711 = document.querySelector(`#table-t711`);
const tableSelectorT851 = document.querySelector(`#table-t851`);
const tableSelectorT701 = document.querySelector(`#table-t701`);
const tableSelectorT853 = document.querySelector(`#table-t853`);
const tableSelectorT700 = document.querySelector(`#table-t700`);
const tableSKUsEXP = document.querySelector('#table-SKUs');


const tabelas = document.querySelectorAll('.table');
const secTabelas = document.querySelectorAll('.tableSection');

const LINHAS_VISIVEIS = 30;
const ALTURA_LINHA = 20;


async function totaisAdjust(t852, t711, t851,t701, t853, t700, bloqueio, desbloqueio) {
    let data = await fetch(`http://172.16.0.213:6842/totaisAdjust`);
    data = await data.json();

    const objt852 = data.find(coluna => coluna.CODIGO_TRANSACAO === "852");
    const objt711 = data.find(coluna => coluna.CODIGO_TRANSACAO === "711");
    const objt851 = data.find(coluna => coluna.CODIGO_TRANSACAO === "851");
    const objt701 = data.find(coluna => coluna.CODIGO_TRANSACAO === "701");
    const objt853 = data.find(coluna => coluna.CODIGO_TRANSACAO === "853");
    const objT700 = data.find(coluna => coluna.CODIGO_TRANSACAO === "700");


    const valort852 = Number(objt852?.TOTAL || 0);
    const valort711 = Number(objt711?.TOTAL || 0);
    const valort851 = Number(objt851?.TOTAL || 0);
    const valort701 = Number(objt701?.TOTAL || 0);
    const valort853 = Number(objt853?.TOTAL || 0);
    const valorT700 = Number(objT700?.TOTAL || 0);

    const valorDesbloqueio = valort852+valort711;
    const valorBloqueio = valorT700+valort853+valort701+valort851;

    t852.innerText = valort852;
    t711.innerText = valort711;
    t851.innerText = valort851;
    t701.innerText = valort701;
    t853.innerText = valort853;
    t700.innerText = valorT700;

    desbloqueio.innerText = valorDesbloqueio;
    bloqueio.innerText = valorBloqueio;
}

async function totais(http, total) {
    try{
        let data = await fetch(http);
        data = await data.json();
        obj = data.find(coluna => coluna.TOTAL);
        total.innerText = obj?.TOTAL || 0;
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
                
            let dataFormatada = func.DATA_MOVIMENTO?.split("T")[0] || "";
            let horaFormatada = func.DATA_MOVIMENTO?.split("T")[1]?.split(".")[0] || "";
            let statusMsg = func.CTRL_MSG_ENVIO;
            let statusMsg2 = func.GS_DSC_MSG;
                
                linhas +=
                   `<tr id="topo">
                        <th>ID_MOVIMENTO</th>
                        <th>IDENTIFICADOR</th>
                        <th>CODIGO_ARMAZEM</th>
                        <th>CODIGO_ARMAZEM_DESTINO</th>
                        <th>CODIGO_ITEM</th>
                        <th>CODIGO_ITEM_DISPLAY</th>
                        <th>CODIGO_CLIENTE</th>
                        <th>CODIGO_TRANSACAO</th>
                        <th>QUANTIDADE_PRE_AJUSTE</th>
                        <th>QUANTIDADE_POS_AJUSTE</th>
                        <th>QUANTIDADE_ALTERADA</th>
                        <th class="th_gsMsgErro">CTRL_MSG_ENVIO</th>
                        <th>DATA_MOVIMENTO</th>
                        <th>STATUS_PRE_INVENTARIO</th>
                        <th>STATUS_POS_INVENTARIO</th>
                        <th>CODIGO_MOTIVO</th>
                        <th>ENDERECO_AJUSTE</th>
                        <th>IDENTIFICADOR_KIT</th>
                        <th>USUARIO_EXECUCAO</th>
                        <th>NUMERO_LOTE</th>
                        <th>HUID</th>
                        <th>FORNECEDOR</th>
                        <th>COD_INTEGRACAO</th>
                        <th>COD_CONFIGURACAO</th>
                        <th>CHAVE_INTEGRACAO</th>
                        <th>CTRL_STATUS_ENVIO</th>
                        <th>CTRL_COD_MSG_ENVIO</th>
                        <th>CTRL_DATA_ENVIO</th>
                        <th>RMS_CTRL_DESBLOQUEADO</th>
                        <th>GS_COD_MSG</th>
                        <th class="th_gsMsgErro">GS_DSC_MSG</th>
                        <th>GS_OBS_NOTIF_RMS</th>
                        <th>ATTRIBUTE_1</th>
                        <th>GET_ESTOQUE_ANTERIOR</th>
                        <th>GET_ESTOQUE_POSTERIOR</th>
                        <th>QUANTIDADE_BLOQ_BLOQ</th>
                        <th>SALDO_TROCAS</th>
                    </tr>
                    <tr>
                        <td id="id">${func.ID_MOVIMENTO}</td>
                        <td>${func.IDENTIFICADOR}</td>
                        <td>${func.CODIGO_ARMAZEM}</td>
                        <td>${func.CODIGO_ARMAZEM_DESTINO}</td>
                        <td>${func.CODIGO_ITEM}</td>
                        <td>${func.CODIGO_ITEM_DISPLAY}</td>
                        <td>${func.CODIGO_CLIENTE}</td>
                        <td>${func.CODIGO_TRANSACAO}</td>
                        <td>${func.QUANTIDADE_PRE_AJUSTE}</td>
                        <td>${func.QUANTIDADE_POS_AJUSTE}</td>
                        <td>${func.QUANTIDADE_ALTERADA}</td>
                        <td class="gsMsg-tableErro">${statusMsg}</td>
                        <td>${dataFormatada} </br> ${horaFormatada}</td>
                        <td>${func.STATUS_PRE_INVENTARIO}</td>
                        <td>${func.STATUS_POS_INVENTARIO}</td>
                        <td>${func.CODIGO_MOTIVO}</td>
                        <td>${func.ENDERECO_AJUSTE}</td>
                        <td>${func.IDENTIFICADOR_KIT}</td>
                        <td>${func.USUARIO_EXECUCAO}</td>
                        <td>${func.NUMERO_LOTE}</td>
                        <td>${func.HUID}</td>
                        <td>${func.FORNECEDOR}</td>
                        <td>${func.COD_INTEGRACAO}</td>
                        <td>${func.COD_CONFIGURACAO}</td>
                        <td>${func.CHAVE_INTEGRACAO}</td>
                        <td>${func.CTRL_STATUS_ENVIO}</td>
                        <td>${func.CTRL_COD_MSG_ENVIO}</td>
                        <td>${func.CTRL_DATA_ENVIO}</td>
                        <td>${func.RMS_CTRL_DESBLOQUEADO}</td>
                        <td>${func.GS_COD_MSG}</td>
                        <td class="gsMsg-tableErro">${statusMsg2}</td>
                        <td>${func.GS_OBS_NOTIF_RMS}</td>
                        <td>${func.ATTRIBUTE_1}</td>
                        <td>${func.GET_ESTOQUE_ANTERIOR}</td>
                        <td>${func.GET_ESTOQUE_POSTERIOR}</td>
                        <td>${func.QUANTIDADE_BLOQ_BLOQ}</td>
                        <td>${func.SALDO_TROCAS}</td>
                    </tr>`;
            }else{

            let dataFormatada = func.DATA_MOVIMENTO?.split("T")[0] || "";
            let horaFormatada = func.DATA_MOVIMENTO?.split("T")[1]?.split(".")[0] || "";
            let statusMsg = func.CTRL_MSG_ENVIO;
            let statusMsg2 = func.GS_DSC_MSG;

                linhas +=
                   `<tr>
                        <td id="id">${func.ID_MOVIMENTO}</td>
                        <td>${func.IDENTIFICADOR}</td>
                        <td>${func.CODIGO_ARMAZEM}</td>
                        <td>${func.CODIGO_ARMAZEM_DESTINO}</td>
                        <td>${func.CODIGO_ITEM}</td>
                        <td>${func.CODIGO_ITEM_DISPLAY}</td>
                        <td>${func.CODIGO_CLIENTE}</td>
                        <td>${func.CODIGO_TRANSACAO}</td>
                        <td>${func.QUANTIDADE_PRE_AJUSTE}</td>
                        <td>${func.QUANTIDADE_POS_AJUSTE}</td>
                        <td>${func.QUANTIDADE_ALTERADA}</td>
                        <td class="gsMsg-tableErro">${statusMsg}</td>
                        <td>${dataFormatada} </br> ${horaFormatada}</td>
                        <td>${func.STATUS_PRE_INVENTARIO}</td>
                        <td>${func.STATUS_POS_INVENTARIO}</td>
                        <td>${func.CODIGO_MOTIVO}</td>
                        <td>${func.ENDERECO_AJUSTE}</td>
                        <td>${func.IDENTIFICADOR_KIT}</td>
                        <td>${func.USUARIO_EXECUCAO}</td>
                        <td>${func.NUMERO_LOTE}</td>
                        <td>${func.HUID}</td>
                        <td>${func.FORNECEDOR}</td>
                        <td>${func.COD_INTEGRACAO}</td>
                        <td>${func.COD_CONFIGURACAO}</td>
                        <td>${func.CHAVE_INTEGRACAO}</td>
                        <td>${func.CTRL_STATUS_ENVIO}</td>
                        <td>${func.CTRL_COD_MSG_ENVIO}</td>
                        <td>${func.CTRL_DATA_ENVIO}</td>
                        <td>${func.RMS_CTRL_DESBLOQUEADO}</td>
                        <td>${func.GS_COD_MSG}</td>
                        <td class="gsMsg-tableErro">${statusMsg2}</td>
                        <td>${func.GS_OBS_NOTIF_RMS}</td>
                        <td>${func.ATTRIBUTE_1}</td>
                        <td>${func.GET_ESTOQUE_ANTERIOR}</td>
                        <td>${func.GET_ESTOQUE_POSTERIOR}</td>
                        <td>${func.QUANTIDADE_BLOQ_BLOQ}</td>
                        <td>${func.SALDO_TROCAS}</td>
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
        totalT852, 
        totalT711, 
        totalT851, 
        totalT701, 
        totalT853, 
        totalT700,
        totalDesbloqueio,
        totalBloqueio
    );

    await totais(`http://172.16.0.213:6842/totalSKU`, totalSKUs)
    await tables(`http://172.16.0.213:6842/t852`, tableSelectorT852);
    await tables(`http://172.16.0.213:6842/t711`, tableSelectorT711);
    await tables(`http://172.16.0.213:6842/t851`, tableSelectorT851);
    await tables(`http://172.16.0.213:6842/t701`, tableSelectorT701);
    await tables(`http://172.16.0.213:6842/t853`, tableSelectorT853)
    await tables(`http://172.16.0.213:6842/t700`, tableSelectorT700);
    await tables(`http://172.16.0.213:6842/tableSKU`, tableSKUsEXP);
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
     const expSKUs = document.getElementById('expSKUs');

     secTabelas.forEach(sectable => {
        sectable.classList.add('table-hidden')
     });

    botoes.forEach(botao => {
        botao.addEventListener('click', function() {
        
        const targetId = this.dataset.target;
        const tabelaAlvo = document.getElementById(targetId);
        atualizarDados();


        secTabelas.forEach(sectable => {
            if(sectable !== tabelaAlvo){
                sectable.classList.add('table-hidden')
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
        const sectionAtiva = document.querySelector('.tableSection:not(.table-hidden)');
        const tableTarget = sectionAtiva?.querySelector('.table');
        tableTarget.exportCSV();
    } catch (error) {
        alert(`abra uma tabela antes de exportar`);
    }
});

expSKUs.addEventListener('click', () => {
    try{
        if(tableSKUsEXP.innerText.includes('Loading...')){
            alert("aguarde o carregamento da tabela")
        }else{
            tableSKUsEXP.exportCSV();
        }
    }catch (error) {
        alert(`tabela ainda em estado de carregamento, espere antes de exportar`);
    }
});