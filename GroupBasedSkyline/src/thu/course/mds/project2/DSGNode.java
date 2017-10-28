package thu.course.mds.project2;

import java.util.ArrayList;
import java.util.List;

public class DSGNode {
	private int layerIndex;
	private int pointIndex;
	private List<Integer> parents;//
	private List<Integer> children;
	private int d;//dimension
	private List<Double> attributes;
	public DSGNode(){}
	public DSGNode(int pointIndex, int d, List<Double> attributes) {
		super();
		this.pointIndex = pointIndex;
		this.d = d;
		this.attributes = attributes;
		parents = new ArrayList<Integer>();
		children = new ArrayList<Integer>();
	}
	public int getLayerIndex() {
		return layerIndex;
	}
	public void setLayerIndex(int layerIndex) {
		this.layerIndex = layerIndex;
	}
	public int getPointIndex() {
		return pointIndex;
	}
	public void setPointIndex(int pointIndex) {
		this.pointIndex = pointIndex;
	}
	public List<Integer> getParents() {
		return parents;
	}
	public void setParents(List<Integer> parents) {
		this.parents = parents;
	}
	public List<Integer> getChildren() {
		return children;
	}
	public void setChildren(List<Integer> children) {
		this.children = children;
	}
	public int getD() {
		return d;
	}
	public void setD(int d) {
		this.d = d;
	}
	public List<Double> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Double> attributes) {
		this.attributes = attributes;
	}
	
	
	
}
