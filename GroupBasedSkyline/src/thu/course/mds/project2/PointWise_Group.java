package thu.course.mds.project2;

import java.util.ArrayList;
import java.util.List;

public class PointWise_Group {
	List<DSGNode> list;
	int tailIndex; // 第一个tail的index,由0~Sk-1
	int maxLayer;// 所有的节点中的最大层数，层数由1~k
	
	public PointWise_Group() {//根节点
		list = new ArrayList<DSGNode>();//空集合
		tailIndex = 0; // g0的tailIndex是0，因为所有的节点都是它的tail
		maxLayer = 0;//只允许层数为1的节点加入空集合
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
