
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class Server {

	private static final int PORT = 1121;// 서버가 사용하는 포트

	private static ArrayList<String> names = new ArrayList<String>();
	private static ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();

	// static Select selcet;
	private static int[] type = { -1, -1, -1, -1 };
	private static int inx = 0;

	private static int m;

	public static void main(String[] args) throws Exception {
		System.out.println("The Server is running.");
		ServerSocket listener = new ServerSocket(PORT);
		try {
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	private static class Handler extends Thread {
		private String exit_name;
		private String name;
		private Socket socket;
		private BufferedReader in; // 클라이언트로부터 데이터를 수신받기 위한
		private PrintWriter out; // 클라이언트에게 데이터를 내보내기 위한

		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			System.out.println("굳");
			try {
				/*
				 * in = new BufferedReader(new
				 * InputStreamReader(socket.getInputStream())); out = new
				 * PrintWriter(socket.getOutputStream(), true);
				 * 
				 * 
				 * 
				 * while (true) { out.println("SUBMITNAME"); //클라이언트에게
				 * SUBMITNAME이란 메세지를 보냄 name = in.readLine(); // 클라이언트로 부터 받은
				 * 이름을 name에 저장함 if (name == null) //만약에 name이 null이면 { return;
				 * //리턴 } synchronized (names) //names Array list 동기화 { if
				 * (!names.contains(name)) //names Array list에 name이 없으면 {
				 * names.add(name); //names Array list에 name을 추가한다. break; } }
				 * 
				 * } out.println("NAMEACCEPTED"); //while문을 빠져나오면 클라이언트에게
				 * NAMEACCEPTED라는 메세지를 보냄 writers.add(out); //writers Array
				 * list에 out을 추가함 -> broadcast를 위한
				 * 
				 * for (PrintWriter writer : writers) //writer Array list에 있는 모든
				 * writers들에 대해 { writer.println("ENTRANCE"+name); //ENTRANCE
				 * 메세지와 이름을 클라이언트에게 전송함 }
				 * 
				 * while (true) { String input = in.readLine();//클라이언트로부터 받은 채팅
				 * 내용을 input에 저장한다. if (input == null) //만약 input값이 null이면 {
				 * return;//리턴 }
				 * 
				 * String tok[] = input.split("@"); //input값을 @기준으로 나눠서 tok
				 * array에 저장한다.
				 * 
				 * if(tok.length>1) //만약에 tok array의 길이가 1이상이면 ->귓속말 { for(int
				 * i=0; i<writers.size(); i++) //writers Array의 크기만큼 i를 for loop
				 * 돌린다. { if(tok[0].equals(names.get(i))) //만약 이름인 tok[0]과 같은
				 * string이 names Array list에 있으면 {
				 * writers.get(i).println("WHISPER" + name + ": " + tok[1]); //그
				 * i번째 이름의 클라이언트에게 "WHISPER name: message" format으로 데이터를 보낸다. }
				 * } } else {
				 * 
				 * for (PrintWriter writer : writers) //writer Array list에 있는 모든
				 * writers들에 대해 { writer.println("MESSAGE " + name + ": " +
				 * input); //모든 클라이언트들에게 "MESSAGE name: message" format으로 데이터를
				 * 보낸다. }
				 * 
				 * }
				 */
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				String input2;
				while ((input2 = in.readLine()) != null) {
					// String input2 = in.readLine();
					/*
					 * if (input2 == null) //만약 input값이 null이면 { return;//리턴 }
					 */
					if (input2.startsWith("food")) {
						input2 = input2.substring(4);
						System.out.println("왔다");
						System.out.println("food " + input2);
						type[inx] = Integer.parseInt(input2);
						// System.out.println( type[inx]);
						inx++;
						// 디비로 연동해서 food 선택
						// 음식 서버에 저장해놓고

					} else if (input2.startsWith("priority")) {
						System.out.println("p " + input2);
						input2 = input2.substring(8);
						m = Integer.parseInt(input2);
						// 디비로 연동해서 우선순위 ㅇㅇ
						// 우선순위 정해지면 out.println("selected"+선택된 음식정보); 해서 보내줌
					}
					if((input2=in.readLine())==null)
						System.out.println(input2+"gggg");
					else{
						System.out.println("0000000000000000000000000");
						break;
					}
				}
				// this.callSelect();
				// for (int i = 0; i < 4; i++)
				// System.out.println(type[i]);

			} catch (IOException e) {
				System.out.println("가은");
				System.out.println(e);
			}

			finally {
				System.out.println("dkdhdhdhhdhdh111");
				if (name != null) // 네임이 null값이 아니면
				{
					exit_name = name; // name을 exit_name에 저장시킨다.
					names.remove(name); // names Array list에서 종료된 클라이언트의 name을
					// 삭제한다.
				}
				if (out != null) {

					writers.remove(out); // writers Array list에서 종료된 클라이언트의 out을
											// 삭제한다.
				}
				try {
					socket.close(); // 소켓을 종료시킨다.
					for (PrintWriter writer : writers) // writer Array list에 있는
					// 모든 writer들에 대해
					{
						writer.println("EXIT" + exit_name); // 모든 클라이언트들에게 "EXIT
						// 종료한 클라이언트의 이름"
						// format으로 데이터를
						// 전송한다.
					}
				} catch (IOException e) {

				}
			}
			System.out.println("dkdhdhdhhdhdh2222");
			this.callSelect();
		}

		private void callSelect() {
			// TODO Auto-generated method stub
			System.out.println(type[0]);
			Select selcet = new Select(type, m);
			// Select.main();
			System.out.println("Result : T " + selcet.Type);
			System.out.println("Result : F " + selcet.Food);
			System.out.println("Result : D " + selcet.Desc);
		}

	}

}