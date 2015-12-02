package com.util;

import java.util.ArrayList;
import java.util.List;

public class ListMaxMin {

	/*
	 * 获取ArrayList中的最大值
	 */
	public static double ArrayListMax(ArrayList<String> sampleList) {
		try {
			double maxDevation = 0.0;
			int totalCount = sampleList.size();
			if (totalCount >= 1) {
				double max = Double.parseDouble(sampleList.get(0));
				for (int i = 0; i < totalCount; i++) {
					double temp = Double.parseDouble(sampleList.get(i));
					if (temp > max) {
						max = temp;
					}
				}
				maxDevation = max;
			}
			return maxDevation;
		} catch (Exception ex) {
			try {
				throw ex;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 20;

	}

	/*
	 * 获取ArrayList中的最小值
	 */
	public static double ArrayListMin(ArrayList<String> sampleList) {
		try {
			double mixDevation = 0.0;
			int totalCount = sampleList.size();
			if (totalCount >= 1) {
				double min = Double.parseDouble(sampleList.get(0));
				for (int i = 0; i < totalCount; i++) {
					double temp = Double.parseDouble(sampleList.get(i));
					if (min > temp) {
						min = temp;
					}
				}
				mixDevation = min;
			}
			return mixDevation;
		} catch (Exception ex) {
			try {
				throw ex;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/*
	 * @params 获取ArrayList中的平均值
	 */
	public static double ArrayListAverage(ArrayList<String> sampleList) {
		try {
			double all = 0.0;
			double averageDevation = 0.0;
			int totalCount = sampleList.size();
			if (totalCount >= 1) {
				for (int i = 0; i < totalCount; i++) {
					all += Double.parseDouble(sampleList.get(i));
				}
				averageDevation = all / totalCount;
			}
			return averageDevation;
		} catch (Exception ex) {
			try {
				throw ex;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

}
