package com.zai.otr_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.java.otr4j.OtrEngineImpl;
import net.java.otr4j.OtrException;
import net.java.otr4j.OtrPolicyImpl;
import net.java.otr4j.session.SessionID;
import net.java.otr4j.session.SessionStatus;
import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;

public class Main extends Activity {

	private static final int PORT = 9000;
	private static final String IP = "10.16.64.97";

	private OtrEngineImpl usZai;

	private SessionID zaiSessionId = new SessionID("zai@otr", "moon@otr",
			"test");

	private MyOtrEngine host;

	private TextView txtStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		txtStatus = (TextView) findViewById(R.id.txtStatus);

		new RunClient();
	}

	private void showStatus(final String text) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				txtStatus.append(text + "\n");
			}
		});
	}

	class RunClient implements Runnable {

		private Thread thread;

		public RunClient() {
			host = new MyOtrEngine(new OtrPolicyImpl(OtrPolicyImpl.ALLOW_V2
					| OtrPolicyImpl.ERROR_START_AKE));

			usZai = new OtrEngineImpl(host);

			thread = new Thread(this);
			thread.start();
		}

		@Override
		public void run() {
			try {
				connectServer();
			} catch (OtrException e) {
				e.printStackTrace();
			}
		}

		private void connectServer() throws OtrException {
			try {
				Socket socket = new Socket(IP, PORT);
				showStatus("Connected with Server");
				DataOutputStream dos = new DataOutputStream(
						socket.getOutputStream());
				DataInputStream dis = new DataInputStream(
						socket.getInputStream());
				String receive = readBytes(dis);
				showStatus("Received: " + receive);

				if (usZai.getSessionStatus(zaiSessionId) != SessionStatus.ENCRYPTED) {
					showStatus("Session is not encrypted");
				}

				usZai.transformReceiving(zaiSessionId, receive);
				String send = host.lastMessage;
				writeBytes(dos, send);
				receive = readBytes(dis);
				usZai.transformReceiving(zaiSessionId, receive);
				send = host.lastMessage;
				writeBytes(dos, send);
				receive = readBytes(dis);
				usZai.transformReceiving(zaiSessionId, receive);

				if (usZai.getSessionStatus(zaiSessionId) != SessionStatus.ENCRYPTED) {
					showStatus("Session is not encrypted");
					return;
				}
				showStatus("Session is encrypted");

				// while (true) {
				//
				// }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private int writeBytes(DataOutputStream dos, String data)
				throws IOException {
			dos.writeUTF(data);
			dos.flush();
			return data.length();
		}

		private String readBytes(DataInputStream dis) throws IOException {

			return dis.readUTF();
		}
	}

}
