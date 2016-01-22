package gpms.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class SerializationHelper {

	@SuppressWarnings("unchecked")
	public static <T> T cloneThroughSerialize(T t) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		serializeToOutputStream((Serializable) t, bos);
		byte[] bytes = bos.toByteArray();
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
				bytes));
		return (T) ois.readObject();
	}

	private static void serializeToOutputStream(Serializable ser,
			OutputStream os) throws IOException {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(os);
			oos.writeObject(ser);
			oos.flush();
		} finally {
			oos.close();
		}
	}
}
