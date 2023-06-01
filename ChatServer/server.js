const express = require('express');
const axios = require('axios');
const app = express();

app.get('/test', async (req, res) => {
  res.json({ message: "here" });
  console.log("Shaked");
});

app.get('/test2', async (req, res) => {
  const response = await axios.post('https://api.openai.com/v1/chat/completions', {
    model: "gpt-3.5-turbo-0301",
    messages: [
      {
        role: "user",  
        content: `Hi, I'm planning a Italian Meal in a potluck style for 10 friends at My house. Can you create a list of things we need to bring please? Make sure the list contains appetizers, mains, sides, dessert, drinks and any other food or non-food items needed, not ingredients or partial dishes. Have only the names of things, have appropriate amounts and be thorough please. No intro and no outro or tips. Thank you!`
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
  console.log("msg=", message, response.data);

  // upload to db
  // let success = await uploadToDB(message)
  return res.send({ answer: message });
});

app.post('/message', async (req, res) => {
  const prompt = req.body;
  console.log("body", req.body);
  const maxTokens = req.body.maxTokens || 60;
  console.log("in msg", prompt, maxTokens);
});

const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});