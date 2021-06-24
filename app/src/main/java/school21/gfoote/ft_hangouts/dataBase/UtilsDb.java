package school21.gfoote.ft_hangouts.dataBase;

public class UtilsDb {

    protected static final String DATABASE_NAME = "Ft_hangouts.db";
    protected static final int DATABASE_VERSION = 1;

    protected static final String CREATE_CONTACT_TABLE = "create table Contact ("
            + "Id integer primary key autoincrement,"
            + "FirstName text not null,"
            + "LastName text not null,"
            + "Phone text not null,"
            + "Mail text not null,"
            + "Address text,"
            + "Photo text"
            + ")";
    protected static final String CREATE_SMS_TABLE =  "create table Sms ("
            +   "Id integer primary key autoincrement,"
            +   "Phone text not null,"
            +   "Message text not null,"
            +   "Who text not null"
            + ")";

    protected static String INSERT_ELEM(String table, String parameters){
        return "insert into " + table + " values (" + parameters + ")";
    }

    protected static String UPDATE_ELEM(String table, String parameters, String[] params){
        return "update " + table + " set " + parameters + " where "
                + params[0] + " ='" + params[1] + "'";
    }

    protected static String SELECT_FROM_TABLE(String table, String[] params){
        return (params == null) ? "select * from " + table :
                "select * from " + table + " where " + params[0] + " ='" + params[1] + "'";
    }

    protected static String DELETE_FROM_TABLE(String table, String[] params){
        return "delete from " + table +" where " + params[0] + " =" + params[1];
    }
}
