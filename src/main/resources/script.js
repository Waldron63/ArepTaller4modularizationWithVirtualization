// Event listeners para los formularios
document.getElementById('getForm').addEventListener('submit', loadGetMsg);
document.getElementById('postForm').addEventListener('submit', loadPostMsg);

async function loadGetMsg(event) {
    event.preventDefault();
    const nameVar = document.getElementById(`getname`).value;
    
    try {
        const response = await fetch(`/getName?name=${encodeURIComponent(nameVar)}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        document.getElementById('getrespmsg').innerHTML = `¡Hola, ${data.name}!`;
    } catch (error) {
        console.error("Error en la solicitud GET:", error);
        document.getElementById("getrespmsg").innerHTML = `Hubo un problema al procesar la solicitud.`;
    }
}

async function loadPostMsg(event) {
    event.preventDefault();
    const nameVar = document.getElementById(`postname`).value;
    try {
        const response = await fetch('/updateName', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'},
            body: JSON.stringify({ name: nameVar })
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        document.getElementById('postrespmsg').innerHTML = `Tu nombre ha sido actualizado a: "${data.name || nameVar}".`;
        
    } catch (error) {
        console.error("Error en la solicitud POST:", error);
        document.getElementById("postrespmsg").innerHTML = `Hubo un problema al procesar la solicitud.`;
    }
}   