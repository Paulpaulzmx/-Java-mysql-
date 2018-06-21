package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;


public class login extends Application {

    private DataBase db = new DataBase();                       //数据库定义
    private ResultSet rs = null;                                //查询结果定义
    private StringBuffer currentUser = new StringBuffer();    //当前用户定义


    public void start(Stage primary_Stage) {
        //主舞台
        primary_Stage.setTitle("登入界面");
        primary_Stage.setMinHeight(280);
        primary_Stage.setMinWidth(608);
        //设置初始场景为登录场景
        primary_Stage.setScene(setScene_sceneLogin(primary_Stage));

        primary_Stage.show();
    }

    //设置为Login登录场景
    private Scene setScene_sceneLogin(Stage primary_Stage){
        //创建GridPane对象(登录的布局)
        GridPane grid_Login = new GridPane();
        grid_Login.setAlignment(Pos.CENTER);
        grid_Login.setHgap(10);
        grid_Login.setVgap(10);
        grid_Login.setPadding(new Insets(25, 25, 25, 25));

        //创建Scene对象(登录场景)
        Scene scene_Login = new Scene(grid_Login, 600, 375);

        //创建Text对象(标题)
        Text text_TitleWelcome = new Text("Welcome!");             //标题
        text_TitleWelcome.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid_Login.add(text_TitleWelcome, 0, 0, 2, 1);

        //创建Label对象(User Name:)，放到第0列，第1行
        Label label_UserName = new Label("User Name:");
        grid_Login.add(label_UserName, 0, 1);

        //创建TextField对象(文本输入框)，放到第1列，第1行
        TextField textField_UserName = new TextField();
        grid_Login.add(textField_UserName, 1, 1);

        //创建Label对象(Password:)，放到第0列，第1行
        Label label_Password = new Label("Password:");
        grid_Login.add(label_Password, 0, 2);

        //创建PasswordField对象(密码输入框)，放在1列，2行
        PasswordField pF_PW = new PasswordField();
        grid_Login.add(pF_PW, 1, 2);

        //创建Button对象(登录按钮)
        Button btn_signIn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn_signIn);//将按钮控件作为子节点
        grid_Login.add(hbBtn, 1, 4);//将HBox pane放到grid中的第1列，第4行

        //创建提示登录错误的Text对象
        Text text_LoginError = new Text();
        grid_Login.add(text_LoginError,0,4,2,1);
        text_LoginError.setFill(Color.ORANGERED);

        //登录按钮的动作
        btn_signIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //初始化数据库和查询结果集
                db.connect();
                rs = null;

                //获取用户输入内容
                String username = textField_UserName.getText().trim();   //trim用于去掉文本前后的空字符
                String user_pswd = pF_PW.getText();
//                pF_PW.clear();      //登录后清除内容
                currentUser.setLength(0);
                currentUser.append(username);

