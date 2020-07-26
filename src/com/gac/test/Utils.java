package com.gac.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Utils {

	/**
	 * 文件转化成byte[]
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] file2Bytes(File file) {
		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
			byte[] fileBytes = new byte[(int) randomAccessFile.length()];
			randomAccessFile.readFully(fileBytes);
			randomAccessFile.close();
			return fileBytes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 文件重命名
	 * @param srcFile
	 */
	public static void renameFile(File srcFile) {
		if (srcFile == null) {
			System.out.println("file is null!");
			return ;
		}
		File[] files = srcFile.listFiles();
		for (File file : files) {
			if(file.isFile()) {
				if (file.getName().endsWith(".dex")) {
					String oldNameString = file.getName();
					int index = oldNameString.indexOf(".dex");
					oldNameString = oldNameString.substring(0, index);
					String newFileNameString = file.getParent() + File.separator + 
							oldNameString + "_1" + ".dex";
					file.renameTo(new File(newFileNameString));
				}
			}
		}
	}
	
	/**
	 * 将文件从原地址拷贝到新地址
	 * 
	 * @param src
	 * @param dest
	 */
	public static void copyFile(File src,File dest) {
		byte[] srcBytes = file2Bytes(src);
		//System.out.println(dest.getAbsolutePath());
		if (!dest.exists()) {
			try {
				dest.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(dest);
			fos.write(srcBytes);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
