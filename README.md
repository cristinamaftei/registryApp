# Registry Application

## System Requirements
The Registry application requires access to a MySql server.

The configuration details must be specified in the *application.properties* file available under the resources folder
     
     spring.datasource.url = jdbc:mysql://localhost:3306/registry?useSSL=false
     spring.datasource.username = root
     spring.datasource.password =
     
If you don't already have a schema the this can be created by changing in the *application.properties* file the following

    spring.jpa.hibernate.ddl-auto = create

Once the schema has be created, change the property value back to *update*.   
    
## How to start the application

Run *ReceptionRegistryApplication class*.
