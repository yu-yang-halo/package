package ToGet;

import java.util.Arrays;

import android.util.Log;
import android.widget.Toast;

import com.lr.javaBean.Utility;

///合胜协议
public class HSPackage implements IPackage {
	private IHead head;
	private IBody body;
	private short checkNo;
	private byte endCode;
	private byte[] bytes;

	// 发送时的构造方法
	public HSPackage(IHead head, IBody body) {
		this.head = head;
		this.body = body;

		bytes = new byte[((IHSHead) head).HeadLength + body.GetBody().length
				+ 3];
		// 复制Head
		System.arraycopy(((IHSHead) head).GetBytes(), 0, bytes, 0,
				((IHSHead) head).HeadLength);
		// 在协议头部填入协议内容的长度
		System.arraycopy(
				Utility.shortToByteArray((short) body.GetBody().length), 0,
				bytes, ((IHSHead) head).HeadLength - 2, 2);
		System.arraycopy(body.GetBody(), 0, bytes, ((IHSHead) head).HeadLength,
				body.GetBody().length);

		short sum = 0;
		short[] arr = new short[bytes.length - 3];
		for (int i = 0; i < bytes.length - 3; i++) {
			arr[i] = (short) bytes[i];
			sum += arr[i] & 0xFF;

		}

		// 填入校验码
		System.arraycopy(Utility.shortToByteArray(sum), 0, bytes,
				bytes.length - 3, 2);
		bytes[bytes.length - 1] = (byte) '#';
	}

	// 收的时候的字节
	@SuppressWarnings("static-access")
	public HSPackage(byte[] bytes) {

		head = new HSHead(Arrays.copyOfRange(bytes, 0,
				((IHSHead) head).HeadLength));
		// 内容
		body = new HSBody(Arrays.copyOfRange(bytes,
				((IHSHead) head).HeadLength,
				((IHSHead) head).HeadLength + head.GetBodyLength()));

		// 校验码
		checkNo = (short) (bytes[((IHSHead) head).HeadLength
				+ head.GetBodyLength()] * 256 + bytes[((IHSHead) head).HeadLength
				+ head.GetBodyLength() + 1]);
		// 结束符

		endCode = bytes[((IHSHead) head).HeadLength + head.GetBodyLength() + 2];
		// 如果需要做检验,在此处校验,数据不正确的情况下做丢包处理
		// TODO
	}

	@Override
	public IHead GetHead() {
		return head;
	}

	@Override
	public IBody GetBody() {
		return body;
	}

	@Override
	public short GetCheckNo() {
		// TODO Auto-generated method stub
		return checkNo;
	}

	public byte[] GetBytes() {
		return bytes;
	}

}
