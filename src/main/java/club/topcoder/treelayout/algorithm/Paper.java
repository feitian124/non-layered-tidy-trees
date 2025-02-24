package club.topcoder.treelayout.algorithm;

/* The extended Reingold-Tilford algorithm as described in the paper
 * "Drawing Non-layered Tidy Trees in Linear Time" by Atze van der Ploeg
 * Accepted for publication in Software: Practice and Experience, to Appear.
 *
 * This code is in the public domain, use it any way you wish. A reference to the paper is
 * appreciated!
 */

public class Paper {


    static void layout(Tree t) {
        firstWalk(t);
        secondWalk(t, 0);
    }

	/**
	 * foreach Each child of root do
	 * 		Layout(child);
	 * 		Separate((left siblings,child)) ;
	 * 	Set position of root;
	 */
	static void firstWalk(Tree t) {
        if (t.cs == 0) {
            setExtremes(t);
            return;
        }

        firstWalk(t.children[0]);
		double minY = bottom(t.children[0].er);
        IYL ih = updateIYL(minY, 0, null);

        for (int i = 1; i < t.cs; i++) {
            firstWalk(t.children[i]);
            // Store lowest vertical coordinate while extreme nodes still point in current subtree
            minY = bottom(t.children[i].er);
            seperate(t, i, ih);
            ih = updateIYL(minY, i, ih);
        }
        positionRoot(t);
        setExtremes(t);
    }

    static void setExtremes(Tree t) {
        if (t.cs == 0) {
            t.el = t;
            t.er = t;
            t.msel = t.mser = 0;
        } else {
            t.el = t.children[0].el;
            t.msel = t.children[0].msel;
            t.er = t.children[t.cs - 1].er;
            t.mser = t.children[t.cs - 1].mser;
        }
    }

	/**
	 * current subtree is moved to the right such that it does not overlap with its left siblings.
	 */
    static void seperate(Tree t, int i, IYL ih) {
        // Right contour node of left siblings and its sum of modfiers
        Tree left = t.children[i - 1];
        double leftMod = left.mod;
        // Left contour node of current subtree and its sum of modfiers
        Tree right = t.children[i];
        double rightMod = right.mod;
        while (left != null && right != null) {
            if (bottom(left) > ih.lowY) {
                ih = ih.nxt;
            }
            // How far to the left of the right side of left is the left side of current?
            double dist = (leftMod + left.prelim + left.width) - (rightMod + right.prelim);
            if (dist > 0) {
                rightMod += dist;
                moveSubtree(t, i, ih.index, dist);
            }
            double leftY = bottom(left), rightY = bottom(right);
            // Advance highest node(s) and sum(s) of modifiers
            if (leftY <= rightY) {
                left = nextRightContour(left);
                if (left != null) leftMod += left.mod;
            }
            if (leftY >= rightY) {
                right = nextLeftContour(right);
                if (right != null) rightMod += right.mod;
            }
        }
        // Set threads and update extreme nodes.
        // In the first case, the current subtree must be taller than the left siblings.
        if (left == null && right != null) setLeftThread(t, i, right, rightMod);
            // In this case, the left siblings must be taller than the current subtree.
        else if (left != null && right == null) setRightThread(t, i, left, leftMod);
    }

    static void moveSubtree(Tree t, int i, int si, double dist) {
        // Move subtree by changing mod.
        t.children[i].mod += dist;
        t.children[i].msel += dist;
        t.children[i].mser += dist;
        distributeExtra(t, i, si, dist);
    }

    static Tree nextLeftContour(Tree t) {
        return t.cs == 0 ? t.tl : t.children[0];
    }

    static Tree nextRightContour(Tree t) {
        return t.cs == 0 ? t.tr : t.children[t.cs - 1];
    }

    static double bottom(Tree t) {
        return t.y + t.height;
    }

    static void setLeftThread(Tree t, int i, Tree cl, double modsumcl) {
        Tree li = t.children[0].el;
        li.tl = cl;
        // Change mod so that the sum of modifier after following thread is correct.
        double diff = (modsumcl - cl.mod) - t.children[0].msel;
        li.mod += diff;
        // Change preliminary x coordinate so that the node does not move.
        li.prelim -= diff;
        // Update extreme node and its sum of modifiers.
        t.children[0].el = t.children[i].el;
        t.children[0].msel = t.children[i].msel;
    }

    // Symmetrical to setLeftThread.
    static void setRightThread(Tree t, int i, Tree sr, double modsumsr) {
        Tree ri = t.children[i].er;
        ri.tr = sr;
        double diff = (modsumsr - sr.mod) - t.children[i].mser;
        ri.mod += diff;
        ri.prelim -= diff;
        t.children[i].er = t.children[i - 1].er;
        t.children[i].mser = t.children[i - 1].mser;
    }

    static void positionRoot(Tree t) {
        // Position root between children, taking into account their mod.
        t.prelim = (t.children[0].prelim + t.children[0].mod + t.children[t.cs - 1].mod +
                t.children[t.cs - 1].prelim + t.children[t.cs - 1].width) / 2 - t.width / 2;
    }

    static void secondWalk(Tree t, double modsum) {
        modsum += t.mod;
        // Set absolute (non-relative) horizontal coordinate.
        t.x = t.prelim + modsum;
        addChildSpacing(t);
        for (int i = 0; i < t.cs; i++) secondWalk(t.children[i], modsum);
    }

    static void distributeExtra(Tree t, int i, int si, double dist) {
        // Are there intermediate children?
        if (si != i - 1) {
            double nr = i - si;
            t.children[si + 1].shift += dist / nr;
            t.children[i].shift -= dist / nr;
            t.children[i].change -= dist - dist / nr;
        }
    }

    // Process change and shift to add intermediate spacing to mod.
    static void addChildSpacing(Tree t) {
        double d = 0, modsumdelta = 0;
        for (int i = 0; i < t.cs; i++) {
            d += t.children[i].shift;
            modsumdelta += d + t.children[i].change;
            t.children[i].mod += modsumdelta;
        }
    }

    // A linked list of the indexes of left siblings and their lowest vertical coordinate.
    static class IYL {
        double lowY;
        int index;
        IYL nxt;

        public IYL(double lowY, int index, IYL nxt) {
            this.lowY = lowY;
            this.index = index;
            this.nxt = nxt;
        }
    }

    static IYL updateIYL(double minY, int i, IYL ih) {
        // Remove siblings that are hidden by the new subtree
        while (ih != null && minY >= ih.lowY) {
            ih = ih.nxt;
        }
        // Prepend the new subtree
        return new IYL(minY, i, ih);
    }
}
