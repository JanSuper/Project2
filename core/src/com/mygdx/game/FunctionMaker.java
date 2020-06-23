package com.mygdx.game;
import Model.Function2d;
import Model.Vector2d;

import java.util.*;
import java.util.regex.Pattern;

/**
 * FunctionMaker : makes a function that returns operations entered upon creation of Object
 *	op1: addition subtraction (lowest priority)
 *	op2: multiplication division (higher priority)
 *	op3: power (highest priority)
 *	sp: cos, acos, log...: treat as function whatever is on the right?
 */

public class FunctionMaker implements Function2d {
    private String function;
    private static String basicfunction = "0";//" sin (x) + y ^ 2";


    private  ArrayList<String> arguments; // translation into seperate arguments
    private  ArrayList<String> type; //type of arguments (operators, numerical value,etc...)
    private static HashMap<String, Function> map=null; // map all functions that could be use
    //Pattern of decimal, natural numbers
    private static final String decimalPattern = "([0-9]*)\\.([0-9]*)";
    private static final String naturalPattern = "([0-9]*)";
    private static final String semiDecimalPattern = "([0-9]*)\\.";
    private static final double step = 0.000001;

    private FunctionMaker[] functions;//functions in that function (funcCeption!)
    private int funcounter;//keep in memory which function has been computed already

    //call this once only as map is Static
    private FunctionMaker(){
        // make a Hashmap with all functions
        map = new HashMap<String, Function>();

        // Populate commands map
        map.put("+", new Function() {
            public double compute(double a, double b) { return a+b; }
        });
        map.put("-", new Function() {
            public double compute(double a, double b) { return a-b; }
        });
        map.put("/", new Function() {
            public double compute(double a, double b) { return a/b; }
        });
        map.put("*", new Function() {
            public double compute(double a, double b) { return a*b; }
        });
        map.put("^", new Function() {
            public double compute(double a, double b) { return Math.pow(a,b); }
        });
        map.put("sin", new Function() {
            public double compute(double a, double b) { return Math.sin(a); }
        });
        map.put("cos", new Function() {
            public double compute(double a, double b) { return Math.cos(a); }
        });
        map.put("tan", new Function() {
            public double compute(double a, double b) { return Math.tan(a); }
        });
        map.put("asin", new Function() {
            public double compute(double a, double b) { return Math.asin(a); }
        });
        map.put("acos", new Function() {
            public double compute(double a, double b) { return Math.acos(a); }
        });
        map.put("atan", new Function() {
            public double compute(double a, double b) { return Math.atan(a); }
        });
        map.put("log", new Function() {
            public double compute(double a, double b) { return Math.log(a); }
        });
        map.put("sqrt", new Function(){
            public double compute(double a, double b) { return Math.sqrt(a); }
        });
    }

    /**
     * CONSTRUCTOR
     * @param function : String to translate into a function
     */
    public FunctionMaker(String function){
        if(map == null) new FunctionMaker();
        this.function = function.toLowerCase().replace(" ","");
        transfer();
        int c=0;
        for(int i=0;i<type.size();i++) if(type.get(i).equals("FUN")) c++;
        functions = new FunctionMaker[c];
        c=0;
        for(int i=0;i<type.size();i++){
            if(type.get(i).equals("FUN")) functions[c++] = new FunctionMaker(arguments.get(i).substring(1, arguments.get(i).length()-1));
        }
    }


    /**
     * transfer the string into arguments and keep in memory the type
     */
    private void transfer(){
        arguments = new ArrayList<>();
        type = new ArrayList<>();
        int counter = 0;
        int end = counter+1;
        while(counter < function.length()){
            //IF NUMBER
            if(Pattern.matches(naturalPattern,function.substring(counter, counter+1))){
                end=counter;
                while(end <= function.length()&&
                        (Pattern.matches(naturalPattern,function.substring(counter, end))||
                        Pattern.matches(decimalPattern,function.substring(counter, end))||
                        Pattern.matches(semiDecimalPattern,function.substring(counter, end))))
                    end++;
                arguments.add(function.substring(counter, end-1));
                type.add("NUM");
                counter = Math.max(end-1, counter+1);
                //IF OPERATION
            }else if(Pattern.matches("[*/+-^]", function.substring(counter, counter+1))){
                end = counter+1;
                arguments.add(function.substring(counter, counter+1));
                if(Pattern.matches("[+-]", function.substring(counter, counter+1))){
                    if(function.substring(counter, end).equals("+")){
                        type.add("OP1");
                    }else{//IF minus is not OPERATOR but (-1)*... :
                        if(counter-1<0 || function.charAt(counter-1)=='('){
                            arguments.remove(arguments.size()-1);
                            arguments.add("(-1)");
                            type.add("MIN");
                            counter = end-1;
                        }else{
                            type.add("OP1");
                        }
                    }
                }
                else if(Pattern.matches("[*/]",function.substring(counter, counter+1))) type.add("OP2");
                else type.add("OP3");
                counter++;
                // IF SPECIAL OPERATION
            }else if(Pattern.matches("[asclt]", function.substring(counter, counter+1))){
                end = counter+1;
                while(Pattern.matches("[a-w]*", function.substring(counter, end)))end++;
                arguments.add(function.substring(counter, end-1));
                type.add("SP");
                counter = end-1;
                // TODO: verify the next thing is a function
            }else if(function.charAt(counter) =='('){
                int n =0;
                end = counter+1;
                while(function.charAt(end)!=')' || n!=0){
                    if(function.charAt(end) == '(') n ++;
                    else if(n>0 && function.charAt(end) == ')') n--;
                    end++;
                }
                arguments.add(function.substring(counter, end+1));
                type.add("FUN");
                counter = end+1;
                //IF VARIABLE
            }else if(function.charAt(counter)=='x'||function.charAt(counter)=='y'){
                if(type.size()>0 && ( type.get(type.size()-1).equals("VAR") ||
                        type.get(type.size()-1).equals("FUN") || type.get(type.size()-1).equals("NUM")) ){
                    arguments.add("*");
                    type.add("OP2");
                }
                arguments.add(function.substring(counter, counter+1));
                type.add("VAR");
                counter++;
            }else{
                System.out.println("WARNING ! There was a problem reading the formula");
            }
        }
    }

