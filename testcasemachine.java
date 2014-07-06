/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package codeanalyzer;
import javax.swing.tree.*;


/**
 *
 * @author susdeshp
 */
public class testcasemachine {
    
    private StringBuffer testcases;
    private String Dummystr2;
    public testcasemachine(){
 
        testcases = new StringBuffer("");
        
    }
    
    public StringBuffer gettestcases(DefaultMutableTreeNode codetree, StringBuffer path2){

        StringBuffer Dummy,x;
        String y = path2.toString();
        StringBuffer path = new StringBuffer(y);
        
        
        String Dummystr;
        TreeNode tempNode;
        int childcount = codetree.getChildCount();
        
        for (int i = 0; i < childcount; i++){
            tempNode = codetree.getChildAt(i);
            if(tempNode.isLeaf()){
                Dummystr = path + "->" + tempNode.toString();
                //testcases = testcases.append(Dummystr);
                //testcases = testcases.append("\n");
            }
            else {
                
                y = path.toString();
                Dummy = new StringBuffer(y);
                Dummy = Dummy.append("->");
                
                Dummy = Dummy.append(tempNode.toString());
                testcases = testcases.append(Dummy);
                testcases = testcases.append("\n");

                
                x = gettestcases((DefaultMutableTreeNode)tempNode, Dummy);
            }
            
        }
                
        
        return testcases;
        
    }
    
}
    
