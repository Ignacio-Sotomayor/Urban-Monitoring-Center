// Inicializar el mapa y establecer la vista inicial en Mar del Plata, Argentina
var map = L.map('map', {
    minZoom: 15,
    maxZoom: 20
}).setView([-38.00033863056437, -57.556192022806705], 17);

// Objeto para guardar una referencia a cada marcador por su ID
var markers = {};
var repairQueue = [];
var repairIntermittentQueue = [];
var normalModeQueue = []; // New queue for switching to normal mode

// Añadir una capa de mapa base de OpenStreetMap
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

/**
 * Función que recibe los dispositivos iniciales desde Java y los añade al mapa.
 */
function setMarkers(devices) {
    if (!Array.isArray(devices)) return;

    devices.forEach(function(device) {
        if (device.lat === undefined || device.lon === undefined) return;

        var customIcon = L.icon({
            iconUrl: device.icon,
            iconSize:    [25, 41],
            iconAnchor:  [12, 41],
            popupAnchor: [1, -34],
            shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
            shadowSize:  [41, 41]
        });

        var marker = L.marker([device.lat, device.lon], {icon: customIcon}).addTo(map);

        var popupContent = createPopupContent(device);
        marker.bindPopup(popupContent);

        markers[device.id] = marker;
    });
}

/**
 * Función llamada por Java para actualizar el estado de un marcador existente.
 */
function updateMarkerState(deviceId, newStatus, newPopupText, newIconUrl, deviceType) {
    var marker = markers[deviceId];
    if (!marker) return;

    var newIcon = L.icon({
        iconUrl: newIconUrl,
        iconSize:    [25, 41],
        iconAnchor:  [12, 41],
        popupAnchor: [1, -34],
        shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
        shadowSize:  [41, 41]
    });

    marker.setIcon(newIcon);

    var device = { id: deviceId, popup: newPopupText, status: newStatus, type: deviceType };
    var newPopupContent = createPopupContent(device);
    marker.setPopupContent(newPopupContent);
}

function createPopupContent(device) {
    var content = device.popup + "<br><b>Estado:</b> " + device.status;
    if (device.status !== 'OPERATIVO') {
        if (device.type === 'TrafficLightController') {
            content += `<br><button onclick="queueForRepair('${device.id}', 'normal')">Reparar (Normal)</button>`;
            content += `<br><button onclick="queueForRepair('${device.id}', 'intermittent')">Reparar (Intermitente)</button>`;
        } else {
            content += `<br><button onclick="queueForRepair('${device.id}', 'normal')">Reparar Dispositivo</button>`;
        }
    } else if (device.type === 'TrafficLightController' && device.popup.includes('MODO INTERMITENTE')) {
        content += `<br><button onclick="queueForNormalMode('${device.id}')">Cambiar a Modo Normal</button>`;
    }
    return content;
}

function queueForRepair(deviceId, repairType) {
    if (repairType === 'intermittent') {
        if (!repairIntermittentQueue.includes(deviceId)) {
            repairIntermittentQueue.push(deviceId);
        }
    } else {
        if (!repairQueue.includes(deviceId)) {
            repairQueue.push(deviceId);
        }
    }
}

function queueForNormalMode(deviceId) {
    if (!normalModeQueue.includes(deviceId)) {
        normalModeQueue.push(deviceId);
    }
}

function getAndClearRepairQueue() {
    var queue = [...repairQueue];
    repairQueue = [];
    return JSON.stringify(queue);
}

function getAndClearIntermittentRepairQueue() {
    var queue = [...repairIntermittentQueue];
    repairIntermittentQueue = [];
    return JSON.stringify(queue);
}

function getAndClearNormalModeQueue() {
    var queue = [...normalModeQueue];
    normalModeQueue = [];
    return JSON.stringify(queue);
}