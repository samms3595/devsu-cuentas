## Api Rest Cuentas y Movimientos
* Java 17
* Docker
* Maven
* Spring boot 3.1.6
* Open Feign
* Lombok
* Mysql 8.0.33

## Configuración inicial del proyecto
## 1. Configurar Base de datos en entorno docker.
En la raiz del proyecto se encuentra un archivo docker-compose.yml que levanta una base de datos en mysql en el puerto 3306.
Tambien se crea una red en donde se conectan los microservicios entre si, para poder tener comunicacion entre ellos, ya que 
usé el cliente Feign
```
 ./docker-compose.yml
```
Estando en la raiz del proyecto se procedera a abrir terminal y ejecutar el comando

```
docker compose up
```
si solicita algun permiso se le debera anteponer la palabra sudo para darle permisos de administrador.
```
sudo docker compose up
```
## 2. Ejecutar proyecto.
El proyecto corre sobre el puerto 9092
## 3. Api disponible

* Link: http://localhost:9092

## 4. Postman
En la raiz del proyecto se encuentra el archivo.
```
./Cuentas y Movimientos.postman_collection.json
```
## 5. Postman
Link de documentación Swagger.
```
http://localhost:9092/swagger-ui/index.html#/
```
# Cuentas y Movimientos API

Esta colección de Postman proporciona un conjunto de servicios de API para la gestión de cuentas y movimientos bancarios. 

### Gestión de Cuentas `/cuentas`
La API ofrece un conjunto completo de servicios relacionados con la gestión de cuentas bancarias. Estos servicios incluyen:

- **Crear Cuenta:** Permite a los usuarios abrir una nueva cuenta bancaria, proporcionando detalles como el ID del cliente, el tipo de cuenta (por ejemplo, ahorro, corriente), y el saldo inicial. Esto facilita la inclusión rápida de nuevos clientes en el sistema.

- **Obtener Información de Cuenta:** Los usuarios pueden consultar los detalles de una cuenta específica, como su saldo actual, tipo de cuenta y estado. Esta función es esencial para el seguimiento y la gestión de cuentas individuales.

- **Actualizar Detalles de la Cuenta:** Ofrece la posibilidad de actualizar la información de una cuenta existente, como cambiar el tipo de cuenta o modificar el saldo. Esta característica es crucial para mantener la precisión de los datos de la cuenta a lo largo del tiempo.

- **Eliminar Cuenta:** Permite eliminar una cuenta bancaria existente del sistema. Esta operación es importante para manejar cuentas inactivas o cerradas.

### Gestión de Movimientos `/movimientos`
La API también incluye servicios para gestionar los movimientos de las cuentas, que abarcan:

- **Registrar Movimientos:** Los usuarios pueden registrar nuevos movimientos en una cuenta, incluyendo tanto depósitos como retiros. Esto permite un seguimiento detallado de todas las transacciones realizadas en una cuenta, asegurando una contabilidad precisa.

- **Consultar Movimientos:** Permite a los usuarios obtener una lista de todos los movimientos asociados con una cuenta o cliente específico. Esta funcionalidad es vital para el análisis financiero y la auditoría de cuentas.

- **Actualizar y Eliminar Movimientos:** Ofrece la capacidad de modificar o eliminar movimientos previamente registrados. Esto es esencial para corregir errores o ajustar registros de transacciones.
