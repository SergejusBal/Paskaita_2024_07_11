


class Client {        

    constructor(name, surname, email, phone) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;        
    }
}


async function sendClicked() {

    
    const client = fillClientClass();
    clearFields();
    createClient(client).then();
    fetchclientsForEvent();  
    
}



async function fetchclientsForEvent() {
        const clients = await getAllClients();        
        displayClients(clients);    
}


function displayClients(clients) {
    const tableBody = document.getElementById('clientTable').getElementsByTagName('tbody')[0];
    tableBody.innerHTML = ''; // Clear existing clients entries

    clients.forEach(clients => {
        const row = tableBody.insertRow();
        row.insertCell(0).textContent = clients.id || 'No name available';
        row.insertCell(1).textContent = clients.name || 'No name available';
        row.insertCell(2).textContent = clients.surname || 'No name available';
        row.insertCell(3).textContent = clients.email || 'No name available';
        row.insertCell(4).textContent = clients.phone || 'No name available';

         const modifyCell = row.insertCell(5);
                const modifyButton = document.createElement('button');                
                modifyButton.textContent = 'Modify';
                modifyButton.className = 'modify-button';
                modifyButton.onclick = async function() {
                    await setFields(clients.id);                   

                };
                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'Delete';
                deleteButton.className = 'delete-button';
                deleteButton.onclick = async function() {
                    if (confirm("Do you want to proceed?")) {                        
                        await deleteClient(clients.id);
                        fetchclientsForEvent();
                    } else {
                        
                    }
                    
                };

                modifyCell.appendChild(modifyButton);
                modifyCell.appendChild(deleteButton);
    });
}



async function editClientCliked() {
    const client = fillClientClass(); 
    clearFields();
    await alterClient(client);    
    fetchclientsForEvent();    
}


async function setFields(id){
    const clients = await getClientbyID(id);
    document.getElementById("name").value = clients.name;
    document.getElementById("surname").value = clients.surname;    
    document.getElementById("email").value = clients.email;
    document.getElementById("phone").value = clients.phone;
    document.getElementById('hiddenInput').value = clients.id;

}


function clearFields(){
    document.getElementById("surname").value = "";
    document.getElementById("name").value = "";
    document.getElementById("email").value = "";
    document.getElementById("phone").value = "";
    document.getElementById('hiddenInput').value = "";
}

function fillClientClass(){    
    var name = document.getElementById("name").value;
    var surname = document.getElementById("surname").value;
    var email = document.getElementById("email").value;
    var phone = document.getElementById("phone").value;
    var id = document.getElementById("hiddenInput").value || "0";

    const client = new Client(name, surname, email, phone);
    client.id = id;

    return client;
}

document.addEventListener('DOMContentLoaded', async function() {
    const authorized =  await autorize();
    if(!authorized){
        window.location.href = "./index.html"; 
    }else
    {
    fetchclientsForEvent();
    }    
  }
  )
