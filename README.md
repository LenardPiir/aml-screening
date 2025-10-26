Test assignment to find sanctioned people from EU financial sanctions list.

Prerequisite software to run the application:
Docker

Run this command in root folder to start the project:
docker-compose up --build

Note: If you are behind a proxy make sure you can get images from Docker hub since this project pulls both Postgres and Elastic images.

To find sanctioned people use check-name endpoint in Swagger. Swagger URL http://localhost:8080/swagger-ui.html
There are also endpoints for adding, changing and deleting a name.

How the application works:
Using Elasticsearch the application gives matching names with confidence (how likely is given name a match) and explanation why it flagged it as match.
A List of people the application has flagged is also given. Names are given distinctly.
Data about sanctioned people is stored in project and put into both Postgres and Elasticsearch. 

For future development:
Make a production grade solution for keeping Postgres and Elastic data in sync. For example use event driven architecture.
Fuzzy query calibration according to more specific needs.
Create a scheduler to download the sanctioned people list regularly.
Add better API response feedback to user (for example name is empty).
Fine tune match confidence and explanations.
