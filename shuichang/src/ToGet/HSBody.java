package ToGet;

public class HSBody implements IHSBody {
	private byte[] bytes;

	// 协议包体部分构造方法
	public HSBody(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public byte[] GetBody() {
		return bytes;
	}

}
