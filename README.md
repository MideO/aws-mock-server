
### An example MockServer deployable to elastic beanstalk
Simple [Wiremock](http://wiremock.org/docs) deployment

[Swagger Docs](https://localhost:500/docs)


#### Requirements
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

##### OS X
* Install Scala and ElasticBeanstalkCli on MacOS
   ```bash   
   $ brew install scala@2.11
   $ brew install sbt  
   $ brew install awsebcli (optional - if you want to deploy to aws)
   $ sbt "runMain com.github.mideo.mockserver.MockServer" (run the app)
   ```

    
    
##### Windows

Verify the JDK installation on your windows machine by typing the following commands in the command prompt.

 * Ensure javac is added you your windows PATH (if not, add it)
    ```javac -version```

 * Installing scala
     ```  
    Download Scala binaries from http://www.scala-lang.org/download/. 
    The Scala installer file will be downloaded with .msi extension.
    Double Click or Open scala-2.11.6.msi file and select Run. 
    The Setup Wizard appears, click on Next and complete the installation process. 
    Scala installer will set the Path environment variable too, so that you can run it from anywhere.
    ```


##### AWS 
* Install ElasticBeanstalkCli on MacOS
   ```bash     
   $ brew install awsebcli
   ```

* Set up `~/.aws/config` with aws_access_key_id and aws_secret_access_key see [documentation](https://aws.amazon.com/blogs/security/wheres-my-secret-access-key/)
    ``` 
    [profile eb-cli]
    aws_access_key_id = XXXX
    aws_secret_access_key = XXZZZZ

    ```
#### Usage    
For more information see documentation [here](http://wiremock.org/docs/stubbing/)

##### Adding static stubs    
* Add mapping and files as per example below:
    
    ./mocks/mappings/ping.json
    ````json
    {
      "request": {
        "method": "GET",
        "url": "/ping"
      },
      "response": {
        "status": 200,
        "bodyFileName": "ping.json"
      }
    }
    ````
    
     ./mocks/__files/ping.json
    ````json
    {
      "ping":"pong"
    }
    ````

##### Adding runtime stubs
Example http request below:
```
POST /__admin/mappings
Body: {
        "request": {
                    "method":"GET",
                    "url":"/ping"
                    },
        "response":{
                    "status":200,
                    "jsonBody":{"ping":"pong"}
                    }
       }
```       
Will prime the server to return the response below:
```
GET /ping
Status Code: 200
Body: {
        "ping":"pong"
      }
```

##### Run App locally 
* Assemble dependencies and run generated target jar
   ```bash   
   $ sbt clean assembly
   $ java -jar target/scala-2.11/mock-server-assembly-0.1.jar mock-server-assembly-0.1.jar  
   ```

##### AWS Deployment
* Package and deploy artifact
   ````bash 
   $ sbt clean awsPackage awsDeploy
   ````
   
* Confirm Mock Server is up by checking [App Url](http://mockserver.xxxxx.elasticbeanstalk.com/ping)

#### Optional: tearing down and rebuilding aws stack
* Install terraform
    ```bash   
   $ brew install [terraform](https://www.terraform.io/docs)
   ```
* Rebuild stack and deploy
```bash
   $ cd terraform/mock-server # init stack 
   $ terraform init # init stack
   $ terraform destroy # destroy stack
   $ terraform plan # dryrun
   $ terraform apply # build or update stack
   $ cd - # return to OLD_CWD
   $ sbt clean awsPackage #package 
   $ stack=mock-server bin/deploy #deploy mock server
   ``` 
* commit terraform changes i.e. terraform && terraform.tfstate
