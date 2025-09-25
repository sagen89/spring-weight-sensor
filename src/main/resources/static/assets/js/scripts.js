const displayTargetElementAttr = "displayTargetElement";
const classForHidingElement = "d-none";


const portNameSelection = document.getElementById("portNameSelection");
portNameSelection.addEventListener("click", handlePortNameSelection);

const serialPortTab = document.getElementById("serialPort-tab");
serialPortTab.addEventListener("submit", async function(event) {
            handleTubFormSubmit(event).
            then(response => {
                if (response === null) {
                    return;
                }
                const mcuUsed = response["mcuUsed"];
                var btnConnect = document.querySelector("#connectingToSerialPortForm button");
                var btnDissconnect = document.querySelector("#disconnectingFromSerialPortForm button");

                btnConnect.classList.remove(classForHidingElement);
                btnDissconnect.classList.remove(classForHidingElement);

                btnConnect.classList.add(mcuUsed ? classForHidingElement : "btn");
                btnDissconnect.classList.add(mcuUsed ? "btn" : classForHidingElement);

                btnConnect, btnDissconnect, response = null;
            }
        )
    }
);

const mainTab = document.getElementById("main-tab");
mainTab.addEventListener("click", (event) => { event.preventDefault(); document.getElementById("mainTabPane-firstChildTab").click(); });

const serialPortForms = document.getElementById("serialPortForms");
serialPortForms.addEventListener('submit', handleSeveralFormsSubmit);

const blinkForms = document.getElementById("blinkForm");
blinkForms.addEventListener('submit', handleFormSubmit);

const weightMeasurementForm = document.getElementById("weightMeasurementForm");
weightMeasurementForm.addEventListener('submit', async function(event) {
    event.preventDefault();
    console.log(`Event target: ${JSON.stringify(event.target)}`);
    
    sendData(event.target.action, dataFormToDataObj(event.target), event.target.method);

    var x = [];
    for (var i = 0; i < 500; i ++) {
        x[i] = Math.random();
    }

    var trace = {
        x: x,
        type: 'histogram',
    };
    var data = [trace];

    var layout = {
        title: {
        text: 'Responsive to window\'s size!'
        },
        font: {size: 18}
    };

    var config = {responsive: true};
    var myChart = await Plotly.newPlot('tester', data, layout, config);
    console.log(myChart);
})

async function handlePortNameSelection(event) {
    var target = event.target;
    var data = Array.prototype.slice.call(target.children, 0).map(opt => opt.value);   
    var response = await sendData("/api/updatePortNames", data, "POST");

    response["namesToRemove"]
        .forEach(name => { 
                document
                    .querySelector(`#${event.target.getAttribute("id")} > [value=\"${name}\"]`)
                    .remove();
            }
        )

    var fragment = document.createDocumentFragment();
    var opt = null;
    response["namesToAdd"]
        .forEach(name => {
                opt = document.createElement("option");
                opt.textContent = name;
                opt.value = name;
                fragment.appendChild(opt);
            }
        )
    event.target.appendChild(fragment);

    data, response, fragment, opt = null;
}

async function handleTubFormSubmit(event) {
    event.preventDefault();
    console.log("Event target:");
    console.log(event.target);

    var data = dataFormToDataObj(event.target);
    var response = await sendData(event.target.action, data, event.target.method);

    if (!response["result"]) {
        alert(`Something not right:\n${response["msg"]}`);
        return;
    }

    var objsForForms = getObjectsFromresponse(response);

    document.querySelectorAll("[method=POST]").forEach(e => {
        objsForForms.forEach(item =>  {
            for (prop in item) {
                if (e[prop]) {
                    console.log(`${prop} = ${item[prop]}`);
                    e[prop].value = item[prop];   
                }
            }         
        });
    });
    
    data, objsForForms = null;
    return response;
    // document.querySelector(document.getElementById("connectingToSerialPortForm").getElementsByTagName("button")[0].dataset["displayTargetElement"]);

}

async function handleSeveralFormsSubmit(event) {    
    event.preventDefault();
    console.log("Event target");
    console.log(event.target);
    var formSubmitter = event.submitter;
    var selectorOfTargetElement = formSubmitter.dataset[displayTargetElementAttr];

    var data = dataFormToDataObj(event.target);
    var response = await sendData(event.target.action, data, event.target.method);
    
    if (response["result"] && selectorOfTargetElement) {
        formSubmitter.classList.toggle("d-none");
        document.querySelectorAll(selectorOfTargetElement)
            .forEach(element => element.classList.toggle("d-none"));
    } else {
        alert(`Something not right:\n${response["msg"]}`);
    }

    formSubmitter, selectorOfTargetElement, data, response = null;
}

async function handleFormSubmit(event) {
    event.preventDefault();
    console.log(`Event target: ${JSON.stringify(event.target)}`);
    
    sendData(event.target.action, dataFormToDataObj(event.target), event.target.method);
}

function getObjectsFromresponse(resp = {}) {
    var objects = new Array;

    for (prop in resp) {
        if (typeof resp[prop] === "object") {
            objects.push(resp[prop]);
        }     
    }
    return objects;
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

