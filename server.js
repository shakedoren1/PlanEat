const express = require('express');
const axios = require('axios');
const app = express();
app.use(express.json());

app.post('/message', async (req, res) => {
  const prompt = req.body.prompt;
  const maxTokens = req.body.maxTokens || 60;

  try {
    const response = await axios.post('https://api.openai.com/v4/engines/davinci-codex/completions', {
      prompt: prompt,
      max_tokens: maxTokens
    }, {
      headers: {
        'Authorization': `Bearer ${process.env.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}`
      }
    });

    const message = response.data.choices[0].text.trim();
    res.json({ message: message });
  } catch (error) {
    res.json({ error: error.message });
  }
});

const port = process.env.PORT || 3000;
app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});