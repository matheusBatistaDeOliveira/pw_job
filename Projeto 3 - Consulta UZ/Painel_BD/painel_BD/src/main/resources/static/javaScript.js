let uzPlace = document.getElementById('uzPlace');
let orderPlace = document.getElementById('orderPlace');
let buttonListar = document.querySelector('.buttonListar');
let table = document.getElementById('tableDados');
let ps = document.querySelectorAll('p');

let uzValue;
let orderValue;

uzPlace.addEventListener("input", () => {
    uzPlace.value = uzPlace.value.replace(/\D/g, "").slice(0, 16);
});

orderPlace.addEventListener("input", () => {
    orderPlace.value = orderPlace.value.replace(/[^0-9-]/g, "").slice(0, 17);
});

buttonListar.addEventListener('click', () => {


    table.innerHTML = `<tr id="loads"><td colspan="13">Carregando...</td></tr>`;
    uzPlace.classList.remove("erro");
    orderPlace.classList.remove("erro");
    ps.forEach(p => p.innerHTML = "");
    uzPlace.focus();

    uzValue = uzPlace.value.trim();
    orderValue = orderPlace.value.trim();

    if (uzValue.length === 0 && orderValue.length === 0) {
        uzPlace.classList.add("erro");
        ps[0].innerHTML = "valor uz inválido, por favor tente novamente";
        table.innerHTML = ` `;
        return;
    }

    const url = `http://192.168.0.51:1911/valuesOfSearch?uz=${uzValue}&order=${orderValue}`;

    fetch(url)
        .then(resp => resp.json())
        .then(data => {

            console.log("Resposta do Java:", data);

            if (!Array.isArray(data) || data.length === 0) {
                uzPlace.classList.add("erro");
                orderPlace.classList.add("erro");
                ps.forEach(p => p.innerHTML = "valor uz e/ou order inválido, por favor tente novamente");
                table.innerHTML = `<tr><td colspan="13">Nenhum dado encontrado...</td></tr>`;
                return;
            }

            montarTabela(data);
        })
        .catch(e => {
            console.error("Erro na resposta Java: ", e);
            table.innerHTML = `<tr><td colspan="13">Nenhum dado encontrado...</td></tr>`;
        });
});

uzPlace.addEventListener("keypress", e => {
    if (e.key === "Enter"){
        buttonListar.click();
    }
})

orderPlace.addEventListener("keypress", e => {
    if (e.key === "Enter"){
        buttonListar.click();
    }
})

function montarTabela(data) {
    let linhas = "";

    data.forEach(func => {
        let dataFormatada = func.GS_DATA_RETORNO_WMS?.split("T")[0] || "";
        let horaFormatada = func.GS_DATA_RETORNO_WMS?.split("T")[1]?.split(".")[0] || "";
        let statusMsg = func.GS_STATUS_MSG?.split(":")[1] || "";

        if (statusMsg.length < 16) {
        statusMsg = func.GS_STATUS_MSG;
        };

        linhas += `
        <tr>
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
    });

/*     if(data.length > 1){
        alert("UZ duplicado!");
    } */

    table.innerHTML = linhas;
}
window.reload = (() => {
    uzPlace.value = "";
    orderPlace.value = "";
    table.innerHTML = "";
    uzPlace.focus();
});