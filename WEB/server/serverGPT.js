const express = require('express');
const axios = require('axios');
const bodyParser = require('body-parser')
const app = express();
app.use(bodyParser.json());
const { MongoClient, ObjectId } = require('mongodb');
const url = 'mongodb+srv://PlanEatList:PlanEat123@planeat.selzkm5.mongodb.net/?retryWrites=true&w=majority';
const databaseName = 'PlanEat';
const collectionName = 'Ingredients';
const client = new MongoClient(url);

app.get('/test', async (req, res) => {
  res.json({ message: "here" });
  console.log("Shaked");
});

app.get('/message', async (req, res) => {
  const response = await axios.post('https://api.openai.com/v1/chat/completions', {
    model: "gpt-3.5-turbo-0301",
    messages: [
      {
        role: "user",  
        content: `Hi, I'm planning a Italian Meal in a potluck style for 10 friends at My house. Can you create a list of things we need to bring please? Make sure the list contains appetizers, mains, sides, dessert, drinks and any other food or non-food items needed, not ingredients or partial dishes. Have only the names of things, have appropriate amounts and be thorough please. No intro and no outro or tips. Thank you! Please return this as a JSON file where each item has an amount and an item name.`
      }
    ]
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer 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`
    }
  });

  console.log('Response:', response.data);
  let message = response.data.choices[0].message.content;

  // upload to db
  const newList = {
    content: message
  };
  console.log(newList); // for debug

  (async () => {
    try {
      const response = await insertList(newList);
      console.log(response); // for debug
      res.status(200).json(response); // Send the responses of the insertion
    } catch (error) {
      console.error(error); // for debug
      res.status(500).json({ error: 'Failed to insert ingerdient list' });
    }
  })();
});

const port = process.env.PORT || 8081;
app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});

// A function that gets an event and insert it to the data base
async function insertList(list) {
  let result = await client.connect();
  db = result.db(databaseName);
  collection = db.collection(collectionName);
  const insertResult = await collection.insertOne(list);
  return insertResult;
}

// A function to retrieve event by ID from the database
async function getListById(listId) {
  let result = await client.connect();
  db = result.db(databaseName);
  collection = db.collection(collectionName);
  const list = await collection.findOne({ _id: new ObjectId(listId) });
  return list;
}

// The call from the group task list page to to get a list based on an id from the database
app.get('/listInfo/:id', async (req, res) => {
  console.log('Entered /listInfo'); // for debug

  const listId = req.params.id;
  console.log(listId); // for debug

  try {
    const list = await getListById(listId);
    console.log(list); // for debug
    res.status(200).json(list);
  } catch (error) {
    console.error(error); // for debug
    res.status(500).json({ error: 'Failed to retrieve ingredient list' });
  }
});