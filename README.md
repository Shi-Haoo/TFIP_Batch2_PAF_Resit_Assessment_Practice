
Deploying MongoDB to Railway:

Import MongoDB to Railway:
	1. Create new project
	2. Provision mongodb
	3. Go to mongodb at railway, go to Connect tab, see the password for the environment variables
	4. Go to your project in railway and set the environment variables using raw editor:
i.e: set MONGOHOST= password obtained from environment variables for mongodb at railway
….
	5. Copy the railway link for raw cli command under connect tab
	
	6. To import json files into MongoDB on railway:
	On CMD, go to where the file to be imported to db is: 
	- For .json Files:
	mongoimport --uri <your railway mongo url> -dbggdb -cgames --jsonArray game.json --authenticationDatabase admin
	
	Example:
	mongoimport --uri "mongodb://mongo:9S8TYP4Ikg6Jgq8nArwF@containers-us-west-129.railway.app:7136/?authSource=admin" -dboardgames -cgames --jsonArray game.json 
	--authenticationDatabase admin
	
	Where -d is database and -c is collection
	
	- For .csv File:
	mongoimport --uri <your railway mongo url> -dbggdb -cperson --file=data.csv --type=csv --fields="id","first_name","last_name","age"
	
	
*to run the program again, on CMD need set values for environment properties based on that obtained after deploying to railway

---------------------------------------------------------------------------------------------------------------------------------------------
Alternate Method to Step 3 & 4:
Instead of setting multiple environment variables (i.e MONGOHOST,MONGOPORT,MONGODB,MONGOUSER etc), there is a way to just use 1 environment variable.

In Railway:
	- Go to mongodb at railway, go to Connect tab, copy the railway link for raw cli command

	- Go to your project in railway and set the environment variable using raw editor:
i.e: set MONGO_URL= password obtained from environment variables for mongodb at railway and add /<database name>?authSource=admin
	  

mongodb://mongo:9S8TYP4Ikg6Jgq8nArwF@containers-us-west-129.railway.app:7136/myDB?authSource=admin


In application.properties: 
	
	spring.data.mongodb.uri=${MONGO_URL}
	
Where the value of ${MONGO_URL} is <the uri obtained from railway for mongo and add /<database name>?authSource=admin>

i.e: mongodb://mongo:9S8TYP4Ikg6Jgq8nArwF@containers-us-west-129.railway.app:7136/myDB?authSource=admin

	
If connect to local MongoDB and not railway, then value of it will be: mongodb://localhost:27017/<database name>
----------------------------------------------------------------------------------------------------------------------------------------------
To test whether record will be saved to MongoDB in Railway:
Run your localhost after setting environemnt variable to the mongoDB connection string in railway