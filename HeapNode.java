
//used in question 17.8 shortest supersequence
public class HeapNode implements Comparable {

    public int list_id;
    public int index_in_list;

    public HeapNode(int list_id, int index_in_list){
        this.list_id  = list_id;
        this.index_in_list = index_in_list;
    }

    public int compareTo(Object other){
        HeapNode other_node = (HeapNode)other;
        return this.index_in_list - other_node.index_in_list;
    }
}
