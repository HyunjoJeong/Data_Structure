import java.io.*;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Main {
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Hash Table=new Hash();
		LinkedList<String> Text=new LinkedList<>();
		int student_id=0;

		while (true)
		{
			try
			{
				StringTokenizer st = new StringTokenizer(br.readLine());
				String option = st.nextToken();
				option = option.toUpperCase();

				if(option.equals("Q"))
				{
					break;
				}
				else if(option.equals("I")) {
					String sequence = st.nextToken("\n").toUpperCase().trim();
					student_id++;
					Table.insertSequence(sequence, student_id);
					Text.add(sequence);
				}
				else if(option.equals("S"))
				{
					String pattern = st.nextToken("\n").toUpperCase().trim();
					Table.retrievalPattern(pattern, Text);
				}
				else if(option.equals("P")) {
					String strHashValue = st.nextToken().trim();
					int HashValue = Integer.parseInt(strHashValue);
					Table.getAVLTree(HashValue).PrintByHashValue(HashValue);
				}
			}
			catch (IOException e)
			{
				System.out.println("Wrong input : " + e.toString());
			}
		}
	}
}

class Hash{
	private int TableSize=0;
	private AVLTree AVLTrees[] = null;

	public Hash(){
		TableSize=64;
		AVLTrees=new AVLTree[64];

		for(int i=0; i<64; i++){
			AVLTrees[i]=new AVLTree(i);
		}
	}
	public int CalcHashValue(String HashValueStr){
		HashValueStr = HashValueStr.replace("A", "1");
		HashValueStr = HashValueStr.replace("T", "2");
		HashValueStr = HashValueStr.replace("G", "3");
		HashValueStr = HashValueStr.replace("C", "4");
		int HashValue = Integer.parseInt(HashValueStr);
		return HashValue % 64;
	}
	public AVLTree getAVLTree(int HashValue){
		return AVLTrees[HashValue];
	}
	public void insertSequence(String sequence, int student_id){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		int seq_length = sequence.length();
		for(int i=0; i<=seq_length-6; i++) {
			int start_pos = i + 1;
			ListNode nNode = new ListNode(student_id, start_pos);
			String subseq = sequence.substring(i, i + 6);
			int hashvalue = CalcHashValue(subseq);
			if (AVLTrees[hashvalue].getSuperParent().getPattern() == null) {
				TreeNode newtree = new TreeNode(subseq);
				newtree.pushList(nNode);
				AVLTrees[hashvalue].setSuperParent(newtree);
			}
			else if (!AVLTrees[hashvalue].SearchNode(subseq).getPattern().equals(subseq)) {
				// 해당 sequence를 가지는 treenode가 없으면 새로 만들어야지.
				TreeNode newtree = new TreeNode(subseq);
				newtree.pushList(nNode);
				// 여기서부터 잘못의 시작..? ㄴㄴ 밑에 256 라인
				newtree.setParent(AVLTrees[hashvalue].SearchNode(subseq));
				if (newtree.getParent().getPattern().compareTo(subseq) > 0) {
					newtree.getParent().setLeft(newtree);
				}
				else {
					newtree.getParent().setRight(newtree);
				} // 이 부분이 지금 잘못됐음. PARENT의 LEFT, RIGHT만 바뀌잖아 병시나
				// 아래 부분은 tree balancing
				TreeNode par_node = newtree;
				TreeNode par_par_node = new TreeNode();
				while (par_node != null) {
					AVLTrees[hashvalue].updateheight(par_node);
					int bl_factor = AVLTrees[hashvalue].updatebalancefactor(par_node);
					if (bl_factor > 1 || bl_factor < -1) {
						if (par_node.getParent() != null) {
							par_par_node = par_node.getParent();
							if (par_node.getParent().getRight() == par_node) {
								par_node.getParent().setRight(AVLTrees[hashvalue].MakeTreeBalanced(par_node));
							} else {
								par_node.getParent().setLeft(AVLTrees[hashvalue].MakeTreeBalanced(par_node));
							}
							AVLTrees[hashvalue].MakeTreeBalanced(par_node).setParent(par_par_node);
						} else {
							AVLTrees[hashvalue].setSuperParent(AVLTrees[hashvalue].MakeTreeBalanced(par_node));
						}
						break;
					}
					par_node = par_node.getParent();
				}
				AVLTrees[hashvalue].incrementNOE();
			}
			else {
				// 같은 subseq를 가지는 treenode가 있는 경우.
				AVLTrees[hashvalue].SearchNode(subseq).pushList(nNode);
			}
		}
	}
	public void retrievalPattern(String pattern, LinkedList<String> Text){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		// pattern의 길이가 6이상이므로 우선 길이부터 구하고..? 그냥 Text에서 찾아도 되잖아,..?
		int id, index;
		int NumberOfPrinted=0;
		for(int i=0; i<Text.size(); i++){
			String sequence = Text.get(i);
			for(int j=0; j<=sequence.length()-pattern.length(); j++){
				String subsequence = sequence.substring(j, j+pattern.length());
				if(subsequence.equals(pattern)){
					id = i + 1;
					index = j + 1;
					if(NumberOfPrinted != 0) {System.out.print(" ");}
					System.out.print("(");
					System.out.print(id);
					System.out.print(", ");
					System.out.print(index);
					System.out.print(")");
					NumberOfPrinted++;
				}
			}
		}
		if(NumberOfPrinted == 0){ System.out.print("Not Found"); }
		System.out.println("");
	}
}

