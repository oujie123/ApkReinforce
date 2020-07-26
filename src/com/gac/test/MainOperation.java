package com.gac.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainOperation {

	public static File apkTempFiles = new File("source/apk/temp");
	public static File arrTempFiles = new File("source/arr/temp");
	public static File arrFile = new File("source/arr/mylibrary-debug.aar");
	public static File apkFile = new File("source/apk/app-debug.apk");
	public static File newDexFiles = new File("source/apk/temp/classes.dex");
	public static File resuleUnsignedApkFile = new File("result/unsigned-app.apk");
	public static File resulesignedApkFile = new File("result/apk-signed.apk");
	
	public static void main(String[] args) throws Exception{
		
		//Step 1   创建过程中处理的文件加
		makeProcessingDir();
		
		//Step 2   解压原始apk,加密.dex文件
		upzipAndEncryption();
		
		//Step 3   处理arr获取壳dex文件
		arr2dex();
		
		//Step 4   打包签名
		zipApkAndSign();
	}
	
	public static void zipApkAndSign() throws Exception{
		//打包apk
		Zip.zip(apkTempFiles, resuleUnsignedApkFile);
		//签名apk
        Signature.signature(resuleUnsignedApkFile, resulesignedApkFile);
	}
	
	public static void arr2dex() {
		//解压arr文件
		Zip.unZip(arrFile, arrTempFiles);
		
		//将jar转化为dex文件
		File dexfile = Dx.jar2Dex(arrTempFiles);
		
		//将新生成的dex合并到/apk/temp目录
		Utils.copyFile(dexfile, newDexFiles);
	}

	public static void upzipAndEncryption() {
		//初始化加解密器
		AES.init();
		
		//解压apk文件
		Zip.unZip(apkFile, apkTempFiles);
		
		//对dex文件进行加密
		AES.encryption(apkTempFiles);
		
		//给dex文件重命名，以便后面还可以在找到
		Utils.renameFile(apkTempFiles);
	}
		
	public static void makeProcessingDir() {
		if (!apkTempFiles.exists()) {
			apkTempFiles.mkdirs();
		} else {
			//遍历temp目录下所有解压的文件，删除临时文件,目录不删除，删除需要递归删除里面所有文件
			File[] files = apkTempFiles.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					file.delete();
				}
			}
		}
		
		if (!arrTempFiles.exists()) {
			arrTempFiles.mkdirs();
		} else {
			//遍历temp目录下所有解压的文件，删除临时文件,目录不删除，删除需要递归删除里面所有文件
			File[] files = arrTempFiles.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					file.delete();
				}
			}
		}
	}

}
