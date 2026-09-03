const totalT852 = document.querySelector(`.numberShops852`);
const totalT711 = document.querySelector(`.numberShops711`);
const totalT851 = document.querySelector(`.numberShops851`);
const totalT701 = document.querySelector(`.numberShops701`);
const totalT853 = document.querySelector(`.numberShops853`);
const totalT700 = document.querySelector(`.numberShops700`);
const totalTSUCCEDED = document.querySelector(`.numberShopsSUCCEDED`);
const totalSKUs = document.querySelector(`.totalDesbloqueio`);
const totalDesbloqueado = document.querySelector(`.totalDesbloqueado`);


const tableSelectorT852 = document.querySelector(`#table-t852`);
const tableSelectorT711 = document.querySelector(`#table-FAILED`);
const tableSelectorT851 = document.querySelector(`#table-t851`);
const tableSelectorT701 = document.querySelector(`#table-t701`);
const tableSelectorT853 = document.querySelector(`#table-t853`);
const tableSelectorT700 = document.querySelector(`#table-t700`);
const tableSelectorSUCCEDED = document.querySelector(`#table-SUCCEDED`);
const tableSKUsEXP = document.querySelector('#table-SKUs');


const tabelas = document.querySelectorAll('.table');
const secTabelas = document.querySelectorAll('.tableSection');

async function totaisAdjust(t852, t711) {
    let data = await fetch(`http://172.16.0.197:6842/totaisAdjust`);
    data = await data.json();

    const obj852 = data.find(array => array.STATUS === "STOPPED");
    const obj711 = data.find(array => array.STATUS === "FAILED");

    const atributo852 = obj852?.TOTAL || 0;
    const atributo711 = obj711?.TOTAL || 0;

    t852.innerHTML = atributo852;
    t711.innerHTML = atributo711;
}

async function totais(http, total) {
    try{
        let data = await fetch(http);
        data = await data.json();
        obj = data.find(coluna => coluna.TOTAL);
        total.innerHTML = obj?.TOTAL || 0;
    } catch (Error) {
        console.error(`Erro ao carregar o total: `, Error);
    }
}

async function totais2(http, total1, total2) {
    try{
        let data = await fetch(http);
        data = await data.json();
        obj = data.find(coluna => coluna.TOTAL);
        total1.innerHTML = obj?.TOTAL || 0;
        total2.innerHTML = obj?.TOTAL || 0;
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
                        <th>DATA</th>
                        <th>OWNER</th>
                        <th>JOB_NAME</th>
                        <th>STATUS</th>
                        <th>ULT_EXEC</th>
                        <th>DATA_ATUAL</th>
                        <th>RUN_DURATION</th>
                        <th>ADDITIONAL_INFO</th>
                    </tr>
                    <tr>
                        <td id="id">${func.DATA}</td>
                        <td>${func.OWNER}</td>
                        <td>${func.JOB_NAME}</td>
                        <td>${func.STATUS}</td>
                        <td>${func.ULT_EXEC}</td>
                        <td>${func.DATA_ATUAL}</td>
                        <td>${func.RUN_DURATION}</td>
                        <td class="gsMsg-tableErro">${func.ADDITIONAL_INFO}</td>
                    </tr>`;
            }else{

            let dataFormatada = func.DATA_MOVIMENTO?.split("T")[0] || "";
            let horaFormatada = func.DATA_MOVIMENTO?.split("T")[1]?.split(".")[0] || "";
            let statusMsg = func.CTRL_MSG_ENVIO;
            let statusMsg2 = func.GS_DSC_MSG;

                linhas +=
                   `<tr>
                        <td id="id">${func.DATA}</td>
                        <td>${func.OWNER}</td>
                        <td>${func.JOB_NAME}</td>
                        <td>${func.STATUS}</td>
                        <td>${func.ULT_EXEC}</td>
                        <td>${func.DATA_ATUAL}</td>
                        <td>${func.RUN_DURATION}</td>
                        <td class="gsMsg-tableErro">${func.ADDITIONAL_INFO}</td>
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
        totalT711
    );

    await tables(`http://172.16.0.197:6842/t852`, tableSelectorT852);
    await totais(`http://172.16.0.197:6842/totalSKU`, totalSKUs);
    await tables(`http://172.16.0.197:6842/t711`, tableSelectorT711);
    await totais2(`http://172.16.0.197:6842/totalSucesso`, totalTSUCCEDED, totalDesbloqueado);
    await tables(`http://172.16.0.197:6842/tableSucesso`, tableSelectorSUCCEDED);
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