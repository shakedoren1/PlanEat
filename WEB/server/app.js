const express = require('express')
const app = express()
const mongoClient = require('mongodb').mongoClient

const url = "mongodb://localhost.27017"

const eventID = "0"

app.use (express.json())

mongoClient.connect(url, (err, db) => {
    if (err) {
        console.log("Eror while connecting mongo client.")
    } else {
        const myDB = db.db('planEatDB')
        const collection = myDB.collection('eventTable')

        // The call from the new event to insert a new event into the data base
        app.post('/newEvent', (req, res) => {
            const newEvent = {
                title: req.body.title,
                date: req.body.date,
                time: req.body.time,
                place: req.body.place,
                concept: req.body.concept,
                number: req.body.number
            }
            collection.insertOne(newEvent, (err, result) => {
                if (err) {
                    res.status(500).send("Error while inserting a new event");
                } else {
                    eventID = result.insertedId; // Retrieve the auto-incremented ID
                    res.status(200).json(eventID); // Send the ID in the response
                }
            });
        })

        // The call from the home page to receive the data on the event
        app.post('/eventDetails', (req, res) => {
            eventID = req.body.eventID; // Retrieve the eventID from the request body
            const query = { insertedId: eventID }; // Use the eventID to search for the event in the collection

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
                        number: result.number
                    };
                    res.status(200).json(eventToSend); // Send the event details in the response
                } else {
                    res.status(404).send('Event not found');
                }
            });
        });
    }
})

app.listen(3000, () => {
    console.log("Listening on port 3000.")
});