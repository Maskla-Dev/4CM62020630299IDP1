/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ipn.cm62020630299idp1.XML_API;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author LAMM_
 */
public class XML_API {

    XML_API(String server_path, String xml_file_name) {
        try
        {
            //File initialization
            this.file = new File(server_path + xml_file_name);
            if (!this.file.exists())
            {
                System.out.println("Creating file...");
                this.root = new Element("DataBase");
                this.xml_document = new Document(this.root);
                this.out_file_stream = new FileOutputStream(this.file);
                this.xml_output = new XMLOutputter(Format.getPrettyFormat().setOmitEncoding(false));
                this.xml_output.output(this.xml_document, this.out_file_stream);
                this.out_file_stream.close();
            } else
            {
                System.out.println("Reading file...");
                this.builder = new SAXBuilder();
                this.in_file_stream = new FileInputStream(this.file);
                this.xml_document = this.builder.build(this.in_file_stream);
                this.root = this.xml_document.getRootElement();
                System.out.println("Root: " + this.root.getName());
                this.in_file_stream.close();
            }
            //User List initialization
            initUserList();
            this.loggedUserIndex = -1;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Extrae del archivo XML la lista de usuarios, las convierte en objetos con 
    //sus respetivos cuestionarios y preguntas para una mejor manipulacion.
    private void initUserList() {
        List<Element> users = this.root.getChildren();
        this.users_list = new ArrayList<User>();
        if (!users.isEmpty())
        {
            System.out.println("Init user list...");
            int user_index = 0;
            System.out.println("\tUsers: ");
            for (Element user : users)
            {
                String nickname = user.getAttributeValue("nickname"), password = user.getAttributeValue("password");                
                System.out.println("\t\tIn XML - nickname = " + nickname + "\tpassword = " + password);
                this.users_list.add(new User(nickname, password));
                System.out.println("\t\t" + users_list.get(user_index).getNickname() + " : " + users_list.get(user_index).getPassword());
                int questionnaire_index = 0;
                System.out.println("\t\tQuestionnaires: ");
                for (Element questionnaire : user.getChildren())
                {
                    System.out.println("\t\t\tIn XML - titulo = " + questionnaire.getAttributeValue("titulo"));
                    this.users_list.get(user_index).addQuestionnaire(questionnaire.getAttributeValue("titulo"));
                    System.out.println("\t\t\t" + users_list.get(user_index).getQuestionnaire(questionnaire_index).getTitle());
                    System.out.println("\t\t\tQuestions:");
                    int question_index = 0;
                    for (Element question : questionnaire.getChildren())
                    {
                        System.out.println("\t\t\t\tIn XML - texto = " + question.getAttributeValue("texto"));
                        this.users_list.get(user_index).getQuestionnaire(questionnaire_index).addQuestion(question.getAttributeValue("texto"));
                        String answer_index = question.getAttributeValue("respuesta");
                        if (answer_index != null)
                        {
                            this.users_list.get(user_index).getQuestionnaire(questionnaire_index).getQuestion(question_index).changeAnswerIndex(Integer.parseInt(answer_index));
                        }
                        System.out.println("\t\t\t\t" + users_list.get(user_index).getQuestionnaire(questionnaire_index).getQuestion(question_index).getText() + " : " + users_list.get(user_index).getQuestionnaire(questionnaire_index).getQuestion(question_index).getAnswerIndex());
                        System.out.println("\t\t\t\tAnswer options:");
                        int answer_option_index = 0;
                        for (Element answer : question.getChildren())
                        {
                            System.out.println("\t\t\t\t\tIn XML text content = " + answer.getText());
                            users_list.get(user_index).getQuestionnaire(questionnaire_index).getQuestion(question_index).addOption(answer.getText());
                            System.out.println("\t\t\t\t\t" + users_list.get(user_index).getQuestionnaire(questionnaire_index).getQuestion(question_index).getOption(answer_option_index).getTextContent());
                        }
                        ++question_index;
                    }
                    ++questionnaire_index;
                }
                ++user_index;
            }
        }
    }

    private boolean signIn(String nickname, String password) {
        System.out.println("SignIn user...");
        if (searchUser(nickname) != -1 && !nickname.isEmpty())
        {
            return false;
        } else
        {
            try
            {
                users_list.add(new User(nickname, password));
            } catch (Exception e)
            {
                System.out.println("Error adding user: " + e.getMessage());
                e.printStackTrace();
            }
            return true;
        }
    }

    public int searchUser(String nickname) {
        System.out.println("\tSearching: " + nickname);
        for (int i = 0; i < this.users_list.size(); ++i)
        {
            if (nickname.equals(this.users_list.get(i).getNickname()))
            {
                System.out.println("\t" + nickname + " already exists");
                return i;
            }
        }
        System.out.println(nickname + " does not exist");
        return -1;
    }
    public boolean removeUser(String nickname){
        int user_index = searchUser(nickname);
        if(user_index == -1)
            return false;
        else
            users_list.remove(user_index);
        return true;
    }
    public boolean updateXML() {
        try
        {
            System.out.println("Updating xml file...");
            this.file = new File("DataBase.xml");
            this.out_file_stream = new FileOutputStream(this.file);
            this.xml_output = new XMLOutputter(Format.getPrettyFormat());
            root = new Element("DataBase");
            for (User user : users_list)
            {
                root.addContent(user.getXMLTag());
            }
            xml_document.detachRootElement();
            xml_document.setRootElement(root);
            this.xml_output.output(this.xml_document, this.out_file_stream);
            this.out_file_stream.close();
        } catch (IOException e)
        {
            System.out.println("Error updating file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean logOut() {
        if (loggedUserIndex != -1)
        {
            loggedUserIndex = -1;
            return true;
        }
        return false;
    }

    public boolean logIn(String nickname, String password) {
        this.loggedUserIndex = searchUser(nickname);
        if (this.loggedUserIndex != -1)
        {
            System.out.println("user password: " + users_list.get(loggedUserIndex).getPassword() + " | data: " + password + " | state: " + users_list.get(loggedUserIndex).getPassword().equals(password));
            if (users_list.get(loggedUserIndex).getPassword().equals(password))
            {
                return true;
            }
        }
        return false;
    }

    public User getUserInSession() {
        return loggedUserIndex != -1 ? users_list.get(loggedUserIndex) : null;
    }
    //XML File streams
    private File file;  //Archivo en disco XML
    private SAXBuilder builder; //Constructor de archivo XML
    private Element root;   //Representa la etiqueta raiz del archivo XML como objeto
    private Document xml_document; //Representa el archivo XML como objeto
    private FileOutputStream out_file_stream;   //Objeto con instrucciones de escritura de archivo
    private XMLOutputter xml_output;    //Directivas de escritura de archivo XML
    private FileInputStream in_file_stream; //Objeto con instrucciones de lectura de arhivo
    //Functional members
    private List<User> users_list;  //Representa la etiqueta de usuarios como objeto
    private int loggedUserIndex;    //Indica la posicion en el catalogo de usuarios aquel que se ha loggeado
}