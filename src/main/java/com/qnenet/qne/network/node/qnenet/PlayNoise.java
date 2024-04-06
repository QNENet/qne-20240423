// package com.qnenet;

// import static org.junit.jupiter.api.Assertions.assertArrayEquals;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.fail;

// import java.security.NoSuchAlgorithmException;

// import javax.crypto.BadPaddingException;
// import javax.crypto.ShortBufferException;

// import com.southernstorm.noise.protocol.CipherState;
// import com.southernstorm.noise.protocol.CipherStatePair;
// import com.southernstorm.noise.protocol.HandshakeState;

// public class PlayNoise {

//     private void runTest(TestVector vec, HandshakeState initiator, HandshakeState responder) throws ShortBufferException, BadPaddingException, NoSuchAlgorithmException
// 	{
// 		// Set all keys and special values that we need.
// 		if (vec.init_prologue != null)
// 			initiator.setPrologue(vec.init_prologue, 0, vec.init_prologue.length);
// 		if (vec.init_static != null)
// 			initiator.getLocalKeyPair().setPrivateKey(vec.init_static, 0);
// 		if (vec.init_remote_static != null)
// 			initiator.getRemotePublicKey().setPublicKey(vec.init_remote_static, 0);
// 		if (vec.init_hybrid != null)
// 			initiator.getFixedHybridKey().setPrivateKey(vec.init_hybrid, 0);
// 		if (vec.init_ephemeral != null)
// 			initiator.getFixedEphemeralKey().setPrivateKey(vec.init_ephemeral, 0);
// 		if (vec.init_psk != null)
// 			initiator.setPreSharedKey(vec.init_psk, 0, vec.init_psk.length);
// 		if (vec.resp_prologue != null)
// 			responder.setPrologue(vec.resp_prologue, 0, vec.resp_prologue.length);
// 		if (vec.resp_static != null)
// 			responder.getLocalKeyPair().setPrivateKey(vec.resp_static, 0);
// 		if (vec.resp_remote_static != null)
// 			responder.getRemotePublicKey().setPublicKey(vec.resp_remote_static, 0);
// 		if (vec.resp_ephemeral != null) {
// 			// Note: The test data contains responder ephemeral keys for one-way
// 		    // patterns which doesn't actually make sense.  Ignore those keys.
// 			if (vec.pattern.length() != 1)
// 				responder.getFixedEphemeralKey().setPrivateKey(vec.resp_ephemeral, 0);
// 		}
// 		if (vec.resp_hybrid != null)
// 			responder.getFixedHybridKey().setPrivateKey(vec.resp_hybrid, 0);
// 		if (vec.resp_psk != null)
// 			responder.setPreSharedKey(vec.resp_psk, 0, vec.resp_psk.length);

// 		// Start both sides of the handshake.
// 		assertEquals(HandshakeState.NO_ACTION, initiator.getAction());
// 		assertEquals(HandshakeState.NO_ACTION, responder.getAction());
// 		initiator.start();
// 		responder.start();
// 		assertEquals(HandshakeState.WRITE_MESSAGE, initiator.getAction());
// 		assertEquals(HandshakeState.READ_MESSAGE, responder.getAction());

// 		// Work through the messages one by one until both sides "split".
// 		int role = HandshakeState.INITIATOR;
// 		int index = 0;
// 		HandshakeState send, recv;
// 		boolean isOneWay = (vec.pattern.length() == 1);
// 		boolean fallback = vec.fallback_expected;
// 		byte[] message = new byte [8192];
// 		byte[] plaintext = new byte [8192];
// 		for (; index < vec.messages.length; ++index) {
// 			if (initiator.getAction() == HandshakeState.SPLIT &&
// 					responder.getAction() == HandshakeState.SPLIT) {
// 				break;
// 			}
// 			if (role == HandshakeState.INITIATOR) {
// 				// Send on the initiator, receive on the responder.
// 				send = initiator;
// 				recv = responder;
// 				if (!isOneWay)
// 					role = HandshakeState.RESPONDER;
// 			} else {
// 				// Send on the responder, receive on the initiator.
// 				send = responder;
// 				recv = initiator;
// 				role = HandshakeState.INITIATOR;
// 			}
// 			assertEquals(HandshakeState.WRITE_MESSAGE, send.getAction());
// 			assertEquals(HandshakeState.READ_MESSAGE, recv.getAction());
// 			TestMessage msg = vec.messages[index];
// 			int len = send.writeMessage(message, 0, msg.payload, 0, msg.payload.length);
// 			assertEquals(msg.ciphertext.length, len);
// 			assertSubArrayEquals(Integer.toString(index) + ": ciphertext", msg.ciphertext, message);
// 			if (fallback) {
// 				// Perform a read on the responder, which will fail.
// 				try {
// 					recv.readMessage(message, 0, len, plaintext, 0);
// 					fail("read should have triggered fallback");
// 				} catch (BadPaddingException e) {
// 					// Success!
// 				}

