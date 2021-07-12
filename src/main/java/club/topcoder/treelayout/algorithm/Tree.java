package club.topcoder.treelayout.algorithm;

public class Tree {
    double w, h;   // Width and height.
    double x, y;   // x, y coordinate
    double prelim; // prelim is used to remember the preliminary horizontal coordinate of the node
    double mod;    // to store for each node how much its entire subtree should be moved horizontally
    double shift, change; //  Moving intermediate siblings in O(1)
    Tree tl, tr;          // Left and right thread, to make the getting the next node of a contour faster
    Tree el, er;          // Extreme left and right nodes, to make the threads up-to-date
    double msel, mser;    // Sum of modifiers at the extreme nodes.
    Tree[] c;
    int cs;     // Array of children and number of children.

    Tree(double w, double h, double y, Tree... c) {
        this.w = w;
        this.h = h;
        this.y = y;
        this.c = c;
        this.cs = c.length;
    }
}
