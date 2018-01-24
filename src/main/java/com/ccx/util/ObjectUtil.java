package com.ccx.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtil {
	/**对象转byte[]
	 * @param obj
	 * @return
	 * @throws
	 */
	public static byte[] objectToBytes(Object obj){
		byte[] bytes=null;
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();
			bo.close();
			oo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}
	/**byte[]转对象
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static Object bytesToObject(byte[] bytes) {
		if(bytes!=null){
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			ObjectInputStream sIn = null;
			try {
				sIn=new ObjectInputStream(in);
				return sIn.readObject();
			} catch (Exception e) {
				return null;
			}
		}else{
			return null;
		}
	}
}
