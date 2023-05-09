import java.io.*;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) {
		
		DepartmentLinkedList attendance_list = new DepartmentLinkedList();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(true)
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
				else if(option.equals("P"))
				{
					attendance_list.PrintAll();
				}
				else if(option.equals("I"))
				{
					String department = st.nextToken();
					String student_name = st.nextToken();
					String student_id = st.nextToken();
					attendance_list.insertOrdered(department, student_name, student_id);
				}
				else if(option.equals("D"))
				{
					String department = st.nextToken();
					String student_id = st.nextToken();
					attendance_list.delete(department, student_id);
				}
			}
			catch (Exception e)
			{
				System.out.println("잘못된 입력입니다. I,D,P,Q 네가지 옵션 중 하나를 선택하고, 올바른 인자를 입력하세요. 오류 : " + e.toString());
			}
		}
	}
}


class DepartmentLinkedList{

	private DPNode head;

	private class DPNode{
		private String department;
		private DPNode nextdpnode;
		private StudentLinkedList stdlist = new StudentLinkedList();

		public DPNode(String department){
			this.department = department;
			this.nextdpnode = null;
		}
	}

	public void insertOrdered(String department, String student_name, String student_id){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */

		DepartmentLinkedList.DPNode current = head;
		DepartmentLinkedList.DPNode nextnode;

		if(head == null){
			DPNode newNode = new DPNode(department);
			head = newNode;
			newNode.stdlist.insertOrdered(student_name, student_id);
		}
		else {
			if (head.department.equals(department)) {
				head.stdlist.insertOrdered(student_name, student_id);
			} else {
				boolean is_exist = false;
				while(current != null){
					nextnode = current.nextdpnode;
					if(current.department.equals(department)){
						current.stdlist.insertOrdered(student_name, student_id);
						is_exist = true;
						break;
					}
					current = nextnode;
				}
				if(!is_exist) {
					DPNode newNode = new DPNode(department);
					newNode.nextdpnode = head;
					head = newNode;
					head.stdlist.insertOrdered(student_name, student_id);
					sort();
				}
			}
		}
	}

	public void sort(){
		DepartmentLinkedList.DPNode current = head;
		DepartmentLinkedList.DPNode nextnode;
		DepartmentLinkedList.DPNode tempnode;

		nextnode = current.nextdpnode;
		tempnode = new DPNode(null);
		while (nextnode!=null && current.department.compareTo(nextnode.department) > 0) {
			tempnode.department = nextnode.department;
			nextnode.department = current.department;
			current.department = tempnode.department;
			tempnode.stdlist = nextnode.stdlist;
			nextnode.stdlist = current.stdlist;
			current.stdlist = tempnode.stdlist;
			tempnode.department = null;
			tempnode.stdlist = null;
			current = nextnode;
			nextnode = nextnode.nextdpnode;
		}
	}
	
	public void delete(String department, String student_id){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		DepartmentLinkedList.DPNode current = head;
		DepartmentLinkedList.DPNode nextnode;
		DepartmentLinkedList.DPNode prevnode;
		if(current != null){
			if(current.department.equals(department)){ // 첫째 department에서 바로 일치할 경우
				current.stdlist.delete(student_id);
				if(current.stdlist.getStdcount() ==0){
					head = current.nextdpnode;
				}
			}
			else{ // 2번 째 이상의 department에서 일치할 경우
				nextnode = current.nextdpnode;
				if(nextnode != null) {
					prevnode = current;
					current = nextnode;
					while (current != null) {
						if (current.department.equals(department)) {
							current.stdlist.delete(student_id);
							if (current.stdlist.getStdcount() == 0) {
								prevnode.nextdpnode = current.nextdpnode;
							}
							break;
						}
						prevnode = current;
						current = current.nextdpnode;
					}
				}
			}
		}
	}
	
	public void PrintAll(){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		DPNode current = head;
		if(current == null){
			System.out.println("Empty!");
		}
		else {
			while (current != null) {
				current.stdlist.PrintAll(current.department);
				current = current.nextdpnode;
			}
			System.out.println("End!");
		}
	}
}


class StudentLinkedList{

	private stdnode head;
	private int stdcount = 0;

	private class stdnode{
		private String student_name;
		private String student_id;
		private stdnode nextstdnode;

		public stdnode(String student_name, String student_id){
			this.student_name = student_name;
			this.student_id = student_id;
			this.nextstdnode = null;
		}
	}

	public void insertOrdered(String stdname, String stdid) {
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		if(head == null){
			stdnode newstdnode = new stdnode(stdname, stdid);
			head = newstdnode;
			stdcount++;
		}
		else {
			if (head.student_id.equals(stdid)) {
				return;
			} else {
				stdnode newstdnode = new stdnode(stdname, stdid);
				newstdnode.nextstdnode = head;
				head = newstdnode;
				stdcount++;
				sort();
			}
		}

	}

	public void sort(){
		stdnode current = head;
		stdnode nextnode;
		String tempname, tempid;

		if(current != null){
			nextnode = current.nextstdnode;
			while(nextnode != null){
				if(current.student_id.compareTo(nextnode.student_id)>0){
					tempname = current.student_name;
					tempid = current.student_id;
					current.student_name = nextnode.student_name;
					current.student_id = nextnode.student_id;
					nextnode.student_id = tempid;
					nextnode.student_name = tempname;

					current = nextnode;
					nextnode = nextnode.nextstdnode;
				}
				else if(current.student_id.equals(nextnode.student_id)){
					delete(current.student_id);
					break;
				}
				else{
					break;
				}
			}
		}
	}

	public int getStdcount(){
		return stdcount;
	}

	public void delete(String student_id){
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		stdnode current = head;
		stdnode nextnode;
		if(current != null) {
			if (head.student_id.equals(student_id)) { //맨 앞에 있는 애를 지우는 것일 때
				head = head.nextstdnode;
				stdcount--;
			} else { // 맨 앞이 아닌 애를 지울 때
				while (current != null) {
					nextnode = current.nextstdnode;
					if (nextnode!=null && nextnode.student_id.equals(student_id)) {
						current.nextstdnode = nextnode.nextstdnode;
						stdcount--;
						break;
					}
					current = nextnode;
				}
			}
		}
	}
	
	public void PrintAll(String department) {
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		stdnode current = head;
		while(current != null){
			System.out.println("(" + department + ", " + current.student_name + ", " + current.student_id + ")");
			current = current.nextstdnode;
		}
	}
}