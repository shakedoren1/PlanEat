const express = require('express');
const app = express();
const mongoClient = require('mongodb').MongoClient;

// const url = "mongodb://127.0.0.1:27017";
const url = "mongodb://localhost:27017";
const dbName = 'planEatDB';
const collectionName = 'eventTable';

app.use(express.json());

mongoClient.connect(url, (err, db) => {
  if (err) {
    console.log('Error while connecting to MongoDB client.');
  } else {
    console.log('connecting to MongoDB client.');
    const myDb = db.db(dbName);
    const collection = myDb.collection(collectionName);

    // The call from the new event to insert a new event into the database
    app.post('/newEvent', (req, res) => {
      console.log('Entered /newEvent');
      const newEvent = {
        title: req.body.title,
        date: req.body.date,
        time: req.body.time,
        place: req.body.place,
        concept: req.body.concept,
        number: req.body.number,
      };
        console.log(newEvent);

      collection.insertOne(newEvent, (err, result) => {
        if (err) {
            console.log('error in insert /newEvent');
          res.status(500).send('Error while inserting a new event');
        } else {
            console.log('inside insert /newEvent');
          const eventID = result.insertedId.toString(); // Retrieve the auto-incremented ID
          res.status(200).json({ eventID }); // Send the ID in the response
        }
      });
    });

    // The call from the home page to receive the data on the event
    app.post('/eventDetails', (req, res) => {
        console.log('Entered /eventDetails');
      const eventID = req.body.eventID; // Retrieve the eventID from the request body
      const query = { _id: ObjectId(eventID) }; // ?Use the eventID to search for the event in the collection

      collection.findOne(query, (err, result) => {
        if (err) {
          res.status(500).send('Error retrieving event details');
        } else if (result) {
          const eventToSend = {
            title: result.title,
            date: result.date,
            time: result.time,
            place: result.place,
            concept: result.concept,
            number: result.number,
          };
          res.status(200).send(JSON.stringify(eventToSend)); // Send the event details in the response
        } else {
          res.status(404).send('Event not found');
        }
      });
    });
  }
});

app.listen(8080, () => {
  console.log('Listening on port 8080.');
});
