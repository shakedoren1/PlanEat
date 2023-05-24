const express = require('express');
const app = express();
const mongoClient = require('mongodb').MongoClient;

// const url = "mongodb://127.0.0.1:27017";
const url = "mongodb://localhost:27017";
const dbName = 'PlanEat';
const collectionName = 'eventsInfo';

app.use(express.json());

function mongoConnect() {
	console.log("Entered mongo function")
	console.log(url)
		mongoClient.connect(url, (err, db) => {
		if (err) {
		    console.log('Error while connecting to MongoDB client.');
		  } else {
		    console.log('connecting to MongoDB client.');
		    const myDb = db.db(dbName);
		    const collection = myDb.collection(collectionName);
		    return collection;
		}
	});
}

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

  // collection = mongoConnect();

  mongoClient.connect(url, (err, db) => {
    if (err) {
      console.log('Error while connecting to MongoDB client.');
    } else {
      console.log('connecting to MongoDB client.');
      const myDb = db.db(dbName);
      const collection = myDb.collection(collectionName);
    }

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
});

app.listen(8080, () => {
  console.log('Listening on port 8080.');
});
