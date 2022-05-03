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
public class Question {

    Question(String id, String text) {
        initEmptyOptionsList();
        this.answer_index = -1;
        this.text = text;
        this.id = id;
    }

    Question(String id, int answer_index, String text, ArrayList<Answer> options) {
        this.id = id;
        this.text = text;
        if (options != null) {
            this.options = options;
        } else {
            initEmptyOptionsList();
        }
        if (answer_index < options.size() && answer_index >= 0) {
            this.answer_index = answer_index;
        } else {
            System.out.println("Error initializing question: answer cannot be greater than option size or negative integer");
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    private void initEmptyOptionsList() {
        this.options = new ArrayList<Answer>();
    }

    public Answer getOption(int index) {
        if (index >= 0) {
            return options.get(index);
        } else {
            return null;
        }
    }

    public void setOption(int index, Answer option) {
        if (index >= 0) {
            options.set(index, option);
        }
    }

    public void addOption(String text_content) {
        options.add(new Answer(String.valueOf(options.size()), text_content));
    }

    public String getAnswer() {
        return this.options.get(answer_index).getTextContent();
    }

    public int getAnswerIndex() {
        return this.answer_index;
    }

    public void changeAnswerIndex(int answer_index) {
        if (answer_index >= 0 && answer_index < this.options.size()) {
            this.answer_index = answer_index;
        }
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean setOptions(int answer_index, ArrayList<Answer> options) {
        if (this.answer_index > 0 && this.answer_index < options.size()) {
            this.answer_index = answer_index;
            return true;
        } else {
            System.out.println("Cannot change options");
            return false;
        }
    }

    public List<Answer> getOptions() {
        return options;
    }

    public int searchOptionById(String id) {
        for (int i = 0; i < options.size(); ++i) {
            if (options.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public int searchOptionByContent(String content) {
        for (int i = 0; i < options.size(); ++i) {
            if (options.get(i).getTextContent().equals(content)) {
                return i;
            }
        }
        return -1;
    }

    public boolean removeOption(String id) {
        return removeOption(searchOptionById(id));
    }

    public boolean removeOption(int pos) {
        if (pos >= 0 && pos < options.size()) {
            options.remove(pos);
            updateOptionsId(pos);
            return true;
        }
        return false;
    }

    private void updateOptionsId(int start) {
        if (start > 0 && start < options.size()) {
            for (int i = start; i < options.size(); ++i) {
                options.get(i).setId(String.valueOf(i));
            }
        }
    }

    public Element getXMLTag() {
        Element question_tag = new Element("Pregunta");
        question_tag.setAttribute("id", id);
        question_tag.setAttribute("texto", text);
        if (answer_index != -1) {
            question_tag.setAttribute("respuesta", Integer.toString(answer_index));
        }
        for (Answer option : options) {
            question_tag.addContent(option.getXMLTag());
        }
        return question_tag;
    }
    private int answer_index;
    private String text;
    private String id;
    private List<Answer> options;
}
