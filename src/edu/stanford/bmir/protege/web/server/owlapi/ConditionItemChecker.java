package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.ConditionSuggestion;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2012
 */
public class ConditionItemChecker {

    /**
     * A character that isn't part of a well formed Manchester syntax expression that can be used to force a parse error.
     */
    private static final String ERROR_CHARACTER = "*";

    private OWLAPIProject project;
    
    public ConditionItemChecker(OWLAPIProject project) {
        this.project = project;
    }
    
    public boolean isValid(String conditionItemText) {
        try {
            ConditionItemParser parser = new ConditionItemParser(project);
            parser.parse(conditionItemText);
            return true;
        }
        catch (ParserException e) {
            return false;
        }
    }
    
    
    public ConditionSuggestion getConditionSuggestion(String conditionItemText, int caretPosition) {
        // Needs to be done in two stages, because Web-Protege blurs error checking and autocompletion.
        ConditionSuggestion result = new ConditionSuggestion();
        setValidityAndErrorMessage(conditionItemText, result);
        setAutoCompletionSuggestions(conditionItemText, caretPosition, result);
        return result;
    }


    private void setAutoCompletionSuggestions(String conditionItemText, int caretPosition, ConditionSuggestion result) {
        List<EntityData> suggestions = getAutoCompleteSuggestions(conditionItemText, caretPosition);
        result.setSuggestions(suggestions);
    }

    private void setValidityAndErrorMessage(String conditionItemText, ConditionSuggestion conditionSuggestion) {
        try {
            ConditionItemParser fullTextParser = new ConditionItemParser(project);
            conditionSuggestion.setValid(true);
            fullTextParser.parse(conditionItemText);
        }
        catch (ParserException e) {
            conditionSuggestion.setValid(false);
            conditionSuggestion.setMessage(e.getMessage());
        }
    }

    public List<EntityData> getAutoCompleteSuggestions(String conditionItemText, int caretPosition) {
        List<EntityData> result = new ArrayList<EntityData>();


        final String textUpToCaret = conditionItemText.substring(0, caretPosition);
        // Here, we force a parse error at the caret position
        final String textWithErrorChar = textUpToCaret + ERROR_CHARACTER;

        try {
            ConditionItemParser parser = new ConditionItemParser(project);
            parser.parse(textWithErrorChar);
        }
        catch (ParserException e) {
            RenderingManager rm = project.getRenderingManager();

            List<OWLEntity> expectedEntities = getPossibleEntities(e);
            String lastWord = getLastWord(textWithErrorChar);
            String start = lastWord.substring(0, lastWord.length() - 1).toLowerCase();
            if(start.startsWith("'")) {
                start = start.substring(1);
            }

            for (OWLEntity entity : expectedEntities) {
                final EntityData entityData = rm.getEntityData(entity);
                escapeBrowserTextIfNecessary(entityData);
                if (lastWord.equals(ERROR_CHARACTER)) {
                    result.add(entityData);
                }
                else {
                    String shortForm = rm.getShortForm(entity);
                    if (shortForm.toLowerCase().startsWith(start)) {
                        result.add(entityData);
                    }
                }
            }
            for (String kw : e.getExpectedKeywords()) {
                // Not nice having to pretend keywords are entities, but it's helpful to complete on key words!!!!!!!
                if (lastWord.equals(ERROR_CHARACTER) || kw.startsWith(start)) {
                    result.add(new EntityData(kw, kw));
                }
            }
        }
        return result;
    }

    private void escapeBrowserTextIfNecessary(EntityData entityData) {
        if(entityData.getBrowserText().contains(" ")) {
            entityData.setBrowserText("'" + entityData.getBrowserText() + "'");
        }
    }

    private List<OWLEntity> getPossibleEntities(ParserException e) {
        List<OWLEntity> result = new ArrayList<OWLEntity>();

        OWLOntology rootOntology = project.getRootOntology(); 
        if (e.isClassNameExpected()) {
            result.addAll(rootOntology.getClassesInSignature());
        }
        if (e.isObjectPropertyNameExpected()) {
            result.addAll(rootOntology.getObjectPropertiesInSignature());

        }
        if (e.isDataPropertyNameExpected()) {
            result.addAll(rootOntology.getDataPropertiesInSignature());

        }
        if (e.isIndividualNameExpected()) {
            result.addAll(rootOntology.getIndividualsInSignature());
        }
        return result;
    }

    private static String getLastWord(String textWithErrorChar) {
        String[] words = textWithErrorChar.split("\b|\\s");
        String lastWord = "";
        if (words.length > 0) {
            lastWord = words[words.length - 1];
        }
        return lastWord;
    }
}
