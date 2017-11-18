package thu.course.mds.project2.phase1;

import java.util.ArrayList;
import java.util.List;

public class PointWise_Group {
	List<Integer> list;
	
	public PointWise_Group(List<Integer> list , int node) {
		this.list = new ArrayList<Integer>(list);
		list.add(node);
	}
	
	public PointWise_Group(int k) {//根节点
		list = new ArrayList<Integer>(k);//空集合
	}
	
	public PointWise_Group(PointWise_Group g , int candidate , int k) {
		list = new ArrayList<Integer>(k);
		list.addAll(g.list);
		list.add(candidate);
	}
}
