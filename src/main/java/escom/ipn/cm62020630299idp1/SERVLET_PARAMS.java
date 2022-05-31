/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ipn.cm62020630299idp1;

import escom.ipn.cm62020630299idp1.API.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 *
 * @author LAMM_
 */
public class SERVLET_PARAMS {

    static public String BD_NAME = "database.xml";
    static public String BD_NAME_IN_SESSION = "bd";
    static public String ROOT_USER = "admin";
    static public String ROOT_PASSWORD = "admin";

    static public XML_API getXML_API(HttpServletRequest request) {
        XML_API xml_api = new XML_API(request.getServletContext().getRealPath("/"), BD_NAME);
        return xml_api;
    }

    static public User getBaseBD(HttpServletRequest request) {
        XML_API xml_api = SERVLET_PARAMS.getXML_API(request);
        if (xml_api.logIn(ROOT_USER, ROOT_PASSWORD))
        {
            if (xml_api.signIn(ROOT_USER, ROOT_PASSWORD))
            {
                xml_api.logIn(ROOT_USER, ROOT_PASSWORD);
            }
        }
        return xml_api.getUserInSession();
    }

    static public PrintWriter getWriter(String content_type, String encoding, HttpServletResponse from_response) throws IOException {
        from_response.setContentType(content_type);
        from_response.setCharacterEncoding(encoding);
        return from_response.getWriter();
    }
}
