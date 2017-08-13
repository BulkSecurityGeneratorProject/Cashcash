# What to do right after launching mysql database ?

## Change the root password

    mysql --host=192.168.1.2 --port=3306 -u root  -p

    mysql> use mysql;
    ERROR 1820 (HY000): You must reset your password using ALTER USER statement before executing this statement.

    set password = password("123456");

## Change the cashcashUser password

    SET PASSWORD FOR 'cashcashUser'@'%' = PASSWORD('123456');