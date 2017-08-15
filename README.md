[![Logo][cashcash-image]]
# Cashcash

## Purpose

The goal of this project is to be able to manage and visualize your money through a self-hosted service.

### Build for development
First of all, this application was first generated using JHipster scaffolding tool (documentation here [https://jhipster.github.io](https://jhipster.github.io)) but then evolve freely without using the tool.

To build your application, simply launch the maven command:

    mvn install

### Run for development

Several steps are needed to start the development environnment:
- Start the mysql database by running one of the docker compose file in src/main/docker:


    docker-compose -f ./mysql-dev.yml up

- Run the app in your IDE by configuring a spring boot application with ```com.gagnepain.cashcash.CashcashApp``` as the main class.
- If you need to modify the frontend, run webpack-dev-server with the following command:


    yarn start

Then navigate to [http://localhost:8081](http://localhost:8081) in your browser.

## Build for production
To build your application, simply launch the maven command:

    mvn -Pprod clean install

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.


## License

This project is under the MIT License. See the [LICENSE](https://github.com/Winbee/Cashcash/blob/master/LICENSE) file for the full license text.


[cashcash-image]: https://cdn.rawgit.com/Winbee/Cashcash/01e57c27/src/main/webapp/resources/images/cashcash.png