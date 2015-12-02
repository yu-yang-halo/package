package ToGet;

import com.lr.javaBean.Utility;

import android.util.Log;

public class HSHead implements IHSHead {
	private int bodyLength;
	private byte version;
	private DeviceType deviceType;
	private String deviceID;
	private byte[] serialNo;
	private short orderWord;
	private byte operateSign;
	private byte[] head = new byte[HeadLength];

	// 构造方法(收数据构造方法)
	public HSHead(byte[] bytes) {
		System.arraycopy(bytes, 0, head, 0, HeadLength);

		version = bytes[3];
		switch (bytes[4] * 256 + bytes[5]) {
		case 1:
			deviceType = DeviceType.Water;
			break;
		case 2:
			deviceType = DeviceType.Plant;
			break;
		case 3:
			deviceType = DeviceType.Farm;
		case -5:
			deviceType = DeviceType.Server;
			break;
		default:
			break;
		}
		deviceID = Utility.byteToHex(bytes[6]) + "-"
				+ Utility.byteToHex(bytes[7]) + "-"
				+ Utility.byteToHex(bytes[8]) + "-"
				+ Utility.byteToHex(bytes[9]);
		// serialNo=Arrays.copyOfRange(bytes, 10, 18);

		System.out.println("bytes[18]" + bytes[18]);
		System.out.println("bytes[19]" + bytes[19]);

		orderWord = (short) (bytes[18] * 256 + bytes[19]);

		System.out.println("orderWord" + orderWord);

		String order = "0x" + String.format("%02x", bytes[18])
				+ String.format("%02x", bytes[19]);

		Log.e("order>>>>>>>", order);

		if (order.equals(0x00fe)) {
			Log.e("orderWord状态", "在线");
		}

		operateSign = bytes[20];
		bodyLength = bytes[IHSHead.HeadLength - 2] * 256
				+ bytes[IHSHead.HeadLength - 1];
	}

	// 发数据时的构造方法
	public HSHead(byte version, DeviceType deviceType, String deviceID,
			byte[] serialNo, short orderWord, byte operateSign) {
		head[0] = (byte) '*';
		head[1] = (byte) 'H';
		head[2] = (byte) 'S';
		head[3] = version;

		switch (deviceType) {
		case Water:
			head[4] = 0x00;
			// head[5] = 0x01;
			head[5] = (byte) 0xFE;
			break;
		case Plant:
			head[4] = 0x00;
			head[5] = 0x02;
			break;
		case Farm:
			head[4] = 0x00;
			head[5] = 0x03;
			break;
		case Android:
			head[4] = 0x00;
			// head[5] = 0x01;
			head[5] = (byte) 0xFE;
		default:
			break;
		}

		// 填入设备地址
		System.arraycopy(
				Utility.HexString2Bytes(deviceID.replaceAll("-", "").trim()),
				0, head, 6, 4);

		this.serialNo = serialNo;
		System.arraycopy(serialNo, 0, head, 10, 8);
		System.arraycopy(Utility.shortToByteArray(orderWord), 0, head, 18, 2);
		head[20] = operateSign;
	}

	@Override
	public int GetBodyLength() {
		// TODO Auto-generated method stub
		return bodyLength;
	}

	@Override
	public String GetKey() {
		return "*HS";
	}

	@Override
	public byte GetVersion() {
		return version;
	}

	@Override
	public DeviceType GetDeviceType() {
		return deviceType;
	}

	@Override
	public String GetDeviceID() {
		return deviceID;
	}

	@Override
	public byte[] GetSerialNo() {
		return serialNo;
	}

	@Override
	public short GetOrderWord() {
		return orderWord;
	}

	@Override
	public byte GetOperateSign() {
		return operateSign;
	}

	public byte[] GetBytes() {
		return head;
	}

}
