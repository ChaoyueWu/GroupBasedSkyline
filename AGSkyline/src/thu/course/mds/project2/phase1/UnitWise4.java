package thu.course.mds.project2.phase1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnitWise4 {
	public List<Set<Integer>> unitWiseCalculate(int k , List<DSGNode> dsg){
		List<Set<Integer>> resultIdx = new ArrayList<Set<Integer>>();
		
		List<Set<Integer>> sets = new ArrayList<Set<Integer>>();
		int Sk = dsg.size();
		for(int i = 0 ; i <= Sk - 1 ; i ++) {
			Set<Integer> set = new HashSet<Integer>(dsg.get(i).getParents());
			set.add(i);
			sets.add(set);
		}
		
		for(int i = Sk - 1 ; i >= 0 ;i --) {
			Set<Integer> set = sets.get(i);
			
			Set<Integer> glast = new HashSet<Integer>();
			glast.addAll(set);
			for(int j = i - 1 ; j >= 0 ; j --) {
				glast.addAll(sets.get(j));
			}
			if (glast.size() == k) {
				resultIdx.add(new HashSet<Integer>(glast));
				break;
			}
			else if (glast.size() < k) {
				break;
			}
			List<Set<Integer>> candiGroups = new ArrayList<Set<Integer>>();
			List<Integer> unitsIndex = new ArrayList<Integer>();
			candiGroups.add(set);
			unitsIndex.add(i);
			while(true) {
				if (candiGroups.isEmpty()) {
					break;
				}
				List<Set<Integer>> newCandiGroups = new ArrayList<Set<Integer>>();
				List<Integer> newUnitsIndex = new ArrayList<Integer>();
				int size = candiGroups.size();
				for(int j = 0 ; j < size ; j ++) {
					Set<Integer> ps = candiGroups.get(j);
					for(int m = unitsIndex.get(j) - 1 ; m >= 0 ; m --) {
						if(!ps.contains(m)) {
							Set<Integer> newSet = new HashSet<Integer>(ps);
							newSet.addAll(sets.get(m));
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
