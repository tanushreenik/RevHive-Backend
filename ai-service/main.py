from fastapi import FastAPI
from pydantic import BaseModel

from chains.caption import generate_caption
from chains.hashtag import generate_hashtag
from chains.summary import summary
from chains.moderation import moderate_text

app = FastAPI()


class AIRequest(BaseModel):
    type: str
    content: str

# Contains python main files
@app.post("/ai")
def process(req: AIRequest):

    if req.type == "caption":
        result = generate_caption(req.content)

    elif req.type == "hashtags":
        result = generate_hashtag(req.content)

    elif req.type == "summarize":
        result = summary(req.content)

    elif req.type == "moderate":
        result = moderate_text(req.content)

    else:
        result = "Invalid type"

    return {"result": result}
