# 4CM62020630299IDP1
Practica 1 en equipo | CRUD - Base de datos con XML

Para poder hacer uso de la base de datos es necesario hacer un login con un usuario y contraseña.

##Constructor `XML_API(String path, String file_name)`
El constructor admite 2 parametros del tipo _String_ `path` y `file_name`. El primero de ellos refiere a la ruta en sistema, puede ser relativa o absoluta (se recomienda usar `getRealPath()`) y el segundo al archivo que funcionara como base de datos. En caso de que el archivo no exista en la ruta especificada se creara un archivo nuevo. 

```
String ruta = request.getRealPath("/");
XML_API xml_database = new XML_API(ruta, "BaseDeDatos.xml");
```
