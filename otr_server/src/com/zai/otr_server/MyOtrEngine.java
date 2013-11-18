package com.zai.otr_server;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import net.java.otr4j.OtrEngineHost;
import net.java.otr4j.OtrException;
import net.java.otr4j.OtrPolicy;
import net.java.otr4j.session.SessionID;

public class MyOtrEngine implements OtrEngineHost {

	public String lastMessage;
	private OtrPolicy policy;

	public MyOtrEngine(OtrPolicy policy) {
		this.policy = policy;
	}

	@Override
	public void askForSecret(SessionID sessionId, String arg1) {
		System.out.println("askforSecret SessionId: "
				+ sessionId.getAccountID() + " and arg1: " + arg1);
	}

	@Override
	public void finishedSessionMessage(SessionID sessionId) throws OtrException {
		System.out.println("finishedSessionMessage SessionId: "
				+ sessionId.getAccountID());
	}

	@Override
	public String getFallbackMessage() {
		return null;
	}

	@Override
	public byte[] getLocalFingerprintRaw(SessionID sessionId) {
		System.out.println("getLocalFingerPrintRaw SessionId: "
				+ sessionId.getAccountID());
		return null;
	}

	@Override
	public KeyPair getLocalKeyPair(SessionID sessionId) throws OtrException {
		KeyPairGenerator kg;
		try {
			kg = KeyPairGenerator.getInstance("DSA");

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

		return kg.genKeyPair();

	}

	@Override
	public String getReplyForUnreadableMessage() {
		return null;
	}

	@Override
	public OtrPolicy getSessionPolicy(SessionID sessionId) {
		System.out.println("geSessionPolicy SessionId: "
				+ sessionId.getAccountID());
		return this.policy;
	}

	@Override
	public void injectMessage(SessionID sessionId, String msg)
			throws OtrException {
		this.lastMessage = msg;
		System.out.println("injectMessage SessionId: "
				+ sessionId.getAccountID() + " and arg1: " + msg);
	}

	@Override
	public void requireEncryptedMessage(SessionID sessionId, String arg1)
			throws OtrException {
		System.out.println("requireEncryptedMessage : SessionId: "
				+ sessionId.getAccountID() + " and arg1: " + arg1);

	}

	@Override
	public void showError(SessionID sessionId, String arg1) throws OtrException {
		System.out.println("showError SessionId: " + sessionId.getAccountID()
				+ " and arg1: " + arg1);
	}

	@Override
	public void smpAborted(SessionID sessionId) throws OtrException {
		System.out
				.println("smpAborted: SessionId: " + sessionId.getAccountID());
	}

	@Override
	public void smpError(SessionID sessionId, int arg1, boolean arg2)
			throws OtrException {
		System.out.println("smpError: SessionId: " + sessionId.getAccountID()
				+ " and arg1: " + arg1 + " and arg2" + arg2);

	}

	@Override
	public void unencryptedMessageReceived(SessionID sessionId, String arg1)
			throws OtrException {
		System.out.println("unencryptedMessageReceived: SessionId: "
				+ sessionId.getAccountID() + " and arg1: " + arg1);
	}

	@Override
	public void unreadableMessageReceived(SessionID sessionId)
			throws OtrException {
		System.out.println("unreadableMessageReceived: SessionId: "
				+ sessionId.getAccountID());
	}

	@Override
	public void unverify(SessionID sessionId) {
		System.out.println("Unverify: SessionId: " + sessionId.getAccountID());
	}

	@Override
	public void verify(SessionID sessionId, boolean arg1) {
		System.out.println("Verify: SessionId: " + sessionId.getAccountID()
				+ " and arg1: " + arg1);
	}

}
