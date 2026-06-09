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
 
 const tableColeta = document.querySelector('#table-coleta');
 const tablePendentes = document.querySelector('#table-pendentes');
 const tableCriticadas = document.querySelector('#table-criticadas');
 const tableAtualizadas = document.querySelector('#table-atualizadas');
 const tableLOJAS = document.querySelector('#table-original');

 const btnColeta = document.querySelector('#numberColeta');
 const btnCriticadas = document.querySelector('#numberCriticadas');
 const btnPendentes = document.querySelector('#numberPendentes');
 const btnAtualizadas = document.querySelector('#numberAtualizadas');


 async function findTableLojas(url, tabela) {
   try {
     tabela.innerHTML =
     `<tr>
       <th>Nome</th>
       <th>Filial</th>
       <th>UF</th>
       <th>Data</th>
       <th>Mapa</th>
     </tr>`;

     const response = await fetch(url);
     const data = await response.json();

 	if (Array.isArray(data) && data.length > 0) {
        let linhas = "";
         data.forEach(loja => {
 	        linhas +=
            `<tr>
 	          <td class="nomes">${loja.NOME}</td>
 	          <td>${loja.FILIAL}</td>
 	          <td>${loja.UF}</td>
 	          <td>${loja.DATA}</td>
 	          <td>${loja.MAPA}</td> 	       
 	        </tr>`;
 	      });
        tabela.innerHTML += linhas;
 	    } else {
 	      tabela.innerHTML += `<tr><td colspan="7">Nenhuma loja encontrada</td></tr>`;
 	    }
 	  } catch (error) {
 	    console.error(error);
 	    tabela.innerHTML = `<tr><td colspan="7">Erro ao carregar dados</td></tr>`;
 	  }
 	}

    let atualizadas = Number(data.ATUALIZADAS_TOTAL);

    let criticadas = Number(data.CRITICADAS_TOTAL);

    let pendentes = Number(data.PENDENTES_TOTAL);

    let coletadas = Number(data.COLETADAS_TOTAL);

    coletadas = atualizadas-criticadas-pendentes;

    atualizadas = atualizadas-coletadas-criticadas-pendentes;



 async function findTotalLojas() {
   try {
     const response = await fetch('http://192.168.0.51:1812/totalLojas');
     const data = await response.json();
     document.querySelector('#numberShopsBoxTotal p.numberShopsTotal').textContent = data.TOTAL_LOJAS;
   } catch (error) {
     console.log(error);
   }
 }
 async function findTotalColeta() {
   try {
     const response = await fetch('http://192.168.0.51:1812/totalColeta');
     const data = await response.json();
     document.querySelector('#numberBD-Coleta p.numberShopsColeta').textContent = coletadas;
   } catch (error) {
     console.log(error);
   }
 }
 async function findTotalCriticadas() {
   try {
     const response = await fetch('http://192.168.0.51:1812/totalCriticadas');
     const data = await response.json();
     document.querySelector('#numberBD-Criticadas p.numberShopsCriticadas').textContent = data.CRITICADAS_TOTAL;
   } catch (error) {
     console.log(error);
   }
 }
 async function findTotalPendentes() {
   try {
     const response = await fetch('http://192.168.0.51:1812/totalPendentes');
     const data = await response.json();
     document.querySelector('#numberBD-Pendentes p.numberShopsPendentes').textContent = data.PENDENTES_TOTAL;
   } catch (error) {
     console.log(error);
   }
 }
 async function findTotalAtualizadas() {
   try {
     const response = await fetch('http://192.168.0.51:1812/totalAtualizadas');
     const data = await response.json();
     document.querySelector('#numberBD-Atualizadas p.numberShopsAtualizadas').textContent = atualizadas;
   } catch (error) {
     console.log(error);
   }
 }

     async function  atualizarTotais() {
         console.log("Atualizando dados...");
         await findTotalLojas(); // Total de lojas gerais
         await findTotalColeta(); // Total de lojas coleta
         await findTotalCriticadas(); // Total de lojas criticadas
         await findTotalPendentes(); // Total de lojas pendentes
         await findTotalAtualizadas(); // Total de lojas atualizadas
         await findTableLojas('http://192.168.0.51:1812/tabela_coleta', tableColeta);
         await findTableLojas('http://192.168.0.51:1812/tabela_criticadas', tableCriticadas);
         await findTableLojas('http://192.168.0.51:1812/tabela_pendentes', tablePendentes);
         await findTableLojas('http://192.168.0.51:1812/tabela_atualizadas', tableAtualizadas);
         console.log("atualizados com sucesso!");
     }

     window.onload = function() {
         atualizarTotais();
         setInterval(atualizarTotais, 600000);
     };



async function carregarTotais() {
  const [atu, crit, pend, col, tot] = await Promise.all([
    fetch('http://192.168.0.51:1812/totalAtualizadas').then(r => r.json()),
    fetch('http://192.168.0.51:1812/totalCriticadas').then(r => r.json()),
    fetch('http://192.168.0.51:1812/totalPendentes').then(r => r.json()),
    fetch('http://192.168.0.51:1812/totalColeta').then(r => r.json()),
    fetch('http://192.168.0.51:1812/totalLojas').then(r => r.json())
  ]);

  let coletadas = col.COLETADAS_TOTAL - crit.CRITICADAS_TOTAL - pend.PENDENTES_TOTAL;

  let atualizadas = atu.ATUALIZADAS_TOTAL-crit.CRITICADAS_TOTAL-pend.PENDENTES_TOTAL;

  document.querySelector('#numberBD-Atualizadas p').textContent = atualizadas;
  document.querySelector('#numberBD-Coleta p.numberShopsColeta').textContent = coletadas;
  document.querySelector('#numberBD-Pendentes p.numberShopsPendentes').textContent = pend.PENDENTES_TOTAL;
  document.querySelector('#numberBD-Criticadas p.numberShopsCriticadas').textContent = crit.CRITICADAS_TOTAL;
}