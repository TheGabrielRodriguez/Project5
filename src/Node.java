public class Node {
    private final int g;
    private final int f;
    private final int h;
    private final Point current;
    private final Node prior;
    public Node(Point current, Node prior, int g, int h){
        this.current = current;
        this.prior = prior;
        this.g = g;
        this.h = h;
        this.f = g+h;
    }
    public Node getPrior(){
        return prior;
    }
    public Point getCurrent() {
        return current;
    }
    public int getG(){
        return g;
    }
    public int getH(){return h;}
    public int getF(){
        return f;
    }

}
