/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codeanalyzer;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 *
 * @author susdeshp
 */
public class codestatemachine2 {
    
    public static final int S_INSIDECODEBLOCK = 3;
    public static final int S_INSIDECOMMENTBLOCK = 4;
    public static final int S_ONCOMMENTLINE = 5;
    public static final int S_PREPROCESSORLINE = 6;

    public static final int E_SEMICOLON = 3;
    public static final int E_CURVEDOPENBRACKET = 4;
    public static final int E_CURVEDCLOSEDBRACKET = 5;
    public static final int E_CURLYOPENBRACKET = 6;
    public static final int E_CURLYCLOSEDBRACKET = 7;
    public static final int E_SQUAREOPENBRACKET = 8;
    public static final int E_SQUARECLOSEDBRACKET = 9;
    public static final int E_FORWARDSLASH = 10;
    public static final int E_BACKWARDSLASH = 11;
    public static final int E_SPACE = 12;
    public static final int E_STAR = 13;
    public static final int E_RETURN = 14;
    public static final int E_HASH = 15;
    public static final int E_COLON = 16;
    public static final int E_TAB = 17;
    public static final int E_NULL = 0;
    
    private int LastEvent;
    private int CurrentState;
    private int CurrentEvent;
    private char CurrentChar;
    private javax.swing.tree.DefaultMutableTreeNode CodeTree;
    private javax.swing.tree.DefaultMutableTreeNode CurrentNodePointer;
    private StringBuffer CurrentLine;

    
    public codestatemachine2() {

        CodeTree = new javax.swing.tree.DefaultMutableTreeNode("Root Of Code Tree");
        CurrentNodePointer = CodeTree;

        CurrentState = S_INSIDECODEBLOCK;
        InitializeStateMachine();
        
    }
    
    public void EvaluateNextChar(String StrCode) {
        
        // Reinitialize codestatemachine
        CurrentState = S_INSIDECODEBLOCK;
        InitializeStateMachine();
        CodeTree = new javax.swing.tree.DefaultMutableTreeNode("Root Of Code Tree");
        CurrentNodePointer = CodeTree;
        

        // Iterate Through Each Character in Code
        int Codelength = StrCode.length();
        for(int i = 0; i < Codelength; i++){
            // Get Next Character in Code
            CurrentChar = StrCode.charAt(i);
            
            LastEvent = CurrentEvent;
            CurrentEvent = IdentifyEvent();
            ProcessCurrentEvent();
            
        }
        
    }
    
    private void ProcessCurrentEvent(){
        
        if (CurrentState == S_INSIDECODEBLOCK){
            
            switch (CurrentEvent) {
                case E_SEMICOLON:
                    if (CurrentLine.toString().contains("for")) CurrentLine.append(CurrentChar);
                    else AddNewLeafChild();
                    break;
                case E_CURLYOPENBRACKET:
                    AddNewParentChild();
                    break;
                case E_CURLYCLOSEDBRACKET:
                    GoToParent();
                    break;
                case E_FORWARDSLASH:
                    if (LastEvent == E_FORWARDSLASH) CurrentState = S_ONCOMMENTLINE;
                    else if(CurrentLine.length()!=0) CurrentLine.append(' ');
                    break;
                case E_RETURN:
                    if (LastEvent != E_RETURN && LastEvent != E_SPACE && LastEvent != E_TAB){
                        if(CurrentLine.length()!= 0 ) CurrentLine.append(' ');
                    }
                    break;
                case E_STAR:
                    if (LastEvent == E_FORWARDSLASH) CurrentState = S_INSIDECOMMENTBLOCK;
                    break;
                case E_SPACE:
                    if (LastEvent != E_RETURN && LastEvent != E_SPACE && LastEvent != E_TAB){
                        if(CurrentLine.length()!= 0 ) CurrentLine.append(' ');
                    }
                    break;
                case E_TAB:
                    if (LastEvent != E_RETURN && LastEvent != E_SPACE && LastEvent != E_TAB){
                        if(CurrentLine.length()!= 0 ) CurrentLine.append(' ');
                    }
                    break;
                case E_HASH:
                    CurrentState = S_PREPROCESSORLINE;
                    break;
                default:
                    CurrentLine.append(CurrentChar);
                    
            }
        }else if (CurrentState == S_INSIDECOMMENTBLOCK){
            switch (CurrentEvent) {
                case E_FORWARDSLASH:
                    if (LastEvent == E_STAR) CurrentState = S_INSIDECODEBLOCK;
                    break;
                default:
                    //CurrentLine.append(CurrentChar);
                    
            }
            
        }else if (CurrentState == S_ONCOMMENTLINE){
            switch (CurrentEvent) {
                case E_RETURN:
                    CurrentState = S_INSIDECODEBLOCK;
                    InitializeStateMachine();
                    break;
                default:
                    
            }
            
        }else if (CurrentState == S_PREPROCESSORLINE){
            switch (CurrentEvent) {
                case E_RETURN:
                    
                        CurrentState = S_INSIDECODEBLOCK;
                        InitializeStateMachine();
                    
                    break;
                
                    
                default:
                    
            }
            
        }
        
        
    }
    
    public javax.swing.tree.DefaultMutableTreeNode GetCodetree() {
      
        return CodeTree;
    }
    
    private int IdentifyEvent() {
        int myevent;
        switch(CurrentChar){
                    
                    case '\n' :
                        myevent = E_RETURN;
                        break;
                    case '*' :
                        myevent = E_STAR;
                        break;
                    case '\\' :
                        myevent = E_BACKWARDSLASH;
                        break;
                    case '#' :
                        myevent = E_HASH;
                        break;
                    case ';' :
                        myevent = E_SEMICOLON;
                        break;
                    case ':' :
                        myevent = E_COLON;
                        break;
                    case '{' :
                        myevent = E_CURLYOPENBRACKET;
                        break;
                    case '}' :
                        myevent = E_CURLYCLOSEDBRACKET;
                        break;
                    case '(' :
                        myevent = E_CURVEDOPENBRACKET;
                        break;
                    case ')' :
                        myevent = E_CURVEDCLOSEDBRACKET;
                        break;
                    case '/' :
                        myevent = E_FORWARDSLASH;
                        break;
                    case ' ' :
                        myevent = E_SPACE;
                        break;
                    case '\t':
                        myevent = E_TAB;
                        break;
                    default :
                        myevent = E_NULL;
                        
                }
        
        return myevent;
    } 
    
    
 /*
 * Initialize all the variables that determine the state of the Line.
 * It is called at the begining of processing a line.
 */
    private void InitializeStateMachine() {
        
        CurrentLine = new StringBuffer("");
        LastEvent = E_NULL;
        
    }
    
    private void AddNewLeafChild() {
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode(CurrentLine.toString());
        CurrentNodePointer.add(treeNode2);
        InitializeStateMachine();
    }
    
    private void AddNewParentChild() {
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode(CurrentLine.toString());
        CurrentNodePointer.add(treeNode2);
        CurrentNodePointer = treeNode2;
        InitializeStateMachine();
    }
    
    private void GoToParent() {
        if(CurrentLine.length()!= 0 ){
            javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode(CurrentLine.toString());
            CurrentNodePointer.add(treeNode2);
        }
        CurrentNodePointer = (DefaultMutableTreeNode) CurrentNodePointer.getParent();
        InitializeStateMachine();
    }
    
}
