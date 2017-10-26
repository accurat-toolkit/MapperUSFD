package shef.ac.uk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class CognateSim {

	public static String longestCommanSubsequence(String a, String b) {
	    int[][] lengths = new int[a.length()+1][b.length()+1];
	 
	    // row 0 and column 0 are initialized to 0 already
	 
	    for (int i = 0; i < a.length(); i++)
	        for (int j = 0; j < b.length(); j++)
	            if (a.charAt(i) == b.charAt(j))
	                lengths[i+1][j+1] = lengths[i][j] + 1;
	            else
	                lengths[i+1][j+1] =
	                    Math.max(lengths[i+1][j], lengths[i][j+1]);
	 
	    // read the substring out from the matrix
	    StringBuffer sb = new StringBuffer();
	    for (int x = a.length(), y = b.length();
	         x != 0 && y != 0; ) {
	        if (lengths[x][y] == lengths[x-1][y])
	            x--;
	        else if (lengths[x][y] == lengths[x][y-1])
	            y--;
	        else {
	            assert a.charAt(x-1) == b.charAt(y-1);
	            sb.append(a.charAt(x-1));
	            x--;
	            y--;
	        }
	    }
	 
	    return sb.reverse().toString();
	}
	
	public static String longestCommonSubstring(String first, String second) {
		          
		     String tmp = "";
		     String max = "";
		                     
		     for (int i=0; i < first.length(); i++){
		         for (int j=0; j < second.length(); j++){
		             for (int k=1; (k+i) <= first.length() && (k+j) <= second.length(); k++){
		                                      
		                 if (first.substring(i, k+i).equals(second.substring(j, k+j))){
		                     tmp = first.substring(i,k+i);
		                 }
		                 else{
		                     if (tmp.length() > max.length())
		                         max=tmp;
		                     tmp="";
		                 }
		             }
		                 if (tmp.length() > max.length())
		                         max=tmp;
		                 tmp="";
		         }
		     }
		             
		     return max;       
		             
		}
	
	
	public static int getLevenshteinDistance (String s, String t) {
		  if (s == null || t == null) {
		    throw new IllegalArgumentException("Strings must not be null");
		  }
								
		  int n = s.length(); // length of s
		  int m = t.length(); // length of t
				
		  if (n == 0) {
		    return m;
		  } else if (m == 0) {
		    return n;
		  }

		  int p[] = new int[n+1]; //'previous' cost array, horizontally
		  int d[] = new int[n+1]; // cost array, horizontally
		  int _d[]; //placeholder to assist in swapping p and d

		  // indexes into strings s and t
		  int i; // iterates through s
		  int j; // iterates through t

		  char t_j; // jth character of t

		  int cost; // cost

		  for (i = 0; i<=n; i++) {
		     p[i] = i;
		  }
				
		  for (j = 1; j<=m; j++) {
		     t_j = t.charAt(j-1);
		     d[0] = j;
				
		     for (i=1; i<=n; i++) {
		        cost = s.charAt(i-1)==t_j ? 0 : 1;
		        // minimum of cell to the left+1, to the top+1, diagonally left and up +cost				
		        d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);  
		     }

		     // copy current distance counts to 'previous row' distance counts
		     _d = p;
		     p = d;
		     d = _d;
		  } 
				
		  // our last action in the above loop was to switch d and p, so p now 
		  // actually has the most recent cost counts
		  return p[n];
		}

	public static double getCosineSim(Vector<String> aListLeft, Vector<String> aListRight) {
		if (aListLeft.size() == 0 || aListRight.size() == 0) {
			return 0;
		}
		Vector<String> aListLeftCopyLowerCase = new Vector<String>();
		Map<String, Double> leftCount = new HashMap<String, Double>();
		Map<String, Double> rightCount = new HashMap<String, Double>();
		Map<String, Double> intersection = new HashMap<String, Double>();

		for (int k = 0; k < aListLeft.size(); k++) {
			aListLeftCopyLowerCase.add(aListLeft.get(k).toLowerCase());
			Double count = leftCount.get(aListLeft.get(k).toLowerCase());
			if (count == null) {
				count = new Double(0);
			}
			count = new Double (count.doubleValue() + 1);
			leftCount.put(aListLeft.get(k).toLowerCase(), count);
		}
		
		for (int k = 0; k < aListRight.size(); k++) {
			if (aListLeftCopyLowerCase.contains(aListRight.get(k).toLowerCase())) {
				Double count = intersection.get(aListRight.get(k).toLowerCase());
				if (count == null) {
					count = new Double(0);
				}
				count = new Double (count.doubleValue() + 1);
				intersection.put(aListRight.get(k).toLowerCase(), count);
			}
			Double count = rightCount.get(aListRight.get(k).toLowerCase());
			if (count == null) {
				count = new Double(0);
			}
			count = new Double (count.doubleValue() + 1);
			rightCount.put(aListRight.get(k).toLowerCase(), count);

		}
		
		double numerator = 0;
		Iterator<String> it = intersection.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			double value1 = leftCount.get(key);
			double value2 = rightCount.get(key);
			numerator += value1*value2;
		}
		
		double denominator1 = 0;
		it = leftCount.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			double value = leftCount.get(key);
			denominator1 += value*value;
		}
		double denominator2 = 0;
		it = rightCount.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			double value = rightCount.get(key);
			denominator2 += value*value;
		}

		
		return numerator / (Math.sqrt(denominator1) * Math.sqrt(denominator2));
	}
	
    public static void main(String[] args) {    	
    	System.out.println(longestCommonSubstring("abcde", "abscfe"));
    	System.out.println(getLevenshteinDistance("abcde", "abscfe"));
    	String x = "abcde";
        String y = "abscfe";
        int M = x.length();
        int N = y.length();

        // opt[i][j] = length of LCS of x[i..M] and y[j..N]
        int[][] opt = new int[M+1][N+1];

        // compute length of LCS and all subproblems via dynamic programming
        for (int i = M-1; i >= 0; i--) {
            for (int j = N-1; j >= 0; j--) {
                if (x.charAt(i) == y.charAt(j))
                    opt[i][j] = opt[i+1][j+1] + 1;
                else 
                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
            }
        }

        // recover LCS itself and print it to standard output
        int i = 0, j = 0;
        while(i < M && j < N) {
            if (x.charAt(i) == y.charAt(j)) {
                System.out.print(x.charAt(i));
                i++;
                j++;
            }
            else if (opt[i+1][j] >= opt[i][j+1]) i++;
            else                                 j++;
        }
        System.out.println();

    }

}
