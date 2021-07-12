package club.topcoder.treelayout;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public final class TreeNode {

	// input
	public double width, height;
	public Vector<TreeNode> children;
	public double hgap, vgap;
	// output
	public double x,y;

	public TreeNode(double width, double height, TreeNode ... children){
		this.width = width;
		this.height = height;
		this.children = new Vector<TreeNode>();
		this.children.addAll(Arrays.asList(children));
	}

	public BoundingBox getBoundingBox(){
		BoundingBox result = new BoundingBox(0, 0);
		getBoundingBox(this,result);
		return result;
	}

	private static void getBoundingBox(TreeNode tree,BoundingBox b) {
		b.width = Math.max(b.width,tree.x + tree.width);
		b.height = Math.max(b.height,tree.y + tree.height);
		for(TreeNode child : tree.children){
			getBoundingBox(child, b);
		}
	}

	public void moveRight(double move){
		x += move;
		for(TreeNode child : children){
			child.moveRight(move);
		}
	}

	public void normalizeX(){
		double minX = getMinX();
		moveRight(-minX);
	}

	public double getMinX(){
		double res = x;
		for(TreeNode child : children){
			res = Math.min(child.getMinX(),res);
		}
		return res;
	}

	public void addGap(double hgap,double vgap){
		this.hgap += hgap;
		this.vgap += vgap;
		this.width+=2*hgap;
		this.height+=2*vgap;
		for(TreeNode child : children){
			child.addGap(hgap,vgap);
		}
	}

	public void print(){
		System.out.printf("new TreeNode(%f,%f %f,%f ",x,y,width,height);
		for(TreeNode child : children){
			System.out.printf(", ");
			child.print();
		}
		System.out.printf(")");

	}

	public void layer() {
		layer(0);
	}

	public void layer(double d){
		y = d;
		d+=height;
		for(TreeNode child : children){
			child.layer(d);
		}
	}

	public void randExpand( TreeNode t,Random r){
		t.y+=height;
		int i = r.nextInt(children.size() + 1);
		if(i == children.size()){
			children.add(t);
		} else {
			children.get(i).randExpand( t, r);
		}
	}

}
