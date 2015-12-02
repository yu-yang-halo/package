package ToGet;

///设备接口
public interface IDevice {
	// /设备ID
	String GetDeviceID();

	// 设备名称
	String GetDeviceName();

	// 设备类型
	DeviceType GetDeviceType();

	// 设备状态
	DeviceState GetState();
}