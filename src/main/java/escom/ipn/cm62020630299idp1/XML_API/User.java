/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ipn.cm62020630299idp1.XML_API;

import java.util.*;
import org.jdom2.*;
import java.lang.*;

/**
 *
 * @author LAMM_
 */
public class User {

    User(String nickname, String password) throws IllegalArgumentException {
        if (!nickname.isEmpty() && !password.isEmpty())
        {
            this.nickname = nickname;
            this.password = password;
            initEmptyQuestionnaire();
        } else
        {
            throw new IllegalArgumentException("Nickname or password can't be empty string");
        }
    }

    User(String nickname, String password, ArrayList<Questionnaire> questionnaire) throws IllegalArgumentException {
        if (!nickname.isEmpty() && !password.isEmpty())
        {
            this.nickname = nickname;
            this.password = password;
            if (questionnaire != null)
            {
                this.questionnaires = questionnaires;
            } else
            {
                initEmptyQuestionnaire();
            }
        } else
        {
            throw new IllegalArgumentException("Nickname or password can't be empty string");
        }
    }

    private void initEmptyQuestionnaire() {
        questionnaires = new ArrayList<Questionnaire>();
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public List<Questionnaire> getQuestionnaires() {
        return questionnaires;
    }

    public Questionnaire getQuestionnaire(int index) {
        if (index >= 0 && index < questionnaires.size())
        {
            return questionnaires.get(index);
        }
        return null;
    }

    public boolean setQuestionnaire(int index, Questionnaire questionnaire) {
        if (index >= 0 && index < questionnaires.size())
        {
            questionnaires.set(index, questionnaire);
            return true;
        }
        return false;
    }

    public void addQuestionnaire(String title) {
        questionnaires.add(new Questionnaire(String.valueOf(questionnaires.size()), title));
    }

    public void addQuestionnaire(String title, ArrayList<Question> questions) {
        questionnaires.add(new Questionnaire(String.valueOf(questionnaires.size()), title, questions));
    }

    public int searchQuestionnaireByTitle(String title) {
        for (int i = 0; i < questionnaires.size(); ++i)
        {
            if (questionnaires.get(i).getTitle().equals(title))
            {
                return i;
            }
        }
        return -1;
    }
    public int searchQuestionnaireById(String id){
        for(int i = 0; i < questionnaires.size(); ++i){
            if(questionnaires.get(i).getId().equals(id)){
                return i;
            }
        }
        return -1;
    }
    public boolean removeQuestionnaire(String id){
        return removeQuestionnaire(searchQuestionnaireById(id));
    }
    public boolean removeQuestionnaire(int pos){
        if(pos >= 0 && pos < this.questionnaires.size()){
            this.questionnaires.remove(pos);
            updateQuestionnairesId(pos);
            return true;
        }
        return false;
    }
    private void updateQuestionnairesId(int start){
        if(start<questionnaires.size()){
            for(int i = start; i < questionnaires.size(); ++i){
                questionnaires.get(i).setId(String.valueOf(i));
            }
        }
    }
    public Element getXMLTag() {
        Element user_tag = new Element("user");
        user_tag.setAttribute("nickname", nickname);
        user_tag.setAttribute("password", password);
        for (Questionnaire questionnaire : questionnaires)
        {
            user_tag.addContent(questionnaire.getXMLTag());
        }
        return user_tag;
    }
    private String nickname;
    private String password;
    List<Questionnaire> questionnaires;
}
