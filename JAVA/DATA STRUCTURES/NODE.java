import java.util.*;

public class NODE<T> {
  private NDOE parent;
  private ArrayList<NODE> children;
  private T value;
  
  public NODE(NDOE parent, ArrayList<NODE> children, T value) {
    this.parent = parent;
    this.children = children;
    this.value = value;
  }

  public NODE getParent(){
    return parent;
  }
  public ArrayList<NODE> getChildren(){
    return children;
  }
  public T getValue(){
    return value;
  }
  public void addChild(NODE child) {
    children.add(child);
  }
}
