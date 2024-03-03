# Yummy

Simple online catering management application. Supports tables, customer and related orders management as well as menus, sections, and menu items management. Furthermore, a public section is available for customers that allows them to view the assigned menu and make orders.

This is an experimental project made while learning the Spring Framework, for this reason it is not really meant to be used in production since it may be incomplete.

## How to use this image

### Running as a single container

To run your own instance of Yummy, use the following command:

````bash
docker run -p 8080:8080 -d --name yummy enricosola/yummy:latest
````

Here you can find all the supported environment variables:

- `SPRING_DATASOURCE_URL`: The connection url form the MySQL database to use.
- `SPRING_DATASOURCE_USERNAME`: The MySQL database authentication username.
- `SPRING_DATASOURCE_PASSWORD`: The MySQL database authentication password.
- `ADMIN_USERNAME`: The admin user username.
- `ADMIN_PASSWORD`: The admin user password.

You can also change the storage directory binding a different one like this: `-v ./data/storage:/home/yummy/storage`

### Running via docker compose

Here you can find an example to run this application alongside a MySQL database instance using docker compose:

````yaml
version: '3.8'
services:
    webapp:
        image: 'enricosola/yummy:latest'
        container_name: 'yummy-webapp'
        hostname: 'yummy-webapp'
        restart: 'always'
        environment:
            - SPRING_DATASOURCE_URL=jdbc:mysql://yummy-mysql:3306/yummy?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
            - SPRING_DATASOURCE_USERNAME=yummy
            - SPRING_DATASOURCE_PASSWORD=yummy
            - ADMIN_USERNAME=admin
            - ADMIN_PASSWORD=admin
        volumes:
            - ./data/storage:/home/yummy/storage
        ports:
            - '9999:8080'
        networks:
            - yummy-network

    mysql:
        image: 'mysql:8.0'
        container_name: 'yummy-mysql'
        hostname: 'yummy-mysql'
        restart: always
        ports:
            - '3306:3306'
        volumes:
            - ./data/db:/var/lib/mysql
            - ./logs/mysql:/var/log/mysql
        environment:
            MYSQL_DATABASE: yummy
            MYSQL_ROOT_PASSWORD: root
            MYSQL_USER: yummy
            MYSQL_PASSWORD: yummy
            MYSQL_ROOT_HOST: 0.0.0.0
        security_opt:
            - seccomp:unconfined
        networks:
            - yummy-network

networks:
    yummy-network:
        driver: bridge
````

## License

This work is licensed under a
[Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License][cc-by-nc-sa].

[![CC BY-NC-SA 4.0][cc-by-nc-sa-image]][cc-by-nc-sa]

[cc-by-nc-sa]: http://creativecommons.org/licenses/by-nc-sa/4.0/
[cc-by-nc-sa-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png
[cc-by-nc-sa-shield]: https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-lightgrey.svg

Developed with ❤️ by [Enrico Sola](https://www.enricosola.dev).
