import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Main {
	public static void main(String[] args) {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StackCalculator calc = new StackCalculator();
		while(true)
		{
			try
			{
				String infix = br.readLine();
				if(infix.equals("Q") || infix.equals("q"))
				{
					break;
				}
				else
				{
					calc.calculate(infix);
				}
			}
			catch (Exception e)
			{
				System.out.println("Wrong Expression!");
			}
		}
	}
}

class StackCalculator{
	LinkedList<String> postfix = new LinkedList<>();
	LinkedList<Long> numeric = new LinkedList<>();
	LinkedList<String> operator = new LinkedList<>();

	public long calculate(String infix) throws Exception {
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */

		// tab으로 된 공백 제거
		String infix2 = infix.replaceAll("\t", " ");
		int infixlength = infix2.length();

		// * * 의 꼴이 있을 때 예외 처리
		for(int i=0; i<infixlength-1; i++){
			int j = 0;
			if(infix2.charAt(i) == '*'){
				j = i+1;
				if(infix2.charAt(j) == ' '){
					j++;
					if(j<infixlength && infix2.charAt(j) == '*') { throw new Exception(); }
					while(j<infixlength && infix2.charAt(j)==' '){
						j++;
						if(j<infixlength && infix2.charAt(j) == '*'){ throw new Exception(); }
					}
				}
			}
		}

		// 숫자 사이에 공백도 예외 처리?
		for(int i=0; i<infixlength-1; i++){
			int j = 0;
			if(distinguisher(infix2.charAt(i)) == 1){
				j = i+1;
				if(infix2.charAt(j) == ' '){
					j++;
					if(j<infixlength && distinguisher(infix2.charAt(j)) == 1) { throw new Exception(); }
					while(j<infixlength && infix2.charAt(j)==' '){
						j++;
						if(j<infixlength && distinguisher(infix2.charAt(j)) == 1){ throw new Exception(); }
					}
				}
			}
		}

		// 입력에서 빈칸을 삭제하고 새로운 infix string 생성
		String updatedinfix = " ";
		for(int i=0; i<infixlength; i++){ // 입력에서 빈칸을 삭제
			if(infix2.charAt(i)!=' '){
				updatedinfix += Character.toString(infix2.charAt(i));
			}
		}
		int UIlength = updatedinfix.length();

		//단항 연산자 -를 구분해주자.
		String temp = new String();
		for(int i=1; i<UIlength; i++){
			if(updatedinfix.charAt(i)=='-'){
				if(updatedinfix.charAt(i-1) == ' ' || updatedinfix.charAt(i-1) == '$' || (distinguisher(updatedinfix.charAt(i-1))>=2 && distinguisher(updatedinfix.charAt(i-1))<=5 )){
					temp = updatedinfix.substring(0,i) + '$' + updatedinfix.substring(i+1);
					updatedinfix = temp;
				}
			}
		}

		// 괄호의 갯수가 맞는지, )가 먼저 나오진 않았는지 확인
		int left_par_num = 0;
		int right_par_num =0;
		for(int i=1; i<UIlength; i++){
			if(updatedinfix.charAt(i) == '('){ left_par_num++; }
			if(updatedinfix.charAt(i) == ')'){ right_par_num++; }
			if(right_par_num > left_par_num) { throw new Exception (); } // )가 (보다 먼저 나온 경우임.
		}
		if(left_par_num != right_par_num) { throw new Exception(); } // 갯수가 다른 경우.

		// 마지막 입력이 연산자인 경우 예외처리. 즉 마지막이 숫자 혹은 ) 가 아닌 경우 예외 처리.
		int distlast = distinguisher(updatedinfix.charAt(UIlength-1));
		if(distlast == 2 || distlast ==3 || distlast == 4 || distlast == 5) { throw new Exception(); }

		// 입력 중에 잘못된 입력이 있을 경우 예외 처리 -> distinguisher 함수 자체에 exception을 설정했으므로 그냥 함수 구동만 해도 됨.
		// 불필요하다고 판단되면 지울 것
		for(int i=1; i<UIlength; i++){distinguisher(updatedinfix.charAt(i));}

		// 괄호 앞에 숫자이거나 괄호 뒤에 숫자 인 경우 예외 처리
		for(int i=1; i<UIlength-1; i++){
			if(distinguisher(updatedinfix.charAt(i))==1 && distinguisher(updatedinfix.charAt(i+1))==5 ) { throw new Exception(); }
			if(distinguisher(updatedinfix.charAt(i))==6 && distinguisher(updatedinfix.charAt(i+1))==1 ) { throw new Exception(); }
		}

		// ** 이거나, 뒤에 연산자가 -(단항) 가 아닌 이상 연속된 연산자는 예외 처리.
		for(int i=1; i<UIlength-2; i++){
			if(updatedinfix.charAt(i) == '*') {
				if (updatedinfix.charAt(i + 1) == '*') {
					if (distinguisher(updatedinfix.charAt(i + 2)) == 2 || distinguisher(updatedinfix.charAt(i + 2)) == 4) {
						throw new Exception();
					}
				} else if (distinguisher(updatedinfix.charAt(i + 1)) == 2 || distinguisher(updatedinfix.charAt(i + 1)) == 4){
					throw new Exception();
				}
			}
			else if(distinguisher(updatedinfix.charAt(i)) == 4){
				if(distinguisher(updatedinfix.charAt(i + 1)) == 2 || distinguisher(updatedinfix.charAt(i+1))==4){
					throw new Exception();
				}
			}
			else if (distinguisher(updatedinfix.charAt(i))==2){
				if(distinguisher(updatedinfix.charAt(i + 1)) == 2 || distinguisher(updatedinfix.charAt(i+1))==4){
					throw new Exception();
				}
			}
		}

		// 새로운 string을 만들어 제곱은 ^으로 저장되게 하자 - 편의를 위해
		String calcinput = "";
		for(int i=1; i<UIlength; i++){
			if(updatedinfix.charAt(i) == '*' && updatedinfix.charAt(i+1) == '*'){
				calcinput += "^";
				i += 2;
			}
			calcinput += String.valueOf(updatedinfix.charAt(i));
		}
		int sizeofcalcinput = calcinput.length();

		//
		operator.push("(");
		for(int i=0; i<sizeofcalcinput; i++){
			if(calcinput.charAt(i) == '(' || calcinput.charAt(i) == '^' || calcinput.charAt(i) == '*'
			|| calcinput.charAt(i) == '/' || calcinput.charAt(i) == '%' || calcinput.charAt(i) == '$'
			|| calcinput.charAt(i) == '+' || calcinput.charAt(i) == '-'){
				operator.push(String.valueOf(calcinput.charAt(i)));
				operator_f(operator,postfix,operator.size(),numeric);
			}
			else if (calcinput.charAt(i) == ')'){
				operator.push(String.valueOf(')'));
				operator_f(operator,postfix,operator.size(),numeric);
				closedoperatorchecking(operator,postfix,numeric);
			}
			else{ // 숫자
				operator_f(operator,postfix,operator.size(),numeric);
				long temporary=Long.parseLong(String.valueOf(calcinput.charAt(i)));
				if(i+1<sizeofcalcinput && (distinguisher(calcinput.charAt(i+1))== 1)){
					temporary = Long.parseLong(String.valueOf(calcinput.charAt(i))) * 10 + Long.parseLong(String.valueOf(calcinput.charAt(i+1)));
					i++;
					while(i+1<sizeofcalcinput && (distinguisher(calcinput.charAt(i+1))== 1)){
						temporary = temporary*10 + Long.parseLong(String.valueOf(calcinput.charAt(i+1)));
						i++;
					}
					//postfix.push(Long.toString(temporary));
				}
				postfix.push(Long.toString(temporary));
				calculator(postfix,numeric);
			}
		}
		operator.push(")");
		closedoperatorchecking(operator,postfix,numeric);
		for(int i=postfix.size()-1; i>=0; i--){
			if (String.valueOf(postfix.get(i)).equals("^")){
				System.out.print("**");
				System.out.print(" ");
			} else{
				System.out.print(postfix.get(i));
				System.out.print(" ");
			}
		}
		System.out.println("");
		System.out.print((long)numeric.getFirst());
		System.out.println("");
		postfix = new LinkedList<>();

		return 1; // 임시
	}

