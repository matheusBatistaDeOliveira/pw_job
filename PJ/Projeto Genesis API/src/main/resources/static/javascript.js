const total1 = document.querySelector('#primeiraDiv');
const totalDois = document.querySelector('#segundaDiv');

const table1 = document.querySelector('#primeiraTable');
const tableDois = document.querySelector('#segundaTable');

let data;
let linhas = '';

async function tables(){
    data = await fetch('http://10.11.40.129:8080/1');
    data = await data.json();

        linhas +=
            `
            <tr>
            <th>ID</th>
            <th>COLABORADOR</th>
            <th>BASE INSS</th>
            <th>SALÁRIO LÍQUIDO</th>
            </tr>
            `
        ;        

        data.forEach(func => {

            linhas +=
                `
                <tr>
                <td>${func.ID_NF_GEDI}</td>
                <td>${func.USUARIO_APROVADOR}</td>
                <td>${func.INSS_BASE_CALCULO}</td>
                <td>${func.VALOR_LIQUIDO}</td>
                </tr>
                `
            ;
        });

    table1.innerHTML = 'N/A';
    tableDois.innerHTML = linhas;
};

async function totais(){
    data = await fetch('http://10.11.40.129:8080/2');
    data = await data.json();

    data = data.find(coluna => coluna['TOTAL'])

    total1.innerHTML = 'N/A';
    totalDois.innerHTML = data.TOTAL;
};

tables();
totais();