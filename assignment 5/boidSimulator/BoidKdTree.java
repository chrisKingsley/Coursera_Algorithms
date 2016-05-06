/*************************************************************************
 *  Compilation:  javac KdTree.java
 *
 *  Implementation of set of points in the plane using a 2d-tree.
 *
 *
 *************************************************************************/


public class BoidKdTree {
    private static final boolean VERTICAL   = false;
    private static final boolean HORIZONTAL = true;
    private static final double XMIN =  -1000;
    private static final double YMIN =  -1000;
    private static final double XMAX = +1000;
    private static final double YMAX = +1000;

    private Node root;         // root of 2d-tree
    private int N;             // number of points in 2d-tree
    
    // 2D tree node
    private static class Node {
        private Node lb;       // left or bottom
        private Node rt;       // right or top
        private Boid p;     // the point
        private RectHV rect;   // subdivision of plane corresponding to this node
        
        private Node(Boid p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    // return the number of points in the 2d-tree
    public int size() { return N; }

    // is the data structure empty?
    public boolean isEmpty() { return N == 0; }

    // insert the point p into the 2d-tree
    public void insert(Boid p) {
    }

    // insert the point p into the 2d-tree rooted at h
    private Node insert(Node h, Boid p, double xmin, double ymin,
                                           double xmax, double ymax,
                                           boolean orientation) {
    }

    // is the point p in the 2d-tree?
    public boolean contains(Boid p) {
    }

    // is the point p in the 2d-tree rooted at h?
    private boolean contains(Node h, Boid p, boolean orientation) {
        
    }
        

    // draw the 2d-tree to standard draw
    public void draw() {
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.square(.5, .5, .5);
        draw(root, VERTICAL);
    }

    // draw the 2d-tree to standard draw
    private void draw(Node h, boolean orientation) {

    }

    public Boid nearest(Boid p) {

    }

    public Iterable<Boid> kNearest(Boid p, int k) {
        if (root == null) return null;
        MaxPQ<BoidPQNode> nearest = new MaxPQ<BoidPQNode>();
        Boid infinitelyFar = new Boid(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        BoidPQNode bpqn = new BoidPQNode(infinitelyFar, Double.POSITIVE_INFINITY);

        for (int i = 0; i < k; i++)
            nearest.insert(bpqn);

        kNearest(root, p, nearest, VERTICAL, k);
        Queue<Boid> nearestBoids = new Queue<Boid>();
        for (BoidPQNode bn : nearest)
            nearestBoids.enqueue(bn.b());

        return nearestBoids;
    }

    // find the nearest point to point p
    private MaxPQ<BoidPQNode> kNearest(Node h, Boid p, MaxPQ<BoidPQNode> nearest, boolean orientation, int k) {

    }

    // find the nearest point to point p
    private Boid nearest(Node h, Boid p, Boid best, boolean orientation) {

    }


    // return all points that intersect this axis-aligned rectangle
    public Iterable<Boid> range(RectHV rect) {

    }

    // all points that intersect this axis-aligned rectangle in 2d-tree rooted at h
    private void range(Node h, Queue<Boid> queue, RectHV rect) {

    }
}
