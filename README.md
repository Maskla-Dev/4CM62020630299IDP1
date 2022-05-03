# 4CM62020630299IDP1
Practica 1 en equipo | CRUD - Base de datos con XML

Para poder hacer uso de la base de datos es necesario hacer un login con un usuario y contraseña.

# Funcionamiento
La API simula una base de datos, por lo tanto requiere un inicio de sesion previo para poder manipularla (altas, consulta y bajas).<br>
## Altas
### Usuarios
Ya que la estructura es totalmente basada en usuarios, una de las altas básicas es la alta de un usuario y esta puede hacerse de la siguiente manera:
```Java
...
String user_name = "Carlos";
String user_password = "1234";
String path = request.getRealPath("/");
XML_API bd = new XML_API(path, "DataBase.xml");
if(bd.signIn(user_name, user_password)){
    System.out.println("User added");
    bd.updateXML();
}
else
    System.out.println("Invalid user name or user already exists");
```
Es importante aclarar que cada cambio primero se realiza en los objetos del programa y para que todo cambio sea reflejado en archivo es necesario utilizar `updateXML()`.
### Cuestionarios, preguntas y opciones de pregunta
Cada cuestionario, pregunta u opcion, puede ser rastreada hasta un usuario en especifico, podria decirse que cierto cuestionario pertenece a cierto usuario. Para facilitar la manipulacion de los mismo, es neceario hacer un inicio de sesion.
```Java
...
    if(bd.logIn(user_name, user_password)){
        User current_user = bd.getUserInSession();
        System.out.println("User logged");
        List<Answer> options = new ArrayList({
            new Answer("a0", "Mexico"),  //Constructor(id, text)
            new Answer("a1", "Francia"),
            new Answer("a2", "Estados Unidos"),
            new Answer("a3", "Japon")
        });
        List<Question> questions = new ArrayLis({
            new Question("q0", 0, "Cual es el pais en el que vives?", options),
            new Question("q1", 1, "Es un pais famoso en Europa", options),
            new Question("q2", 3, "Pais donde proviene la palabra KAWAII", options)
        });
        current_user.addQuestionnarie("Trivia de paises", questions);
        bd.updateXML();
    }
    else
        System.out.println("Invalid user/password");
...
```
## Modificaciones
Las modificaciones se pueden hacer mediante los setters de cada atributo miembro, siendo este el modo directo y más especifico. En casos de listas, se requiere especificar el indice/posicion del elemento a modificar, para ello los metodos _`searchBy`_ pueden ayudar a encontrar los elementos por _id_, contenido o titulo.
```Java
...
        int questionnaire_index = current_user.searchQuestionnaireByTitle("Trivia de paises");
        int question_index;
        if(questionnaire_index != -1){
            question_index = current_user.getQuestionnaire(questionnaire_index).searchQuestionById("q0");
            //Modifica una sola opcion a la pregunta con id = "q0"
            if(question_index != -1) 
                current_user.getQuestionnaire(questionnaire_index).getQuestion(question_index).setOption(0, new Answer("0", Alemania));
                bd.updateXML();
        }
...
```
Es posible realizar modificaciones completas a las listas y reemplazar ya sea un cuestionario, pregunta u opciones.
```Java
...
        int questionnaire_index = current_user.searchQuestionnaireByTitle("Trivia de paises");
        if(questionnaire_index != -1){
                List<Answer> new_options = new ArrayList({
                new Answer("a0", "Albert Camus"),  //Constructor(id, text)
                new Answer("a1", "Pamela Anderson"),
                new Answer("a2", "Andres Mujica"),
                new Answer("a3", "Andrea Legarreta")
            });
            List<Question> new_questions = new ArrayLis({
                new Question("q0", 0, "Cual de estos famosos es conocido por el programa HOY de Mexico?", options),
            });
            Questionnaire qq0 = new Questionnaire("qq0", "Trivia famosos", questions);
            //Reemplaza el cuestionario cuyo titulo sea "Trivia de paises" por uno nuevo
            if(current_user.setQuestionnaire(questionnaire_index, qq0))
                bd.updateXML();
        }
...
```
## Bajas
Al igual que las modificaciones, utilizando el metodo `remove` y apoyandose por _`searchBy`_ se pueden dar bajas de items.
```Java
...
        int questionnaire_index = current_user.searchQuestionnaireByTitle("Trivia de paises");
        if(current_user.removeQuestionnaire(questionnaire_index))
            bd.updateXML();
...
```
# Clases
Documentacion rapida de clases y sus respectivos metodos y atributos.
## XML_API
Esta se encarga de la gestion de base de datos del archivo XML.
### Constructores
#### `XML_API(String path, String file_name)`
Inicializa el objeto, admite 2 argumentos `path` y `file_name`; el primero indica la ruta absoluta o relativa del archivo y el segundo corresponde al nombre del archivo.<br>
En caso de que el archivo no se encuentre, creara uno nuevo cuyo contenido solo es el encabezado basico y la etiqueta raiz.
### Métodos
#### `boolean loginIn(String nickname, String password)`
Inicia sesión con un usuario y contraseña, devuelve `true` si el usuario y contraseña es correcto, `false` en cualquier otro caso.
#### `boolean signIn(String nickname, String password)`
Da de alta a un usuario con su contraseña, devuelve `true` si el usuario se guardo con exito (usuario no existe en la base de datos), `false` si usuario ya existe o el campo contraseña esta vacio.
#### `boolean logOut()`
Cierra la sesion del usuario, regresa `true` si habia un usuario en sesion, `false` cualquier otro caso.
#### `User getUserInSession()`
Regresa el objeto de aquel usuario que inicio sesion, en caso de no existir sesion activa regresa `null`
#### `boolean updateXML()`
Actualiza los cambios realizados, los datos en las listas las sobrescribe en el archivo XML
####