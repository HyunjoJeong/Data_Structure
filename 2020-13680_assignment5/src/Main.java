import java.util.Scanner;
import java.util.LinkedList;

public class Main {

	public static final int INF = Integer.MAX_VALUE;

	public static void main(String[] args) {
		LinkedList<int[]> GraphInfo=new LinkedList<>();

		// Input graph
		// (N, E) = (# of nodes, # of edges)
		Scanner scanner = new Scanner(System.in);
		int numOfNodes = scanner.nextInt();
		int numOfEdges = scanner.nextInt();

		// (src, dst, distance)
		for (int i = 0; i < numOfEdges; i++) {
			int[] list = new int[3];
			list[0] = scanner.nextInt(); // src
			list[1] = scanner.nextInt(); // dst
			list[2] = scanner.nextInt(); // distance
			GraphInfo.add(list);
		}

		// D = reachable city distance threshold
		int D = scanner.nextInt();

		/*  ################ IMPLEMENT YOUR CODES ##################### */

		int[][] edgevalues = new int[numOfNodes][numOfNodes];
		// edge 값들을 채워넣는 중. 일단 첫상태는 전부 INF로 하고.. 이후 주어진 edge들을 추가.

		for(int i=0; i<numOfNodes; i++){
			for(int j=0; j<numOfNodes; j++) {
				if(i==j) edgevalues[i][j] = 0;
				else edgevalues[i][j] = INF/2;
			}
		}

		// 주어진 edge 추가.
		for(int i=0; i<numOfEdges; i++){
			int startV, endV, edgevalue;
			startV = GraphInfo.get(i)[0];
			endV = GraphInfo.get(i)[1];
			edgevalue = GraphInfo.get(i)[2];
			edgevalues[startV-1][endV-1] = edgevalue;
		}

		// 각 vertice에서 부터 다른 vertice까지 가장 짧은 path를 구하자.
		int[][] Shortestdist = new int[numOfNodes][numOfNodes];
		int[][][] path = new int[numOfNodes][numOfNodes][numOfNodes];
		for(int i=0; i<numOfNodes; i++) Shortestdist[i] = findingpath(numOfNodes, edgevalues, i, path);

		// 접근 편의성 계산
		int[] accessibilities = new int[numOfNodes];
		boolean allzero = true;
		do {
			for (int i=0; i < numOfNodes; i++) {
				for (int j=0; j < numOfNodes; j++) {
					if (Shortestdist[i][j] <= D && Shortestdist[i][j] != 0) accessibilities[i]++;
				}
			}
			for(int i=0; i<numOfNodes; i++){
				if(accessibilities[i] != 0) allzero = false;
			}
			if(allzero) D++;
			//System.out.println(D); //D가 몇인지 확인용.
		} while(allzero);

		// 가장 좋은 위치 찾기. // 같은 접근 편의성을 가지면 그 도시들을 place_sameAccess에 저장한다.
		int bestplace=0;
		int num_sameAccess=0;
		int[] place_sameAccess = new int[numOfNodes];
		int[] Total_distance = new int[numOfNodes];
		for(int i=1; i<numOfNodes; i++) { if(accessibilities[bestplace] < accessibilities[i]) bestplace = i; }
		for(int i=0; i<numOfNodes; i++) { if(accessibilities[bestplace] == accessibilities[i]) place_sameAccess[num_sameAccess++] = i; }
		//
		if(num_sameAccess>1){
			for(int i=0; i<num_sameAccess; i++){
				int addr = place_sameAccess[i];
				for(int j=0; j<numOfNodes; j++){
					Total_distance[addr] += (Shortestdist[addr][j] <= D && Shortestdist[addr][j] != 0)? Shortestdist[addr][j] : 0;
				}
			}
			int tempbest =0;
			while(Total_distance[tempbest] == 0) tempbest++;
			for(int i=0; i<numOfNodes; i++){
				if(Total_distance[i] != 0 && Total_distance[i] < Total_distance[tempbest]) tempbest = i;
			}
			bestplace = tempbest;
		}
		else if(num_sameAccess==1){
			for(int j=0; j<numOfNodes; j++){
				Total_distance[bestplace] += (Shortestdist[bestplace][j] <= D && Shortestdist[bestplace][j] != 0)? Shortestdist[bestplace][j] : 0;
			}
		}

		//출력 부분
		System.out.println("Best city " + (bestplace+1)); //index값이므로 +1해서 출력
		System.out.println("Accessibility " + accessibilities[bestplace]);
		System.out.println("Total distance " + Total_distance[bestplace]);
		for(int j=0; j<numOfNodes; j++){
			if(Shortestdist[bestplace][j] <= D && Shortestdist[bestplace][j] != 0) {
				System.out.print("Path ");
				System.out.print(bestplace+1 + " ");
				if(path[bestplace][j][0] != 0) System.out.print(path[bestplace][j][0] + " ");
				System.out.println(j+1);
				System.out.print("Distance ");
				System.out.println(Shortestdist[bestplace][j]);
			}
		}
	}

	//vertice가 지나온 path와 shortestpath를 구하는 함수.
	public static int[] findingpath(int numOfNodes, int[][] edgevalues, int start, int[][][] path){
		int[] distances = new int[numOfNodes];
		for(int i=0; i<numOfNodes; i++) distances[i] = edgevalues[start][i];
		int[][] pathes = new int[numOfNodes][numOfNodes]; // 첫번째 index는 도착지점, 두번째에는 중간에 도착하는 곳들을 저장.
		int pathindex = 0;
		distances[start] = 0;
		for(int h=0; h<numOfNodes-1; h++) {
			for (int i = 0; i < numOfNodes; i++) {
				for (int j = 0; j < numOfNodes; j++) {
					if (edgevalues[i][j] != INF / 2) {
						if (distances[j] > (distances[i] + edgevalues[i][j])) {
							distances[j] = distances[i] + edgevalues[i][j];
							pathes[j][0] = i + 1;
						} else distances[j] = distances[j];
					}
				}
			}
		}
		path[start] = pathes;
		return distances;
	}
}