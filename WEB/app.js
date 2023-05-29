// Get the ID from the URL
const urlParams = new URLSearchParams(window.location.search);
const eventID = urlParams.get('eventID');

// Set the placeholder value with the ID
const placeholder = document.getElementById('event-id');
placeholder.innerHTML += `<h4>The event id is: ${eventID}</h4>`;