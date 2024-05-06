The project is about creating, editing, getting, and deleting notes.
Notes can be associated with tags. So you will be able to filter notes by tags for example.

This project was done with Java, using Spring framework.

I used:
- Spring Security, to handle authentication and authorization on the server side, and JWT to generate access tokens that can be sent securely between the client and the server, 
which contains the necessary authentication and authorization information.
- MapStruct, to simplify the mapping process between objects of different types (DTOs to entity and vice versa).
- Lombok, to avoid writing boilerplate code.
- MySQL.
- JUnit and Mockito, to develop unitary tests.
- Postman, to test all endpoints.
- Swagger, for documentation.

This is not deployed, yet. So if you want to test it by yourself, you must create your own database and put the information as environmental variables.
You should have something like the following:

database.url=jdbc:mysql://localhost:YourPort/YourDataBaseName?allowPublicKeyRetrieval=true&useSSL=false&useTimezone=true&serverTimezone=GMT&characterEncoding=UTF-8

database.username=YourUsername

database.password=YourPassword


If you want to try the endpoints on Postman, you can use the documentation on Swagger, so you don't need to write it.
Remember that you should have the project running in order to be able to try it.
