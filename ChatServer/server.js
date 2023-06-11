const express = require('express');
const axios = require('axios');
const bodyParser = require('body-parser')
const app = express();
app.use(bodyParser.json());
const { MongoClient, ObjectId } = require('mongodb');
const url = 'mongodb+srv://PlanEatList:PlanEat123@planeat.selzkm5.mongodb.net/?retryWrites=true&w=majority';
const databaseName = 'PlanEat';
let collectionName = '';
const client = new MongoClient(url);

app.use(express.json());
// Allowing to run the website on the server
app.use(express.static(__dirname + '/..'));

// A function that gets an item and inserts it in the collection
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
      console.log(response); // for debug
      res.status(200).json({ message: 'Confirmation received!' }); // Send the responses of the insertion
    } catch (error) {
      console.error(error); // for debug
      res.status(500).json({ error: 'Failed to insert confirmation' });
    }
  })();

});


app.post('/prompt', async (req, res) => {
  try {
    // console.log(req.body.concept)
    // console.log(req.body.number)
    const response = await axios.post('https://api.openai.com/v1/chat/completions', {
      model: "gpt-3.5-turbo-0301",
      messages: [
        {
          role: "user",  
          content: `Hi, I'm planning a ${req.body.concept} in a potluck style for ${req.body.number} friends at My house. Can you create a list of things we need to bring please? Make sure the list contains Appetizers, Mains, Sides, Dessert, Drinks and Others food or non-food items needed, not ingredients or partial dishes. Have only the names of things, have appropriate amounts and be thorough please. No intro and no outro or tips. Thank you! Please return this as a JSON file where each item has an amount and an item name.`
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
    
    // const itemId = await insertItem(JSON.parse(prompt), collectionName);
    
    // res.status(200).json({ prompt, insertedId: itemId.toString() });

    const result = await insertItem(JSON.parse(prompt), collectionName);

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

const port = 8080;
app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});