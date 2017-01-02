package cz.matyapav.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 02.01.2017.
 */
public class StatusMessages {

    private List<String> errors;
    private List<String> messages;

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error){
        if(errors == null){
            errors = new ArrayList<>();
        }
        errors.add(error);
    }

    public void addMultipleErrors(List<String> errors){
        if(this.errors == null){
            this.errors = new ArrayList<>();
        }
        this.errors.addAll(errors);
    }

    public boolean hasErrors(){
        if(errors == null) {
            return false;
        }
        return !errors.isEmpty();
    }

    public List<String> getMessages() {
        return messages;
    }

    public boolean hasMessages(){
        if(messages == null){
            return false;
        }
        return !messages.isEmpty();
    }

    public void addMessage(String message){
        if(messages == null){
            messages = new ArrayList<>();
        }
        messages.add(message);
    }

    public void addMultipleMessages(List<String> messages){
        if(this.messages == null){
            this.messages = new ArrayList<>();
        }
        this.messages.addAll(messages);
    }
}
