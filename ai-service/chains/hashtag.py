from langchain_community.chat_models import ChatOllama

llm = ChatOllama(model="phi3")

def generate_hashtag(text):
    prompt=f""""Generate 10 trending hashtags for my post to go viral relevant to the content Content:{text}"""
    res=llm.invoke(prompt)
    return res.content