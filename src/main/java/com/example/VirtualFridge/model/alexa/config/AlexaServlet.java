package com.example.VirtualFridge.model.alexa.config;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.servlet.SkillServlet;
import com.example.VirtualFridge.model.alexa.handlers.CancelandStopIntentHandler;
import com.example.VirtualFridge.model.alexa.handlers.LaunchRequestHandler;

public class AlexaServlet extends SkillServlet {

    public AlexaServlet() {
        super(getSkill());
    }

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new LaunchRequestHandler()
                        /*,
                        new HelloWorldIntentHandler(),
                        new HelpIntentHandler(),
                        new SessionEndedRequestHandler()*/
                )
                .build();
    }
}