// 				// Look up the pattern to fall back to.
// 				String pattern = vec.fallback_pattern;
// 				if (pattern == null)
// 					pattern = "XXfallback";

// 				// Initiate fallback on both sides.
// 				initiator.fallback(pattern);
// 				responder.fallback(pattern);

// 				// Restart the protocols.
// 				initiator.start();
// 				responder.start();

// 				// Only need to fallback once.
// 				fallback = false;
// 			} else {
// 				int plen = recv.readMessage(message, 0, len, plaintext, 0);
// 				assertEquals(msg.payload.length, plen);
// 				assertSubArrayEquals(Integer.toString(index) + ": payload", msg.payload, plaintext);
// 			}
// 		}
// 		if (vec.fallback_expected) {
// 			// The roles will have reversed during the handshake.
// 			assertEquals(HandshakeState.RESPONDER, initiator.getRole());
// 			assertEquals(HandshakeState.INITIATOR, responder.getRole());
// 		} else {
// 			assertEquals(HandshakeState.INITIATOR, initiator.getRole());
// 			assertEquals(HandshakeState.RESPONDER, responder.getRole());
// 		}

// 		// Handshake finished.  Check the handshake hash values.
// 		if (vec.handshake_hash != null) {
// 			assertArrayEquals(vec.handshake_hash, initiator.getHandshakeHash());
// 			assertArrayEquals(vec.handshake_hash, responder.getHandshakeHash());
// 		}

// 		// Split the two sides to get the transport ciphers.
// 		CipherStatePair initPair;
// 		CipherStatePair respPair;
// 		assertEquals(HandshakeState.SPLIT, initiator.getAction());
// 		assertEquals(HandshakeState.SPLIT, responder.getAction());
// 		if (vec.init_ssk != null)
// 			initPair = initiator.split(vec.init_ssk, 0, vec.init_ssk.length);
// 		else
// 			initPair = initiator.split();
// 		if (vec.resp_ssk != null)
// 			respPair = responder.split(vec.resp_ssk, 0, vec.resp_ssk.length);
// 		else
// 			respPair = responder.split();
// 		assertEquals(HandshakeState.COMPLETE, initiator.getAction());
// 		assertEquals(HandshakeState.COMPLETE, responder.getAction());

// 		// Now handle the data transport.
// 		CipherState csend, crecv;
// 		for (; index < vec.messages.length; ++index) {
// 			TestMessage msg = vec.messages[index];
// 			if (role == HandshakeState.INITIATOR) {
// 				// Send on the initiator, receive on the responder.
// 				csend = initPair.getSender();
// 				crecv = respPair.getReceiver();
// 				if (!isOneWay)
// 					role = HandshakeState.RESPONDER;
// 			} else {
// 				// Send on the responder, receive on the initiator.
// 				csend = respPair.getSender();
// 				crecv = initPair.getReceiver();
// 				role = HandshakeState.INITIATOR;
// 			}
// 			int len = csend.encryptWithAd(null, msg.payload, 0, message, 0, msg.payload.length);
// 			assertEquals(msg.ciphertext.length, len);
// 			assertSubArrayEquals(Integer.toString(index) + ": ciphertext", msg.ciphertext, message);
// 			int plen = crecv.decryptWithAd(null, message, 0, plaintext, 0, len);
// 			assertEquals(msg.payload.length, plen);
// 			assertSubArrayEquals(Integer.toString(index) + ": payload", msg.payload, plaintext);
// 		}

// 		// Clean up.
// 		initiator.destroy();
// 		responder.destroy();
// 		initPair.destroy();
// 		respPair.destroy();
// 	}


