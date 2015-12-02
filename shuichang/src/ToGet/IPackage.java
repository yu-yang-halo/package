package ToGet;

public interface IPackage {
	// 协议头部
	IHead GetHead();

	// 协议内容部分
	IBody GetBody();

	// 校验码
	short GetCheckNo();

	// 结束符
	byte EndCode = (byte) '#';

	byte[] GetBytes();
}
