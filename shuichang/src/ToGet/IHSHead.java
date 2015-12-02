package ToGet;

///合胜协议头接口
public interface IHSHead extends IHead {
	// /获取Key
	String GetKey();

	// /版本号
	byte GetVersion();

	// 设备类型
	DeviceType GetDeviceType();

	// 设备地址
	String GetDeviceID();

	// 流水号
	byte[] GetSerialNo();

	// 命令字
	short GetOrderWord();

	// 操作标志
	byte GetOperateSign();

	// /协议头部长度
	int HeadLength = 23;

	// /协议头部
	byte[] GetBytes();
}
