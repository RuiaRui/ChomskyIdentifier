package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Controller {
    @FXML
    private TextField gName;
    @FXML
    private TextField gNonTerminal;
    @FXML
    private TextArea gProduction;
    @FXML
    private TextArea gOutput;

    @FXML
    private Label lblErr;



    @FXML
    private void IdentifierClicked(ActionEvent event){
        gOutput.setText("");

        if (gName.getText().equals("") || gNonTerminal.getText().equals("") || gProduction.getText().equals("")){
            lblErr.setText("输入不完整");
            return;
        }
        if(!check()) {
            lblErr.setText("输入不合法");
            return;
        }

        lblErr.setText("");

        ChomskyGrammar cg=new ChomskyGrammar();
        cg.setName(gName.getText().charAt(0));
        cg.setStartChar(gName.getText().charAt(2));

        ArrayList<Character> Vn=new ArrayList<Character>();
        String[] temp=gNonTerminal.getText().split(",");
        for(String s:temp){
            Vn.add(s.charAt(0));
        }
        cg.setNonTerminals(Vn);

        if(!Vn.contains(cg.getStartChar())){
            lblErr.setText("文法错误");
            return;
        }

        cg.setProductions(productionHandler(gProduction.getText()));

        cg.setTerminals();

        cg.setType(cg.Idenifier());

        gOutput.setText(cg.toString());

    }

    private void ErrMessage(String Message) {
        Alert error = new Alert(Alert.AlertType.ERROR,Message);
        Button err = new Button();
        err.setOnAction((ActionEvent e) -> {
            error.showAndWait();
        });
    }

    private boolean check(){

        //检查s
        String regEx1 = "[A-Z]\\[[A-Z]\\]";
        Pattern pattern1 = Pattern.compile(regEx1);
        Matcher matcher1 = pattern1.matcher(gName.getText());
        if(!matcher1.matches()){
            return false;
        }

        //检查vn
//        String regEx2 = "[A-Z](,[A-Z])*";
//        Pattern pattern2 = Pattern.compile(regEx2);
//        Matcher matcher2 = pattern2.matcher(gName.getText());
//        if(!matcher2.matches()){
//            return false;
//        }

       //检查p

        String[] productions = gProduction.getText().split("\n");
        for (String s:productions) {
            String[] temp=s.split(":=");
            if( temp.length!=2){
                return false;
            }
        }

        return true;
    }

    private ArrayList<Production> productionHandler(String s) {
        ArrayList<Production> productions = new ArrayList<>();
        String[] temp = s.split("\n");
        for (String ss : temp) {
            String[] expression = ss.split("::=");
            if (expression[1].contains("|")) {
                String[] suffixes = expression[1].split("\\|");
                for (int j = 0; j < suffixes.length; j++) {
                    Production p = new Production();
                    p.setLeft(expression[0]);
                    p.setRight(suffixes[j]);
                    productions.add(p);
                }
            } else {
                Production p = new Production();
                p.setLeft(expression[0]);
                p.setRight(expression[1]);
                productions.add(p);
            }
        }
        return productions;
    }







}
