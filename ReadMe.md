# GCP Pub/Sub Spring Boot Application

This is a sample Spring Boot application that demonstrates how to use Google Cloud Pub/Sub for publishing and subscribing to messages. The application allows publishing events with message ordering enabled and disabled, and it also shows how to consume messages from both ordered and unordered subscriptions.

## GCP Related Configurations

### Prerequisites

Before you start using the application, you need to set up a Google Cloud Platform (GCP) account and create a Pub/Sub topic with two subscriptions (one with message ordering enabled and another with message ordering disabled).

1. **Create a GCP Account**: If you don't have a GCP account, you can sign up for one [here](https://cloud.google.com/).

2. **Create a Pub/Sub Topic**: Create a Pub/Sub topic in your GCP project. You can do this using the Google Cloud Console or using the `gcloud` command-line tool.

3. **Create Two Subscriptions**: After creating the Pub/Sub topic, create two subscriptions for it - one with message ordering enabled and another with message ordering disabled. You can configure these subscriptions accordingly using the Google Cloud Console or `gcloud` command-line tool.

### Set Up Google Cloud Credentials

To connect your Spring-based application to GCP, you need to generate a credentials file.

1. Go to the GCP Console.
2. Navigate to **APIs & Services > Credentials**.
3. Click on the "Create Credentials" button and select "Service Account".
4. Fill in the necessary information for the service account and grant the appropriate permissions (Pub/Sub Publisher and Pub/Sub Subscriber).
5. Click on "Create" to create the service account.
6. Once the service account is created, click on the "Create Key" button and select "JSON" as the key type.
7. The JSON file containing the credentials will be downloaded. Keep this file secure, as it will be used to authenticate your application.

## Application Build and Run

Before running the application, make sure you have Java and Maven installed on your system.

1. Clone this repository and navigate to the project's root directory.

2. Build the application using Maven:

```bash
mvn clean install
```

3. Run the application using the following command, replacing `<your-gcp-project-id>` and `<your-gcp-credentials-file-path>` with your actual GCP project ID and the path to the credentials JSON file generated earlier:

```bash
java -jar -Dproject-id=<your-gcp-project-id> -Dcredentials-file-path=<your-gcp-credentials-file-path> target/<app-jar-file-name>.jar
```

## Publishing Events

To publish events, the application uses the `PubSubConstants` enum file to define the topic.

## Subscribing/Consuming Events

The application uses the `PubSubConstants` enum file to define the ordered and unordered subscriptions. You can switch the consumer between the ordered and unordered subscription by updating the `getSubscription()` method in the `PubSubListener` file.

## Steps to Reproduce

To test message ordering, follow these steps:

1. Disable the `PubSubListener` by removing or commenting out the `@Bean` annotation from the method `itemListener(PubSubTemplate pubSubTemplate)`.

2. Publish multiple events having the same ordering key using the following curl command:

```bash
curl --location 'localhost:8080/pub-sub/publish/items?orderingEnabled=true' \
--header 'Content-Type: application/json' \
--data '[
    {
        "itemNumber": "12344",
        "itemDescription": "Event 1"
    },
    {
        "itemNumber": "12344",
        "itemDescription": "Event 2"
    },
    {
        "itemNumber": "12344",
        "itemDescription": "Event 3"
    },
    {
        "itemNumber": "12344",
        "itemDescription": "Event 4"
    },
    {
        "itemNumber": "12344",
        "itemDescription": "Event 5"
    },
    {
        "itemNumber": "12344",
        "itemDescription": "Event 6"
    },
    {
        "itemNumber": "12344",
        "itemDescription": "Event 7"
    },
    {
        "itemNumber": "12344",
        "itemDescription": "Event 8"
    }
]'
```

3. Enable the listener and use the subscription that has message ordering enabled and run the application.

4. Observe the received order of the consumed events.

Remember, the ordering key is hardcoded for the sake of this proof of concept in the `PubSubProducer` file as `1234`.
