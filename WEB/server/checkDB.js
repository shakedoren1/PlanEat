const { MongoClient } = require('mongodb')
const url = 'mongodb://127.0.0.1:27017';
const databaseName = 'PlanEat';
const collectionName = 'eventsInfo';
const client = new MongoClient(url);

async function getData() {
  let result = await client.connect();
  db = result.db(databaseName);
  collection = db.collection(collectionName);
  let data = await collection.find({}).toArray();
  console.log(data);
}

async function insertData() {
  let result = await client.connect();
  db = result.db(databaseName);
  collection = db.collection(collectionName);
  const newEvent = {
    title: "check",
    date: "21.05.23",
    time: "14:28",
    place: "Shaked's home",
    concept: "ok",
    number: "1",
  };
  const insertResult = await collection.insertOne(newEvent);
  console.log(typeof(insertResult));
}

// getData();
insertData();
// getData();


// checkDB.listen(8080, () => {
//   console.log('Listening on port 8080.');
// });
