from fastapi import FastAPI
from pydantic import BaseModel
from sqlalchemy.engine import result

from chains.caption import generate_caption;
from chains.hashtag import generate_hashtag;
from chains.summary import summary;
from chains.moderation import moderate_text;

app=FastAPI()


class AIRequest(BaseModel):
    text: str
    content:str

@app.post("/ai")
def process(req: AIRequest):
    if req.type == "caption":
        result=generate_caption(req.text)
    elif req.type == "hashtag":
        result=generate_hashtag(req.text)
    elif req.type == "summary":
        result = summary(req.text)
    elif req.type == "moderation":
        result = moderate_text(req.text)
    else:
        result="Invalid type"
    return {"result":result}