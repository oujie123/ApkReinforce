package com.gac.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Zip {

	public static void unZip(File srcFile,File destFile) {
		if (srcFile == null) {
			System.out.println("source apk is null!");
			return ;
		} 
		try {
			destFile.delete();
			ZipFile zipFile = new ZipFile(srcFile);
			Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
			while (zipEntrys.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) zipEntrys.nextElement();
				String fileNameString = zipEntry.getName();
				//System.out.println(fileNameString);
				if (fileNameString.equals("META-INF/CERT.RSA") || fileNameString.equals("META-INF/CERT.SF") 
						|| fileNameString.equals("META-INF/MANIFEST.MF")){
					continue;
				}
				if(!zipEntry.isDirectory()) {
					File newFile = new File(destFile, fileNameString);
					if(!newFile.getParentFile().exists()) {
						newFile.getParentFile().mkdirs();
					}
					FileOutputStream fos = new FileOutputStream(newFile);
					InputStream is = zipFile.getInputStream(zipEntry);
					byte[] bytes = new byte[1024];
					int length = 0;
					while ((length = is.read(bytes)) != -1) {
						fos.write(bytes,0,length);
					}
					//此处一定要关闭，否者会报内存泄露，此处不用flush，因为不是用的bufferedOutputStream
					is.close();
					fos.close();
				}
			}
			zipFile.close();
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void zip(File src,File dest) {
		if(src == null) {
			System.out.println("src file is null");
			return;
		}
		//如果没有父目录，就先把结果目录创建出来
		if(!dest.getParentFile().exists()) {
			System.out.println("创建父目录");
			dest.getParentFile().mkdirs();
		}
		//如果有老的包存在，就先删掉
		if (dest.exists()) {
			dest.delete();
		}
		try {
			CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(dest), 
					new CRC32());
			ZipOutputStream zipStream = new ZipOutputStream(cos);
			compress(src, zipStream, "");
			zipStream.flush();
			zipStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void compress(File src,ZipOutputStream zipStream,String basePath) throws Exception{
		if(src.isDirectory()) {
			compressDir(src,zipStream,basePath);
		} else {
			compressFile(src,zipStream,basePath);
		}
	}
	
	private static void compressDir(File dir, ZipOutputStream zos, String basePath) throws Exception {
		//System.out.println(basePath + dir.getName() + "/");
		File[] files = dir.listFiles();
		// 构建空目录
		if (files.length < 1) {
			ZipEntry entry = new ZipEntry(basePath + dir.getName() + "/");
			zos.putNextEntry(entry);
			zos.closeEntry();
		}
		for (File file : files) {
			// 递归压缩
			compress(file, zos, basePath + dir.getName() + "/");
		}
	}

	private static void compressFile(File file, ZipOutputStream zos, String dir)throws Exception {
		String dirName = dir + file.getName();
		//System.out.println("--->" + dirName);
		String[] dirNameNew = dirName.split("/");
		
		StringBuffer buffer = new StringBuffer();
		
		if (dirNameNew.length > 1) {
			for (int i = 1; i < dirNameNew.length; i++) {
				buffer.append("/");
				buffer.append(dirNameNew[i]);
			}
		} else {
			buffer.append("/");
		}
		
		ZipEntry entry = new ZipEntry(buffer.toString().substring(1));
		zos.putNextEntry(entry);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		int count;
		byte data[] = new byte[1024];
		while ((count = bis.read(data, 0, 1024)) != -1) {
			zos.write(data, 0, count);
		}
		bis.close();
		zos.closeEntry();
	}
}
