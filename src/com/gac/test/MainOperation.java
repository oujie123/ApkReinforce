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
		
		//Step 1   ���������д�����ļ���
		makeProcessingDir();
		
		//Step 2   ��ѹԭʼapk,����.dex�ļ�
		upzipAndEncryption();
		
		//Step 3   ����arr��ȡ��dex�ļ�
		arr2dex();
		
		//Step 4   ���ǩ��
		zipApkAndSign();
	}
	
	public static void zipApkAndSign() throws Exception{
		//���apk
		Zip.zip(apkTempFiles, resuleUnsignedApkFile);
		//ǩ��apk
        Signature.signature(resuleUnsignedApkFile, resulesignedApkFile);
	}
	
	public static void arr2dex() {
		//��ѹarr�ļ�
		Zip.unZip(arrFile, arrTempFiles);
		
		//��jarת��Ϊdex�ļ�
		File dexfile = Dx.jar2Dex(arrTempFiles);
		
		//�������ɵ�dex�ϲ���/apk/tempĿ¼
		Utils.copyFile(dexfile, newDexFiles);
	}

	public static void upzipAndEncryption() {
		//��ʼ���ӽ�����
		AES.init();
		
		//��ѹapk�ļ�
		Zip.unZip(apkFile, apkTempFiles);
		
		//��dex�ļ����м���
		AES.encryption(apkTempFiles);
		
		//��dex�ļ����������Ա���滹�������ҵ�
		Utils.renameFile(apkTempFiles);
	}
		
	public static void makeProcessingDir() {
		if (!apkTempFiles.exists()) {
			apkTempFiles.mkdirs();
		} else {
			//����tempĿ¼�����н�ѹ���ļ���ɾ����ʱ�ļ�,Ŀ¼��ɾ����ɾ����Ҫ�ݹ�ɾ�����������ļ�
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
			//����tempĿ¼�����н�ѹ���ļ���ɾ����ʱ�ļ�,Ŀ¼��ɾ����ɾ����Ҫ�ݹ�ɾ�����������ļ�
			File[] files = arrTempFiles.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					file.delete();
				}
			}
		}
	}

}
