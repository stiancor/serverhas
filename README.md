# Serverhas

This project is created to test the performance of several servers calling each other in a chain. More specifically to test the performance of using servers in AWS compared to using lambdas like in this project https://github.com/stiancor/dummy-lambda

You are required to set one of the two parameter `WAIT_TIME` or `PROXY_URL`. If non of them are set the requests will fail 

- Handle a request, sleeping for a specified amount of milliseconds before returning a dumb "hello world" JSON response. The time it sleeps is configured through and environment variable you need to set. The variable is called `WAIT_TIME`
- Sending a new request to a URL you configure in the environment variable. The environment variable is called `PROXY_URL`.

To build the artifact run `./gradlew shadowJar`, and to create a zip that can be uploaded to beanstalk run the make command `make zip`. I run the server in AWS beanstalk. Default port on beanstalk is 5000, but it can be overridden by setting `PORT` to 8080 in the environment variables.

