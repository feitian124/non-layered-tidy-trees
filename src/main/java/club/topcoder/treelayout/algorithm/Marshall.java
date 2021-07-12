package club.topcoder.treelayout.algorithm;

import club.topcoder.treelayout.TreeNode;

public class Marshall{
	public Tree convert(TreeNode root) {
		if(root == null) return null;
		Tree[] children = new Tree[root.children.size()];
		for(int i = 0 ; i < children.length ; i++){
			children[i] = convert(root.children.get(i));
		}
		return new Tree(root.width,root.height,root.y, children);
	}

	public void convertBack(Tree converted, TreeNode root) {
		Tree conv = converted;
		root.x = conv.x;
		for(int i = 0; i < conv.children.length ; i++){
			convertBack(conv.children[i], root.children.get(i));
		}
	}

	public void runOnConverted(Tree root) {
		Paper.layout(root);
	}
}
