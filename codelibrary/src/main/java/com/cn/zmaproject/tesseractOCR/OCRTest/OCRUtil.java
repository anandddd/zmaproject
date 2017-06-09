package com.cn.zmaproject.tesseractOCR.OCRTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cn.zmaproject.tesseractOCR.tesseractUtil;


/**
 * 调用本地tesseractOCR识别图片信息
 * @author zhangmingan
 *
 */
public class OCRUtil {
	private final String LANG_OPTION = "-l"; // 英文字母小写l，并非数字1
	private final String EOL = System.getProperty("line.separator");
	private String tessPath = "D:\\Pictures\\Tesseract-OCR";
	private static String OS = System.getProperty("os.name").toLowerCase();

	public String recognizeText(File imageFile, String imageFormat)
			throws Exception {
		File tempImage = ImageIOHelper.createImage(imageFile, imageFormat);
		File outputFile = new File(imageFile.getParentFile(), "output");
		StringBuffer strB = new StringBuffer();
		List<String> cmd = new ArrayList<String>();
		if (OS.indexOf("windows") >= 0) {
			cmd.add(tessPath + "\\tesseract");
		} else if (OS.indexOf("linux") >= 0) {
			cmd.add("tesseract");
		} else {
			cmd.add("tesseract");
		}
		cmd.add("");
		cmd.add(outputFile.getName());
		cmd.add(LANG_OPTION);
//		 cmd.add("chi_sim");
		cmd.add("eng");

		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(tempImage.getParentFile());

		cmd.set(1, imageFile.getName());
		pb.command(cmd);
		pb.redirectErrorStream(true);

		Process process = pb.start();
		// tesseract.exe 1.jpg 1 -l chi_sim
		int w = process.waitFor();

		// 删除临时正在工作文件
		tempImage.delete();

		if (w == 0) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(outputFile.getAbsolutePath() + ".txt"),
					"UTF-8"));

			String str;
			while ((str = in.readLine()) != null) {
				strB.append(str).append(EOL);
			}
			in.close();
		} else {
			String msg;
			switch (w) {
			case 1:
				msg = "Errors accessing files.There may be spaces in your image's filename.";
				break;
			case 29:
				msg = "Cannot recongnize the image or its selected region.";
				break;
			case 31:
				msg = "Unsupported image format.";
				break;
			default:
				msg = "Errors occurred.";
			}
			tempImage.delete();
			throw new RuntimeException(msg);
		}
		new File(outputFile.getAbsolutePath() + ".txt").delete();
		return strB.toString();
	}

	/**
	 * 解析base64图片成文本
	 * @param base64Image
	 * @return
	 * @throws Exception
	 */
	public String base64ImageToText(String base64Image)throws Exception {
//		File outputFile = new File("D:\\", "output");
		StringBuffer strB = new StringBuffer();
		List<String> cmd = new ArrayList<String>();
		if (OS.indexOf("windows") >= 0) {
			cmd.add(tessPath + "\\tesseract");
		} else if (OS.indexOf("linux") >= 0) {
			cmd.add("tesseract");
		} else {
			cmd.add(tessPath + "\\tesseract");
		}
		cmd.add("");
//		cmd.add(outputFile.getName());
		cmd.add(LANG_OPTION);
//		 cmd.add("chi_sim");
		cmd.add("eng");

		ProcessBuilder pb = new ProcessBuilder();
//		pb.directory(imageFile.getParentFile());

//		cmd.set(1, tempImage.getName());
		pb.command(cmd);
		pb.redirectErrorStream(true);

		Process process = pb.start();
		// tesseract.exe 1.jpg 1 -l chi_sim
		int w = process.waitFor();

		// 删除临时正在工作文件
//		tempImage.delete();

		return strB.toString();
	}
	
	public static void main(String[] args) {
		/*String path = "D:\\2.jpg";
		try {
			String valCode = new OCRUtil().recognizeText(new File(path), "jpg");
			System.out.println(valCode);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		
		String path = "D:\\Pictures\\wan.jpg";
		
//		Base64Image.GenerateImage(base64Image, path);
		
		System.out.println(System.getProperty("user.dir"));
		
		try {
//			String valCode = new OCRUtil().recognizeText(new File(path), "jpg");
			String valCode = tesseractUtil.excuteImage(new File(path));
			System.out.println(valCode);
			Pattern pattern = Pattern.compile("([A-Z]\\d{8})");
			Matcher matcher = pattern.matcher(valCode);
			if (matcher.find()) {
				System.out.println(matcher.group(1));
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}