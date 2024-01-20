package com.example.VirtualFridge.model.alexa.handlers;

import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.example.VirtualFridge.model.alexa.AlexaUtils;
import org.springframework.beans.factory.BeanFactory;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.example.VirtualFridge.model.alexa.IntentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HandlerSpeechlet implements SpeechletV2 {

    @Autowired
    private BeanFactory beanFactory;
    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope){

    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope){
        Session session = requestEnvelope.getSession();
        AlexaUtils.setConversationMode(session, true);

        String speechText = "Hello. " + AlexaUtils.SamplesHelpText;

        Card card = AlexaUtils.newCard("Welcome!", speechText);
        PlainTextOutputSpeech speech = AlexaUtils.newSpeech(speechText, false);

        return AlexaUtils.newSpeechletResponse(card, speech, session, false);

    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope){
        IntentRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();

        Intent intent = request.getIntent();
        String intentName = intent.getName();

        String handlerBeanName = intentName + "Handler";
        Object handlerBean =  beanFactory.getBean(handlerBeanName);

        IntentHandler intentHandler = (IntentHandler) handlerBean;
        return intentHandler.handleIntent(intent, request, session);
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope){

    }
}
