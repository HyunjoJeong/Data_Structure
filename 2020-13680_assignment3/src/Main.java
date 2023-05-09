import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		String option = sc.nextLine();
		option = option.toUpperCase();
		
		int N = sc.nextInt();
		int[] unsorted_array = new int[N];
		for(int i=0; i<N; i++)
		{
			unsorted_array[i] = sc.nextInt();
		}

		//long beforetime = System.currentTimeMillis();
		
		if(option.equals("B"))
		{
			BubbleSort(unsorted_array);
		}
		else if(option.equals("I"))
		{
			InsertionSort(unsorted_array);
		}
		else if(option.equals("Q"))
		{
			QuickSort(unsorted_array);
		}
		else if(option.equals("T"))
		{
			ThreeWayQuickSort(unsorted_array);
		}
		else if(option.equals("M"))
		{
			MergeSort(unsorted_array);
		}
		else if(option.equals("R"))
		{
			RadixSort(unsorted_array);
		}

		//long aftertime = System.currentTimeMillis();
		//System.out.print("	/	time difference : ");
		//System.out.print(aftertime-beforetime);
	}

	private static void BubbleSort(int[] arr) {
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		if(arr == null || arr.length == 0) return;
		for (int i=0; i<arr.length-1; i++){
			for(int j=0; j<arr.length-i-1; j++){
				if(arr[j] > arr[j+1])
					swap(arr, j, j+1);
			}
		}
		printsort(arr);
	}

	private static void InsertionSort(int[] arr) {
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		if(arr == null || arr.length == 0) return;
		for(int i=1; i<arr.length; i++){
			int temp = arr[i];
			int j = i;
			while(j>0 && arr[j-1]>temp){
				arr[j] = arr[j-1];
				j--;
			}
			arr[j] = temp;
		}
		printsort(arr);
	}
	
	private static void QuickSort(int[] arr) {
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		quicksort(arr, 0, arr.length-1);
		printsort(arr);
	}
	
	private static void ThreeWayQuickSort(int[] arr) {
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		quicksort3(arr,0, arr.length-1);
		printsort(arr);
	}
	
	private static void MergeSort(int[] arr) {
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		mergesort(arr);
		printsort(arr);
	}
	
	private static void RadixSort(int[] arr) {
		/*  ################ IMPLEMENT YOUR CODES HERE##################### */
		radixsort(arr);
		printsort(arr);
	}

	/******************************************************************************************/

	public static void quicksort(int[] arr, int left, int right){
		if(left >= right) return;
		int pivot = partition(arr, left, right);

		quicksort(arr, left, pivot-1);
		quicksort(arr, pivot+1, right);
	}

	public static int partition(int[] arr, int leftB, int rightB){
		int pivot= rightB;
		int left = leftB -1;
		int right = rightB;

		while(true){
			while(arr[++left]<arr[pivot]) {;}
			while(right>leftB && arr[--right]> arr[pivot]){;}
			if(left>=right) break;
			else{swap(arr, left, right);}
		}
		swap(arr,left,pivot);
		return left;
	}

	/******************************************************************************************/

	public static void quicksort3(int[]arr, int left, int right){
		if(left>= right) return;
		int pivot1 = partitionbyleft(arr, left, right);
		int pivot2 = partitionbyright(arr, left, right);
		if(pivot1 > pivot2){
			quicksort(arr, left, pivot2-1);
			quicksort(arr, pivot2+1, pivot1-1);
			quicksort(arr, pivot1+1, right);
		}
		else if(pivot2 > pivot1){
			quicksort(arr, left, pivot1-1);
			quicksort(arr, pivot1+1, pivot2-1);
			quicksort(arr, pivot2+1, right);
		}
		else{
			quicksort(arr, left, pivot1-1);
			quicksort(arr, pivot1+1, right);
		}
	}

	public static int partitionbyright(int[]arr, int leftB, int rightB){
		int pivot = rightB;
		int left = leftB-1;
		int right = rightB;

		while(true){
			while(arr[++left]<arr[pivot]){;}
			while(right>leftB && arr[--right] > arr[pivot]){;}
			if(left>=right) break;
			else{swap(arr, left, right);}
		}
		swap(arr, left, pivot);
		return left;
	}

	public static int partitionbyleft(int[]arr, int leftB, int rightB){
		int pivot = leftB;
		int left = leftB;
		int right = rightB+1;
		while(true){
			while(arr[--right]>arr[pivot]){;}
			while(left<rightB && arr[++left] < arr[pivot]){;}
			if(left >= right) break;
			else{swap(arr, left, right);}
		}
		swap(arr, right, pivot);
		return right;
	}

	/******************************************************************************************/

	public static int getMax(int[] arr) {
		int max = arr[0];
		for(int i=1; i<arr.length; i++)
			if(arr[i] > max) max = arr[i];
		return max;
	}

	public static void countSort(int[] arr, int exp) {
		int[] output = new int[arr.length];
		int[] count = {0,0,0,0,0,0,0,0,0,0};

		for(int i=0; i<arr.length; i++) count[(arr[i]/exp)%10]++;
		for(int i=1; i<10; i++) count[i] += count[i-1];
		for(int i=arr.length-1; i>=0; i--) {
			output[count[(arr[i]/exp)%10]-1] = arr[i];
			count[(arr[i]/exp)%10]--;
		}

		for(int i=0; i<arr.length; i++) arr[i] = output[i];
	}

	public static void radixsort(int[] arr) {
		int m = getMax(arr);
		for(int exp=1; m/exp>0; exp*=10) countSort(arr, exp);
	}

	/******************************************************************************************/

	public static void mergesort(int[] arr){
		if(arr == null || arr.length <= 1) return;

		int middle = arr.length/2;
		int[]left = new int[middle];
		int[]right = new int [arr.length - middle];
		for(int i=0; i<middle; i++) left[i] = arr[i];
		for(int i=0; i< arr.length-middle; i++) right[i] = arr[middle+i];

		mergesort(left);
		mergesort(right);

		int l=0;
		int r=0;
		for(int i=0; i<arr.length; i++){
			if(r>=right.length || (l < left.length && left[l] < right[r])) arr[i] = left[l++];
			else {arr[i] = right[r++];}
		}
	}

	/******************************************************************************************/

	public static void printsort(int[] arr){
		int size = arr.length;
		for(int i=0; i<size; i++){
			System.out.print(arr[i] + " ");
		}
	}

	public static void swap(int[] arr, int i, int j){
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}