	// updatedinfix의 각 index의 값이 숫자, operator, 괄호인지 구별 (첫 index는 빈칸임을 잊지 말자)
	public int distinguisher(char arr) throws Exception { // String의 각 index의 값이 숫자, operator, 괄호인지 구별
		if(arr == '0' || arr == '1' || arr == '2' || arr == '3' || arr == '4' || arr == '5' || arr == '6' || arr == '7' || arr == '8' || arr == '9'){
			return 1;
		}
		else if(arr == '+' || arr == '-' || arr == '*' || arr == '/' || arr == '%' ){
			if(arr == '+'){ return 2; }
			else if(arr == '-'){ return 3; } // 단항 연산자, 이항 연산자 여부는 추후 결정
			else{ return 4; } // **인 경우는 추후 생각해보자
		}
		else if(arr == '(' || arr == ')'){
			if(arr == '(') { return 5; }
			return 6; //arr이 )인 경우
			// 우선 연산자, 숫자 stack에 쌓은 이후 postfix 꼴로 바꿀 때, '(' 이후 ')'가 나왔을 때, 괄호안의 것들을 먼저 postfix stack에 쌓는다.
		}
		else if(arr == ' ' ) {
			return 9; // updatedinfix의 첫 index만 공백이므로 이를 위함임.
		}
		else if(arr == '$' || arr == '^'){
			return 100;
		}
		throw new Exception(); //이 외의 것들이 입력으로 들어오면 예외 처리
	}

