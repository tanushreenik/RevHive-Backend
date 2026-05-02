from langchain_community.chat_models import ChatOllama

llm = ChatOllama(model="llama3")

def moderate_text(text):
    prompt = f"""
    Check if this content is harmful, toxic, or abusive.
    Content: {text}
    Respond with:
    SAFE or UNSAFE + short reason.
    """
    res = llm.invoke(prompt)
    return res.content