import java.util.*;

public class HEAP {
  private TREE binaryTree;
  private boolean isMin;

//comparitor
  
  public HEAP(TREE binaryTree, boolean isMin){ // and pass a comparitor function for T
    this.binaryTree = binaryTree;
    this.isMinHeap = isMin;

    //comparitor
  }

  private void sortHeap(){
    // use comparitor on binaryTree
  }

  public void add(NODE node){ //perhaps change node to be a value that is then declared as a node and possibly check if it is the same type as the rest of the nodes (integer, string etc)
    binaryTree.addNode(node);
    sortHeap();
  }
  public void remove(NODE node){
    binaryTree.removeNode(node);
    sortHeap();
  }
  public NODE getRoot(){
    return binaryTree.getRootNode();
  }
  public boolean getIsMin(){
    return isMin;
  }
  public TREE getHeap(){
    return binaryTree;
  }
}
