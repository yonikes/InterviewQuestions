
//useful in many questions, such as traversing in a matrix as if it was a graph
public class Pair {
    int r;
    int c;

    public Pair(int r, int c){
        this.r = r;
        this.c = c;
    }

    public String toString(){
        return "(" + r + " , " + c + ")";
    }
}
