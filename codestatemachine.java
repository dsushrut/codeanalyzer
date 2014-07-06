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




public class codestatemachine {
    public static final int S_INSIDECODEBLOCK = 3;
    public static final int S_INSIDECOMMENT = 4;
    public static final int S_PREPROCESSORLINE = 5;

    private boolean HitAFunction;
    private boolean ItsAPreprocessorLine;
    private boolean FoundForwardSlash;
    private boolean FoundStarInsideCommentBlock;
    private boolean FoundTwoForwardSlash;
    private boolean FoundBackwardSlash;
    private boolean InsideCommentBlock;
    private int CurrentDepth;
    private int State;
    
    private javax.swing.tree.DefaultMutableTreeNode codetree;
    private javax.swing.tree.DefaultMutableTreeNode currentnodepointer;
    private StringBuffer CurrentLine;
    
    public codestatemachine() {
        
        InitializeStateMachine();
        // System.out.println("Inside  codestatemachine");
        codetree = new javax.swing.tree.DefaultMutableTreeNode("Root Of Code Tree");
        currentnodepointer = codetree;
        
    }
    
    
/*
 * This Function Evaluates the C code passed as string parameter
 * It creates codetree based on the evaluation.
 */
    
    public void EvaluateNextChar(String StrCode) {
        // System.out.println("Inside  EvaluateNextChar");
        // Reinitialize codestatemachine
        InitializeStateMachine();
        codetree = new javax.swing.tree.DefaultMutableTreeNode("Root Of Code Tree");
        currentnodepointer = codetree;

        // Iterate Through Each Character in Code
        char c;
        int Codelength = StrCode.length();
        for(int i = 0; i < Codelength; i++){
            
            
            // Get Next Character in Code
            c = StrCode.charAt(i);
            
            
            if(ItsAPreprocessorLine  && !FoundBackwardSlash){
                // comment line evaluation
                switch(c){
                    
                    case '\n' :      
                        // End of Preprocessorline
                        InitializeStateMachine();
                        break;
                    case '\\' :
                        // Preprocessor line extended to next line or next control character ignored
                        FoundBackwardSlash = true;
                        break;
                    //default :
                        
                }
            
            }else if(FoundTwoForwardSlash && !FoundBackwardSlash){
                //  comment line evaluation
                switch(c){
                    
                    case '\n' :      
                        // End of commentline
                        InitializeStateMachine();
                        break;
                    case '\\' :
                        // Preprocessor line extended to next line or next control character ignored
                        FoundBackwardSlash = true;
                        break;
                    //default :
                        
                }
            
            }else if(InsideCommentBlock){
                switch(c){
                   
                    case '*' :
                        InsideCommentBlock = false;
                        FoundStarInsideCommentBlock = true;
                        break;
                        
                }
            }else if(FoundStarInsideCommentBlock){
                switch(c){
                   
                    case '/' :
                        InsideCommentBlock = false;
                        FoundStarInsideCommentBlock = false;
                        //InitializeStateMachine();
                        break;
                    case '*' :
                        InsideCommentBlock = false;
                        FoundStarInsideCommentBlock = true;
                        break;
                    default :
                        InsideCommentBlock = true;
                        FoundStarInsideCommentBlock = false;
                        
                }
            }else if(FoundForwardSlash){
                switch(c){
                    
                    case '/' :
                        FoundTwoForwardSlash = true;
                        FoundForwardSlash = false;
                        break;
                    case '*' :
                        InsideCommentBlock = true;
                        FoundForwardSlash = false;
                       
                    //default :
                        
                }
                
            }else if(FoundBackwardSlash){
                FoundBackwardSlash = false;
            }else{
                //System.out.println(c);
                switch(c){
                    case '#' :
                        ItsAPreprocessorLine = true;
                        // System.out.println("ItsAPreprocessorLine = true");
                        break;
                    case ';' :
                        if (CurrentLine.toString().contains("for")) CurrentLine.append(c);
                        else FoundSemicolon();
                        break;
                    case '{' :
                        FoundOpenCurlyBracket();
                        break;
                    case '}' :
                        FoundClosedCurlyBracket();
                        break;
                    case '(' :
                        CurrentLine.append(c);
                        FoundOpenCurvedBracket();
                        break;
                    case ')' :
                        CurrentLine.append(c);
                        FoundClosedCurvedBracket();
                        break;
                    case '\\' :
                        FoundBackwardSlash = true;
                        break;
                    case '/' :
                        FoundForwardSlash = true;
                        break;
                    case ' ' :
                        if(CurrentLine.length()!=0) CurrentLine.append(c);
                        break;
                    case '\n' :
                        if(CurrentLine.length()!=0) CurrentLine.append(' ');
                        break;
                    default :
                        CurrentLine.append(c);
                     
                }
            
            }
        }
    }
    
/*
 * This Function returns the Codetree.
 * 
 */
    
    public javax.swing.tree.DefaultMutableTreeNode GetCodetree() {
        
        return codetree;

    }

    
/*
 * Semicolon is Found so this function adds current line to the codetree if 
 * this line had a function call or branching statement
 */
    private void FoundSemicolon() {
        // System.out.println(CurrentLine.toString());
        if(HitAFunction) {
            javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode(CurrentLine.toString());
            currentnodepointer.add(treeNode2);
        }
        InitializeStateMachine();
        
    }
    
    
/*
 * Open Curly Bracket is Found so this function adds current line to the codetree and 
 * makes the new node as the parent for nodes found further
 */
    private void FoundOpenCurlyBracket() {
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode(CurrentLine.toString());
        currentnodepointer.add(treeNode2);
        currentnodepointer = treeNode2;
        InitializeStateMachine(); 
        
    }
    
    
/*
 * Closed Curly Bracket is Found so this function  
 * makes the parent node as the current node for nodes found further
 */
    private void FoundClosedCurlyBracket() {
    
        currentnodepointer = (DefaultMutableTreeNode) currentnodepointer.getParent();
        InitializeStateMachine();
        
    }

 /*
 * Opened Curved Bracket is Found so this function  
 * marks this line as a line with a function call or branching call
 */
    private void FoundOpenCurvedBracket() {
        
        HitAFunction = true;
                
    }

/*
 * Opened Curved Bracket is Found so this function  
 * marks this line as a line with a function call or branching call
 */
    private void FoundClosedCurvedBracket() {
        
        HitAFunction = true;
        
    }

    
/*
 * Initialize all the variables that determine the state of the Line.
 * It is called at the begining of processing a line.
 */
    private void InitializeStateMachine() {
        
        HitAFunction = false;
        ItsAPreprocessorLine = false;
        FoundForwardSlash = false;
        FoundStarInsideCommentBlock = false;
        FoundTwoForwardSlash = false;
        InsideCommentBlock = false;
        FoundBackwardSlash = false;
        CurrentDepth = 0;
        CurrentLine = new StringBuffer("");
        State = 0;
        
    }
    
}

