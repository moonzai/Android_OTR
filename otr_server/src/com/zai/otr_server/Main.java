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

	private OtrEngineImpl usMoon;

	private SessionID moonSessionId = new SessionID("moon@otr", "zai@otr",
			"test");

	private MyOtrEngine host;

	private TextView txtStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		txtStatus = (TextView) findViewById(R.id.txtStatus);

		new RunServer();
	}

	private void showStatus(final String text) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				txtStatus.append(text + "\n");
			}
		});
	}

	class RunServer implements Runnable {

		private Thread thread;

		public RunServer() {
			host = new MyOtrEngine(new OtrPolicyImpl(OtrPolicyImpl.ALLOW_V2
					| OtrPolicyImpl.ERROR_START_AKE));

			usMoon = new OtrEngineImpl(host);

			thread = new Thread(this);
			thread.start();
		}

		@Override
		public void run() {
			try {
				startServer();
			} catch (OtrException e) {
				e.printStackTrace();
			}
		}

		private void startServer() throws OtrException {
			try {
				ServerSocket server = new ServerSocket(PORT);
				showStatus("Server listening at port: " + PORT);
				Socket socket = server.accept();
				showStatus("Client connected");
				DataOutputStream dos = new DataOutputStream(
						socket.getOutputStream());
				DataInputStream dis = new DataInputStream(
						socket.getInputStream());
				usMoon.startSession(moonSessionId);

				if (usMoon.getSessionStatus(moonSessionId) != SessionStatus.ENCRYPTED) {
					showStatus("Session is not encrypted");
				}

				String send = host.lastMessage;
				int written = writeBytes(dos, send);
				showStatus("Bytes Written: " + written);
				String receive = readBytes(dis);
				usMoon.transformReceiving(moonSessionId, receive);
				send = host.lastMessage;
				writeBytes(dos, send);
				receive = readBytes(dis);
				usMoon.transformReceiving(moonSessionId, receive);
				send = host.lastMessage;
				writeBytes(dos, send);

				if (usMoon.getSessionStatus(moonSessionId) != SessionStatus.ENCRYPTED) {
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
