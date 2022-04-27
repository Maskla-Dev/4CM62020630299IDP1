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

    Question(String text) {
        initEmptyOptionsList();
        this.answer_index = -1;
        this.text = text;
    }

    Question(int answer_index, String text, ArrayList<Answer> options) {
        if (answer_index < options.size() && answer_index >= 0)
        {
            this.answer_index = answer_index;
            this.text = text;
            if (options != null)
            {
                this.options = options;
            } else
            {
                initEmptyOptionsList();
            }
        } else
        {
            System.out.println("Error initializing question: answer cannot be greater than option size or negative integer");
        }
    }

    private void initEmptyOptionsList() {
        this.options = new ArrayList<Answer>();
    }

    public Answer getOption(int index) {
        if (index >= 0)
        {
            return options.get(index);
        } else
        {
            return null;
        }
    }

    public void setOption(int index, Answer option) {
        if (index >= 0)
        {
            options.set(index, option);
        }
    }

    public void addOption(String text_content) {
        options.add(new Answer(text_content));
    }

    public String getAnswer() {
        return this.options.get(answer_index).getTextContent();
    }

    public int getAnswerIndex() {
        return this.answer_index;
    }

    public void changeAnswerIndex(int answer_index) {
        if (answer_index >= 0 && answer_index < this.options.size())
        {
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
        if (this.answer_index > 0 && this.answer_index < options.size())
        {
            this.answer_index = answer_index;
            return true;
        } else
        {
            System.out.println("Cannot change options");
            return false;
        }
    }

    public List<Answer> getOptions() {
        return options;
    }

    public Element getXMLTag() {
        Element question_tag = new Element("Pregunta");
        question_tag.setAttribute("texto", text);
        if (answer_index != -1)
        {
            question_tag.setAttribute("respuesta", Integer.toString(answer_index));
        }
        for (Answer option : options)
        {
            question_tag.addContent(option.getXMLTag());
        }
        return question_tag;
    }
    private int answer_index;
    private String text;
    private List<Answer> options;
}
