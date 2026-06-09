const loading = document.querySelector("main");
const h1s = document.querySelectorAll("h1");


//style
loading.innerHTML = "olá";
loading.style.fontSize = "8rem"
loading.style.maxWidth = "30%";
loading.style.height = "auto";
loading.style.fontWeight = "900"
loading.style.fontFamily = "Arial"

//replace()
loading.innerHTML = "caixas de lojas 200 -- cheias^^]]s[[sd[s[%$#"
loading.innerHTML = loading.innerText.replace(/[-]/gi,"");
loading.innerHTML = loading.innerText.replace(/[^a-z0-9 ]/gi,"");
loading.innerHTML = loading.innerText.replace(/cheias..../gi,"cheias");


function toggle (){
    loading.classList.toggle("error");
};

setInterval(toggle, 1000);

window.reload(()=>{
    alert("atualizou a página");
});




//Com Array
//Forma Reference
for(const a of h1s){
    a.innerHTML = "oi";
    a.style.fontWeight = "900"
};

//Forma forEach
h1s.forEach(a => {
    a.innerHTML = "oi";
    a.style.fontWeight = "900"
});