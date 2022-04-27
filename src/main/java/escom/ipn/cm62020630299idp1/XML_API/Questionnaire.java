/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ipn.cm62020630299idp1.XML_API;

import java.util.*;
import org.jdom2.*;

/**
 *
 * @author LAMM_
 */
public class Questionnaire {

    Questionnaire(String title) {
        this.title = title;
        initEmptyQuestionsList();
    }

    Questionnaire(String title, ArrayList<Question> questions) {
        this.title = title;
        if (questions != null)
        {
            this.questions = questions;
        } else
        {
            initEmptyQuestionsList();
        }
    }

    private void initEmptyQuestionsList() {
        this.questions = new ArrayList<Question>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void addQuestion(String text) {
        this.questions.add(new Question(text));
    }

    public void addQuestion(int answer_index, String text, ArrayList<Answer> options) {
        this.questions.add(new Question(answer_index, text, options));
    }

    
    public boolean setQuestion(int index, Question question) {
        if (index >= 0 && index < questions.size())
        {
            this.questions.set(index, question);
            return true;
        }
        return false;
    }

    public Question getQuestion(int index) {
        if (index >= 0 && !this.questions.isEmpty())
        {
            return questions.get(index);
        } else
        {
            return null;
        }
    }
    public Element getXMLTag(){
        Element questionnaire_tag = new Element("Cuestionario");
        questionnaire_tag.setAttribute("titulo", title);
        for(Question question : questions){
            questionnaire_tag.setContent(question.getXMLTag());
        }
        return questionnaire_tag;
    }
    private String title;
    private List<Question> questions;
}
