package kr.kinow.log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

public class TailServlet extends WebSocketServlet {

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		return new TailLogInbound();
	}

	private class TailLogInbound extends MessageInbound implements LogFileTailerListener {
		WsOutbound outbound;		
		static final String filepath = "/opt/var/test.log";
		LogFileTailer tailer = new LogFileTailer(filepath, 1000, false);

		@Override
		protected void onClose(int status) {
			tailer.stopTailing();
			this.outbound = null;
		}
		
		public void readLine(String line) {
			try {
				outbound.writeTextMessage(CharBuffer.wrap(line));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onOpen(WsOutbound outbound) {
			this.outbound = outbound;			
			tailer.addListener(this);
			tailer.start();
		}

		@Override
		protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onTextMessage(CharBuffer arg0) throws IOException {
			System.out.println("send message");
			
		}
	}
}
