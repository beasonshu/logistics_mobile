package com.ia.logistics.comm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	private String SDCardRoot;

	public FileUtil() {
		// 得到当前外部存储设备目录
		SDCardRoot = "/mnt/sdcard/DCIM";
	}

	/**
	 * 在SDCard上创建文件
	 * */
	public File createFileInSDCard(String fileName, String dir)
			throws Exception {
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SDCard上创建目录
	 * */
	public File createSDDir(String dir) {
		File file = new File(SDCardRoot + dir + File.separator);
		file.mkdirs();
		return file;
	}

	/**
	 * 判断SDCard上的文件是否存在
	 * */
	public boolean isFileExist(String fileName, String path) {
		File file = new File(SDCardRoot + path + File.separator + fileName);
		return file.exists();
	}

	/**
	 * 获取文件
	 *
	 * @param fileName
	 * @param path
	 * @return
	 */
	public File getFile(String fileName, String path) {
		File file = new File(SDCardRoot + path + File.separator + fileName);
		return file;
	}

	/**
	 * 删除文件
	 *
	 * @param fileName
	 * @param path
	 */
	public void deleteFile(String fileName, String path) {
		CommSet.d("baosight", "delete");
		File file = new File(SDCardRoot + path + File.separator + fileName);
		file.delete();
	}

	/**
	 * 将一个InputStream写入SDCard
	 * */
	public File writeToSDCardFromInput(String path, String fileName,
									   InputStream inputStream) {
		File file = null;
		OutputStream outputStream = null;

		try {
			createSDDir(path);
			file = createFileInSDCard(fileName, path);

			outputStream = new FileOutputStream(file);
			byte data[] = new byte[4 * 1024];
			int tmp;
			while ((tmp = inputStream.read(data)) != -1) {
				outputStream.write(data, 0, tmp);
			}
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * @param file
	 * @return
	 */
	public byte[] getBytesFromFile(File file) {
		if (file == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}

			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {

		}
		return null;
	}

	/**
	 * 获取sdcard目录下的所有文件
	 *
	 * @return
	 */
	public List<String> getFilesList() {
		List<String> list = getPictures(SDCardRoot);
		if (list != null && list.size() > 0) {
			CommSet.d("baosight", "图片的数量：" + list.size());
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 获取sdcard图片的数量
	 *
	 * @return
	 */
	public int getPhotoNumber() {
		List<String> list = getPictures(SDCardRoot);
		if (list.size() > 0) {
			return list.size();
		} else {
			return 0;
		}
	}

	/**
	 * 获取某个目录下所有的图片
	 *
	 * @param strPath
	 * @return
	 */
	public List<String> getPictures(final String strPath) {
		List<String> list = new ArrayList<String>();

		File file = new File(strPath);
		File[] files = file.listFiles();

		if (files == null) {
			return null;
		}

		for (int i = 0; i < files.length; i++) {
			final File f = files[i];
			if (f.isFile()) {
				try {
					int idx = f.getPath().lastIndexOf(".");
					if (idx <= 0) {
						continue;
					}
					String suffix = f.getPath().substring(idx);
					if (suffix.toLowerCase().equals(".jpg")
							|| suffix.toLowerCase().equals(".jpeg")
							|| suffix.toLowerCase().equals(".bmp")
							|| suffix.toLowerCase().equals(".png")) {
						list.add(f.getPath());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

}
