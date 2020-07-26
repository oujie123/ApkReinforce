package com.gac.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

public class Dx {
	
	public static File jar2Dex(File srcFile){
		if(srcFile == null) {
			System.out.println("source file is null!");
			return null;
		}
		File[] jarFiles = srcFile.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith("classes.jar");
			}
		});
		if (jarFiles == null || jarFiles.length <= 0) {
			throw new RuntimeException("error arr package"); 
		}
		File jarFile = jarFiles[0];
		File dexFile = new File(jarFile.getParent(),"classes.dex");
		doDx(jarFile, dexFile);
		
		//此时在arr/temp中保存一个副本
		//System.out.println(dexFile.getAbsolutePath());
		
		return dexFile;
	}
	
	/**
	 * 使用dx工具将jar转化为dex
	 */
	public static void doDx(File src, File dest) {
		Runtime runtime = Runtime.getRuntime();
//		System.out.println("src jar path:" + src.getAbsolutePath());
//		System.out.println("dest jar path:" + dest.getAbsolutePath());
		Process process = null;
		try {
			process = runtime.exec("cmd.exe /C dx --dex --output=" + dest.getAbsolutePath() 
			+ " " + src.getAbsolutePath());
			process.waitFor();
			//如果执行失败了，这打印log
			if (process.exitValue() != 0) {
				InputStream inputStream = process.getErrorStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len = 0;
				byte[] bytes = new byte[2048];
				while ((len = inputStream.read(bytes)) != -1) {
					baos.write(bytes, 0, len);
				}
				System.out.println(new String(baos.toByteArray(), "UTF-8"));
				baos.close();
				inputStream.close();
				throw new RuntimeException("dx run failed");
			}
			//System.out.println("Dx run success! dex length = " + dest.length());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		process.destroy();
	}

}
