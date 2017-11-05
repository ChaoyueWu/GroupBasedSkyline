package thu.course.mds.project2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnitWise3 {
	public List<Set<Integer>> unitWiseCalculate(int k , List<DSGNode> dsg){
		List<Set<Integer>> resultIdx = new ArrayList<Set<Integer>>();
		List<Unit> units = new ArrayList<Unit>();
		
		int Sk = dsg.size();
		for(int i = 0 ; i <= Sk - 1 ; i ++) {
			Unit unit = new Unit(dsg.get(i));
			units.add(unit);
		}
		
		for(int i = Sk - 1 ; i >= 0 ;i --) {
			Unit unit = units.get(i);
			long startTime = System.nanoTime();//毫微秒
			Set<Integer> glast = new HashSet<Integer>();
			glast.addAll(unit.unitPoints);
			for(int j = i - 1 ; j >= 0 ; j --) {
				glast.addAll(units.get(j).unitPoints);
			}
			long endTine = System.nanoTime();//毫微秒
			System.out.println("glast time : " +(endTine - startTime)/1000);
			
			if (glast.size() == k) {
				resultIdx.add(new HashSet<Integer>(glast));
				break;
			}
			else if (glast.size() < k) {
				break;
			}
			
			List<Set<Integer>> candiGroups = new ArrayList<Set<Integer>>();
			List<Integer> unitsIndex = new ArrayList<Integer>();
			candiGroups.add(unit.unitPoints);
			unitsIndex.add(unit.pointIndex);
//			System.out.println("first level:"+i);
			while(true) {
				if (candiGroups.isEmpty()) {
					break;
				}
				List<Set<Integer>> newCandiGroups = new ArrayList<Set<Integer>>();
				List<Integer> newUnitsIndex = new ArrayList<Integer>();
				int size = candiGroups.size();
//				System.out.println("candidate size:"+size);
				for(int j = 0 ; j < size ; j ++) {
					Set<Integer> ps = candiGroups.get(j);
					for(int m = unitsIndex.get(j) - 1 ; m >= 0 ; m --) {
						if(!ps.contains(m)) {
							Set<Integer> newSet = new HashSet<Integer>(ps);
							newSet.addAll(units.get(m).unitPoints);
							if(newSet.size() == k) {
								resultIdx.add(newSet);
							}
							else if(newSet.size() < k) {
								newCandiGroups.add(newSet);
								newUnitsIndex.add(m);
							}
						}
					}
				}
				candiGroups = newCandiGroups;
				unitsIndex = newUnitsIndex;
			}
		}
		return resultIdx;
	}
}
