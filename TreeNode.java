
//to represent a BST - used in 'serializeTree' question
public class TreeNode {

    public int value;
    public TreeNode right;
    public TreeNode left;

    public TreeNode(int value){
        this.value = value;
        this.left = null;
        this.right = null;
    }

    //add a new node to the tree rooted at this Node. Assume no duplicate values
    public void add(int val){

        //add to the left
        if (val < this.value){
            if (this.left == null)
                this.left = new TreeNode(val);
            else
                this.left.add(val);
        }
        else{
            if (this.right == null)
                this.right = new TreeNode(val);
            else
                this.right.add(val);
        }
    }

    //returns a string representation of the tree rooted at the node
    public String preOrder(){

        String ans = "";

        ans += this.value;

        if (this.left != null)
            ans += (" " + this.left.preOrder());

        if (this.right != null)
            ans += (" " + this.right.preOrder());

        return ans;
    }
}
