package thu.course.mds.project2.phase1;

import java.util.HashSet;
import java.util.Set;

public class Unit {
	int pointIndex;
	Set<Integer> unitPoints;
	
	public Unit(DSGNode node) {
		this.pointIndex = node.getPointIndex();
		unitPoints = new HashSet<Integer>();
		unitPoints.addAll(node.getParents());
		unitPoints.add(node.getPointIndex());
	}
}
