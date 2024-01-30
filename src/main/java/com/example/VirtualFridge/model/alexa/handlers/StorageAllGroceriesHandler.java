package com.example.VirtualFridge.model.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class StorageAllGroceriesHandler implements IntentRequestHandler {
    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        //return intentRequest.getIntent().equals("StorageAllGroceries");
        return input.matches(intentName("StorageAllGroceries").or(intentName("AMAZON.CancelIntent")));
    }


    @Override
    public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
        Slot username = intentRequest.getIntent().getSlots().get("userName");
        //Slot storagename = intentRequest.getIntent().getSlots().get("storageName");
        String speechText =  "Das Lager entält folgende Lebensmittel: username: " +
                username.getValue()
                //+ "storage: " + storagename.getValue()
         ;
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HelloWorld", speechText)
                .build();
    }
}
