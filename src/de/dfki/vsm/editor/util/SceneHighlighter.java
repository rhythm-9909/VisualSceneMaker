package de.dfki.vsm.editor.util;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import java.awt.*;

public class SceneHighlighter {

    private final Document document;
    private final String sceneName;
    private final String sceneLanguage;
    private final Highlighter highlighter;
    private DefaultHighlighter.DefaultHighlightPainter highlightPainter;

    public SceneHighlighter(Document document, String sceneName, String sceneLanguage, Highlighter highlighter){
        this.document = document;
        this.sceneName = sceneName;
        this.sceneLanguage = sceneLanguage;
        this.highlighter = highlighter;
    }

    public int highlightScene() throws BadLocationException {
        return findAndHightLightScene();

    }

    public void highlightAndScrollToScene(JEditorPane editorPane) throws BadLocationException {
        int foundPosition = findAndHightLightScene();
        Rectangle rView = editorPane.modelToView(foundPosition-1);
        rView.setSize((int)rView.getWidth(),(int)rView.getHeight() + 100);
        editorPane.scrollRectToVisible(rView);
    }



    private int findAndHightLightScene() throws BadLocationException {
        for (int index = 0; index + sceneName.length() < document.getLength(); index++) {
            int foundIndex = find(index);
            if(foundIndex > 0){
                highlight(foundIndex);
                return foundIndex;
            }
        }
        return -1;
    }

    private int find(int index) throws BadLocationException {
        String match = document.getText(index, sceneName.length());
        if (sceneName.equals(match) && isScene(index)) {
            return index;
        }
        return -1;
    }

    private void highlight(int index) throws BadLocationException {

        highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.red);
        highlighter.addHighlight(index, index + sceneName.length(), highlightPainter); //TODO improve selection
    }

    private boolean isScene(int index) throws BadLocationException {
        return document.getText(index + sceneName.length(), 1).equals("\n");
    }
}