    /**
     * get the ouput of the function encoded upon creation of object
     * @param vector2d : vector which contains x and y coordinates
     * @return z coordinate
     */
    public double evaluate(Vector2d vector2d){
        //new calc -> no functions was used yet and nothing is computed
        double x = vector2d.getX(), y = vector2d.getY();
        funcounter=0;
        boolean[] comput = new boolean[arguments.size()];
        //find first argument
        double temp = helper(comput,0, x,y);
        int index = next(comput);
        //while there is something left to compute
        while(index!=-1){
            temp = helper(temp, comput, index,x,y);
            index = next(comput);
        }
        return temp;

    }

    /**
     * get the ouput of the function encoded upon creation of object
     * @param x :  x and y coordinates
     * @param y : x and y coordinates
     * @return z coordinate
     * TODO REMOVE THIS METHOD
     */
    public double evaluate(double x, double y ){
        //new calc -> no functions was used yet and nothing is computed

        funcounter=0;
        boolean[] comput = new boolean[arguments.size()];
        //find first argument
        double temp = helper(comput,0, x,y);
        int index = next(comput);
        //while there is something left to compute
        while(index!=-1){
            temp = helper(temp, comput, index,x,y);
            index = next(comput);
        }
        return temp;

    }

    @Override
    public Vector2d gradient(Vector2d p) {
       return new Vector2d((evaluate(p.getX()+step, p.getY()) - evaluate(p.getX()-step, p.getY()))/(2*step),
               (evaluate(p.getX(), p.getY()+step) - evaluate(p.getX(), p.getY()-step))/(2*step));
    }

    /**
     * Helper to compute the output
     * @param computed boolean array to keep track of done computations
     * @param index to keep track of where we are in the computation
     * @param x : x coordinate
     * @param y : y coordinate
     * @return first argument
     */
    public double helper(boolean[] computed, int index, double x, double y){
        double arg1=0;

        if(type.get(index).equals("VAR")){
            if(arguments.get(index).equals("x")) arg1 =x;
            else arg1=y;
            computed[index]=true;
        }else if(type.get(index).equals("NUM")){
            if(index>0) arg1 = Double.parseDouble(arguments.get(index-1));
            else arg1 = Double.parseDouble(arguments.get(index));
            computed[index]=true;
        }else if(type.get(index).equals("SP")){
            if(type.get(index+1).equals("FUN")){

                arg1=map.get(arguments.get(index)).compute(functions[funcounter++].evaluate(x,y),1.0);
                computed[index]=true;
                computed[index+1]=true;

            }//else{ we have sinx }

        }else if(type.get(index).equals("MIN")){
            computed[index]=true;
            if(type.get(index+1).equals("VAR")){
                if(arguments.get(index).equals("x")) arg1 =-x;
                else arg1=-y;
                computed[index+1]=true;
            }else if(type.get(index+1).equals("NUM")){
                arg1 = (-1)*Double.parseDouble(arguments.get(index-1));
                computed[index+1]=true;
            }else if(type.get(index+1).equals("SP")){
                if(type.get(index+2).equals("FUN")){
                    arg1=(-1)*map.get(arguments.get(index)).compute(functions[funcounter++].evaluate(x,y),1.0);
                    computed[index+1]=true;
                    computed[index+2]=true;

                }//else{ we have sinx }
            }
        }
        return arg1;
    }

