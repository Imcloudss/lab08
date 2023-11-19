package it.unibo.deathnote.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote {

    private static final byte ILLEGAL_CAUSE_TIMEOUT = 40;
    private static final long ILLEGAL_DETAILS_TIMEOUT = 6000 + ILLEGAL_CAUSE_TIMEOUT;

    private Map<String, Death> deathNote = new LinkedHashMap<>();
    private long timeStartWriting;
    private String lastName;

    @Override
    public String getRule(final int ruleNumber) {
        if(ruleNumber > 0 && ruleNumber < RULES.size()) {
            return RULES.get(ruleNumber - 1);
        }
        else {
            throw new IllegalArgumentException("This rule doesn't exist");
        }
    }

    @Override
    public void writeName(String name) {
        Objects.requireNonNull(name);
        this.lastName = name;
        this.timeStartWriting = System.currentTimeMillis();
        this.deathNote.put(name, new Death());
    }

    @Override
    public boolean writeDeathCause(final String cause) {
        if(Objects.isNull(cause) || this.deathNote.isEmpty()) {
            throw new IllegalStateException("The name has to be written before the cause of death");
        }

        if((System.currentTimeMillis() - this.timeStartWriting) <= ILLEGAL_CAUSE_TIMEOUT) {
            this.deathNote.get(this.lastName).setCause(cause);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean writeDetails(String details) {
        if(Objects.isNull(details) || this.deathNote.isEmpty()) {
            throw new IllegalStateException("The name has to be written before the details of death");
        }

        if((System.currentTimeMillis() - this.timeStartWriting) <= ILLEGAL_DETAILS_TIMEOUT) {
            this.deathNote.get(this.lastName).setDetails(details);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getDeathCause(String name) {
        return this.deathNote.get(name).cause;
    }

    @Override
    public String getDeathDetails(String name) {
        return this.deathNote.get(name).details;
    }

    @Override
    public boolean isNameWritten(String name) {
        for(final String names : this.deathNote.keySet()) {
            if(names == name) {
                return true;
            }
        }
        return false;
    }
    
    public class Death {

        private static final String DEFAULT_DEATH = "heart attack";

        private String cause;
        private String details;

        public Death(String cause, String details) {
            this.cause = cause;
            this.details = details;
        }

        public Death() {
            this(DEFAULT_DEATH, "");
        }

        public void setCause(final String cause){
            this.cause = cause;
        }
    
        public void setDetails(final String details){
            this.details = details;
        }
    }    
}
