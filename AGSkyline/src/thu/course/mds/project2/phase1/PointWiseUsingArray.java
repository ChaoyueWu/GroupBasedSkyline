package thu.course.mds.project2.phase1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PointWiseUsingArray {
	long pointWiseCalculate(int k , ProcessResult dsg) {
		List<DSGNode> DSG = dsg.DSG;
		List<List<Integer>> groupListNew = dsg.groupArray;
		
		int Sk = DSG.size();
		
		int[] children = new int[Sk];
		int[] tailList = new int[Sk];
		for(int i = 0 ; i < Sk ; i ++) {
			children[i] = 0;
			tailList[i] = 0;
		}
		
		Set<Integer> set1 = new HashSet<Integer>();
		List<List<Integer>> groupList = new ArrayList<List<Integer>>();
		List<Integer> g0 = new ArrayList<Integer>(k);
		groupList.add(g0);
		long start = 0;
		long count = 1; // count代表第i-1层的节点数目
		long finalCount = 1;
		for(int i = 1 ; i <= k ; i ++) {
			long tmpCount = 0; //记录第i层的节点数目
			//遍历第i-1层的每个group
			for(long j = start ; j < count ; j ++) {
				List<Integer> list = groupList.get((int)j);
				for(int nodeIndex : list) {
					List<Integer> childrenList = DSG.get(nodeIndex).getChildren();
					for(int child : childrenList) {
						children[child] = 1;
					}
				}
				int maxLayer;
				int tailIndex;
				if(list.size() == 0) {
					maxLayer = 0;
					tailIndex = 0;
				}
				else
				{
					DSGNode lastNode = DSG.get(list.get(list.size() - 1));
					maxLayer = lastNode.getLayerIndex();
					tailIndex = lastNode.getPointIndex() + 1;
				}
				for(int t = tailIndex ; t < Sk ; t++) {
					//g的tail Set从 tailIndex 开始
					DSGNode node = DSG.get(t);
					if(children[t] == 0 && !(node.getLayerIndex() == 1)) {
						//如果节点没有在子节点集合中并且不是skyline节点，则不加入
						continue;
					}
					else if(node.getLayerIndex() - maxLayer >= 2) {
						//如果节点的layer比最大layer大及2层以上，不加入
						break;
					}
					else
					{
						tailList[t] = 1;
					}
				}
				for(int candidate = 0 ; candidate < Sk ; candidate ++) {
					if(tailList[candidate] == 0)continue;
					set1.addAll(list);
					set1.addAll(DSG.get(candidate).getParents());
					set1.add(candidate);
					if(set1.size() == i) {
						if(i == k) {
//							groupListNew.add(gNew);
						}
						else
						{
							List<Integer> gNew = new ArrayList<Integer>(k);
							gNew.addAll(list);
							gNew.add(candidate);
							groupList.add(gNew);
						}
						tmpCount ++;
						set1.clear();
					}
					else {
						set1.clear();
						continue;
					}
				}
				for(int m = 0 ; m < Sk ; m ++) {
					children[m] = 0;
					tailList[m] = 0;
				}
			}
//			System.out.println("tmpCount: "+ tmpCount);
			start = count;
			finalCount = tmpCount;
			count = tmpCount + count;
		}
		return (finalCount + dsg.perfectNodeList.size());
	}
}