                //在数据库中查询相应数据，并判断用户是否合法
                String sql = "select * from user_pswd\nwhere NBXW_USER = \""
                        +username+"\" and NBXW_PSWD = \""+user_pswd + "\";";
                rs = db.queryDatabase(sql);
                try{
                    if(rs.next()){
                        text_LoginError.setText("");
                        primary_Stage.setTitle("主界面");
                        primary_Stage.setScene(setScene_sceneMain(primary_Stage));
                        System.out.println("正确");
                    }
                    else{
                        text_LoginError.setText("用户名或密码输入错误，请重试");
                        System.out.println("错误");
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
                db.disconnect();
            }
        });
        return scene_Login;
    }

    //设置为Main主场景
    private Scene setScene_sceneMain(Stage primary_Stage) {
        //BorderPane创建(主界面布局)
        BorderPane border_Main = new BorderPane();                  //创建borderPane布局\


        //基础部分：包括菜单栏、上标签部分和左列表部分
        //创建菜单
        MenuBar menuBar = new MenuBar();                            //创建菜单栏
        Menu menuFile = new Menu("File");                   //创建File菜单
        Menu menuAccount = new Menu("Account");             //创建Account菜单
        MenuItem accountLogout = new MenuItem("Logout");        //创建登出菜单选项Logout
        MenuItem fileExit = new MenuItem("Exit");               //创建退出菜单选项Exit
        menuBar.getMenus().addAll(menuFile, menuAccount);                        //将File,Account菜单加到菜单栏中
        menuFile.getItems().addAll(fileExit);               //将exit加入到菜单File中
        menuAccount.getItems().addAll(accountLogout);                  //将logout加入到菜单Account中

        //创建Label(菜单栏下的标签)
        Label label_Top = new Label();
        label_Top.setText("Welcome Login!");
//        label_Top.setTextFill(Color.BLUEVIOLET);
        label_Top.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        label_Top.setPadding(new Insets(0,0,0,40));

        //创建Separator(text_LoginWelcome下面的分割线)
        Separator separator_horizontal1 = new Separator();
        //TODO 使用css控制分割线的粗细

        //创建top部分的VBox
        VBox vBox_top = new VBox();
        vBox_top.getChildren().addAll(menuBar, label_Top, separator_horizontal1);

        //创建VBox(左选择方式列表)
        VBox vBox_left = new VBox();
        vBox_left.setPadding(new Insets(10));   //内边距
        vBox_left.setSpacing(5);            //节点边距
        Button btn_Write = new Button("写信息");//创建三个按钮
        Button btn_Receive = new Button("收件箱");
        Button btn_Draft = new Button("草稿");
        Button btn_Delete = new Button("垃圾箱");
        btn_Write.setMaxWidth(Double.MAX_VALUE);//设置按钮宽度一致
        btn_Receive.setMaxWidth(Double.MAX_VALUE);
        btn_Draft.setMaxWidth(Double.MAX_VALUE);
        vBox_left.getChildren().addAll(btn_Write, btn_Receive, btn_Draft, btn_Delete);

        //应用布局
        border_Main.setTop(vBox_top);        //布局top部分显示菜单栏
        border_Main.setLeft(vBox_left);     //布局left部分显示操作

        //创建Scene对象(主界面)
        Scene scene_Main = new Scene(border_Main, 600, 375);


        //写
        //创建(CENTER部分“写”中的编辑区)
        Text text_xwNumber = new Text("文稿号：");             //文稿号
        TextField textField_xwNumber = new TextField();
        Text text_xwTitle = new Text("标题：");
        TextField textField_xwTitle = new TextField();        //标题
        Text text_xwAuthor = new Text("作者：");
        TextField textField_xwAuthor = new TextField();       //作者
        Text text_xwDate = new Text("日期：");                 //日期
        DatePicker datePicker_xwDate = new DatePicker();
        datePicker_xwDate.setValue(LocalDate.now());
        datePicker_xwDate.setShowWeekNumbers(true);
        TextArea textArea_xwMain = new TextArea();            //正文
        textArea_xwMain.setPrefColumnCount(40);
        textArea_xwMain.setPrefRowCount(10);
        textArea_xwMain.setWrapText(true);
        Separator separator_vertical1 = new Separator();        //与左侧区域的纵向分割线
        separator_vertical1.setOrientation(Orientation.VERTICAL);
        Separator separator_vertical2 = new Separator();        //与右侧区域的纵向分割线
        separator_vertical2.setOrientation(Orientation.VERTICAL);

        //GridPane
        GridPane grid_CenterWrite = new GridPane();
        grid_CenterWrite.setAlignment(Pos.TOP_CENTER);
        grid_CenterWrite.setHgap(10);
        grid_CenterWrite.setVgap(10);
        grid_CenterWrite.setPadding(new Insets(10, 10, 10, 10));
        grid_CenterWrite.add(separator_vertical1, 0, 0, 1, 4);
        grid_CenterWrite.add(text_xwNumber, 1, 0);
        grid_CenterWrite.add(textField_xwNumber, 2, 0, 3, 1);
        grid_CenterWrite.add(text_xwTitle, 1, 1);
        grid_CenterWrite.add(textField_xwTitle, 2, 1, 3, 1);
        grid_CenterWrite.add(text_xwAuthor, 1, 2);
        grid_CenterWrite.add(textField_xwAuthor, 2, 2);
        grid_CenterWrite.add(text_xwDate, 3, 2);
        grid_CenterWrite.add(datePicker_xwDate, 4, 2);
        grid_CenterWrite.add(textArea_xwMain, 1, 3, 4, 1);
        grid_CenterWrite.add(separator_vertical2, 5, 0, 1, 4);
        GridPane.setVgrow(textArea_xwMain,Priority.ALWAYS);
        GridPane.setHgrow(textArea_xwMain,Priority.ALWAYS);


        //创建(CENTER部分“写”中右侧的功能区)
        //访问数据库获取收信人列表
        db.connect();
        rs = null;
        String sql1 = "select NBXW_USER from user_pswd;";
        rs = db.queryDatabase(sql1);
        int length_receivers = 0;
        try{
            while(rs.next()){
                length_receivers++;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        rs = null;
        rs = db.queryDatabase(sql1);
        String[] string_Receivers = new String[length_receivers];
        int i=0;
        int j;
        try{
            while(rs.next()){
                string_Receivers[i] = rs.getString("NBXW_USER");
                i++;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        db.disconnect();


        //设置布局
        //从内到外格式：checkBoxes_receivers -> vBox_RightInner -> scrollPane_RightInner -> vBox_Right
        CheckBox[] checkBoxes_receivers = new CheckBox[string_Receivers.length-1];      //复选框
        Text text_SelectRecipients = new Text(" 发送到:");
        ScrollPane scrollPane_RightInner = new ScrollPane();
        scrollPane_RightInner.setPrefSize(100,225);
        VBox vBox_RightInner = new VBox();
        vBox_RightInner.setPadding(new Insets(5,5,5,5));
        vBox_RightInner.setSpacing(5);
        Button btn_Send = new Button(" 发送 ");
        Button btn_Save = new Button("另存为");
        HBox hBox_twoBtn = new HBox();
        hBox_twoBtn.getChildren().addAll(btn_Save,btn_Send);
        hBox_twoBtn.setSpacing(10);
        hBox_twoBtn.setPadding(new Insets(20,5,5,5));

        //预设值
        //访问数据库获取当前账户信息
        String username = currentUser.toString();
        //初始化数据库和查询结果集
        db.connect();
        rs = null;
        //查询
        String sql = "select * from user_pswd\nwhere NBXW_USER = \"" + username + "\"" + ";";
        rs = db.queryDatabase(sql);
        //设置TextField默认值
        try {
            rs.next();                              //rs指向第一个也是唯一一个结果
            textField_xwNumber.setText(rs.getString("NBXW_USER") + GetTime());
            textField_xwAuthor.setText(rs.getString("NBXW_USER"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.disconnect();

        //生成(user-1)个数个复选框，并添加监听器
        StringBuffer stringBuffer_SelectedRecipients = new StringBuffer();        //记录被选中的人
        for(i=0,j=0;i<string_Receivers.length;i++,j++) {
            if(!string_Receivers[i].contentEquals(currentUser)){
                System.out.println(string_Receivers[i]);
                System.out.println(currentUser);
                System.out.println(i);
                checkBoxes_receivers[j] = new CheckBox(string_Receivers[i]);
                CheckBox cb = checkBoxes_receivers[j];
                //为复选框的每一个选项添加监听
                checkBoxes_receivers[j].selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if(cb.isSelected()){
                        stringBuffer_SelectedRecipients.append(cb.getText());
                        stringBuffer_SelectedRecipients.append(" ");
                    }
                    else{
                        int strLength = cb.getText().length();
                        int notIn = stringBuffer_SelectedRecipients.indexOf(cb.getText());
                        if(notIn != -1){
                            stringBuffer_SelectedRecipients.delete(notIn,notIn+strLength+1);
                        }
                    }
                });
                vBox_RightInner.getChildren().add(checkBoxes_receivers[j]);
            }
            else{
                j--;
            }
        }

        scrollPane_RightInner.setContent(vBox_RightInner);
        VBox vBox_Right = new VBox();
        vBox_Right.setPadding(new Insets(10, 20, 0, 0));
        vBox_Right.getChildren().addAll(text_SelectRecipients,scrollPane_RightInner,hBox_twoBtn);


        //内容详情
        Separator separator_vertical5 = new Separator();                //分割线左竖
        separator_vertical5.setOrientation(Orientation.VERTICAL);
        Separator separator_vertical6 = new Separator();                //分割线右竖
        separator_vertical6.setOrientation(Orientation.VERTICAL);
        Button btn_Return = new Button("返回");                     //返回按钮
        Text text_DetailTitle = new Text();                             //大标题
        text_DetailTitle.setFont(new Font(25));
        text_DetailTitle.setTextAlignment(TextAlignment.RIGHT);
        Separator separator_horizontal4 = new Separator();              //分割线横
        Button btn_SaveRemark = new Button("保存");                 //保存回复按钮

        Text text_DetailID = new Text("ID：");
        text_DetailID.setTextAlignment(TextAlignment.RIGHT);
        Text text_DetailAuthor = new Text("作者：");
        text_DetailAuthor.setTextAlignment(TextAlignment.RIGHT);
        Text text_DetailDate = new Text("日期：");
        text_DetailDate.setTextAlignment(TextAlignment.RIGHT);
        Text text_DetailContext = new Text("正文：");
        text_DetailContext.setTextAlignment(TextAlignment.RIGHT);
        Text text_DetailRemark = new Text("回复：");
        text_DetailRemark.setTextAlignment(TextAlignment.RIGHT);


        TextField textField_DetailID = new TextField();
        textField_DetailID.setPrefWidth(300);
        TextField textField_DetailAuthor = new TextField();
        textField_DetailAuthor.setPrefWidth(150);
        TextField textField_DetailDate = new TextField();
        textField_DetailDate.setPrefWidth(150);
        TextField textField_DetailContext = new TextField();
        textField_DetailContext.setPrefWidth(300);
        TextArea textArea_DetailRemark = new TextArea();
        textArea_DetailRemark.setPrefWidth(300);
        textArea_DetailRemark.setPrefHeight(160);
        textArea_DetailRemark.setWrapText(true);

        GridPane gridPane_Detail = new GridPane();
        gridPane_Detail.setVgap(10);
        gridPane_Detail.setHgap(10);
        gridPane_Detail.setPadding(new Insets(10,10,10,10));
        gridPane_Detail.add(separator_vertical5,0,0,1,6);
        gridPane_Detail.add(btn_Return,1,0);
        gridPane_Detail.add(text_DetailTitle,2,0);
        gridPane_Detail.add(separator_horizontal4,1,1,4,1);
        gridPane_Detail.add(text_DetailID,1,2);
        gridPane_Detail.add(textField_DetailID,2,2,3,1);
        gridPane_Detail.add(text_DetailAuthor,1,3);
        gridPane_Detail.add(textField_DetailAuthor,2,3);
        gridPane_Detail.add(text_DetailDate,3,3);
        gridPane_Detail.add(textField_DetailDate,4,3);
        gridPane_Detail.add(text_DetailContext,1,4);
        gridPane_Detail.add(textField_DetailContext,2,4,3,1);
        gridPane_Detail.add(text_DetailRemark,1,5);
        gridPane_Detail.add(textArea_DetailRemark,2,5,3,1);//TODO delete button 调整布局，返回放在左下角
        gridPane_Detail.add(separator_vertical6,5,0,1,6);
        gridPane_Detail.add(btn_SaveRemark,6,5,1,1);




        //收
        Separator separator_vertical4 = new Separator();
        separator_vertical4.setOrientation(Orientation.VERTICAL);
        Text text_ReceiveSelectQueryField = new Text("查询：");
        ObservableList<String> options_Receive =
                FXCollections.observableArrayList("编号","标题","作者","正文","日期");
        final ComboBox comboBox_ReceiveSelectQueryField = new ComboBox(options_Receive);
        comboBox_ReceiveSelectQueryField.setVisibleRowCount(4);
        comboBox_ReceiveSelectQueryField.setEditable(true);
        comboBox_ReceiveSelectQueryField.setPrefWidth(110);
        StringBuffer stringBuffer_ReceiveSelectQueryField = new StringBuffer();
        comboBox_ReceiveSelectQueryField.setOnAction((Event ev1)->{
            stringBuffer_ReceiveSelectQueryField.setLength(0);
            stringBuffer_ReceiveSelectQueryField.append(comboBox_ReceiveSelectQueryField.getSelectionModel().getSelectedItem().toString());
        });

        comboBox_ReceiveSelectQueryField.setPromptText("选择查询字段");
        Text text_ReceiveEqual = new Text("=");
        TextField textField_ReceiveSelectQueryField1 = new TextField();
        Button btn_ReceiveQuery = new Button("查询");
        Button btn_ReceiveQueryAll = new Button("查询所有");
        Separator separator_horizontal3 = new Separator();

        TableView<Data> tableView_ReceiveData = new TableView<>();
        tableView_ReceiveData.setPlaceholder(new Text("无内容"));
        tableView_ReceiveData.setEditable(true);

        TableColumn<Data,String> tableColumn_Receive_xwID = new TableColumn<>("ID");
        tableColumn_Receive_xwID.setMinWidth(23);
        tableColumn_Receive_xwID.setMaxWidth(23);
        tableColumn_Receive_xwID.setCellValueFactory(new PropertyValueFactory<>("ID"));

        TableColumn<Data,String> tableColumn_Receive_xwName = new TableColumn<>("标题");
        tableColumn_Receive_xwName.setMinWidth(80);
        tableColumn_Receive_xwName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Data,String> tableColumn_Receive_xwAuthor = new TableColumn<>("作者");
        tableColumn_Receive_xwAuthor.setMinWidth(50);
        tableColumn_Receive_xwAuthor.setMaxWidth(100);
        tableColumn_Receive_xwAuthor.setPrefWidth(50);
        tableColumn_Receive_xwAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Data,String> tableColumn_Receive_xwContext = new TableColumn<>("正文");
        tableColumn_Receive_xwContext.setMinWidth(120);
        tableColumn_Receive_xwContext.setCellValueFactory(new PropertyValueFactory<>("context"));

        TableColumn<Data,String> tableColumn_Receive_xwDate = new TableColumn<>("日期");
        tableColumn_Receive_xwDate.setMinWidth(130);
        tableColumn_Receive_xwDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn tableColumn_ReceiveGoToEdit = new TableColumn("查看");
        tableColumn_ReceiveGoToEdit.setCellValueFactory(new PropertyValueFactory<>(""));
        //查看按钮动作列
        Callback<TableColumn<Data, String>, TableCell<Data, String>> cellFactory_Receive
                =
                new Callback<>() {
                    @Override
                    public TableCell call(final TableColumn<Data, String> param) {
                        final TableCell<Data, String> cell = new TableCell<Data, String>() {
                            final Button btn = new Button("查看");
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        //修改标签
                                        label_Top.setText("Detail");
                                        //获取数据
                                        Data data = getTableView().getItems().get(getIndex());     //获取本行数据
                                        text_DetailTitle.setText(data.getName());
                                        textField_DetailID.setText(data.getID());
                                        textField_DetailAuthor.setText(data.getAuthor());
                                        textField_DetailDate.setText(data.getDate());
                                        textField_DetailContext.setText(data.getContext());
                                        //修改界面
                                        border_Main.setCenter(gridPane_Detail);
                                        border_Main.setRight(null);
                                        //将消息设为已读
                                        db.connect();
                                        String sql = "UPDATE xwcl SET XW_READED = 1 WHERE XW_ID = " + data.getID() +";";
                                        db.updateDatabases(sql);
                                        db.disconnect();
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        tableColumn_ReceiveGoToEdit.setCellFactory(cellFactory_Receive);

        TableColumn tableColumn_DeleteRow = new TableColumn("删除");
        //删除按钮动作列
        Callback<TableColumn<Data, String>, TableCell<Data, String>> cellFactory_Delete
                =
                new Callback<>() {
                    @Override
                    public TableCell call(final TableColumn<Data, String> param) {
                        final TableCell<Data, String> cell = new TableCell<Data, String>() {
                            final Button btn = new Button("删除");
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        //将flag设置为

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        tableColumn_ReceiveGoToEdit.setCellFactory(cellFactory_Delete);


        tableView_ReceiveData.getColumns().addAll(tableColumn_Receive_xwID,tableColumn_Receive_xwName,
                tableColumn_Receive_xwAuthor,tableColumn_Receive_xwContext,tableColumn_Receive_xwDate,tableColumn_ReceiveGoToEdit);
        VBox vBox_ReceiveTable = new VBox();
        vBox_ReceiveTable.setPadding(new Insets(10, 5, 10, 13));
        vBox_ReceiveTable.getChildren().addAll(tableView_ReceiveData);


        //创建RECEIVE的CENTER部分
        GridPane grid_CenterReceive = new GridPane();
        grid_CenterReceive.setAlignment(Pos.TOP_CENTER);
        grid_CenterReceive.setHgap(10);
        grid_CenterReceive.setVgap(10);
        grid_CenterReceive.setPadding(new Insets(10, 10, 10, 10));
        grid_CenterReceive.add(separator_vertical4,0,0,1,3);
        grid_CenterReceive.add(text_ReceiveSelectQueryField,1,0);
        grid_CenterReceive.add(comboBox_ReceiveSelectQueryField,2,0);
        grid_CenterReceive.add(text_ReceiveEqual,3,0);
        grid_CenterReceive.add(textField_ReceiveSelectQueryField1,4,0);
        grid_CenterReceive.add(btn_ReceiveQuery,5,0);
        grid_CenterReceive.add(btn_ReceiveQueryAll,6,0);
        grid_CenterReceive.add(separator_horizontal3,1,1,6,1);
        grid_CenterReceive.add(vBox_ReceiveTable,0,2,7,1);



        //草稿
        Separator separator_vertical3 = new Separator();
        separator_vertical3.setOrientation(Orientation.VERTICAL);
        Text text_DraftSelectQueryField = new Text("查询：");
        ObservableList<String> options_Draft =
                FXCollections.observableArrayList("编号","标题","作者","正文","日期");
        final ComboBox comboBox_DraftSelectQueryField = new ComboBox(options_Draft);
        comboBox_DraftSelectQueryField.setVisibleRowCount(4);
        comboBox_DraftSelectQueryField.setEditable(true);
        comboBox_DraftSelectQueryField.setPrefWidth(110);
        StringBuffer stringBuffer_DraftSelectQueryField = new StringBuffer();
        comboBox_DraftSelectQueryField.setOnAction((Event ev) -> {       //combobox可编辑，可选择值用于查询
            stringBuffer_DraftSelectQueryField.setLength(0);
            stringBuffer_DraftSelectQueryField.append(comboBox_DraftSelectQueryField.getSelectionModel().getSelectedItem().toString());
        });

        comboBox_DraftSelectQueryField.setPromptText("选择查询字段");
        Text text_DraftEqual = new Text("=");
        TextField textField_DraftSelectQueryField = new TextField();
        Button btn_DraftQuery = new Button("查询");
        Button btn_DraftQueryAll = new Button("查询所有");
        Separator separator_horizontal2 = new Separator();

        TableView<Data> tableView_DraftData = new TableView<>();            //表格
        tableView_DraftData.setPlaceholder(new Text("无内容"));
        tableView_DraftData.setEditable(true);

        TableColumn<Data,String> tableColumn_Draft_xwID = new TableColumn<>("ID");
        tableColumn_Draft_xwID.setMinWidth(23);
        tableColumn_Draft_xwID.setMaxWidth(23);
        tableColumn_Draft_xwID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        //ID不可修改

        TableColumn<Data,String> tableColumn_Draft_xwName = new TableColumn<>("标题");
        tableColumn_Draft_xwName.setMinWidth(80);
        tableColumn_Draft_xwName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumn_Draft_xwName.setCellFactory(TextFieldTableCell.<Data>forTableColumn());
        tableColumn_Draft_xwName.setOnEditCommit(               //修改列值同步到数据库
                (TableColumn.CellEditEvent<Data, String> t) -> {
                    ((Data) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setID(t.getNewValue());
                    //更新数据库
                    Data temp = t.getRowValue();
                    String sql_updateName = " UPDATE xw SET XW_NAME=\""+t.getNewValue()+
                            "\" WHERE XW_NAME=\""+temp.getName()+"\";";
                    db.connect();
                    try {
                        if (db.updateDatabases(sql_updateName) < 1) {
                            System.out.println("更新失败");
                        } else {
                            System.out.println("更新成功");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    db.disconnect();
                });

        TableColumn<Data,String> tableColumn_Draft_xwAuthor = new TableColumn<>("作者");
        tableColumn_Draft_xwAuthor.setMinWidth(50);
        tableColumn_Draft_xwAuthor.setMaxWidth(100);
        tableColumn_Draft_xwAuthor.setPrefWidth(50);
        tableColumn_Draft_xwAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        //author不可修改

        TableColumn<Data,String> tableColumn_Draft_xwContext = new TableColumn<>("正文");
        tableColumn_Draft_xwContext.setMinWidth(120);
        tableColumn_Draft_xwContext.setCellValueFactory(new PropertyValueFactory<>("context"));
        tableColumn_Draft_xwContext.setCellFactory(TextFieldTableCell.<Data>forTableColumn());
        tableColumn_Draft_xwContext.setOnEditCommit(
                (TableColumn.CellEditEvent<Data, String> t) -> {
                    ((Data) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setID(t.getNewValue());
                    //更新数据库
                    Data temp = t.getRowValue();
                    String sql_updateContext = " UPDATE xw SET XW_CONTEXT=\""+t.getNewValue()+
                            "\" WHERE XW_CONTEXT=\""+temp.getContext()+"\";";
                    db.connect();
                    try {
                        if (db.updateDatabases(sql_updateContext) < 1) {
                            System.out.println("失 败");
                        } else {
                            System.out.println("成 功");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    db.disconnect();
                });

        TableColumn<Data,String> tableColumn_Draft_xwDate = new TableColumn<>("日期");
        tableColumn_Draft_xwDate.setMinWidth(130);
        tableColumn_Draft_xwDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumn_Draft_xwDate.setCellFactory(TextFieldTableCell.<Data>forTableColumn());
        tableColumn_Draft_xwDate.setOnEditCommit(
                (TableColumn.CellEditEvent<Data, String> t) -> {
                    ((Data) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setID(t.getNewValue());
                    //更新数据库
                    Data temp = t.getRowValue();
                    String sql_updateDate = " UPDATE xw SET XW_DATE=\""+t.getNewValue()+
                            "\" WHERE XW_DATE=\""+temp.getDate()+"\";";
                    db.connect();
                    try {
                        if (db.updateDatabases(sql_updateDate) < 1) {
                            System.out.println(" 失 败");
                        } else {
                            System.out.println(" 成 功");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    db.disconnect();
                });

        TableColumn tableColumn_DraftGoToEdit = new TableColumn("修改和发送");
        tableColumn_DraftGoToEdit.setCellValueFactory(new PropertyValueFactory<>(""));
        //跳转按钮动作列
        Callback<TableColumn<Data, String>, TableCell<Data, String>> cellFactory_Draft
                =
                new Callback<>() {
                    @Override
                    public TableCell call(final TableColumn<Data, String> param) {
                        final TableCell<Data, String> cell = new TableCell<Data, String>() {
                            final Button btn = new Button("发送");
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Data data = getTableView().getItems().get(getIndex());     //获取本列数据

                                        //切换顶部标签
                                        label_Top.setText("modify and send the message.");
                                        //切换场景
                                        border_Main.setCenter(grid_CenterWrite);
                                        border_Main.setRight(vBox_Right);

                                        textField_xwNumber.setText(currentUser.toString() + GetTime());
                                        textField_xwAuthor.setText(data.getAuthor());
                                        textField_xwTitle.setText(data.getName());
                                        datePicker_xwDate.setValue(LocalDate.now());
                                        textArea_xwMain.setText(data.getContext());
                                        System.out.println(datePicker_xwDate.getEditor().getText());//获取时间

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        tableColumn_DraftGoToEdit.setCellFactory(cellFactory_Draft);

        tableView_DraftData.getColumns().addAll(tableColumn_Draft_xwID,tableColumn_Draft_xwName,
                tableColumn_Draft_xwAuthor,tableColumn_Draft_xwContext,tableColumn_Draft_xwDate,tableColumn_DraftGoToEdit);
        VBox vBox_DraftTable = new VBox();
        vBox_DraftTable.setPadding(new Insets(10, 5, 10, 13));
        vBox_DraftTable.getChildren().addAll(tableView_DraftData);


        //创建DRAFT的CENTER部分
        GridPane grid_CenterDraft = new GridPane();
        grid_CenterDraft.setAlignment(Pos.TOP_CENTER);
        grid_CenterDraft.setHgap(10);
        grid_CenterDraft.setVgap(10);
        grid_CenterDraft.setPadding(new Insets(10, 10, 10, 10));
        grid_CenterDraft.add(separator_vertical3,0,0,1,3);
        grid_CenterDraft.add(text_DraftSelectQueryField,1,0);
        grid_CenterDraft.add(comboBox_DraftSelectQueryField,2,0);
        grid_CenterDraft.add(text_DraftEqual,3,0);
        grid_CenterDraft.add(textField_DraftSelectQueryField,4,0);
        grid_CenterDraft.add(btn_DraftQuery,5,0);
        grid_CenterDraft.add(btn_DraftQueryAll,6,0);
        grid_CenterDraft.add(separator_horizontal2,1,1,6,1);
        grid_CenterDraft.add(vBox_DraftTable,0,2,7,1);



        //按钮btn_SaveRemark的动作
        btn_SaveRemark.setOnAction(event -> {
            //修改xwcl表中的内容
            String sql_updateRemark ="UPDATE xwcl set XW_REMARK = \'" + textArea_DetailRemark.getText() + "\' " +
                    "where XW_ID = " + textField_DetailID.getText() +";";
            db.connect();
            try {
                if (db.updateDatabases(sql_updateRemark) < 1) {
                    BuildAlert(Alert.AlertType.INFORMATION,"错误",null,"回复失败！");
                } else {
                    BuildAlert(Alert.AlertType.INFORMATION,"回复成功",null,"已回复！");
                }
            }
            catch (Exception e){
                BuildAlert(Alert.AlertType.ERROR,"数据错误",null,e.getMessage());
                e.printStackTrace();
            }
            db.disconnect();
        });

        //按钮btn_Receive的动作
        btn_Receive.setOnAction(event -> {
            //切换顶部标签
            label_Top.setText("Receives");
            //切换场景
            border_Main.setCenter(grid_CenterReceive);
            border_Main.setRight(null);
        });

        //按钮btn_Return的动作
        btn_Return.setOnAction(event -> {
            //切换顶部标签
            label_Top.setText("Receives");
            //切换场景
            border_Main.setCenter(grid_CenterReceive);
            border_Main.setRight(null);
        });

        //按钮btn_ReceiveQuery的动作
        btn_ReceiveQuery.setOnAction(event ->{

            String condition;
            switch (stringBuffer_ReceiveSelectQueryField.toString()){
                case "编号":{
                    condition = "XW_ID";break;
                }
                case "标题":{
                    condition = "XW_NAME";break;
                }
                case "作者":{
                    condition = "XW_AUTHOR";break;
                }
                case "正文":{
                    condition = "XW_CONTEXT";break;
                }
                case "日期":{
                    condition = "XW_DATE";break;
                }
                default:{
                    condition = "";break;
                }
            }
            if(condition.contentEquals("")){
                BuildAlert(Alert.AlertType.ERROR,"错误",null,"请选择查询条件");
            }
            else{
                db.connect();
                rs = null;
                //条件查询
                String sql23;
                if(condition.contentEquals("XW_ID")){
                    sql23 = "select * from xwcl\n" + "WHERE " + condition + " = " +
                            textField_ReceiveSelectQueryField1.getText() +" AND XW_RECEIVER = \'" +
                            currentUser + "\';";
                }
                else {
                    sql23 = "select * from xwcl\n" + "WHERE " + condition + " = \'" +
                            textField_ReceiveSelectQueryField1.getText() +"\' AND XW_RECEIVER = \'" +
                            currentUser + "\';";
                }
                rs = db.queryDatabase(sql23);

                //根据查询结果建立表格
                BuildTable(tableView_ReceiveData,rs);

                db.disconnect();
            }
        });

        //按钮btn_ReceiveQueryAll的动作
        btn_ReceiveQueryAll.setOnAction(event -> {
            db.connect();
            rs = null;
            //查询所有
            String sql3 = "select * from xwcl WHERE XW_RECEIVER = \'" + currentUser +"\';";
            rs = db.queryDatabase(sql3);
            //根据查询结果建立表格
            BuildTable(tableView_ReceiveData,rs);
            db.disconnect();
        });

        //按钮btn_Draft的动作
        btn_Draft.setOnAction(event -> {
            //切换顶部标签
            label_Top.setText("Drafts.");
            //切换场景
            border_Main.setRight(null);
            border_Main.setCenter(grid_CenterDraft);
        });

        //按钮btn_DraftQuery的动作
        btn_DraftQuery.setOnAction(event -> {

            String condition;
            switch (stringBuffer_DraftSelectQueryField.toString()){
                case "编号":{
                    condition = "XW_ID";break;
                }
                case "标题":{
                    condition = "XW_NAME";break;
                }
                case "作者":{
                    condition = "XW_AUTHOR";break;
                }
                case "正文":{
                    condition = "XW_CONTEXT";break;
                }
                case "日期":{
                    condition = "XW_DATE";break;
                }
                default:{
                    condition = "";
                }
            }

            if(condition.contentEquals("")){
                BuildAlert(Alert.AlertType.ERROR,"错误",null,"请选择查询条件");
            }
            else{
                db.connect();
                rs = null;
                //条件查询
                String sql23;
                if(condition.contentEquals("XW_ID")){
                    sql23 = "select * from xw\n" + "WHERE " + condition + " = " +
                            textField_DraftSelectQueryField.getText() +" AND XW_FLA = 0;";
                }
                else {
                    sql23 = "select * from xw\n" + "WHERE " + condition + " = \'" +
                            textField_DraftSelectQueryField.getText() +"\' AND XW_FLA = 0;";
                }
                rs = db.queryDatabase(sql23);

                //根据查询结果建立表格
                BuildTable(tableView_DraftData,rs);

                db.disconnect();
            }
        });

        //按钮btn_DraftQueryAll的动作
        btn_DraftQueryAll.setOnAction(event -> {
            db.connect();
            rs = null;

            //查询所有
            String sql22 = "select * from xw\n"+"WHERE XW_FLA = 0;";
            rs = db.queryDatabase(sql22);
            //根据查询结果建立表格
            BuildTable(tableView_DraftData,rs);
            db.disconnect();
        });

        //按钮btn_Write的动作
        btn_Write.setOnAction(event -> {
            //切换顶部标签
            label_Top.setText("Write a new message.");
            //切换场景
            border_Main.setCenter(grid_CenterWrite);
            border_Main.setRight(vBox_Right);
        });

        //按钮btn_Send的动作
        btn_Send.setOnAction(event -> {
            db.connect();

            //向表xw中插入数据
            String sql_xw = "insert into xw values(";
            sql_xw = sql_xw + GetSpareId("xw") + ",'";  //用方法GetSpareId查询自动变化的灵性ID
            sql_xw = sql_xw + textField_xwTitle.getText() + "','";
            sql_xw = sql_xw + textField_xwAuthor.getText() + "','";
            sql_xw = sql_xw + textArea_xwMain.getText() + "','";
            sql_xw = sql_xw + GetTime() + "',";
            sql_xw = sql_xw + 1 + ");";     //发送成功的flag为1
            System.out.println(sql_xw);
            try {
                if (stringBuffer_SelectedRecipients.toString().contentEquals("")) {
                    BuildAlert(Alert.AlertType.ERROR,"错误",null,"请至少选择一位收件人");
                }
                else if(db.updateDatabases(sql_xw) < 1){
                    System.out.println("出错了,详情见提示框");
                }
                else{
                    System.out.println("收件人为："+stringBuffer_SelectedRecipients);
                    BuildAlert(Alert.AlertType.INFORMATION,"发送成功",null,
                            "已向"+stringBuffer_SelectedRecipients+"发送消息！");
                }
            }
            catch(Exception e){
                 e.printStackTrace();
            }

            //向表xwcl插入数据
            String[] strings_receivers = stringBuffer_SelectedRecipients.toString().split("\\s+");
            for (String strings_receiver : strings_receivers) {
                String sql_xwcl = "insert into xwcl values(";
                sql_xwcl = sql_xwcl + GetSpareId("xwcl") + ",'";
                sql_xwcl = sql_xwcl + textField_xwTitle.getText() + "','";
                sql_xwcl = sql_xwcl + textField_xwAuthor.getText() + "','";
                sql_xwcl = sql_xwcl + strings_receiver + "','";
                sql_xwcl = sql_xwcl + textArea_xwMain.getText() + "','";
                sql_xwcl = sql_xwcl + GetTime() + "','',";         //XW_REMARK意见暂时为空
                sql_xwcl = sql_xwcl + 0 + ");";                    //XW_READED默认为0，未读
                System.out.println(sql_xwcl);
                try {
                    if (db.updateDatabases(sql_xwcl) < 1) {
                        System.out.println("向xwcl表插入数据失败");
                    } else {
                        System.out.println("成功向xwcl表插入数据");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            db.disconnect();
        });

        //按钮btn_Save的动作
        btn_Save.setOnAction(event -> {
            db.connect();

            //向表xw中插入数据
            String sql2 = "insert into xw values(";
            sql2 = sql2 + GetSpareId("xw") + ",'";  //用方法GetSpareId查询自动变化的灵性ID
            sql2 = sql2 + textField_xwTitle.getText() + "','";
            sql2 = sql2 + textField_xwAuthor.getText() + "','";
            sql2 = sql2 + textArea_xwMain.getText() + "','";
            sql2 = sql2 + GetTime() + "',";
            sql2 = sql2 + 0 + ");";     //另存为的flag为0
            try {
                if(db.updateDatabases(sql2)<1){
                    System.out.println("出错了，详情见提示框");
                }
                else{
                    System.out.println("已保存！");
                    BuildAlert(Alert.AlertType.INFORMATION,"保存成功",null,"已保存！");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            db.disconnect();
        });

        //菜单栏的动作
        fileExit.setOnAction(actionEvent -> Platform.exit());           //exit退出
        //logout回到登陆界面
        accountLogout.setOnAction(event -> primary_Stage.setScene(setScene_sceneLogin(primary_Stage)));
        return scene_Main;
    }

    //获取数据的类
    public static class Data {
        private final SimpleStringProperty ID;
        private final SimpleStringProperty name;
        private final SimpleStringProperty author;
        private final SimpleStringProperty context;
        private final SimpleStringProperty date;

        private Data(String sID, String sname, String sauthor, String scontext, String sdate) {
            this.ID = new SimpleStringProperty(sID);
            this.name = new SimpleStringProperty(sname);
            this.author = new SimpleStringProperty(sauthor);
            this.context = new SimpleStringProperty(scontext);
            this.date = new SimpleStringProperty(sdate);
        }

        public String getID(){
            return ID.get();
        }

        public void setID(String sID) {
            ID.set(sID);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String sname) {
            name.set(sname);
        }

        public String getAuthor() {
            return author.get();
        }

        public void setAuthor(String sauthor) {
            author.set(sauthor);
        }

        public String getContext() {
            return context.get();
        }

        public void setContext(String scontext) {
            context.set(scontext);
        }

        public String getDate() {
            return date.get();
        }

        public void setDate(String sdate) {
            date.set(sdate);
        }
    }

    //获取当前时间
    private StringBuffer GetTime(){
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        StringBuffer currentTime = new StringBuffer("  ");
        currentTime.append(year);
        currentTime.append("/");
        currentTime.append(month);
        currentTime.append("/");
        currentTime.append(date);
        currentTime.append("  ");
        currentTime.append(hour);
        currentTime.append(":");
        if(minute<10)
            currentTime.append("0");
        currentTime.append(minute);

        return currentTime;
    }

    //查找空余id
    private int GetSpareId(String table){
        String sql = "select XW_ID from "+table+";";
        rs = null;
        rs = db.queryDatabase(sql);
        int spare_id = 1;       //找一个空余的id，作为不重复的主码
        StringBuilder stringBuffer_spare_id= new StringBuilder();
        stringBuffer_spare_id.append(spare_id);
        try{
            while(rs.next()) {
                if (rs.getString("XW_ID").contentEquals(stringBuffer_spare_id.toString())) {
                    spare_id++;
                    stringBuffer_spare_id.setLength(0);
                    stringBuffer_spare_id.append(spare_id);
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return spare_id;
    }

    //按要求建立表格
    private void BuildTable(TableView<Data> tableView_data, ResultSet rs){
        try{
            rs.last();
            int dataArrayLength = rs.getRow();              //对象数组长度
            Data[] dataArray = new Data[dataArrayLength];   //建立记录长度的对象数组，
            rs.beforeFirst();
            for(int i=0;rs.next();i++){
                dataArray[i] = new Data(rs.getString("XW_ID"),rs.getString("XW_NAME"),
                        rs.getString("XW_AUTHOR"),rs.getString("XW_CONTEXT"),
                        rs.getString("XW_DATE"));//新建一个对象并初始化
            }
            ObservableList<Data> dataAll = FXCollections.observableArrayList(dataArray);
            tableView_data.setItems(dataAll);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    //建立提示框
    private void BuildAlert(Alert.AlertType type, String title, String headText, String contentText){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}