	public void operator_f(LinkedList<String> operator, LinkedList<String> postfix, int size, LinkedList<Long> numeric) throws Exception {
		if(operator.isEmpty()) { operator.push("(");	}
		else{
			int back = 0;
			int front = 0;
			boolean lastchanged = false;
			boolean frontchanged = false;
			if (operator.get(0) == "$") {
				back = 4;
				lastchanged = true;
				postfix.push("$");
				calculator(postfix, numeric);
				operator.pop();
			}
			if(operator.size()>1) {
				if (operator.get(0).equals("^")) {
					back = 3;
					lastchanged = true;
				} else if (operator.get(0).equals("*") ||operator.get(0).equals("/") || operator.get(0).equals("%")) {
					back = 2;
					lastchanged = true;
				} else if (operator.get(0).equals("+") || operator.get(0).equals("-")) {
					back = 1;
					lastchanged = true;
				}
				if (operator.get(1).equals("$")) {
					front = 4;
					frontchanged = true;
				} else if (operator.get(1).equals("^")) {
					front = 3;
					frontchanged = true;
				} else if (operator.get(1).equals("*") || operator.get(1).equals("/") || operator.get(1).equals("%")) {
					front = 2;
					frontchanged = true;
				} else if (operator.get(1).equals("+") || operator.get(1).equals("-")) {
					front = 1;
					frontchanged = true;
				}
			}
			if (lastchanged && frontchanged && (front >= back)) {
				String s1 = "";
				String s2 = "";
				s1 += operator.get(1);
				s2 += operator.get(0);
				postfix.push(s1);
				calculator(postfix, numeric);
				operator.pop();
				operator.pop();
				operator.push(s2);
			}
		}
	}

	public void calculator(LinkedList<String> postfix, LinkedList<Long> numeric){
		int size = postfix.size();
		if(postfix.get(0).equals("+")){
			long a = (long)numeric.pop();
			long b = (long)numeric.pop();
			numeric.push(a+b);	}
		else if(postfix.get(0).equals("-")){
			long a = (long)numeric.pop();
			long b = (long)numeric.pop();
			numeric.push(b-a);	}
		else if(postfix.get(0).equals("*")){
			long a = (long)numeric.pop();
			long b = (long)numeric.pop();
			numeric.push(a*b);	}
		else if(postfix.get(0).equals("/")){
			long a = (long)numeric.pop();
			long b = (long)numeric.pop();
			numeric.push(b/a);   }
		else if(postfix.get(0).equals("%")){
			long a = (long)numeric.pop();
			long b = (long)numeric.pop();
			numeric.push(b%a);   }
		else if(postfix.get(0).equals("^")){
			long a = (long)numeric.pop();
			long b = (long)numeric.pop();
			numeric.push((long)Math.pow(b,a));   }
		else if(postfix.get(0).equals("$")){
			long a = (long)numeric.pop();
			numeric.push(-a);
		}
		else{ //숫자인 경우
			long c = Long.parseLong(String.valueOf(postfix.get(0)));
			numeric.push(c);
		}
	}

	public void closedoperatorchecking (LinkedList<String> operator, LinkedList<String> postfix, LinkedList<Long> numeric) throws Exception {
		int indexofleftp = 0;
		for(int i=0; i<operator.size(); i++){
			if(operator.get(i).equals("(")){ indexofleftp = i; break; } // 마지막 (가 저장됨
		}
		operator.pop(); // )없애기
		for(int i = 0; i < indexofleftp-1 ; i++){
			if(distinguisher(operator.get(0).toString().charAt(0)) ==1) {
				i++;
				if (i<indexofleftp-1 && (distinguisher(operator.get(i).toString().charAt(0)) ==1)) {
					operator.set(0, String.valueOf(Long.parseLong(String.valueOf(operator.get(0))) + Long.parseLong(String.valueOf(operator.get(i))) * Math.pow(10, i)));
				}
			}
			postfix.push(String.valueOf(operator.get(0))); // 여기서도 숫자 조정
			calculator(postfix, numeric);
			operator.pop();
		}
		operator.pop(); // ( 없애기
	}
}