package cs561hw2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;



public class homework {
	public static int totalDepth=0;
	
	//public static int it=0;
	//toCheckPosition, currentBoard reach empty eventually, use copy for argument plz
	
	
	//update currentBoard return new toCheckList
	public void updateBasedOnChoice(ArrayList<Integer> choice, ArrayList<Integer> currentBoard){
		//long startTime   = System.currentTimeMillis();
		int n=(int) Math.sqrt(currentBoard.size());
		
		//better update method

		
		//choices must be sorted
		for (int i:choice){
			int colIndex=i%n;

			int next=-1;
			for (int j=colIndex;j<=i;j+=n){
				int prev=next;
			    //
				
				next=currentBoard.get(j);
				currentBoard.set(j, prev);
				
			}
		}

	}
	
	public static void main(String[] args) throws IOException {  
		
		long startTime   = System.currentTimeMillis();
		
		FileReader fr = new FileReader("./src/cs561hw2/input.txt");
		BufferedReader bf = new BufferedReader(fr);
		int n=Integer.parseInt(bf.readLine());
		//necessary readLine
		int p=Integer.parseInt(bf.readLine());
		float time=Float.parseFloat(bf.readLine());
		//System.out.println(time);
		ArrayList<Integer> iniBoard=new ArrayList<Integer>();
		readMapFromFile(bf, n,iniBoard);
		bf.close();
		fr.close();
		homework hw=new homework();
		totalDepth=3;
		if (time<=60) totalDepth=2;
		if (time<=12) totalDepth=1;
		
		

		
		State ini=hw.new State(iniBoard,0);
		


		//test only: a big bug here! can not run twice 
		int ans= hw.Alpha_Beta(ini);
	//	System.out.println(totalDepth);
		//int scoreEarned=(int) hw.Alpha_Beta(ini)[0];
		int row=ans/n+1;
		char col=(char) (ans%n+1+64);
		String output= col+String.valueOf(row);
		
		
		//for test 
		//int scoreEarned= (int) Math.pow(firstLevel.get(ans).size(), 2);
		
		
		FileWriter fw = new FileWriter("./src/cs561hw2/output.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(output);
		int outputBoardIndex=0;
		for (int i=0;i<n;i++){
			bw.newLine();
			for (int j=0;j<n;j++){
				if (iniBoard.get(outputBoardIndex)==-1)
					bw.write("*");
				else
					bw.write(Integer.toString(iniBoard.get(outputBoardIndex)));
				outputBoardIndex++;
			}
		}

//		bw.newLine();
//		bw.write(Integer.toString(scoreEarned));
//		
//		long endTime   = System.currentTimeMillis();
//		long totalTime = endTime - startTime;
//		Double outputTime=((double)totalTime/(double)1000);
//		bw.newLine();
//		bw.write(Double.toString(outputTime));
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;

		
		bw.close();
		fw.close();
	
		
		
		//for test 
//		System.out.println(outputTime);
	System.out.println(totalTime+"ms");
//		System.out.println(countState);
		//System.out.println(countUpdateTime);
		//System.out.println(countGetChoiceTime);
		
	}
	
	private int Alpha_Beta(State ini){
		return (int) max_value(ini,-999999999,+999999999)[1];
		
		
		
	}
	private Object[] max_value(State cur,int alpha,int beta){
		if (cur.depth==totalDepth) return new Object[]{cur.upToDownScore,null};
		
		
		//
		//HashMap<Integer,ArrayList<Integer>> AllPossible=
		cur.getAllChoices();
		int curOPT=0;
		
		
		int v=-999999999;
		if (cur.allChoices.size()==0) return new Object[]{cur.upToDownScore,null};
		
		
		//
		Comparator<Integer> comparator = cur.new sizeComparator();
        PriorityQueue<Integer> queue = 
            new PriorityQueue<Integer>(cur.allChoices.size(), comparator);
        for (int i:cur.allChoices.keySet()){
        	queue.add(i);
        	
        }
        
		//depth -1 greedy
        if (cur.depth==totalDepth-1){
        	int toRemove=queue.remove();
        	
        	if (cur.depth==0){
    			//AllPossible.get(curOPT) correct
    			this.updateBasedOnChoice(cur.allChoices.get(toRemove), cur.currentBoard);
    			//System.out.println(AllPossible.get(curOPT));
    			return new Object[]{null,curOPT};
    		}
        	return new Object[]{cur.upToDownScore+(int) Math.pow(cur.allChoices.get(toRemove).size(), 2),null};
        }
        
        //depth!= -1 from bottom
		while (true){
			
			if (queue.size()==0) break;
			int i=queue.remove();
			//result of action
			ArrayList<Integer> nextBoard=new ArrayList<Integer>(cur.currentBoard);
			int scoreChange=(int) Math.pow(cur.allChoices.get(i).size(), 2);
			this.updateBasedOnChoice(cur.allChoices.get(i), nextBoard);
			State nextState=new State(nextBoard,cur.depth+1);
			nextState.upToDownScore=cur.upToDownScore+scoreChange;
			
			int minResult=this.min_value(nextState,alpha,beta);
			if (minResult>v){
				v=minResult;
				curOPT=i;
			}
	
			
			if (v>=beta) return new Object[]{v,null};

			if (v>alpha) alpha=v;
			
		}
		//
		if (cur.depth==0){
			//AllPossible.get(curOPT) correct
			this.updateBasedOnChoice(cur.allChoices.get(curOPT), cur.currentBoard);
			//System.out.println(AllPossible.get(curOPT));
			return new Object[]{null,curOPT};
		}
		// TODO Auto-generated method stub
		return new Object[]{v,null};
		
	}
	private int min_value(State cur, int alpha, int beta) {
		if (cur.depth==totalDepth) return cur.upToDownScore;
		
	
		//HashMap<Integer,ArrayList<Integer>> AllPossible=
		cur.getAllChoices();
		//int curOPT=0;
		
		
		int v=999999999;
		if (cur.allChoices.size()==0) return cur.upToDownScore;
		
		Comparator<Integer> comparator = cur.new sizeComparator();
        PriorityQueue<Integer> queue = 
            new PriorityQueue<Integer>(cur.allChoices.size(), comparator);
        for (int i:cur.allChoices.keySet()){
        	queue.add(i);
        }
		//always sort from max to min

//      depth -1 greedy
        if (cur.depth==totalDepth-1){
        	int toRemove=queue.remove();
        	
        	
        	return cur.upToDownScore-(int) Math.pow(cur.allChoices.get(toRemove).size(), 2);
        }
        
		
        while (true){
			
			if (queue.size()==0) break;
			int i=queue.remove();
			//result of action
			ArrayList<Integer> nextBoard=new ArrayList<Integer>(cur.currentBoard);
			int scoreChange=0-(int) Math.pow(cur.allChoices.get(i).size(), 2);
			
			this.updateBasedOnChoice(cur.allChoices.get(i), nextBoard);
			State nextState=new State(nextBoard,cur.depth+1);
			nextState.upToDownScore=cur.upToDownScore+scoreChange;
			
	
			
			int maxResult=(int) this.max_value(nextState,alpha,beta)[0];
			if (maxResult<v){
				v=maxResult;
			//	curOPT=i;
			}

			if (v<=alpha) return v;

			

			if (v<beta) beta=v;
			
		}
	
		return v;
	}

	

	private static void readMapFromFile(BufferedReader bf, int n,ArrayList<Integer> iniBoard) throws IOException {
		// TODO Auto-generated method stub
		//int tempIndex=0;
		for (int i=0;i<n;i++){
			for (int j=0;j<n;j++){
				int temp=bf.read()-48;
				if (temp<=8 && temp>=0){
					
					iniBoard.add(temp);
				}else
					iniBoard.add(-1);
					
			//	tempIndex++;
			}
			bf.readLine();
		}
	}
	
	
	
	public class State{
		//
		public class sizeComparator implements Comparator<Integer>
		{
			@Override
		    public int compare(Integer x, Integer y)
		    {
				
		        // Assume neither string is null. Real code should
		        // probably be more robust
		        // You could also just return x.length() - y.length(),
		        // which would be more efficient.
		        if (allChoices.get(x).size() > allChoices.get(y).size())
		        {
		            return -1;
		        }
		        if (allChoices.get(x).size() < allChoices.get(y).size())
		        {
		            return 1;
		        }
		        return 0;
		    }

			
		}
		
		
		 //boolean isMaxNode;
		//ArrayList<Integer> toCheckPosition;
		ArrayList<Integer> currentBoard;
		int depth;
		
		int upToDownScore;
		
		//
		HashMap<Integer,ArrayList<Integer>> allChoices;
		//int downToUpScore;
		public State(ArrayList<Integer> currentBoard, int depth){
			
			//this.isMaxNode=isMaxNode;
			//this.toCheckPosition=toCheckPosition;
			this.currentBoard=currentBoard;
			this.depth=depth;
			
			if (depth==0) this.upToDownScore=0;
		}
		
		public void getAllChoices(){
			//long startTime   = System.currentTimeMillis();
			
//			ArrayList<Integer> toCheckPositionC=new ArrayList<Integer>();
			ArrayList<Integer> currentBoardC=new ArrayList<Integer>(currentBoard);
//			for (int i=0;i<currentBoard.size();i++){
//				if (currentBoard.get(i)!=-1) toCheckPositionC.add(i);
//			}
			int n=(int) Math.sqrt(currentBoardC.size());
			Queue<Integer> temp;
			HashMap<Integer,ArrayList<Integer>>ret=new HashMap<Integer,ArrayList<Integer>>();
			int firstPosition=0;
//			while(toCheckPositionC.size()>0){
			while(true){
				if (firstPosition>=currentBoardC.size()) break;
				if (currentBoardC.get(firstPosition)==-1) {
					firstPosition++;
					continue;
				}
				temp = new LinkedList<Integer>();
				//int pick=toCheckPositionC.remove(0);
				int pick=firstPosition;
				ret.put(pick, new ArrayList<Integer>());
				int type=currentBoardC.get(pick);
				currentBoardC.set(pick, -1);
				temp.add(pick);
				while (temp.size()!=0){
					int toExpand=temp.poll();
					ret.get(pick).add(toExpand);
					//up
					int up=toExpand-n;
					//toCheckPositionC.contains(up)
					if (up>firstPosition && currentBoardC.get(up)==type){//safe to check position first
						temp.add(up);
						//toCheckPositionC.remove((Object)up);
						currentBoardC.set(up, -1);
						
					}
					//down
					int down=toExpand+n;
					if (down<currentBoardC.size() && currentBoardC.get(down)==type){//safe to check position first
						temp.add(down);
						//toCheckPositionC.remove((Object)down);
						currentBoardC.set(down, -1);
						
					}
					//left need to check border
					if (toExpand%n!=0){
						int left=toExpand-1;
						if ( currentBoardC.get(left)==type){
							temp.add(left);
						//	toCheckPositionC.remove((Object)left);
							currentBoardC.set(left, -1);
				
						}
					}	
					//right need to check border
					if (toExpand%n!=n-1){
						int right=toExpand+1;
						if (currentBoardC.get(right)==type){
							temp.add(right);
							//toCheckPositionC.remove((Object)right);
							currentBoardC.set(right, -1);
						}
					}	
					
					
				}
				//sort necessary for one by one update
				Collections.sort(ret.get(pick));
			}
		
			allChoices=ret;
			if (depth==0){
				//System.out.println(allChoices.size());
				if (allChoices.size()<=169)
					totalDepth+=1;
					
			}
				
		}
		
	}
}

