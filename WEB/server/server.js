// SWITCHED TO WORK IN REPLIT

const express = require('express');
const app = express();
const axios = require('axios');
const { MongoClient } = require('mongodb')
const { ObjectId } = require('mongodb');
const path = require('path');
const url = 'mongodb+srv://PlanEatList:PlanEat123@planeat.selzkm5.mongodb.net/?retryWrites=true&w=majority';
const databaseName = 'PlanEat';
let collectionName = '';
const client = new MongoClient(url);
const cors = require('cors');
app.use(cors()); // Enable CORS for all routes

app.use(express.json());

// Allowing to run the website on the server localy
// app.use(express.static(__dirname + '/..'));

// Route handler for the root URL // In replit only
app.get('/', (req, res) => {
  const filePath = path.join(__dirname, 'message.html');
  res.sendFile(filePath);
});

// access to the logo // In replit only
app.get('/logo', (req, res) => {
  const filePath = path.join(__dirname, 'icons/logo.png');
  res.sendFile(filePath);
});

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

// A function to retrieve all the confirmations of an event by ID from the database
async function getConfirmationsByEventId(eventId, colName) {
  let result = await client.connect();
  db = result.db(databaseName);
  collection = db.collection(colName);
  const confirmations = await collection.find({ eventID: eventId }).toArray();
  return confirmations;
}

// A function to update the event ID in the list document
async function updateEventID(eventID, listId) {
  let result = await client.connect();
  db = result.db(databaseName);
  collection = db.collection(collectionName);
  await collection.updateOne({ _id: new ObjectId(listId) }, { $set: { eventID: eventID } });
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
  // console.log(newEvent); // for debug

  (async () => {
    try {
      const response = await insertItem(newEvent, collectionName);
      // console.log(response); // for debug
      res.status(200).json(response); // Send the responses of the insertion
    } catch (error) {
      // console.error(error); // for debug
      res.status(500).json({ error: 'Failed to insert event' });
    }
  })();

});

// The call to get an event info based on an id from the database
app.get('/eventInfo/:id', async (req, res) => {
  console.log('Entered /eventInfo'); // for debug

  collectionName = 'eventsInfo'
  const eventId = req.params.id;
  // console.log(eventId); // for debug

  if (eventId != null) {
    try {
      const event = await getEventById(eventId, collectionName);
      // console.log(event); // for debug
      res.status(200).json(event);
    } catch (error) {
      // console.error(error); // for debug
      res.status(500).json({ error: 'Failed to retrieve event info' });
    }
  } else {
    res.status(500).json({ error: 'Failed to retrieve event info' });
  }
});

// The call from the website to insert a confirmation into the database
app.post('/confirmation', (req, res) => {
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
      // console.log(response); // for debug
      res.status(200).json({ message: 'Confirmation received!' }); // Send the responses of the insertion
    } catch (error) {
      // console.error(error); // for debug
      res.status(500).json({ error: 'Failed to insert confirmation' });
    }
  })();

});

// The call to get confirmations for an event based on an id from the database
app.get('/eventCon/:id', async (req, res) => {
  console.log('Entered /eventCon'); // for debug

  collectionName = 'confirmations'
  const eventId = req.params.id;
  console.log(eventId); // for debug

  if (eventId != null) {
    try {
      const confirmations = await getConfirmationsByEventId(eventId, collectionName);
      console.log(confirmations); // for debug
      res.status(200).json(confirmations);
    } catch (error) {
      console.error(error); // for debug
      res.status(500).json({ error: 'Failed to retrieve event info' });
    }
  } else {
    res.status(500).json({ error: 'Failed to retrieve event info' });
  }
});

app.post('/prompt', async (req, res) => {
  try {
    const response = await axios.post('https://api.openai.com/v1/chat/completions', {
      model: "gpt-3.5-turbo-0301",
      messages: [
        {
          role: "user",
          content: `Hi, I'm planning a ${req.body.concept} in a potluck style for ${req.body.number} friends at My house. Can you create a list of things we need to bring please? Make sure the list contains Appetizers, Mains, Sides, Dessert, Drinks and Others food or non-food items needed, not ingredients or partial dishes. Have only the names of things, have appropriate amounts and be thorough please. No intro and no outro or tips. Thank you! Please return this as a JSON file where each item has an "amount" and an "item" name in that order and the headers are as stated vefore.`
        }
      ]
    }, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer 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`
      }
    });

    let prompt = response.data.choices[0].message.content;

    collectionName = 'Ingredients'

    const result = await insertItem(JSON.parse(prompt), collectionName);
    const listId = result.insertedId.toString();
    
    // Update the list document with the event ID
    const eventID = req.body.eventID;
    if (eventID) {
      await updateEventID(eventID, listId);
    }
    
    res.status(200).json({ prompt, insertedId: listId });

    res.status(200).json({ prompt, insertedId: result.insertedId.toString() });

  } catch (error) {
    console.error('Error:', error);
    console.error('Error details:', error.response.data);
    res.status(500).json({ error: 'Failed to generate prompt' });
  }
});

// The call from the group task list page to to get a list based on an id from the database
app.get('/listInfo/:id', async (req, res) => {
  console.log('Entered /listInfo'); // for debug

  collectionName = 'Ingredients'
  const listId = req.params.id;
  console.log(listId); // for debug

  try {
    const list = await getEventById(listId, collectionName);
    console.log(list); // for debug
    res.status(200).json(list);
  } catch (error) {
    console.error(error); // for debug
    res.status(500).json({ error: 'Failed to retrieve ingredient list' });
  }
});

app.listen(8080, () => {
  console.log('Listening on port 8080.');
});
