/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ipn.cm62020630299idp1.XML_API;

import org.jdom2.*;
/**
 *
 * @author LAMM_
 */
public class Answer {
    Answer(String text_content){
        this.text_content = text_content;
    }
    public Element getXMLTag(){
        Element answer_tag = new Element("Opcion");
        answer_tag.addContent(this.text_content);
        return answer_tag;
    }
    public void setTextContent(String text_content){
        this.text_content = text_content;
    }
    public String getTextContent(){
        return this.text_content;
    }
    private String text_content;
}
