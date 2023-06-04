// Get the ID from the URL
const urlParams = new URLSearchParams(window.location.search);
const eventID = urlParams.get('eventID');

// Set the placeholder value with the ID
const placeholder = document.getElementById('event-id');
placeholder.innerHTML += `<h4>The event id is: ${eventID}</h4>`;

// Set the value of the hidden input to the eventID
const eventIDInput = document.getElementById('eventID');
eventIDInput.value = eventID;

// Make an AJAX request to the server to get the event info by the ID
const xhr1 = new XMLHttpRequest();
xhr1.open('GET', `http://localhost:8080/eventInfo/${eventID}`, true);
xhr1.onload = function () {
  if (xhr1.status === 200) {
    const eventData = JSON.parse(xhr1.responseText);
    // Update the event title
    const eventTitle = document.getElementById('event-title');
    eventTitle.innerHTML += `<h4>Will we see you at the ${eventData.title} on ${eventData.date} at ${eventData.place}?</h4>`;
  }
};
xhr1.send();

// // Add an event listener to the submit button
// document.getElementById('btn').addEventListener('click', function(event) {
//     event.preventDefault(); // Prevent the default form submission behavior

//     // Make an AJAX request to the server to insert confirmation when submiting the form
//     const xhr2 = new XMLHttpRequest();
//     xhr2.open('POST', 'http://localhost:8080/confirmation', true);
//     xhr2.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
//     xhr2.onload = function () {
//     if (xhr2.status === 200) {
//         const response = JSON.parse(xhr2.responseText);
//         // Display a toast notification
//         Toastify({
//             text: response.message,
//             duration: 3000, // Toast duration in milliseconds (3 seconds)
//             gravity: 'top', // Position the toast at the top of the screen
//             close: true // Show a close button on the toast
//             }).showToast();
//         // Reset the form
//         document.querySelector('.contact-form').reset(); 
//     }
//     };
//     xhr2.send(new FormData(document.querySelector('.contact-form')));
// });