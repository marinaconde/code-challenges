package com.intenthq.challenge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class JConnectedGraph {
	
  // Find if two nodes in a directed graph are connected.
  // Based on http://www.codewars.com/kata/53897d3187c26d42ac00040d
  // For example:
  // a -+-> b -> c -> e
  //    |
  //    +-> d
  // run(a, a) == true
  // run(a, b) == true
  // run(a, c) == true
  // run(b, d) == false

  static final Logger logger = Logger.getLogger("JConnectedGraph2");
	
  public static boolean run(JNode source, JNode target) {
	    List<JNode> visitedNodes = new ArrayList<JNode>();
		return run(source, target, visitedNodes);
  }
  
  public static boolean run(JNode source, JNode target, List<JNode> visitedNodes) {
	 
	  if (visitedNodes.contains(source)) {
		  logger.info(" Visiting node "+source.value +" , DISCARDED because it has already been visited");
		  return false;
	  }

	  visitedNodes.add(source);
	  
	  String log = "";
	  log += "Visiting node "+source.value;
	  if (source == target) {
		  log += ", it is end node and it is the TARGET node";
		  return true;
	  }
 		  
	  if (source.edges.size()>0)
		  log +=", it has "+source.edges.size()+" children";
	  else
		  log +=", DISCARDED, it is NOT the TARGET node and it does not have children";
  
	  
	  boolean found = false;  
	  for (JNode node : source.edges) {
		  found = run(node,target, visitedNodes);
		  if (found) break;
	  }	  
  
	  logger.info(log);
	  return found;
  }

  public static class JNode {
    
	public final int value;
    public final List<JNode> edges;
    
    public JNode(final int value, final List<JNode> edges) {
      this.value = value;
      this.edges = edges;
    }
    public JNode(final int value) {
      this.value = value;
      this.edges = Collections.emptyList();
    }
    
  }
}