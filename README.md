# Component Processing Microservice

This is the middleware microservice which interacts with both Authorisation Service and Packaging Service
Uses feign client to communicate with these two services

## Process followed for reteriving component processing charges:

1. Microservice at first recevies an GET Call from client with Jwt Token in Request Headers, Quantity and Component Type as Query Params
2. As the token is received, The Microservice makes a GET Call to Authorisation Service to check the validity of the token.
3. If Authorisation Service sends response as inValid token, the process ends with Authorisation Failed 
4. If Authorisation Service sends response as valid token, then the microservice makes an GET Call to Packaging And Delivery Microservice to obtain cost details 
5. If Packaging And Delivery Microservice sends response with errors, the process ends with error 
6. If Packaging And Delivery Microservice sends response without any errors, the process continues by assigning an order id 
7. Remaining computations are performed in ther service layer.
7. Finally success response is sent to client with relevant details.

## Order Placement

1. Microservice receives a POST call from client with Jwt Token in Request Headers with all the necessary details as request body prepared by client side application.
2. As the token is received, The Microservice makes a GET Call to Authorisation Service to check the validity of the token.
3. If Authorisation Service sends response as inValid token, the order process ends with Authorisation Failed.
4. If Authorisation Service sends response as valid token, then the data is extracted from the request body and data is prepared to be saved in database.
5. Once data is saved in database the microservice sends a success response with relevant dates which can be used by client Application to display as order process completed.

## End Points

### 1. GET -> http://{{ BaseUrl }}/v1/componentproceessor/getdetails?type={{ component Type }}&quantity={{ Quantity }}
  ```
  Request Headers 
    Authorization : string
    content-type : application/json
    Access-Control-Allow-Origin : *
    
  Response body
  
  media type : application/json
  status : 200 OK

  {
	  errors : false
	  requestId : String
	  cost : {
		processingCost : number
		packagingAndDeliveryCost : number
		totalCost : number
	  }
	  dateOfDelivery : string
	}

  ```
  ```
  media type : application/json
  status : 200 OK
  
  {
    "errorMessage": "Authorisation failed",
    "errors": true
  }
  ```
  
  ```
  media type : application/json
  status : 200 OK
  
  {
    "errorMessage": " Invalid Arguments",
    "errors": true
  }
  
  ```
  
### 2. POST -> http://{{ BaseUrl }}/v1/componentproceessor/createreturnorder
  ```
  Request Headers 
    Authorization : string 
    content-type : application/json
    Access-Control-Allow-Origin : *
  
  Request Body
  
  {
	"isConversionToOrder" : boolean,
	"orderId" : string,
	"customerDetails":{
		"customerName" : string,
		"contactNumber" : string
	},
	"componentDetails":{
		"componentType" : string,
		"componentName" : string,
		"extraDetails" : string,
		"qty" : int
	},
	"costingDetails":{
		"packagingAndDeliveryCost" : double,
		"processingCost" : double
	},
	"dateOfDelivery" : string
  }
  
  ```
  
  Response Body
  
  ```
  media type : application/json
  status : 201 created
  
  {
    "isSavedSuccessfully": boolean,
    "orderId": string,
    "dateOfDelivery": string,
    "totalCost": double,
    "errors": boolean
  }
  
  ```
  
  ```
  media type : application/json
  status : 409 Conflict
  
  {
    "errorMessage": string,
    "errors": boolean
  }
  
  ```
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
