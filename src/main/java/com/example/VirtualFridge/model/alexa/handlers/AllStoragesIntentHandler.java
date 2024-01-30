package com.example.VirtualFridge.model.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.example.VirtualFridge.dataManagerImpl.PostgresStorageManager;
import com.example.VirtualFridge.dataManagerImpl.PostgresUserManager;
import com.example.VirtualFridge.model.Storage;
import com.example.VirtualFridge.model.User;

import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;

public class AllStoragesIntentHandler implements IntentRequestHandler {
    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {

        return input.matches(intentName("AllStoragesIntent").or(intentName("AMAZON.CancelIntent")));
    }

    @Override
    public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
        Slot username = intentRequest.getIntent().getSlots().get("userNameQuerie");
        String speechText =  "Folgende Lager gehören zum user " +
                username.getValue() + " "
                //+ "storage: " + storagename.getValue()
         ;
        String translatedUsername = translateSubqueryUsername(username.getValue());

        final PostgresUserManager userMng = PostgresUserManager.getPostgresUserManager();
        User user = userMng.getUserByName(translatedUsername);
        final PostgresStorageManager storageMng = PostgresStorageManager.getPostgresStorageManager();
        Collection<Storage> storages = storageMng.getStorages(user.getID());

        for(Storage s : storages){
            speechText += s.getName() + ", ";
        }

        System.out.println(speechText);

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HelloWorld", speechText)
                .build();

    }

    private String translateSubqueryUsername(String query){
        Map<String, String> dict = new HashMap<>();
        dict.put("null", "0");
        dict.put("eins", "1");
        dict.put("zwei", "2");
        dict.put("drei", "3");
        dict.put("vier", "4");
        dict.put("fünf", "5");
        dict.put("sechs", "6");
        dict.put("sieben", "7");
        dict.put("acht", "8");
        dict.put("neun", "9");

        String outputUserName = "";

        String[] tokens = query.split("\\s+");

        for(String token : tokens){
           if(dict.containsKey(token)) {
               outputUserName += dict.get(token);
           }
           else {
               outputUserName += token;
           }
        }

        return outputUserName;
    }
}
