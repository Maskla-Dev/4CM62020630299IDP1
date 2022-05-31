/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ipn.cm62020630299idp1.API;

import java.util.*;
import org.jdom2.*;

/**
 *
 * @author LAMM_
 */
public class Questionnaire {
    
    Questionnaire(String id, String title) {
        this.title = title;
        this.id = id;
        initEmptyQuestionsList();
    }
    
    Questionnaire(String id, String title, ArrayList<Question> questions) {
        this.title = title;
        this.id = id;
        if (questions != null) {
            this.questions = questions;
        } else {
            initEmptyQuestionsList();
        }
    }
    
    private void initEmptyQuestionsList() {
        this.questions = new ArrayList<Question>();
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void addQuestion(String text) {
        this.questions.add(new Question(String.valueOf(this.questions.size()), text));
    }
    
    public void addQuestion(int answer_index, String text, ArrayList<Answer> options) {
        this.questions.add(new Question(String.valueOf(this.questions.size()), answer_index, text, options));
    }
    public int searchQuestionById(String id) {
        for (int i = 0; i < questions.size(); ++i) {
            if (questions.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public boolean removeQuestion(String id) {
        return removeQuestion(searchQuestionById(id));
    }

    public boolean removeQuestion(int pos) {
        if (pos >= 0 && pos < questions.size()) {
            questions.remove(pos);
            updateQuestionsId(pos);
            return true;
        }
        return false;
    }

    private void updateQuestionsId(int start) {
        if (start > 0 && start < questions.size()) {
            for (int i = start; i < questions.size(); ++i) {
                questions.get(i).setId(String.valueOf(i));
            }
        }
    }

    public boolean setQuestion(int index, Question question) {
        if (index >= 0 && index < questions.size()) {
            this.questions.set(index, question);
            return true;
        }
        return false;
    }
    
    public Question getQuestion(int index) {
        if (index >= 0 && !this.questions.isEmpty()) {
            return questions.get(index);
        } else {
            return null;
        }
    }
    public List<Question> getQuestions(){
        return this.questions;
    }
    public Element getXMLTag() {
        Element questionnaire_tag = new Element("Cuestionario");
        questionnaire_tag.setAttribute("id", id);
        questionnaire_tag.setAttribute("titulo", title);
        for (Question question : questions) {
            questionnaire_tag.setContent(question.getXMLTag());
        }
        return questionnaire_tag;
    }
    private String title;
    private String id;
    private List<Question> questions;
}
