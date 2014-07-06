/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codeanalyzer;

/**
 *
 * @author susdeshp
 */

public class tokenmachine {
    private String Identifiers[];
    private final static String IFKEYWORD = "if";
    private final static String ELSEKEYWORD = "else";
    private final static String FORKEYWORD = "for";
    private final static String WHILEKEYWORD = "while";
    private final static String DOKEYWORD = "do";
    private final static int MAXIDENTIFIERS = 1000;
    private int IdentifierCount;
    
    public tokenmachine(){
        InitializeTokenDatabase();
        
    }
    
    private void InitializeTokenDatabase(){
        
        Identifiers = new String[MAXIDENTIFIERS];
        IdentifierCount = 0;
        
    }
    
}
