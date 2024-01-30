package com.example.VirtualFridge.model.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.example.VirtualFridge.dataManagerImpl.PostgresStorageManager;
import com.example.VirtualFridge.dataManagerImpl.PostgresUserManager;
import com.example.VirtualFridge.model.Grocery;
import com.example.VirtualFridge.model.Storage;
import com.example.VirtualFridge.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
        Slot query = intentRequest.getIntent().getSlots().get("userStorageGroceryQuerie");
        String speechText =  "Das Lager enthält die Lebensmittel ";

        String[] tokens = translateSubqueryUserAndStorage(query.getValue());
        String userName = tokens[1], storageName = tokens[0];

        System.out.println("user: " + userName + ", storage: " + storageName);

        final PostgresUserManager userMng = PostgresUserManager.getPostgresUserManager();
        User user = userMng.getUserByName(userName);
        System.out.println("act user: " + user.getName());
        final PostgresStorageManager storageMng = PostgresStorageManager.getPostgresStorageManager();
        Storage storage = storageMng.getStorageByName(user, storageName);
        System.out.println("act storage: " + storage.getName());
        Collection<Grocery> groceries = storageMng.getGroceries(user.getID(), storage.getStorageID());
        System.out.println("act groceries: " + groceries.size());

        for(Grocery g : groceries){
            speechText += g.getName() + ", ";
        }

        System.out.println(speechText);

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HelloWorld", speechText)
                .build();

    }

    private String[] translateSubqueryUserAndStorage(String query){
        String storageName = "";
        String userName = "";

        String[] tokens = query.split(" von nutzer ");
        if(tokens.length == 2){
            if(tokens[0].startsWith("lager ") && tokens[0].length() > 6){
                storageName = tokens[0].substring(6);
                storageName = translateSubqueryName(storageName);
            }
            userName = translateSubqueryName(tokens[1]);
        }

        return new String[]{ storageName, userName};

    }

    private String translateSubqueryName(String query){
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
