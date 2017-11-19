package fornba;

import java.util.ArrayList;
import java.util.List;

public class ProcessResult{
	List<DSGNode> DSG; //比较小的层的点在前面，index比较小
	List<DSGNode> perfectNodeList;
	List<List<Integer>> groupArray;
	List<List<Integer>> resultIdx;
	long perfectNum = 0;
	
	public ProcessResult() {
		DSG = new ArrayList<DSGNode>();
		perfectNodeList = new ArrayList<DSGNode>();
		groupArray = new ArrayList<List<Integer>>();
		resultIdx = new ArrayList<List<Integer>>();
	}
}
