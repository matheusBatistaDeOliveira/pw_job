/* //GET

//Passo 1
//Definir e armazenar tags do HTML

const h1 = document.querySelector('h1');
const a = document.querySelector('#esqueciSenhaA');
const email = document.querySelector('#email').value;


//Passo 2
//RECEBER API

async function get(http, local){

    let data = await fetch(http);
    
//Passo 3
//Tratar API    

    //TIPOS DE PARSEDATA
    //data = await data.json();   //queryForList e queryForMap
    //data = await data.text();   //queryForObject(var, String.class)
    


    //TIPOS DE RETORNO
    //local.innerHTML = data;           //queryForObject(var, String.class)
    //local.innerHTML = data.email;     //queryForMap
    //local.innerHTML = data[0].email;  //queryForList
}

//Passo 4
//FetchFunction

//com @RequestParam no @GetMapping
//get(`http://172.16.0.213:8080/teste?email=${email}`, h1);
//get(`http://172.16.0.213:8080/teste?email=${email}`, a);

//sem @RequestParam no @GetMapping
get(`http://172.16.0.213:8080/teste`, h1);
get(`http://172.16.0.213:8080/teste`, a); */










//POST


//Passo 1
//Definir e armazenar tags do HTML

const form = document.querySelector('form');
const p = document.querySelectorAll('p');
//const h1 = document.querySelector('h1');//utilização do retorno POST inútil


//Passo 2
//RECEBER API

form.addEventListener('submit', async (e) => {
    
    //desativa o action do form
    e.preventDefault();

    //recebe todos os objetos do form em 'dados'
    const dados = new FormData(form);

    let data = await fetch(
        'http://172.16.0.213:8080/cadastro',
        {
            method: 'POST',
            body: dados
        }
    );

    //Passo 3
    //TRATAR API

    data = await data.json();

    //Utilização do retorno POST, inutil mas didático
    //h1.innerHTML = data.email;

    //Se Recebido ou Não, acontecem:


    if(data.id){
        window.location.href = './menuprincipal.html'
    }else{
        p.forEach(noLogin => {
            noLogin.innerHTML = data.mensagem;
            noLogin.style.color = "red";
        });
    };
});


//Anotar no MySql NUVEM
//exemplo RailWay

//conexão num banco da nuvem pela internet
//mysql -h yamabiko.proxy.rlwy.net -u root -p --port 37416 --protocol=TCP railway



//url genérica de conexão a banco
//mysql://root:lGCZVMuQwXulGzlgNbzCWKbJttSPKcwL@yamabiko.proxy.rlwy.net:37416/railway

//essa mesma url separada no formato SpringTools
//spring.datasource.url=jdbc:mysql://@yamabiko.proxy.rlwy.net:37416/railway
//spring.datasource.username=root
//spring.datasource.password=lGCZVMuQwXulGzlgNbzCWKbJttSPKcwL
