package com.gac.test;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.sun.nio.zipfs.ZipDirectoryStream;

public class AES {

	private static Cipher enCipher;
	private static Cipher decrCipher;
	private static final String ALGORITHM_STRING = "AES/ECB/PKCS5Padding";
	private static final String PASSWORD = "abcdefrhijklmnop";
	
	public static void init() {
		try {
			enCipher = Cipher.getInstance(ALGORITHM_STRING);
			decrCipher = Cipher.getInstance(ALGORITHM_STRING);
			byte[] passBytes = PASSWORD.getBytes();
			SecretKeySpec secretKeySpec = new SecretKeySpec(passBytes, "AES");
			enCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			decrCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void encryption(File dest) {
		if (dest == null) {
			System.out.print("destfile is null");
			return ;
		}
		//从temp目录中过滤出.dex文件
		File[] files = dest.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith(".dex");
			}
		});
		for (File file : files) {
			//System.out.println(file.getName());
			//将dex文件读出来
			byte[] filebytes = Utils.file2Bytes(file);
			//使用AES进行加密
			byte[] encryptBytes = encryptContent(filebytes);
			//将数据写会源文件
			try {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(encryptBytes);
				//测试是否加密了   
				//System.out.println(filebytes.length + "==="+ encryptBytes.length + "===" + file.length());
				fos.flush();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static byte[] encryptContent(byte[] content) {
		try {
			return enCipher.doFinal(content);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
