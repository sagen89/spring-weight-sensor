
const MCUconnectTabForm = document.getElementById("MCUconnectTabForm");
MCUconnectTabForm.addEventListener("submit", handleTubFormSubmit);

const connectingToMCUForm = document.getElementById('connectingToMCUForm');
// connectingToMCUForm.addEventListener('submit', handleFormSubmitWhitHiddenButton);


const MCUconnectForms = document.getElementById("MCUconnectForms");
MCUconnectForms.addEventListener('submit', handleSeveralFormsSubmit);

function getObjectsFromRespounse(resp = {}) {
    var objects = new Array;

    for (prop in resp) {
        if (typeof resp[prop] === "object") {
            objects.push(resp[prop]);
        }     
    }
    return objects;
}

function capitalizeString(str) {
  if (typeof str !== 'string' || str.length === 0) {
    return str;
  }
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase().replaceAll("_", " ");
}

async function handleTubFormSubmit(event) {
    event.preventDefault();
    console.log("Event target");
    console.log(event.target);

    var data = dataFormToDataObj(event.target);
    var respounse = await sendData(event.target.action, data, event.target.method);

    if (!respounse["result"]) {
        alert(`Something not right:\n${respounse["msg"]}`);
        return;
    }

    var objsForForms = getObjectsFromRespounse(respounse);
    var postForms = document.querySelectorAll("[method=POST]");

    document.querySelectorAll("[method=POST]").forEach(el => {
        console.log(el);
        objsForForms.forEach(item =>  {
            for (prop in item) {
                if (el[prop]) {
                    console.log(`${prop} = ${item[prop]}`);
                    el[prop].value = item[prop];   
                }
            }         
        });

    });
}

function handleRespounse() {

}

async function handleSeveralFormsSubmit(event) {    
    event.preventDefault();
    console.log("Event target");
    console.log(event.target);
    var buttons = event.currentTarget.getElementsByTagName("button");

    var data = dataFormToDataObj(event.target);
    var respounse = await sendData(event.target.action, data, event.target.method);
    
    if (respounse["result"]) {
        for (i = 0; i < buttons.length; i++) {
            buttons[i].classList.toggle("d-none");
        }
    } else {
        alert(`Something not right:\n${respounse["msg"]}`);
    }

    buttons, data, respounse = null;
}

async function handleFormSubmit(event) {
    event.preventDefault();
    console.log(`Event target: ${JSON.stringify(event.target)}`);
    
    sendData(event.target.action, dataFormToDataObj(event.target), event.target.method);
}

async function sendData(url = "", data = {}, requestMethod = "POST", contentType = "application/json;charset=utf-8") {
    console.log("\t Sending: " + url);
    console.log("\t\t Body: " + JSON.stringify(data));
    var json = {result: false};
    var response; 

    try {        
        if (requestMethod.toUpperCase() === "POST") {
            var options = {
                method: requestMethod,
                headers: {
                    'Content-Type': contentType
                },
                body: JSON.stringify(data)
            }
            response = await fetch(url, options);
            options = null;
        } else {
            response = await fetch(url);
        }
        
        console.log("\t\t status: " + response.status);
        if (response.ok) {
            json = await response.json();   
        }
    } catch (error) {
        console.error("\t\t error while request url: ", error);
    } finally {
        console.log(`\t\t response body: ${JSON.stringify(json)}`);
    return json;
    }
}

function dataFormToDataObj(formNode) {
    const {elements} = formNode;
    var dataObj = {};
    
    Array.from(elements).filter((item) => !!item.name).
        forEach((element) => {
        const { name, value, type } = element;
        dataObj[name] = type === 'checkbox' ? element.checked : element.value;
        console.log({ name, value })
    })
    
    return dataObj;
}

