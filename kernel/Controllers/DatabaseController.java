package kernel.Controllers;

import kernel.Database.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseController {
    private final Connection connection;
    private PreparedStatement preparedStatement;
    public final Connect connect;
    private final String table;

    public DatabaseController(String table){
        this.table = table;
        this.connect = new Connect();
        this.connection = connect.getConnection();
    }
    public ArrayList<HashMap<String, String>> getForTable() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " ORDER BY `time` ASC LIMIT 20 ");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                HashMap<String, String> data = new HashMap<>();
                data.put("name", resultSet.getString("name"));
                data.put("time", resultSet.getString("time"));
                list.add(data);
            }
        } catch (SQLException e){
            System.err.println("Ошибка с запросом: \n" + e);
        }
        return list;
    }

    public void put(String name, int time) {
        try {
            String sql = "INSERT INTO " + table + " (name, time) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, time);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении данных в базу данных: \n" + e);
        }
    }
}
