from langchain_community.chat_models import ChatOllama

llm=ChatOllama(model="llama3")

def summary(text):
    prompt=f"""Summarize the content into 3 lines {text}"""

    res=llm.invoke(prompt)
    return res.content