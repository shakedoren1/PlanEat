const express = require('express')
const app = express()
const mongoClient = require('mongodb').mongoClient

const url = "mongodb://localhost.27017"

app.use (express.json())

mongoClient.connect(url, (err, db) => {
    if (err) {
        console.log("Eror while connecting mongo client.")
    } else {
        const myDB = db.db('planEatDB')
        const collection = myDB.collection('eventTable')

        app.post('/newEvent', (req, res) => {
            const newEvent = {
                ID: req.body.id,
                title: req.body.title,
                date: req.body.date,
                time: req.body.time,
                place: req.body.place,
                concept: req.body.concept,
                number: req.body.number
            }
            collection.insertOne(newEvent, (err, result) => {
                res.status(200).send()
            })
        })

        app.post('/eventDetails', (req, res) => {
            const query = { ID: req.body.id }
            collection.findOne(query, (err, result) => {
                if (result != null) {
                    const eventToSend = {
                        title: result.title,
                        date: result.date,
                        time: result.time,
                        place: result.place,
                        concept: result.concept,
                        number: result.number
                    }
                    res.status(200).send(JSON.stringify(eventToSend))
                } else {
                    res.status(404).send()
                }
            })
        })
    }
})

app.listen(3000, () => {
    console.log("Listening on port 3000.")
});