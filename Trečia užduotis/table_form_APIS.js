const url = 'http://localhost:8080/clients';

async function getAllClients() {   
    
    const authorized =  await autorize();    

    const jwttoken =  getCookie("MyTestCokies");
    try {
        let response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Authorization': jwttoken
            },            
        });

        if (response.status == 401){
            window.location.href = "./index.html"; 
        }
        else if (response.status == 500 ) {
            throw new Error('Failed to fetch ticket data');
        }       

        return await response.json();       


    } catch (error) {
        console.error('Error:', error);
    }   
    
    
}

//Vienas paliktas, kitu stiliumi, kad būtų galima prisiminti then funcija. Realiai jai nereikia async.
async function createClient(client) {        

    let jwttoken =  getCookie("MyTestCokies");

    try{
        fetch(url, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': jwttoken
            },
            body: JSON.stringify({
            "name":client.name,
            "surname":client.surname,
            "email":client.email,
            "phone":client.phone          
            }),        
        })
        .then(response => {
            if (response.status == 401){
                window.location.href = "./index.html"; 
            }
            else if (response.status == 500 ) {
                throw new Error('Failed to fetch ticket data');
            } 
            return response.text();
        })
        .then(data => {
            console.log('Success:', data);
            document.getElementById("test").textContent = "Response: " + data;
        })
    
    } catch (error) {
        console.error('Error:', error);
    } 

    
    fetchclientsForEvent();
    
    
}


async function getClientbyID(id){   

    const jwttoken =  getCookie("MyTestCokies");

    try{
        let response = await fetch(url+"/"+id, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Authorization': jwttoken
            },            
        });

        if (response.status == 401){
            window.location.href = "./index.html"; 
        }
        else if (response.status == 500 ) {
            throw new Error('Failed to fetch ticket data');
        } 

        const clients = await response.json();
        return clients;
    } catch (error) {
        console.error('Error:', error);
    } 
    
}


async function alterClient(client){

    const jwttoken =  getCookie("MyTestCokies");

    try{
        let response = await fetch(url+"/"+client.id, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': jwttoken
            },
            body: JSON.stringify({          
            "name":client.name,
            "surname":client.surname,
            "email":client.email,
            "phone":client.phone          
            })       
        })

        if (response.status == 401){
            window.location.href = "./index.html"; 
        }
        else if (response.status == 500 ) {
            throw new Error('Failed to fetch ticket data');
        } 
        

        await response.text();
    } catch (error) {
        console.error('Error:', error);
    } 
    
    
}


async function deleteClient(id){
       
    const jwttoken =  getCookie("MyTestCokies");

    try{
        let response = await fetch(url+"/"+id, {
            method: 'DELETE',
            headers: {

                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Authorization': jwttoken
            },            
        });

        if (response.status == 401){
            window.location.href = "./index.html"; 
        }
        else if (response.status == 500 ) {
            throw new Error('Failed to fetch ticket data');
        } 
    } catch (error) {
        console.error('Error:', error);
    } 
     
}

async function autorize(){

    const jwttoken =  getCookie("MyTestCokies");
   
    let response = await fetch(url+"/autorize", {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Authorization': jwttoken
        },            
    });   

    const clients = await response.json();
    return clients;
}