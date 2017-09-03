![Logo][cashcash-image]
# Cashcash

## Purpose

The goal of this project is to be able to manage and visualize your money through a self-hosted service.

## Features

- Basic concept of accounting:
  - Every transaction is a flow of money from one account to another.
  - All the accounts belong to the following categories: income, expense, equity, assets, liabilities. The [documentation](https://www.gnucash.org/docs/v2.6/C/gnucash-guide/accts-concepts1.html) of GnuCash explains it quite well.
- Multi-currencies support
- .csv, .ofx and .json import
- Graph of your cash balance: How your assets and liabilities evolve through time?
- Graph of your cash flow: What are your incomes and expenses per month?

## Build for development
First of all, this application was first generated using JHipster scaffolding tool (documentation here [jhipster.github.io](https://jhipster.github.io)) but then evolve freely without using the tool.

To build your application, simply launch the maven command:

    mvn install

## Run for development

Several steps are needed to start the development environnment:
- Start the mysql database by running one of the docker compose file in src/main/docker:


    docker-compose -f ./mysql-dev.yml up

- Run the app in your IDE by configuring a spring boot application with ```com.gagnepain.cashcash.CashcashApp``` as the main class.
- If you need to modify the frontend, run webpack-dev-server with the following command:


    yarn start

- Then navigate to [http://localhost:8081](http://localhost:8081) in your browser.
- Login with the login/password user/user.


## Build for production
To build your application, simply launch the maven command:

    mvn -Pprod clean install

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Run for production

- Change the password of cashcashUser in src/main/docker/app.yml and src/main/docker/config/application-prod.yml.
    
- Start the app by running one of the docker compose file in src/main/docker:


    docker-compose -f ./app.yml up
    
- Change the root password of mysql:


    mysql --host=192.168.1.2 --port=3306 -u root  -p

    mysql> use mysql;
    ERROR 1820 (HY000): You must reset your password using ALTER USER statement before executing this statement.

    set password = password("123456");

- Then navigate to [http://localhost:8081](http://localhost:8081) in your browser.
- Login with the login/password user/user.

- Warning: Several user are created on startup. If you don't want them to be available, connect into the database and modify the column activated to 0 in the table jhi_user.

## License

This project is under the MIT License. See the [LICENCE](https://github.com/Winbee/Cashcash/blob/master/LICENCE) file for the full license text.


[cashcash-image]: https://cdn.rawgit.com/Winbee/Cashcash/01e57c27/src/main/webapp/resources/images/cashcash.png
