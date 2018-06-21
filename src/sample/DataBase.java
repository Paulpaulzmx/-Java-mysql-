package sample;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;

public class DataBase {

    Connection cnect = null;
    ResultSet rs = null;

    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/eidms?useSSL=false&useUnicode=true&characterEncoding=utf8";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "zdhwjfln";

    public DataBase() {
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            System.out.println("JDBC驱动加载成功");
        } catch (java.lang.ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    //连接数据库
    public void connect() {
        try {
            cnect = DriverManager.getConnection(DB_URL, USER, PASS);
            if (cnect != null) {
                System.out.println("mysql数据库连接成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //断开数据库连接
    public void disconnect() {
        try {
            if (cnect != null) {
                cnect.close();
                cnect = null;
            }
            System.out.println("已断开数据库的连接！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //执行查询语句
    public ResultSet queryDatabase(String sql) {
        //first connect
        try {
            Statement stmt = cnect.createStatement();
            System.out.println("SQL查询语句为：" + sql);
            rs = stmt.executeQuery(sql);
            System.out.println("执行查询的结果ResultSet为：" + rs);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return rs;
    }


    public int updateDatabases(String sql) {
        int count = 0;
        try {
            Statement stmt = cnect.createStatement();
            count = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            //弹出错误对话框

            Alert alert_SendFailed = new Alert(Alert.AlertType.ERROR);
            alert_SendFailed.setTitle("出错了...");
            alert_SendFailed.setHeaderText(null);
            alert_SendFailed.setContentText("数据库操作异常");

            // Create expandable Exception.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("异常信息为：");

            TextArea textArea_Exception = new TextArea(exceptionText);
            textArea_Exception.setEditable(false);
            textArea_Exception.setWrapText(true);

            textArea_Exception.setMaxWidth(Double.MAX_VALUE);
            textArea_Exception.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea_Exception, Priority.ALWAYS);
            GridPane.setHgrow(textArea_Exception, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea_Exception, 0, 1);

            // Set expandable Exception into the dialog pane.
            alert_SendFailed.getDialogPane().setExpandableContent(expContent);

            alert_SendFailed.showAndWait();

        }
        return count;
    }
}


