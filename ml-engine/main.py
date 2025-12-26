from fastapi import  FastAPI
from pydantic import  BaseModel
from typing import List, Dict, Optional

app = FastAPI()

# Definition des DTOS

class UserContext(BaseModel):
    userId: str
    age: int
    city: Optional[str] = None
    interests: List[str] = []

class CampaignCandidate(BaseModel):
    id: str
    targetInterests: List[str] = []

class PredictionRequest(BaseModel):
    userContext: UserContext
    candidates: List[CampaignCandidate]


@app.get("/")
def read_root():
    return {"Status": "Ml Engine is Running"}

@app.post("/predict")
def predict_ctr(request: PredictionRequest):
    """
    Simule un modele de Machine Learning.
    Calcul un pCTR entre 0.0 et 1.0
    :param request:
    :return:
    """
    results = {}
    user_interests = set(request.userContext.interests)

    for campaign in request.candidates:
        score = 0.01 #Score de base

        #1. Boost si centres d'interet communs (A faire par une ia complete)
        camp_interests = set(campaign.targetInterests)
        common = user_interests.intersection(camp_interests)

        if common:
            score += (len(common)*0.10)

        # traitement arbitraire
        if request.userContext.age < 30:
            score += 0.05

        results[campaign.id] = min(score, 1.0)

    return results