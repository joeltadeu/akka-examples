package com.akka.examples.blockchain.utils;

import com.akka.examples.blockchain.model.Block;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BlockChainUtils {

	public static String calculateHash(String data) {
		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] rawHash = digest.digest(data.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer(); 
			for (int i = 0; i < rawHash.length; i++) {
				String hex = Integer.toHexString(0xff & rawHash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} 
	}

	public static boolean validateBlock(Block block) {
		String dataToEncode = block.getPreviousHash() + Long.toString(block.getTransaction().getTimestamp()) + Integer.toString(block.getNonce()) + block.getTransaction();
		String checkHash = calculateHash(dataToEncode);
		return (checkHash.equals(block.getHash()));
	}
}
