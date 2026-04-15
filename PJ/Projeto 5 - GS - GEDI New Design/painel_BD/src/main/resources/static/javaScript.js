const totalNAFILA = document.querySelector(`.numberShopsNAFILA`);

const tableSelectorNAFILA = document.querySelector(`#table-NAFILA`);
const tableSelectorCSV = document.querySelector(`#table_GS-GEDI`);

async function totaisAdjust(NAFILA) {
    let data = await fetch(`http://172.16.0.190:6842/totaisAdjust`);
    data = await data.json();

    const objNAFILA = data.find(coluna => coluna.TOTAL);


    NAFILA.innerText = objNAFILA?.TOTAL || 0;
}

async function tables(http, table) {
    try {
        let linhas = ``;
        let csvTable = ``;
        let data = await fetch(http);
        data = await data.json();

        if (data.length < 1) {
            tableSelectorNAFILA.innerHTML = `<tr><td>Sem dados a carregar...</td></tr>`;
            return;
        }else{
            linhas +=
                `<tr id="topo">
                <th>ORIGEM</th>
                <th>DESTINO</th>
                <th>NOTA</th>
                <th>SERIE</th>
                <th>EMISSÃO</th>
                <th>CONDICAOPAGTO</th>
                <th>TOTAL</th>
                <th>DATA</th>
                <th>IMPORTACÃO</th>
                <th>RMS MENSAGEM</th>
                <th>PROTOCOLO GEDI</th>
                <th>FIGURA FISCAL</th>
                <th>CFOP</th>
                <th>PORTADOR</th>
                </tr>`;

                csvTable +=
                `<tr id="topo">
                <th>ORIGEM</th>
                <th>DESTINO</th>
                <th>NOTA</th>
                <th>SERIE</th>
                <th>EMISSÃO</th>
                <th>CONDICAOPAGTO</th>
                <th>TOTAL</th>
                <th>DATA</th>
                <th>IMPORTACÃO</th>
                <th>RMS MENSAGEM</th>
                <th>PROTOCOLO GEDI</th>
                <th>FIGURA FISCAL</th>
                <th>CFOP</th>
                <th>PORTADOR</th>
                </tr>`;
            }

        data.forEach(func => {
         
            let dataFormatada = func.DATA?.split("T")[0] || "";
            let horaFormatada = func.DATA?.split("T")[1]?.split(".")[0] || "";

            let dataFormatadaE = func.EMISSAO?.split("T")[0] || "";
            let horaFormatadaE = func.EMISSAO?.split("T")[1]?.split(".")[0] || "";

            let dataFormatadaI = func.IMPORTACÃO?.split("T")[0] || "";
            let horaFormatadaI = func.IMPORTACÃO?.split("T")[1]?.split(".")[0] || "";

            linhas +=
                `<tr>
                <td>${func.ORIGEM}</td>
                <td>${func.DESTINO}</td>
                <td>${func.NOTA}</td>
                <td>${func.SERIE}</td>
                <td>${dataFormatadaE}</td>
                <td>${func.CONDICAOPAGTO}</td>
                <td>R$${func.TOTAL}</td>
                <td>${dataFormatada} ${horaFormatada}</td>
                <td>${dataFormatadaI}</td>
                <td class="ctrMsgInside"><input class="buttonInside" type="button" value="Mostrar Mensagens"></td>
                <td class="gsMsg hidden">${func.CTRL_RMS_MENSAGEM}</td>
                <td>${func.PROTOCOLO_GEDI}</td>
                <td>${func.FIGURA_FISCAL}</td>
                <td>${func.CFOP}</td>
                <td>${func.PORTADOR}</td>
                </tr>`;

                csvTable +=
                `<tr>
                <td>${func.ORIGEM}</td>
                <td>${func.DESTINO}</td>
                <td>${func.NOTA}</td>
                <td>${func.SERIE}</td>
                <td>${dataFormatadaE}</td>
                <td>${func.CONDICAOPAGTO}</td>
                <td>${func.TOTAL}</td>
                <td>${dataFormatada} ${horaFormatada}</td>
                <td>${dataFormatadaI}</td>
                <td>${func.CTRL_RMS_MENSAGEM}</td>
                <td>${func.PROTOCOLO_GEDI}</td>
                <td>${func.FIGURA_FISCAL}</td>
                <td>${func.CFOP}</td>
                <td>${func.PORTADOR}</td>
                </tr>`;
        });
            
        tableSelectorNAFILA.innerHTML = linhas;
        tableSelectorCSV.innerHTML = csvTable;
    } catch (Error) {
        console.error(`Erro ao carregar a tabela: `, Error);
        table.innerHTML = `<tr><td>Erro ao carregar dados...</td></tr>`;
    }
}

async function getTotais() {
    await totaisAdjust(totalNAFILA);
    await tables(`http://172.16.0.190:6842/tableNAFILA`, tableSelectorNAFILA);
}

async function atualizarDadosIntroducao() {
    console.log(`atualizando dados...`)
    await totaisAdjust(totalNAFILA);
    await getTotais();
    setInterval(atualizarDados, 2 * 60000);
    console.log(`Dados atualizados`);
}

async function atualizarDados() {
    console.log(`atualizando dados...`)
    await getTotais();
    console.log(`Dados atualizados`);
}

window.reload = atualizarDadosIntroducao();

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
    try {
        const tableTarget = document.querySelector('#table_GS-GEDI');
        tableTarget.exportCSV();
    } catch (error) {
        alert(`abra uma tabela antes de exportar`);
    }
});

ctrMsg.addEventListener('click', () => {
    
    const mensagens = document.querySelectorAll('.gsMsg');
    mensagens.forEach(msg => {
        msg.classList.toggle('hidden');
    });

    const buttons = document.querySelectorAll('.ctrMsgInside');
    buttons.forEach(btns => {
        btns.classList.toggle('hidden');
    });
});

const buttonInside = document.querySelectorAll('.buttonInside');

buttonInside.forEach(btnsInside => {

    btnsInside.addEventListener('click', (e) => {
    if (e.target.classList.contains('buttonInside')) {
    alert('oi');
}
        const mensagens = document.querySelectorAll('.gsMsg');
        mensagens.forEach(msg => {
            msg.classList.toggle('hidden');
        });

        const buttons = document.querySelectorAll('.ctrMsgInside');
        buttons.forEach(btns => {
            btns.classList.toggle('hidden');
        });
    });
});