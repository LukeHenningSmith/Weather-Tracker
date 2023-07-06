Weather Tracker is a Java application created by Luke Henning-Smith that uses an input weather API, output email API and JavaFX interface to request the weather of places around the world. The application is able to display weather information on the Java interface, as well as email it to a specified email. The application was designed using the Model-View-Presenter (MVP) architecture with careful consideration of encapsulation, modularity, extensibility and maintainability [SOLID & GRASP principles also considered]. The application also implements concurrency to maintain GUI responsiveness during slow API calls, the observer pattern between MVP components, and an extensive test suite that includes model and GUI testing with mocked external dependencies.

How to run:
1. Set the API keys of SENDGRID_API_EMAIL and SENDGRID_API_KEY for Sendgrid, and the INPUT_API_KEY for Weatherbit.
2. Use cradle run

How to test:
1. gradle test      //runs model tests.
2. gradle viewTest --tests "majorproject.test.view.*"    //runs view tests. Takes 2 minutes and 22 seconds.