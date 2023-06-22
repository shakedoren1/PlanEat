// SWITCHED TO WORK IN REPLIT

// Get the ID from the URL
const urlParams = new URLSearchParams(window.location.search);
const eventID = urlParams.get('eventID');

// Set the value of the hidden input to the eventID
const eventIDInput = document.getElementById('eventID');
eventIDInput.value = eventID;

// Make an AJAX request to the server to get the event info by the ID
const xhr1 = new XMLHttpRequest();
xhr1.open('GET', `https://websiteserver.shakedoren1.repl.co/eventInfo/${eventID}`, true);
xhr1.onload = function() {
  if (xhr1.status == 200) {
    const eventData = JSON.parse(xhr1.responseText);
    // Update the event title
    const eventTitle = document.getElementById('event-title');
    eventTitle.innerHTML += `<h4>Will we see you at the ${eventData.title} on ${eventData.date} at ${eventData.place}?</h4>`;
  } else {
    // Update the event title
    const eventTitle = document.getElementById('event-title');
    eventTitle.innerHTML += `<h4>This event is not valid! <br>Ask you friend for a new link...</h4>`;
  }
};
xhr1.send();

// show the confirm button when an option is checked
const option1 = document.getElementById('option1');
const option2 = document.getElementById('option2');
const list = document.getElementById('demo-list');
const confirmBtn = document.getElementById('btn');

option1.addEventListener('change', function() {
  if (option1.checked) {
    list.style.display = 'block';
    confirmBtn.style.display = 'block';
  }
});

option2.addEventListener('change', function() {
  if (option2.checked) {
    list.style.display = 'none';
    confirmBtn.style.display = 'block';
  }
});

// Select the form element
const form = document.querySelector('.contact-form');

// Add event listener to form submission
form.addEventListener('submit', function(e) {
  e.preventDefault(); // Prevent the default form submission behavior
  submitConfirmation(); // Call the function to submit the confirmation
});

function submitConfirmation() {
  // Get the form data
  const nameInput = document.getElementById('name');
  const optionInput = document.querySelector('input[name="option"]:checked');

  // Validate the form inputs
  if (!nameInput.value || !optionInput) {
    // Show an error message if any of the required fields are missing
    displayToast('Please fill in all fields.', 'error');
    return;
  }

  // Create a new object to store the form data
  const formData = {
    eventID: eventID,
    name: nameInput.value,
    option: optionInput.value,
  };

  // alert(JSON.stringify(formData)); // for debug

  // Determine the pop-up message and smiley based on the chosen option
  let message = '';
  let smiley = '';

  if (formData.option === 'Coming') {
    message = "Great news!\nSee you soon";
    smiley = "😊";
  } else if (formData.option === 'Not coming') {
    message = "Oh no!\nThat's a bummer";
    smiley = "😞";
  }

  // Make an AJAX request to submit the confirmation
  const xhr2 = new XMLHttpRequest();
  xhr2.open('POST', 'https://websiteserver.shakedoren1.repl.co/confirmation', true);
  xhr2.setRequestHeader('Content-Type', 'application/json'); // Set the request header
  xhr2.onload = function() {
    if (xhr2.status === 200) {
      // Show the pop-up message
      alert(message + "\n" + smiley);
      form.reset(); // Reset the form
    } else {
      // Show an error message if there was an issue with submitting the confirmation
      displayToast('Failed to submit confirmation.', 'error');
    }
  };
  xhr2.send(JSON.stringify(formData)); // Convert the object to JSON string before sending
}

function displayToast(message, type) {
  Toastify({
    text: message,
    duration: 3000,
    close: true,
    gravity: 'top',
    position: 'center',
    backgroundColor: type === 'success' ? '#32CD32' : '#FF0000',
  }).showToast();
}
