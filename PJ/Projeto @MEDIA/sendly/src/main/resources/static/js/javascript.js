const h1 = document.querySelector('h1');
const a = document.querySelector('#esqueciSenhaA');

//GET
const email = document.querySelector('#email').value;

async function get(http, local){
    let data = await fetch(http);
    //data = await data.json();
    data = await data.text();//queryForObject(var, String.class)
    
    local.innerHTML = data;//queryForObject(var, String.class)
    //local.innerHTML = data.email;//queryForMap
    //local.innerHTML = data[0].email;//queryForList
}

//com @RequestParam no @GetMapping
//get(`http://172.16.0.213:8080/teste?email=${email}`, h1);
//get(`http://172.16.0.213:8080/teste?email=${email}`, a);

//sem @RequestParam no @GetMapping
get(`http://172.16.0.213:8080/teste`, h1);
get(`http://172.16.0.213:8080/teste`, a);


//POST

const form = document.querySelector('form');

form.addEventListener('submit', async (e) => {

    e.preventDefault();
    const dados = new FormData(form);

    const req = await fetch(
        'http://localhost:8080/cadastro',
        {
            method: 'POST',
            body: dados
        }
    );
    const data = await req.json();

    if(data.id){
        location.href = './menuprincipal.html';
    }
});