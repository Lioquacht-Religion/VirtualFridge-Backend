package com.example.VirtualFridge.model.alexa;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import org.springframework.beans.factory.annotation.Autowired;

public class StorageAllGroceriesIntent implements IntentHandler{

    @Override
    public SpeechletResponse handleIntent(Intent intent, IntentRequest intentRequest, Session session){
       Card card = AlexaUtils.newCard("Your Storage", "testStorageGroceries");
        PlainTextOutputSpeech speech = AlexaUtils.newSpeech("testStorGroc", AlexaUtils.inConversationMode(session));

       return AlexaUtils.newSpeechletResponse(card, speech, session, false);
    }

}
