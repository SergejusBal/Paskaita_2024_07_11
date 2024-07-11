
const url = 'http://localhost:8080/clients';

document.getElementById("submitBtn").addEventListener("click", async function(event) {
    event.preventDefault();
    

    var user = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    

    const response = await getToken(user, password);
   
    
    if(response == "Invalid username or password") {
         document.getElementById("response").textContent = response;//getCookie("MyTestCokies");
    }
    else{
        setCookie("MyTestCokies",response,7); 
        window.location.href = "./table_form.html"; 
    }
               

})

async function getToken(user,password){
    
        const response = await fetch(url +"/user", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({          
              "user":user,
              "password":password                      
            })       
        })
    
        if (!response.ok) {
            throw new Error('Failed to fetch ticket data');
        } 
    
        return await response.text();      
        
}

async function autorize(){
    const jwttoken =  getCookie("MyTestCokies");
   
    const response = await fetch(url+"/autorize", {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Authorization': jwttoken
        },            
    });

    if (!response.ok) {
        throw new Error('Failed to fetch ticket data');
    } 

    const clients = await response.json();
    return clients;
}


document.addEventListener('DOMContentLoaded',  async function() {    
        const authorized =  await autorize();   
        if(authorized){   
        window.location.href = "./table_form.html"; 
        }
        
  }
  )
