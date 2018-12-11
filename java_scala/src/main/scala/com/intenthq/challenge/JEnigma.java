package com.intenthq.challenge;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class JEnigma {

  // We have a system to transfer information from one place to another. This system
  // involves transferring only list of digits greater than 0 (1-9). In order to decipher
  // the message encoded in the list you need to have a dictionary that will allow
  // you to do it following a set of rules:
  //    > Sample incoming message: (​1,2,3,7,3,2,3,7,2,3,4,8,9,7,8)
  //    > Sample dictionary (​23->‘N’,234->‘ ’,89->‘H’,78->‘Q’,37 ->‘A’)
  //  - Iterating from left to right, we try to match sublists to entries of the map.
  //    A sublist is a sequence of one or more contiguous entries in the original list,
  //    eg. the sublist (1, 2) would match an entry with key 12, while the sublist (3, 2, 3)
  //    would match an entry with key 323.
  //  - Whenever a sublist matches an entry of the map, it’s replaced by the entry value.
  //    When that happens, the sublist is consumed, meaning that its elements can’t be used
  //    for another match. The elements of the mapping however, can be used as many times as needed.
  //  - If there are two possible sublist matches, starting at the same point, the longest one
  //    has priority, eg 234 would have priority over 23.
  //  - If a digit does not belong to any matching sublist, it’s output as is.
  //
  // Following the above rules, the message would be: “1N73N7 HQ”
  // Check the tests for some other (simpler) examples.

  private final Map<Integer, Character> map;
  private static final Logger logger = Logger.getLogger("JEnigma2");
  private final Set<String> keySet;

  private JEnigma(final Map<Integer, Character> map) {
    this.map = map;
	this.keySet = map.keySet()
					.stream()
					.map(Object::toString)
					.collect(Collectors.toSet());
	logger.setLevel(Level.INFO);
  }

  public static JEnigma decipher(final Map<Integer, Character> map) {
    return new JEnigma(map);
  }
  
  public String messageToKey(List<Integer> message) {
	return message
			 .stream()
		     .map(Object::toString)
		     .collect(Collectors.joining(""));	  
  }
  
  /*
   * Returns a string containing the encoding of the message passed as an integer list
   * 
   * For each candidate key: 
   * 	STEP 1: Check if it is a map key ("foundKey" boolean) or a prefix of a map key ("foundKeyPrefix" boolean)
   * 	STEP 2: If there is no chance to get a longer candidate from the current starting point, consume the candidate or a valid prefix of the candidate (if any). Otherwise, try a longer candidate
   *  			There are 3 cases:
   * 			-  CASE 1: If the current substring (S) is a key, output the encoding of S.
   * 			-  CASE 2: If the current substring (S) is a not key but there exists a prefix P of S which is a key, output the encoding of P.
   * 			-  CASE 3: If the current substring (S) is a not key and there not exists a prefix P of S which is a key, output without encoding the largest prefix of S. If it is one-character long string, output it without encoding.
 
   * @param message 	The message to encode
   * @return 			The encoded message	
   *   */
  
  public String deciphe(List<Integer> message) {

	  String result = "", candidateKey= "",  encodedKey = "", mapKey = "";
	  boolean foundKeyPrefix = false, foundKey = false, consume = false;
	  int subListStart = 0, subListEnd = 1; 	//Define the start and end of the candidate key, start with the smallest size (1)
	  int lastKeyEnd = 0; 						//Defines the end of the last candidate key that has been recognized as a map key 
	  Iterator<String> iterator;
	  
	  while (subListEnd < message.size()+1) {

	  	candidateKey = messageToKey(message.subList(subListStart, subListEnd));
	  	consume = true;
	  	
	  	iterator = keySet.iterator();
	  	while (iterator.hasNext()) {
	  		
	  		mapKey = iterator.next();
	  		foundKey = mapKey.equals(candidateKey);
	  		foundKeyPrefix = mapKey.startsWith(candidateKey);
	  		
	  		if (foundKey){ 									/* CASE 1: If subList is a valid key, annotate the input position so that we can return to this point in case of not finding a larger key */
  				lastKeyEnd = subListEnd;
  				encodedKey = mapKey; 		
	  		}
  			else if (foundKeyPrefix){ 						/* The current substring is a key prefix, stop searching over the map key set because larger keys have precedence */
  				consume = (subListEnd == message.size());
  				break; 					
	  		}
	  	}
  		
		if (consume) {
			
			if (!foundKey) {

				if (lastKeyEnd != 0) { 						/* CASE 2 */
					
					subListEnd = lastKeyEnd;
					lastKeyEnd = 0;
	  			  	candidateKey = messageToKey(message.subList(subListStart, subListEnd));
	  				encodedKey = this.map.get(Integer.parseInt(candidateKey)).toString();		
				}
	  			else { 										/* CASE 3 */
	  				
	  				if (subListEnd > subListStart+1) subListEnd--;	  			
	  			  	candidateKey = messageToKey(message.subList(subListStart, subListEnd));
	  			    encodedKey = candidateKey;
	  			}
			}

  			/*
  			 * Update the output message and move to the next candidate key
  			 */
			
			result = result.concat(encodedKey);
  			subListStart = subListEnd;
  			subListEnd = subListStart+1;
		}
		else {
			subListEnd++;
		}

	  }
	
	  logger.info("Result: "+result);
	  return result;
  }

}