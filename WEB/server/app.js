const express = require('express')
const app = express()
const mongoClient = require('mongodb').mongoClient

const url = "mongodb://localhost.27017"

app.use (express.json())

mongoClient.connect(url, (err, db) => {
    if (err) {
        console.log("Eror while connecting mongo client.")
    } else {
        const myDB = db.db('myDB')
        const collection = myDB.collection('myTable') // >>>>>>Change the name acording to the tables

        app.post('/event', (req, res) => {
            const newEvent = {
                title: req.body.title,
                date: req.body.date,
                time: req.body.date,
                place: req.body.place,
                concept: req.body.concept,
                number: req.body.number
            }
            collection.insertOne(newEvent, (err, result) => {
                res.status(200).send()
            })
        })
    }
})

app.listen(3000, () => {
    console.log("Listening on port 3000.")
});