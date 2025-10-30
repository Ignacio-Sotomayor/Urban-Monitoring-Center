// Inicializar el mapa y establecer la vista inicial en Mar del Plata, Argentina
var map = L.map('map').setView([-38.0055, -57.5426], 13);

// Objeto para guardar una referencia a cada marcador por su ID
var markers = {};

// --- Variables globales para los iconos ---
var operativeIcon = L.icon({
    iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
    shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
    iconSize:    [25, 41],
    iconAnchor:  [12, 41],
    popupAnchor: [1, -34],
    shadowSize:  [41, 41]
});

var nonOperativeIcon = L.icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-violet.png',
    shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
    iconSize:    [25, 41],
    iconAnchor:  [12, 41],
    popupAnchor: [1, -34],
    shadowSize:  [41, 41]
});


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

        var initialIcon = (device.status === 'OPERATIVO') ? operativeIcon : nonOperativeIcon;
        var marker = L.marker([device.lat, device.lon], {icon: initialIcon}).addTo(map);
        
        var popupContent = device.popup + "<br><b>Estado:</b> " + device.status;
        marker.bindPopup(popupContent);

        markers[device.id] = marker;
    });
}

/**
 * Función llamada por Java para actualizar el estado de un marcador existente.
 */
function updateMarkerState(deviceId, newStatus, newPopupText) {
    var marker = markers[deviceId];
    if (!marker) return;

    var newIcon = (newStatus === 'OPERATIVO') ? operativeIcon : nonOperativeIcon;
    marker.setIcon(newIcon);

    var newPopupContent = newPopupText + "<br><b>Estado:</b> " + newStatus;
    marker.setPopupContent(newPopupContent);
}