    /**
     * Helper to compute the output
     * @param t last argument
     * @param computed boolean array to keep track of done computations
     * @param index to keep track of where we are in the computation
     * @param x : x coordinate
     * @param y : y coordinate
     * @return computed solution so far
     */
    private double helper(double t,boolean[] computed, int index, double x, double y){
        double arg=0;
        //if still in boundaries to see one ahead
        if(index<computed.length-1){
            //if index point to an operation -> get what's next to it and put it in arg
            if(type.get(index).startsWith("OP")) index++;
            if(type.get(index).equals("VAR")){
                if(arguments.get(index).equals("x")) arg =x;
                else arg=y;
                computed[index]=true;
            }else if(type.get(index).equals("NUM")){
                arg = Double.parseDouble(arguments.get(index));
                computed[index]=true;
            }else if(type.get(index).equals("SP")){
                if(type.get(index+1).equals("FUN")){
                    arg=map.get(arguments.get(index)).compute(functions[funcounter++].evaluate(x,y),1.0);
                    computed[index]=true;
                    computed[index+1]=true;

                }//else{ we have sinx }

            }else if(type.get(index).equals("MIN")){
                computed[index]=true;
                if(type.get(index+1).equals("VAR")){
                    if(arguments.get(index).equals("x")) arg =-x;
                    else arg=-y;
                    computed[index+1]=true;
                }else if(type.get(index+1).equals("NUM")){
                    arg = (-1)*Double.parseDouble(arguments.get(index-1));
                    computed[index+1]=true;
                }else if(type.get(index+1).equals("SP")){
                    if(type.get(index+2).equals("FUN")){
                        arg=(-1)*map.get(arguments.get(index)).compute(functions[funcounter++].evaluate(x,y),1.0);
                        computed[index+1]=true;
                        computed[index+2]=true;

                    }//else{ we have sinx }
                }
            }else if(type.get(index).equals("FUN")){
                arg = functions[funcounter++].evaluate(x,y);
                computed[index]=true;
            }///////////////////////COMPARE TO NEXT NON COMPUTED OP//////////////////////////////


            if (index+1>=computed.length || (index>0&&priority(type.get(index-1), type.get(index+1)))){
                if(!computed[index-1]) computed[index-1]=true;
                else computed[index]=true;
            }else{
                int next = nextFrom(computed, index+1);
                if(!computed[index+1])
                arg = helper(arg, computed, index+1, x,y);
                //because earlier if !index -> OP : index++
                if(index>0&&!computed[index-1]) computed[index-1]=true;
                else computed[index]=true;
                //next operation
                int nex = nextFrom(computed, index+1);
                while(nex!=-1&&!priority(type.get(index-1), type.get(nex))){
                    arg=helper(arg, computed, nex, x,y);
                    nex=nextFrom(computed, nex+1);
                }
            }
            /////////////////////////////////
            if(index!=0)
            return map.get(arguments.get(index-1)).compute(t, arg);
            else return arg;
        }else if(index<computed.length){
            if(type.get(index).equals("VAR")){
                if(arguments.get(index).equals("x")) arg =x;
                else arg=y;
                computed[index]=true;
            }else if(type.get(index).equals("NUM")){
                arg = Double.parseDouble(arguments.get(index-1));
                computed[index]=true;
            }
            return arg;
        }
        //We should not reach this far unless all calc are done
        return t;
    }


    /**
     * verify whether operation a is before b
     * @param a operation on the left of the variable/numerical value/...
     * @param b operation on the right
     * @return true if operation on the left is before
     */
    private  boolean priority(String a, String b){
        if(a.startsWith("OP") && b.startsWith("OP")) return a.charAt(2)>=b.charAt(2);
        if(a.equals("FUN")) return true;
        return false;
    }

    /**
     * find next argument not yet computed
     * @param compute the boolean array representing the computations states
     * @return first index of which argument has not been computed yet, -1 otherwise
     */
    private int next(boolean[] compute){
        for(int i=0; i< compute.length; i++) if(!compute[i]) return i;
        return -1;
    }

    /**
     * find next argument not yet computed from a certain index
     * @param computed the boolean array representing the computations states
     * @param i index at which to start the search
     * @returnfirst index of which argument has not been computed yet after index, -1 otherwise
     */
    private int nextFrom(boolean[] computed, int i){
        if(i<computed.length) {
            while(i<computed.length&&computed[i]) i++;
            if(i<computed.length) return i;
        }return -1;
    }

    public static String getFunction() {
        return basicfunction;
    }

    public void setFunction(String function) {

        this.function = function.toLowerCase().replace(" ","");
        transfer();


        //TODO we will have to re-render the course here also
        PuttingSimulator.getInstance().create();
    }

    public void setFunction(String function, boolean DEBUG) {

        this.function = function.toLowerCase().replace(" ","");
        transfer();

    }

    public static void main(String[] args){
        FunctionMaker fm = new FunctionMaker("(x^2+y^2)");
    }
}


interface Function{
    public double compute(double a, double b);
}