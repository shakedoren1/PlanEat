const express = require('express');
const app = express();
const { MongoClient, ObjectId } = require('mongodb');

const url = 'mongodb://localhost:27017';
const dbName = 'planEatDB';
const collectionName = 'eventTable';

app.use(express.json());

MongoClient.connect(url, (err, client) => {
  if (err) {
    console.log('Error while connecting to MongoDB client.');
  } else {
    const db = client.db(dbName);
    const collection = db.collection(collectionName);

    // The call from the new event to insert a new event into the database
    app.post('/newEvent', (req, res) => {
      const newEvent = {
        title: req.body.title,
        date: req.body.date,
        time: req.body.time,
        place: req.body.place,
        concept: req.body.concept,
        number: req.body.number,
      };

      collection.insertOne(newEvent, (err, result) => {
        if (err) {
          res.status(500).send('Error while inserting a new event');
        } else {
          const eventID = result.insertedId.toString(); // Retrieve the auto-incremented ID
          res.status(200).json({ eventID }); // Send the ID in the response
        }
      });
    });

    // The call from the home page to receive the data on the event
    app.post('/eventDetails', (req, res) => {
      const eventID = req.body.eventID; // Retrieve the eventID from the request body
      const query = { _id: ObjectId(eventID) }; // Use the eventID to search for the event in the collection

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
          res.status(200).json(eventToSend); // Send the event details in the response
        } else {
          res.status(404).send('Event not found');
        }
      });
    });
  }
});

app.listen(3000, () => {
  console.log('Listening on port 3000.');
});
