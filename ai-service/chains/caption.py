from langchain_community.chat_models import ChatOllama
llm = ChatOllama(model="llam3",temperature=0.7)

def generate_caption(text):
    prompt =f"""Write a catchy engaging social media caption. Content{text}"""

    res=llm.invoke(prompt)
    return res.content