class AVLTree{
	private int HashValue;
	private int NumberOfElement;
	private TreeNode SuperParent=null;
	private TreeNode NodeForPrint=null;

	public AVLTree(int HashValue){
		initAVLTree(HashValue);
	}
	public void initAVLTree(int HashValue){
		this.HashValue=HashValue;
		NumberOfElement=0;
		SuperParent=new TreeNode();
		NodeForPrint=new TreeNode();
	}
	public int getNumberOfElement(){
		return NumberOfElement;
	}
	public void incrementNOE() { NumberOfElement++; }
	public int getHashValue(){ return HashValue; }
	public void setSuperParent(TreeNode sparent){ SuperParent = sparent; }
	public TreeNode getSuperParent(){ return SuperParent; }

	/*  ##### NOTICE: you should use PrintByHashValue, but you can change other functions to implement AVL tree ###### */
	/*public int InsertNode(ListNode NewNode) {
		// 같은 subseq를 가지는 treenode가 없고, avltree 에 다른 treenode는 존재할 때.
		// avltree의 적절한 위치에 맞게 가도록 avltree의 함수 호출을 통해 삽입
		// subseq 가 작으면 왼쪽, 크면 오른쪽 // 재귀함수 쓰나?
	}*/
	public TreeNode RotateLeft(TreeNode Curr){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		TreeNode right = Curr.getRight();
		TreeNode r_left = right.getLeft();
		right.setLeft(Curr);
		right.setParent(Curr.getParent());
		Curr.setParent(right);
		Curr.setRight(r_left);
		if(r_left != null) {
			r_left.setParent(Curr);
		}
		updateheight(Curr);
		updateheight(right);
		return right;
	}
	public TreeNode RotateRight(TreeNode Curr){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		TreeNode left = Curr.getLeft();
		TreeNode l_right = left.getRight();
		left.setRight(Curr);
		left.setParent(Curr.getParent());
		Curr.setParent(left);
		Curr.setLeft(l_right);
		if(l_right != null) {
			l_right.setParent(Curr);
		}
		updateheight(Curr);
		updateheight(left);
		return left;
	}
	public int updateheight(TreeNode TN){
		if(TN == null) {
			return 0;
		}
		else {
			TN.setHeight(max(updateheight(TN.getLeft()), updateheight(TN.getRight()))+1);
			return TN.getHeight();
		}
	}
	public int max(int left, int right){
		if(left >= right) { return left; }
		else { return right; }
	}
	public int heightofNode(TreeNode TN){ return TN == null ? 0 : TN.getHeight(); }
	public int updatebalancefactor (TreeNode TN){
		if(TN == null){ return 0; }
		else{
			TN.setBalanceFactor(heightofNode(TN.getLeft())-heightofNode(TN.getRight()));
			return TN.getBalanceFactor();
		}
	}
	public TreeNode MakeTreeBalanced(TreeNode NewNode){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		updateheight(NewNode);
		int balance = updatebalancefactor(NewNode);
		if(balance < -1){
			if(heightofNode(NewNode.getRight().getRight()) > heightofNode(NewNode.getRight().getLeft())){
				NewNode = RotateLeft(NewNode);
			}
			else {
				TreeNode temp = NewNode.getRight();
				NewNode.setRight(RotateRight(temp));
				NewNode = RotateLeft(NewNode);
			}
		}
		else if(balance > 1){
			if(heightofNode(NewNode.getLeft().getLeft())>heightofNode(NewNode.getLeft().getRight())){
				NewNode = RotateRight(NewNode);
			}
			else{
				NewNode.setLeft(RotateLeft(NewNode.getLeft()));
				NewNode = RotateRight(NewNode);
				// ####################### 여기도 고쳐야 하나 ###########################
			}
		}
		return NewNode;
	}
	public TreeNode SearchNode(String Pattern){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		// 이 함수에서 해당 pattern을 가지는 treenode를 반환해줌. pattern의 길이는 6.
		// search는 pattern의 대소 비교를 통해 compareTo를 사용?
		TreeNode current = SuperParent;
		while(current != null) {
			if (current.getPattern().equals(Pattern)) {	break; }
			if(current.getLeft() == null && current.getRight() == null) { break; }
			else {
				if(current.getPattern().compareTo(Pattern)>0) {
					if(current.getLeft()==null){ break; }
					else { current = current.getLeft(); }
				}
				else{
					if(current.getRight() == null){ break; }
					else { current = current.getRight(); }
				}
			}
		}
		return current;
	}
	public void PreOrderTraverse(TreeNode Start, int num) {
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		if (Start != null) {
			Start.PrintNode();
			//if(Start.getRight() != null || Start.getLeft() != null){
			//	System.out.print(" ");
			//}
			if(Start.getLeft()!=null){
				System.out.print(" ");
				PreOrderTraverse(Start.getLeft(),1);
			}
			if(Start.getRight()!=null){
				System.out.print(" ");
				PreOrderTraverse(Start.getRight(),1);
			}
		}
	}
	public void PrintByHashValue(int HashValue){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		if(SuperParent.getPattern() == null){
			System.out.println("Empty!");
		}
		else {
			PreOrderTraverse(SuperParent, 1);
			System.out.println("");
		}
	}
}

