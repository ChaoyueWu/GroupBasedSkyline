package thu.course.mds.project2.phase1;

import java.util.ArrayList;
import java.util.List;

public class ProcessResult{
	List<DSGNode> DSG; //比较小的层的点在前面，index比较小
	List<DSGNode> perfectNodeList;
	List<PointWise_Group> groupList;
	List<List<Integer>> groupArray;
	List<List<Integer>> resultIdx;
	
	public ProcessResult() {
		DSG = new ArrayList<DSGNode>();
		perfectNodeList = new ArrayList<DSGNode>();
		groupList = new ArrayList<PointWise_Group>();
		groupArray = new ArrayList<List<Integer>>();
		resultIdx = new ArrayList<List<Integer>>();
	}
}
