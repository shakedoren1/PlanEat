const express = require('express');
const app = express();
const { MongoClient } = require('mongodb')
const { ObjectId } = require('mongodb');
const url = 'mongodb://127.0.0.1:27017';
const databaseName = 'PlanEat';
const collectionName = 'eventsInfo';
const client = new MongoClient(url);

app.use(express.json());

// A function that gets an event and insert it to the data base
async function insertEvent(event) {
  let result = await client.connect();
  db = result.db(databaseName);
  collection = db.collection(collectionName);
  const insertResult = await collection.insertOne(event);
  return insertResult;
}

// A function to retrieve event by ID from the database
async function getEventById(eventId) {
  let result = await client.connect();
  db = result.db(databaseName);
  collection = db.collection(collectionName);
  const event = await collection.findOne({ _id: new ObjectId(eventId) });
  return event;
}

// The call from the new event to insert a new event into the database
app.post('/newEvent', (req, res) => {
  console.log('Entered /newEvent'); // for debug

  const newEvent = {
    title: req.body.title,
    date: req.body.date,
    time: req.body.time,
    place: req.body.place,
    concept: req.body.concept,
    number: req.body.number,
  };
  console.log(newEvent); // for debug

  (async () => {
    try {
      const response = await insertEvent(newEvent);
      console.log(response); // for debug
      res.status(200).json(response); // Send the responses of the insertion
    } catch (error) {
      console.error(error); // for debug
      res.status(500).json({ error: 'Failed to insert event' });
    }
  })();

});

// The call from the home page to to get an event info based on an id from the database
app.get('/eventInfo/:id', async (req, res) => {
  console.log('Entered /eventInfo'); // for debug

  const eventId = req.params.id;
  console.log(eventId); // for debug

  try {
    const event = await getEventById(eventId);
    console.log(event); // for debug
    res.status(200).json(event);
  } catch (error) {
    console.error(error); // for debug
    res.status(500).json({ error: 'Failed to retrieve event info' });
  }
});

app.listen(8080, () => {
  console.log('Listening on port 8080.');
});
