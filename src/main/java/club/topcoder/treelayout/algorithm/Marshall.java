package club.topcoder.treelayout.algorithm;



import club.topcoder.treelayout.TreeNode;
import club.topcoder.treelayout.algorithm.Paper.Tree;

public class Marshall{




	public Object convert(TreeNode root) {
		if(root == null) return null;
		Tree[] children = new Tree[root.children.size()];
		for(int i = 0 ; i < children.length ; i++){
			children[i] = (Tree)convert(root.children.get(i));
		}
		return new Tree(root.width,root.height,root.y, children);
	}


	public void convertBack(Object converted, TreeNode root) {
		Tree conv = (Tree)converted;
		root.x = conv.x;
		for(int i = 0 ; i < conv.c.length ; i++){
			convertBack(conv.c[i], root.children.get(i));
		}

	}

	public void runOnConverted(Object root) {
		Paper.layout((Tree)root);
	}
}
