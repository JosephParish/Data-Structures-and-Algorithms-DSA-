import java.util.*;

public class HEAP {
  private TREE binaryTree;
  private boolean isMinHeap;

//comparitor
  
  public HEAP(TREE binaryTree, boolean isMinHeap){ // and pass a comparitor function for T
    this.binaryTree = binaryTree;
    this.isMinHeap = isMinHeap;

    //comparitor
  }

  private void sortHeap(){
    // use comparitor on binaryTree
  }

  public void addNode(NODE node){
    binaryTree.addNode(node);
    sortHeap();
  }
  public void removeNode(){
    binaryTree.removeNode(node);
    sortHeap();
  }
  public NODE getRootNode(){
    return binaryTree.getRootNode();
  }
  public boolean getIsMinHeap(){
    return isMinHeap;
  }
  public TREE getBinaryTree(){
    return binaryTree;
  }
}
