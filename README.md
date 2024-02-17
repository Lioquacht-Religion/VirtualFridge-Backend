# VirtualFridge-Backend

This is the Backend-Webservice for the VirtualFridge-Webapplication.
It was made using the Spring-Boot-Framework and is part of a project for university.

## Database Connection
To add your database connection information include a ".dblogininfo" file in the resources folder. 
The file needs to specify the following environment variables:

`url=jdbc:postgresql://127.0.0.1:5432/virtualfridge`

`user=postgres`

`password=examplepassword`

## Amazon-Alexa Functionality
To use the Amazon-Alexa functionality include the following enviroment variables in the application.properties file:

`com.amazon.ask.servlet.disableRequestSignatureCheck=true`

`com.amazon.speech.speechlet.servlet.timestampTolerance=3600000`

## WIP: StorageV2, BarcodeScanner, Foodcreation with Attributes
The project includes an unfinished rework of the storage function.
Further unfinished features are:

- Foodcreation with adding of attributes and values (e.g. for nutriments)
- Barcodescanner to request data from the external Open Food API
