/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ipn.cm62020630299idp1;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import escom.ipn.cm62020630299idp1.API.*;
import com.google.gson.Gson;
import java.util.*;

/**
 *
 * @author LAMM_
 */
public class QUERY_API extends HttpServlet {

    User bd_base;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        bd_base = SERVLET_PARAMS.getBaseBD(request);
        String CRUD_ACTION = request.getParameter("action");
        String OBJECT_TYPE = request.getParameter("object");
        String ID = request.getParameter("id");
        String SECONDACTION = request.getParameter("secondaction");
        REQUEST_STATUS status;
        Gson json = new Gson();
        PrintWriter out = SERVLET_PARAMS.getWriter("application/json", "UTF-8", response);
        int questionnaire_index;
        int question_index;
        int option_index;
        if (OBJECT_TYPE != null && CRUD_ACTION != null && SECONDACTION != null && ID != null)
        {
            switch (CRUD_ACTION)
            {
                case "GET":
                    switch (OBJECT_TYPE)
                    {
                        case "QUESTIONNAIRE":
                            if (SECONDACTION.equals("GETLIST"))
                            {
                                out.print(json.toJson(bd_base.getQuestionnaires()));
                            } else
                            {
                                out.print(json.toJson((Questionnaire) readBD("QUESTIONNAIRE", ID)));
                            }
                            break;
                        case "QUESTION":
                            if (SECONDACTION.equals("GETLIST"))
                            {
                                out.print(json.toJson(bd_base.getQuestionnaire(bd_base.searchQuestionnaireById(ID)).getQuestions()));
                            } else
                            {
                                out.print(json.toJson((Question) readBD("QUESTION", ID)));
                            }
                            break;
                        case "OPTION":
                            if (SECONDACTION.equals("GETLIST"))
                            {
                                for (Questionnaire Q : bd_base.getQuestionnaires())
                                {
                                    for (Question q : Q.getQuestions())
                                    {
                                        if (q.getId().equals(ID))
                                        {
                                            out.print(json.toJson(q.getOptions()));
                                        }
                                    }
                                }
                            } else
                            {
                                out.print(json.toJson((Answer) readBD("OPTION", ID)));
                            }
                            break;
                        default:
                            status = new REQUEST_STATUS("no object type match");
                            out.print(json.toJson(status));
                            break;
                    }
                    break;
                case "MODIFY":
                    try
                    {
                        Questionnaire Q = json.fromJson(SECONDACTION, Questionnaire.class);
                        if (bd_base.setQuestionnaire(bd_base.searchQuestionnaireById(ID), Q))
                        {
                            status = new REQUEST_STATUS("modified");
                        } else
                        {
                            status = new REQUEST_STATUS("Error at modify process");
                        }
                } catch (Exception e)
                {
                    status = new REQUEST_STATUS("Json no match");
                }
                out.print(json.toJson(status));
                break;
                case "DELETE":
                    switch (OBJECT_TYPE)
                    {
                        case "QUESTIONNAIRE":
                            if (bd_base.removeQuestionnaire(ID))
                            {
                                status = new REQUEST_STATUS("removed");
                            } else
                            {
                                status = new REQUEST_STATUS("error removing object");
                            }
                            break;
                        case "QUESTION":
                            questionnaire_index = -1;
                            question_index = -1;
                            for (int i = 0; i < bd_base.getQuestionnaires().size(); ++i)
                            {
                                if (bd_base.getQuestionnaire(i).equals(SECONDACTION))
                                {
                                    questionnaire_index = i;
                                }
                            }
                            for (int j = 0; j < bd_base.getQuestionnaire(questionnaire_index).getQuestions().size(); ++j)
                            {
                                if (bd_base.getQuestionnaire(j).getQuestion(j).getId().equals(ID))
                                {
                                    question_index = j;
                                }
                            }
                            if (bd_base.getQuestionnaire(questionnaire_index).removeQuestion(question_index))
                            {
                                status = new REQUEST_STATUS("removed");
                            } else
                            {
                                status = new REQUEST_STATUS("error removing object");
                            }
                            break;
                        case "OPTION":
                            questionnaire_index = -1;
                            question_index = -1;
                            option_index = -1;
                            for (int i = 0; i < bd_base.getQuestionnaires().size(); ++i)
                            {
                                for (int j = 0; j < bd_base.getQuestionnaire(i).getQuestions().size(); ++j)
                                {
                                    if (bd_base.getQuestionnaire(i).getQuestion(j).getId().equals(SECONDACTION))
                                    {
                                        questionnaire_index = i;
                                        question_index = j;
                                    }
                                }
                            }
                            for (int k = 0; k < bd_base.getQuestionnaire(questionnaire_index).getQuestion(question_index).getOptions().size(); ++k)
                            {
                                if (bd_base.getQuestionnaire(questionnaire_index).getQuestion(question_index).getOption(k).equals(ID))
                                {
                                    option_index = k;
                                }
                            }
                            if (bd_base.getQuestionnaire(questionnaire_index).getQuestion(question_index).removeOption(option_index))
                            {
                                status = new REQUEST_STATUS("removed");
                            } else
                            {
                                status = new REQUEST_STATUS("error removing object");
                            }
                            break;
                        default:
                            status = new REQUEST_STATUS("no object type match");
                            break;
                    }
                    out.print(json.toJson(status));
                    break;
                case "NEW":
                    try
                    {
                        switch (OBJECT_TYPE)
                        {
                            case "QUESTIONNAIRE":
                                Questionnaire new_questionnaire = json.fromJson(SECONDACTION, Questionnaire.class);
                                bd_base.addQuestionnaire(new_questionnaire.getTitle(), (ArrayList<Question>) new_questionnaire.getQuestions());
                                status = new REQUEST_STATUS("added");
                                break;
                            case "QUESTION":
                                Question new_question = json.fromJson(SECONDACTION, Question.class);
                                questionnaire_index = bd_base.searchQuestionnaireById(ID);
                                bd_base.getQuestionnaire(questionnaire_index).addQuestion(new_question.getAnswerIndex(), new_question.getText(), (ArrayList<Answer>) new_question.getOptions());
                                status = new REQUEST_STATUS("added");
                                break;
                            case "OPTION":
                                Answer new_answer = json.fromJson(SECONDACTION, Answer.class);
                                questionnaire_index = -1;
                                question_index = -1;
                                for (int i = 0; i < bd_base.getQuestionnaires().size(); ++i)
                                {
                                    for (int j = 0; j < bd_base.getQuestionnaire(i).getQuestions().size(); ++j)
                                    {
                                        if (bd_base.getQuestionnaire(i).getQuestion(j).getId().equals(ID))
                                        {
                                            questionnaire_index = i;
                                            question_index = j;
                                        }
                                    }
                                }
                                bd_base.getQuestionnaire(questionnaire_index).getQuestion(question_index).addOption(new_answer.getTextContent());
                                status = new REQUEST_STATUS("added");
                                break;
                            default:
                                status = new REQUEST_STATUS("no object type match");
                                break;
                        }
                } catch (Exception e)
                {
                    status = new REQUEST_STATUS("json no match");
                }
                out.print(json.toJson(status));
                break;
                default:
                    status = new REQUEST_STATUS("no crud match");
                    break;
            }
            SERVLET_PARAMS.getXML_API(request).updateXML();
        }
    }

    private Question getQuestion(User bd, String id) {
        Question searched_question = null;
        if (id != null)
        {
            for (Questionnaire Q : bd.getQuestionnaires())
            {
                for (Question q : Q.getQuestions())
                {
                    if (q.getId().equals(id))
                    {
                        searched_question = q;
                    }
                }
            }
        }
        return searched_question;
    }

    private Answer getAnswerOption(User bd, String id) {
        Answer searched_answer = null;
        if (id != null)
        {
            for (Questionnaire Q : bd.getQuestionnaires())
            {
                for (Question q : Q.getQuestions())
                {
                    for (Answer a : q.getOptions())
                    {
                        if (a.getId().equals(id))
                        {
                            searched_answer = a;
                        }
                    }
                }
            }
        }
        return searched_answer;
    }

    private Object readBD(String object_type, String id) {
        Object obj;
        switch (object_type)
        {
            case "QUESTIONNAIRE":
                obj = bd_base.getQuestionnaire(bd_base.searchQuestionnaireById(id));
                break;
            case "QUESTION":
                obj = getQuestion(bd_base, id);
                break;
            case "OPTION":
                obj = getAnswerOption(bd_base, id);
            default:
                obj = null;
                break;
        }
        return obj;
    }
}
