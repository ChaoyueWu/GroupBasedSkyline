package thu.course.mds.project2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class PointWise {
	List<PointWise_Group> pointWiseCalculate(int k , int Sk , DSG dsg){
		List<PointWise_Group> groupList = new ArrayList<PointWise_Group>();
		PointWise_Group g0 = new PointWise_Group();
		groupList.add(g0);
		int count = 1; // count代表第i-1层的节点数目
		for(int i = 1 ; i <= k ; i ++) {
			int tmpCount = 0; //记录第i层的节点数目
			//遍历第i-1层的每个group
			for(int j = 0 ; j < count ; j ++) {
				PointWise_Group g = groupList.get(0);
				groupList.remove(0);
				List<DSGNode> list = g.list; // list是group的node list
				
				Set<Integer> childrenSet = new HashSet<Integer>();//获取group的childrenSet
				for(DSGNode node : list) {
					childrenSet.addAll(node.getChildren());
				}
				
				ArrayList<Integer> tailList = new ArrayList<Integer>();
				for(int t = g.tailIndex ; t < Sk ; t ++) {
					//g的tail Set从 tailIndex 开始
					DSGNode node = dsg.DSG.get(t);
					if(!childrenSet.contains(t) && !isSkylinePoint(node)) {
						//如果节点没有在子节点集合中并且不是skyline节点，则不加入
						continue;
					}
					else if(node.getLayerIndex() - g.maxLayer >= 2) {
						//如果节点的layer比最大layer大及2层以上，不加入
						continue;
					}
					else
					{
						tailList.add(t);
					}
				}
				for(int candidate : tailList) {
					PointWise_Group gNew = new PointWise_Group(g,dsg.DSG.get(candidate));
					if(isSkylineGroup(gNew , i)) {
						groupList.add(gNew);
						tmpCount ++;
					}
					else
					{
						continue;
					}
				}
			}
			count = tmpCount;
		}
		return groupList;
	}
	boolean isSkylinePoint(DSGNode node) {
		return node.getLayerIndex() == 1;
	}
	boolean isSkylineGroup(PointWise_Group g , int k) {
		Set<Integer> set = new HashSet<Integer>();
		List<DSGNode> list = g.list;
		for(DSGNode n : list) {
			set.addAll(n.getParents());
			set.add(n.getPointIndex());
		}
		if(set.size() == k) {
			return true;
		}
		else
		{
			return false;
		}
	}
}
