package thu.course.mds.project2.phase1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class PointWise {
	int pointWiseCalculate(int k , ProcessResult dsg){
		List<DSGNode> DSG = dsg.DSG;
		List<PointWise_Group> groupListNew = dsg.groupList;
		
		int Sk = DSG.size();
		
		int[] children = new int[Sk];
		int[] tailList = new int[Sk];
		for(int i = 0 ; i < Sk ; i ++) {
			children[i] = 0;
			tailList[i] = 0;
		}
		
		Set<Integer> set1 = new HashSet<Integer>();
		List<PointWise_Group> groupList = new ArrayList<PointWise_Group>();
		PointWise_Group g0 = new PointWise_Group(k);
		groupList.add(g0);
		int start = 0;
		int count = 1; // count代表第i-1层的节点数目
		for(int i = 1 ; i <= k ; i ++) {
			int tmpCount = 0; //记录第i层的节点数目
			
			//遍历第i-1层的每个group
			for(int j = start ; j < count ; j ++) {
//				System.out.println(j);
				PointWise_Group g = groupList.get(j);
				List<Integer> list = g.list;
				
//				long startTime = System.nanoTime();//毫微秒
				for(int nodeIndex : list) {
					List<Integer> childrenList = DSG.get(nodeIndex).getChildren();
					for(int child : childrenList) {
						children[child] = 1;
					}
				}
//				long endTime = System.nanoTime();//毫微秒
//				System.out.println("get children shuzu"+(endTime - startTime));
				
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
					set1.addAll(g.list);
					set1.addAll(DSG.get(candidate).getParents());
					set1.add(candidate);
					if(set1.size() == i) {
						PointWise_Group gNew = new PointWise_Group(g ,candidate,k);
						if(i == k) {
							groupListNew.add(gNew);
						}
						else
						{
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
			start = count;
			count = tmpCount + count;
		}
		return (groupListNew.size() + dsg.perfectNodeList.size());
	}
}