class TreeNode{
	private int BalanceFactor=0;
	private int height =1;
	// TreeNode has subsequence & positions as its element
	// ex) subsequence = 'AATCGC'
	// ex) positions = [(1, 1), (1, 7), (2, 5)]
	private String subsequence;

	// This is an example of implementation of "positions" with linkedlist
	// you can implement {positions|(student_id, start_index)} with any data structures as you want
	private ListNode head=null;
	private ListNode tail=null;
	// treenode내에서 head는 빈 listnode이다.
	//////////////////////////////////

	private TreeNode parent=null;
	private TreeNode left=null;
	private TreeNode right=null;

	public TreeNode(){
		initList();
	}
	public TreeNode(String Pattern){
		initList(Pattern);
	}

	public void initList(){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		head = new ListNode();
		tail = head;
	}
	public void initList(String Pattern){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		head = new ListNode();
		tail = head;
		subsequence = Pattern;
	}
	public void pushList(ListNode NewNode){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		tail.setNext(NewNode);
		tail = NewNode;
	}

	public void setParent(TreeNode Parent){
		this.parent=Parent;
	}
	public void setLeft(TreeNode Left){
		this.left=Left;
	}
	public void setRight(TreeNode Right){
		this.right=Right;
	}
	public void SetPattern(String Pattern){
		this.subsequence =new String(Pattern);
	}
	public void setBalanceFactor(int newBalanceFactor){
		this.BalanceFactor=newBalanceFactor;
	}
	public String getPattern(){
		return this.subsequence;
	}
	public TreeNode getParent(){
		return this.parent;
	}
	public TreeNode getLeft(){
		return this.left;
	}
	public TreeNode getRight(){
		return this.right;
	}
	public int getBalanceFactor(){
		return BalanceFactor;
	}
	public void setHeight(int h){ height = h; }
	public int getHeight(){ return height; }

	public void PrintNode(){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		System.out.print(subsequence);
	}
	/*
	public ListNode SearchByIndex(int index){
		 ################ IMPLEMENT YOUR CODES HERE#####################
	}
	*/
}

class ListNode{
	private int student_id;
	private int start_pos;

	private ListNode next=null;
	private ListNode prev=null;

	public ListNode(){
	}
	public ListNode(int student_id, int start_pos){
		this.student_id =student_id;
		this.start_pos =start_pos;
	}
	public void setNext(ListNode NextNode){this.next=NextNode;}
	public void setPrev(ListNode PrevNode){this.prev=PrevNode;}
	public int getStudent_id(){return student_id;}
	public int getStart_pos(){return start_pos;}
	public ListNode getNext(){return next;}
	public ListNode getPrev(){return prev;}
}