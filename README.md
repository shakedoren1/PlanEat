# PlanEat
### An android app created to help organize social gatherings.
The app was developed in java using Android Studio. The website that connects to the app is a simple combination of HTML, CSS and JS. And the database for the app and the website is a mongoDB. <br>
The server that connects both the app and the website to the database and other online services (such as openAI api) in a Node.js server.

Here is a brief flow of the app:

##### Creating a new event:<br>
<img src="ScreenShots/CreateEvent.png" alt="CreateEvent" width="200">

##### The event is inserted into the database and gets a unique ID:<br>
<img src="ScreenShots/DataBase.png" alt="DataBase" width="300">

##### You get a custom message to share with your friends on Whatsapp:<br>
<img src="ScreenShots/Invite.png" alt="Invite" width="200"> <br>
<img src="ScreenShots/Whatsapp.jpg" alt="Whatsapp" width="200">

##### The home page created for the event:<br>
<img src="ScreenShots/HomePage1.png" alt="HomePage1" width="200">

##### The confirmation website that links to the specific event available for anyone that received the link:<br>
<img src="ScreenShots/RSVP.jpg" alt="RSVP" width="200">

##### The event home page after a few people responded:<br>
<img src="ScreenShots/HomePage2.png" alt="HomePage2" width="200">
<img src="ScreenShots/Confirmations.png" alt="Confirmations" width="200">
<br>

Inside the app we used fragments for the different windows of the app. Each event is stored inside the database and each RSVP is also saved to the database with the relevant event ID. There is also an AI generated ingredient list for the event based on the concept and number of people invited.