//     private void runTest(TestVector vec, HandshakeState initiator) throws ShortBufferException, BadPaddingException, NoSuchAlgorithmException
// 	{
// 		// Set all keys and special values that we need.
// 		if (vec.init_static != null)
// 			initiator.getLocalKeyPair().setPrivateKey(vec.init_static, 0);
// 		if (vec.init_remote_static != null)
// 			initiator.getRemotePublicKey().setPublicKey(vec.init_remote_static, 0);
// 		if (vec.init_hybrid != null)
// 			initiator.getFixedHybridKey().setPrivateKey(vec.init_hybrid, 0);
// 		if (vec.init_ephemeral != null)
// 			initiator.getFixedEphemeralKey().setPrivateKey(vec.init_ephemeral, 0);
// 			// Start both sides of the handshake.
// 		assertEquals(HandshakeState.NO_ACTION, initiator.getAction());
// 		initiator.start();
// 		assertEquals(HandshakeState.WRITE_MESSAGE, initiator.getAction());

// 		// Work through the messages one by one until both sides "split".
// 		int role = HandshakeState.INITIATOR;
// 		int index = 0;
// 		HandshakeState send, recv;
// 		byte[] message = new byte [8192];
// 		byte[] plaintext = new byte [8192];
// 		for (; index < vec.messages.length; ++index) {
// 			if (initiator.getAction() == HandshakeState.SPLIT ) {
// 				break;
// 			}

//             send = initiator;
            
// 			assertEquals(HandshakeState.WRITE_MESSAGE, send.getAction());
// 			TestMessage msg = vec.messages[index];
// 			int len = send.writeMessage(message, 0, msg.payload, 0, msg.payload.length);
// 			assertEquals(msg.ciphertext.length, len);
// 			assertSubArrayEquals(Integer.toString(index) + ": ciphertext", msg.ciphertext, message);
// 				int plen = recv.readMessage(message, 0, len, plaintext, 0);
// 				assertEquals(msg.payload.length, plen);
// 				assertSubArrayEquals(Integer.toString(index) + ": payload", msg.payload, plaintext);
// 			}
// 		}
// 		if (vec.fallback_expected) {
// 			// The roles will have reversed during the handshake.
// 			assertEquals(HandshakeState.RESPONDER, initiator.getRole());
// 			assertEquals(HandshakeState.INITIATOR, responder.getRole());
// 		} else {
// 			assertEquals(HandshakeState.INITIATOR, initiator.getRole());
// 			assertEquals(HandshakeState.RESPONDER, responder.getRole());
// 		}

// 		// Handshake finished.  Check the handshake hash values.
// 		if (vec.handshake_hash != null) {
// 			assertArrayEquals(vec.handshake_hash, initiator.getHandshakeHash());
// 			assertArrayEquals(vec.handshake_hash, responder.getHandshakeHash());
// 		}

// 		// Split the two sides to get the transport ciphers.
// 		CipherStatePair initPair;
// 		CipherStatePair respPair;
// 		assertEquals(HandshakeState.SPLIT, initiator.getAction());
// 		if (vec.init_ssk != null)
// 			initPair = initiator.split(vec.init_ssk, 0, vec.init_ssk.length);
// 		else
// 			initPair = initiator.split();
// 		if (vec.resp_ssk != null)
// 			respPair = responder.split(vec.resp_ssk, 0, vec.resp_ssk.length);
// 		else
// 			respPair = responder.split();
// 		assertEquals(HandshakeState.COMPLETE, initiator.getAction());
// 		assertEquals(HandshakeState.COMPLETE, responder.getAction());

// 		// Now handle the data transport.
// 		CipherState csend, crecv;
// 		for (; index < vec.messages.length; ++index) {
// 			TestMessage msg = vec.messages[index];
// 			if (role == HandshakeState.INITIATOR) {
// 				// Send on the initiator, receive on the responder.
// 				csend = initPair.getSender();
// 				crecv = respPair.getReceiver();
// 				if (!isOneWay)
// 					role = HandshakeState.RESPONDER;
// 			} else {
// 				// Send on the responder, receive on the initiator.
// 				csend = respPair.getSender();
// 				crecv = initPair.getReceiver();
// 				role = HandshakeState.INITIATOR;
// 			}
// 			int len = csend.encryptWithAd(null, msg.payload, 0, message, 0, msg.payload.length);
// 			assertEquals(msg.ciphertext.length, len);
// 			assertSubArrayEquals(Integer.toString(index) + ": ciphertext", msg.ciphertext, message);
// 			int plen = crecv.decryptWithAd(null, message, 0, plaintext, 0, len);
// 			assertEquals(msg.payload.length, plen);
// 			assertSubArrayEquals(Integer.toString(index) + ": payload", msg.payload, plaintext);
// 		}

// 		// Clean up.
// 		initiator.destroy();
// 		responder.destroy();
// 		initPair.destroy();
// 		respPair.destroy();
// 	}


    
// }
