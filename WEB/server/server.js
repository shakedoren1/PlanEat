const express = require('express');
const app = express();
const { MongoClient } = require('mongodb')
const { ObjectId } = require('mongodb');
const url = 'mongodb://127.0.0.1:27017';
const databaseName = 'PlanEat';
let collectionName = '';
const client = new MongoClient(url);

app.use(express.json());

app.use(express.static(__dirname + '/..'));

// A function that gets an item and insert it to the collection
async function insertItem(item, colName) {
  let result = await client.connect();
  db = result.db(databaseName);
  collection = db.collection(colName);
  const insertResult = await collection.insertOne(item);
  return insertResult;
}

// A function to retrieve event by ID from the database
async function getEventById(eventId, colName) {
  let result = await client.connect();
  db = result.db(databaseName);
  collection = db.collection(colName);
  const event = await collection.findOne({ _id: new ObjectId(eventId) });
  return event;
}

// The call from the creatEvent to insert a new event into the database
app.post('/newEvent', (req, res) => {
  console.log('Entered /newEvent'); // for debug

  collectionName = 'eventsInfo'
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
      const response = await insertItem(newEvent, collectionName);
      console.log(response); // for debug
      res.status(200).json(response); // Send the responses of the insertion
    } catch (error) {
      console.error(error); // for debug
      res.status(500).json({ error: 'Failed to insert event' });
    }
  })();

});

// The call to get an event info based on an id from the database
app.get('/eventInfo/:id', async (req, res) => {
  console.log('Entered /eventInfo'); // for debug

  collectionName = 'eventsInfo'
  const eventId = req.params.id;
  console.log(eventId); // for debug

  try {
    const event = await getEventById(eventId, collectionName);
    console.log(event); // for debug
    res.status(200).json(event);
  } catch (error) {
    console.error(error); // for debug
    res.status(500).json({ error: 'Failed to retrieve event info' });
  }
});

// The call from the website to insert a confirmation into the database
app.post('/confirmation', express.urlencoded({ extended: true }), (req, res) => {
  console.log('Entered /confirmation'); // for debug

  collectionName = 'confirmations'
  const newconfirmation = {
    eventID: req.body.eventID,
    name: req.body.name,
    option: req.body.option,
  };
  console.log(newconfirmation); // for debug

  (async () => {
    try {
      const response = await insertItem(newconfirmation, collectionName);
      console.log(response); // for debug
      res.status(200).json({ message: 'Confirmation received!' }); // Send the responses of the insertion
    } catch (error) {
      console.error(error); // for debug
      res.status(500).json({ error: 'Failed to insert confirmation' });
    }
  })();

});

app.listen(8080, () => {
  console.log('Listening on port 8080.');
});
