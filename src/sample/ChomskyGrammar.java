package sample;

import java.util.ArrayList;
import java.util.HashSet;

public class ChomskyGrammar {
    private char name;
    private char startChar;
    private int type;
    private ArrayList<Character> nonTerminals;
    private ArrayList<Character> terminals;
    private ArrayList<Production> productions;

    public char getName() {
        return name;
    }

    public void setName(char name) {
        this.name = name;
    }

    public char getStartChar() {
        return startChar;
    }

    public void setStartChar(char startChar) {
        this.startChar = startChar;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Character> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(ArrayList<Character> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public ArrayList<Character> getTerminals() {
        return terminals;
    }

    public void setTerminals(ArrayList<Character> terminals) {
        this.terminals = terminals;
    }

    public void setTerminals(){
        ArrayList<Character> Vt=new ArrayList<>();
        HashSet<Character> All=new HashSet<>();
        for(Production p:this.productions){
            for(int i=0;i<p.getLeft().length();i++){
                All.add(p.getLeft().charAt(i));
            }
            for(int i=0;i<p.getRight().length();i++){
                All.add(p.getRight().charAt(i));
            }
        }
        for(Object o : All){
            if(!this.getNonTerminals().contains(o) && !o.equals('ε')){
                Vt.add((Character) o);
            }
        }
        this.terminals=Vt;
    }

    public ArrayList<Production> getProductions() {
        return productions;
    }


    public void setProductions(ArrayList<Production> productions) {
            this.productions = productions;
    }

    @Override
    public String toString() {
        StringBuilder out1 =new StringBuilder().append("文法");
        out1.append(name).append("[").append(startChar).append("] = ( {");
        for (char n:nonTerminals) {
            out1.append(n).append(",");
        }
        out1.deleteCharAt(out1.length()-1).append("} , {");
        for (char t:terminals) {
            out1.append(t).append(",");
        }
        out1.deleteCharAt(out1.length()-1).append("} , P, ");
        out1.append(startChar).append(") \n");

        StringBuilder out2 =new StringBuilder().append("P:");
        for(Production p:productions) {
            out2.append(p.toString()).append("\n");
        }

       // StringBuilder out3 =new StringBuilder().append("该文法是").append(type).append("型文法");

        //modified
        StringBuilder out3 =new StringBuilder().append("该文法是");
        switch (type){
            case 0:
            case 1:
            case 2:
            case 3:
                out3.append(type).append("型文法");
                break;
            case 4:
                out3.append("该文法是扩充的2型文法");
                break;
            case 5:
                out3.append("该文法是扩充的3型文法");
                break;
            case -1:
                out3.append("文法错误");

        }
        out3.append("\n");

        return out1.toString()+out2.toString()+out3.toString();
    }

    public int Idenifier(){
        //是否满足文法条件

       if(Zero()){
           if(Second()){
               if(Third()){
                   if(Expand()){
                       return 5;
                   }
                   return 3;
               }
               else if(Expand()){
                   return 4;
               }
               return 2;

           }else{
               if(First()){
                   return 1;
               }
               return 0;
           }

       }

       return -1;
        //context free or not //左部是否为非终结符
        //2 or 3
            //0 or 1
                //除了空，右>左 is 1


    }


    public boolean Zero() {//
        for (Production p:this.productions) {
            if(p.getLeft().length()==1&&p.getLeft()=="ε"){
                return false;
            }
        }
        return true;
    }

    public boolean First(){
        for (Production p:this.productions) {
            if(p.getLeft().length()>p.getRight().length()||p.getRight().equals("ε")){
                return false;
            }
        }
        return true;
    }

    public boolean Second(){
        for (Production p:this.productions) {
            if(p.getLeft().length()>1||!(this.getNonTerminals().contains(p.getLeft().charAt(0)))){
                return false;
            }

        }
        return true;
    }

    public boolean Third(){
        for (Production p:this.productions) {
            if(p.getRight().length()>3
                    ||(p.getRight().length()==1&&(!((this.getTerminals().contains(p.getRight().charAt(0)))|(p.getRight().equals("ε")))))
                    ||(p.getRight().length()==2&&(!(this.getTerminals().contains(p.getRight().charAt(0))&this.getNonTerminals().contains(p.getRight().charAt(1)))))
            ){

                return false;
            }
        }
        return true;
    }

    public boolean Expand(){
        for(Production p:this.productions){
            if(p.getRight().equals("ε")){
                return true;
            }
        }
        return false;
    }



}
