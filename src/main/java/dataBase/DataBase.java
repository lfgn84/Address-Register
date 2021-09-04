package dataBase;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataBase {

    public MysqlDataSource dataSource(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/address_register");
        dataSource.setUser("root");
        dataSource.setPassword("password");

        return dataSource;
    }



}
