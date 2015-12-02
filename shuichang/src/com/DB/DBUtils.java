package com.DB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.content.Context;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

public class DBUtils {
	private static DbUtils create;

	private static long count;
	private static List<Message> findAll;
	private static List<Message> findone;
	private static HistoryData historyData;

	public static long add(Context context, String mess, String category,
			String DviceID, String time) {
		if (create == null) {
			create = DbUtils.create(context, "message.db");
		}

		try {
			Message message = new Message();
			message.setMessage(mess);
			message.setTypeNo(DviceID);
			message.setTime(time);
			message.setCategory(category);
			create.save(message);
			count = create.count(Message.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;

	}

	public static long addData(Context context, String content, String day,
			String devId) {
		if (create == null) {
			create = DbUtils.create(context, "HistoryData.db");
		}

		try {
			HistoryData history = new HistoryData();
			history.setContent(content);
			history.setDay(day);
			history.setDevId(devId);
			history.setToday(new SimpleDateFormat("yyyy-MM-dd")
					.format(new Date()));
			create.save(history);
			count = create.count(Message.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;

	}

	public static List<Message> getList(Context context, String devid) {
		if (create == null) {
			create = DbUtils.create(context, "message.db");
		}
		try {
			findAll = create.findAll(Selector.from(Message.class).where(
					"typeNo", "=", devid));

		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return findAll;

	}

	public static HistoryData getHistoryDate(Context context, String day,
			String devid) {
		if (create == null) {
			create = DbUtils.create(context, "HistoryData.db");

		}
		try {
			historyData = (HistoryData) create.findFirst(Selector.from(
					HistoryData.class).where(
					WhereBuilder
							.b("devID", "=", devid)
							.and("day", "=", day)
							.and("today",
									"=",
									new SimpleDateFormat("yyyy-MM-dd")
											.format(new Date()))));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return historyData;

	}

	public static void Delete(List<Message> list, Context context) {
		if (create == null) {
			create = DbUtils.create(context, "message.db");
		}
		try {
			create.deleteAll(list);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void DeleteData(String time, Context context) {
		if (create == null) {
			create = DbUtils.create(context, "historyData.db");
		}
		try {
			create.delete(Selector.from(HistoryData.class).where("time", "=",
					time));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<Message> findcatotary(String devid, String cat,
			Context context) {
		if (create == null) {
			create = DbUtils.create(context, "message.db");
		}
		try {
			findone = create.findAll(Selector.from(Message.class).where(
					WhereBuilder.b("typeNo", "=", devid).and("category", "=",
							cat)));

		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return findone;

	}

}
