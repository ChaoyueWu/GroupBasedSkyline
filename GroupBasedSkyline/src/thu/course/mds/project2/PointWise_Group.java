package thu.course.mds.project2;

import java.util.ArrayList;
import java.util.List;

public class PointWise_Group {
	List<DSGNode> list;
	int tailIndex;
	int maxLayer;
	
	public PointWise_Group() {
		list = new ArrayList<DSGNode>();
		tailIndex = 0; // g0的tailIndex是0，因为所有的节点都是它的tail
		maxLayer = 0;
	}
	
	public PointWise_Group(PointWise_Group g , DSGNode node) {
		list = new ArrayList<DSGNode>();
		list.addAll(g.list);
		list.add(node);
		tailIndex = node.getPointIndex() + 1;
		if(node.getLayerIndex() > g.maxLayer) {
			maxLayer = node.getLayerIndex();
		}
		else
		{
			maxLayer = g.maxLayer;
		}
	}
}
