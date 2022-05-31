# 4CM62020630299IDP1
Practica 1 en equipo | CRUD - Base de datos con XML

Para poder hacer uso de la base de datos es necesario hacer un login con un usuario y contraseña.

# Funcionamiento
La API simula una base de datos, por lo tanto requiere un inicio de sesion previo para poder manipularla (altas, consulta y bajas).<br/>
## Querys
Las acciones basicas del CRUD son: Leer, modificar, eliminar, agregar. <br/>
Los parametros que lee son:

1. "action" : accion del crud los valores son `GET` (lectura), `MODIFY` (modificacion), `DELETE` (borrar) y `NEW` (agregar). Los valores son leidos desde `getParameter("action")`
2. "object" : el objeto de modificacion al que apunta, puede ser `QUESTIONNAIRE` (cuestionarios), `QUESTION` (Preguntas), `OPTION` (opciones de respues), se lee desde `getParameter("object")`
3. "id": Indica el ID del elemento, dependiendo de "action" sera el id del elemento padre o el id del objeto que se quiere modificar, se lee desde `getParameter("id")`
4. "SECONDACTION": indica un parametro de apoyo segun "action" lo requiera, , se lee desde `getParameter("secondaction")`

El query se accede desde el metodo GET.
Cada respuesta devuelve un JSON con la estructura del objecto solicitado (Para `GET`, para -`MODIFY`, `DELETE`, `NEW`- devuelve JSON con el estatus de la accion).
### Leer (`action="GET"`)
Obtener un elemento especifico indica el siguiente formato:<br/>
```
ACTION = "GET" 
OBJECT = "QUESTIONNAIRE" | "QUESTION" | "OPTION"
ID = <Cualquier ID>
```
Ejemplo, obtiene el cuestionario con id `QUESTIONNAIRE1`
```
ACTION = "GET" 
OBJECT = "QUESTIONNAIRE"
ID = "QUESTIONNAIRE1"
//URLQUERY: ?action=GET&object=QUESTIONNAIRE&id=QUESTIONNAIRE1
```
Utilizar `SECONDACTION="GETLIST"` para obtener listado completo, ejemplo obteniendo el listado completo de los cuestionarios:
```
ACTION = "GET"
OBJECT = "QUESTIONNAIRE"
SECONDACTION = "GETLIST"
//URLQUERY: ?action=GET&object=QUESTIONNAIRE&secondaction=GETLIST
```
### Modificar (`action="MODIFY"`)
Modifica un objeto, `ID` especifica el id del cuestionario y `OBJECT` el objeto a modificar. `SECONDACTION` requiere la estructura de `OBJECT` a modificar.
`response` devuelve json tal que:
```
{
    status: <modified | error>
}
```
Para simplificar enviar el cuestionario completo en cuestion en `SECONDACTION`
Ejemplo, actualiza los cambios realizados en el cuestionario con `ID="1"` :
```
ACTION = "MODIFY"
ID= "1"
SECONDACTION = "{"id": "1", "title": "Parabolas", "questions": []}"
//URLQUERY: ?action=MODIFY&secondaction={"id": "1", "title": "Parabolas", "questions": []}&ID=1
```
### Eliminar (`action="DELETE"`)
Elimina el objeto con `ID` segun el id del objeto padre en `SECONDACTION`, ejemplo eliminando pregunta con `ID = "QUESTION1"` en el cuestionario con id `SECONDACTION = "QUESTIONNAIRE1"`
```
ACTION = "DELETE"
OBJECT = "QUESTION"
ID = "QUESTION1"
SECONDACTION = "QUESTIONNAIRE1"
//URLQUERY: ?action=DELETE&object=QUESTION&secondaction=QUESTIONNAIRE1
```
`response` devuelve json tal que:
```
{
    status: "removed" | "error"
}
```
### Agregar (`action="NEW"`)
Agrega un nuevo elemento `ID` indica el objeto padre en donde se inserta el nuevo elemento, `SECONDACTION` recibe la estructura del objeto en formato JSON
Ejemplo, inserta pregunta en el cuestionario con id `ID = "QUESTIONNAIRE1"`
```
ACTION = "NEW"
OBJECT = "QUESTION"
ID = "QUESTIONNAIRE1"
SECONDACTION = "{"title": "Pregunta aleatoria", "options": []}"
//URLQUERY: ?action=NEW&object=QUESTION&secondaction={"title": "Pregunta aleatoria", "options": []}
```
`response` devuelve JSON tal que:
```
{
    status: "removed" | "error"
}
```
## Estructuras
### Lista cuestionarios
```
{
    "questionnaires: <arreglo de cuestionarios>"
}
```
### Cuestionario (`QUESTIONNAIRE`)
```
{
    "title": <texto>,
    "id": <texto>
    "questions": <arreglo de preguntas>
}
```
### Pregunta (`QUESTION`)
```
{
    "answer_index": <numero>,
    "title": <texto>,
    "id": <text>,
    "options": <arreglo de opciones de respuestas>
}
```
### Respuesta (`OPTION`)
```
{
    "text_content": <texto>,
    "id": <texto>
}
```