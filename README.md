## Test Assignment: Find Sanctioned People from EU Financial Sanctions List

### Prerequisite Software
- **Docker** is required to run the application.

### How to Run the Application
1. Run the following command in the root folder to start the project:
   ```bash
   docker-compose up --build

Note: If you are behind a proxy make sure you can get images from Docker hub since this project pulls both Postgres and Elastic images.

## Testing

- To find sanctioned people, use the **check-name** endpoint in **Swagger**.
  - Access the Swagger UI at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

- There are additional endpoints available for:
  - Adding a name
  - Changing a name
  - Deleting a name

## How the Application Works

- **Name matching with confidence and explanation**
  - The application uses **Elasticsearch** to match input names against a list of sanctioned individuals.
  - It returns the matching names along with a **confidence score** (how likely the given name is a match).
  - The application also provides an **explanation** for why a name was flagged as a match.

- **Flagged list of names**
  - A list of individuals flagged by the application is generated and presented.
  - Names are clearly **distinct** to avoid ambiguity.

- **Data storage**
  - Information about sanctioned individuals is stored within the project.
  - The data is maintained in both **PostgreSQL** and **Elasticsearch** to ensure consistency and availability.

## Future Development

- **Production-grade solution for syncing PostgreSQL and Elasticsearch**
  - Implement an event-driven architecture to keep PostgreSQL and Elasticsearch data in sync.
  - Use tools like **Kafka** or **Debezium** for change data capture (CDC) and real-time updates.

- **Fuzzy query calibration**
  - Improve fuzzy query performance and calibrate according to more specific application needs.
  - Fine-tune the balance between precision and recall in search results.

- **Sanctioned people list downloader**
  - Develop a scheduled task or cron job to download the **sanctioned people list** regularly.
  - Ensure the list is up-to-date to comply with relevant regulations.

- **Enhanced API response feedback**
  - Provide better feedback in API responses, such as clear error messages (e.g., if a user's name field is empty).
  - Implement meaningful validation messages to guide users in fixing issues.

- **Fine-tuning match confidence and explanations**
  - Refine the match confidence scoring and provide more detailed explanations for results.
  - Improve transparency for users to understand the reasoning behind match results.
