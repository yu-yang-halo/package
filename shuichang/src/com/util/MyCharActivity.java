package com.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.XYChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import android.widget.LinearLayout;

public class MyCharActivity {
	private GraphicalView view;
	public List<String> list1;
	public Context context;
	private LinearLayout layout;
	private ArrayList<String> list;
	double average = 0.0;
	double min = 0;
	private ArrayList<Integer> arrayList;
	double max = 20;
	private int minvalue;
	double t;

	int n = 0;
	// 每个序列的颜色设置
	Integer color;
	int[] colors = new int[] { Color.rgb(21, 210, 239), Color.YELLOW,
			Color.rgb(238, 0, 238), Color.GREEN, Color.WHITE,
			Color.rgb(128, 43, 226) };
	// 每个序列中点的形状设置
	// String[] title = new String[] { "溶氧", "PH", "氨氮", "温度" };
	String title;
	PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE,
			PointStyle.DIAMOND, PointStyle.TRIANGLE, PointStyle.SQUARE,
			PointStyle.CIRCLE, PointStyle.TRIANGLE };

	/**
	 * @param context
	 * @param name
	 * @param list
	 * @param layout
	 * @param ti
	 */
	public MyCharActivity(Context context, ArrayList<String> list,
			ArrayList<Integer> arrayList, String title, Integer color) {// 多条线组合
		this.context = context;
		this.list = list;
		this.arrayList = arrayList;
		this.title = title;
		this.color = color;
	}

	// public MyCharActivity(Context context, String name, ArrayList<String>
	// list,
	// String ti) {// 画一条线
	// list1 = list;
	// average = ListMaxMin.ArrayListAverage(list);
	// DecimalFormat df = new DecimalFormat(".##");
	// String st = df.format(average);
	// min = ListMaxMin.ArrayListMin(list);
	// max = ListMaxMin.ArrayListMax(list);
	// // 1, 构造显示用渲染图
	// XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	// // 2,进行显示
	// XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	// // 2.1 构建数据
	// // 3, 对点的绘制进行设置
	// XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
	// // 3.1设置颜色
	// // xyRenderer.setColor(Color.rgb(21, 210, 239));
	// // xyRenderer.setPointStyle(PointStyle.CIRCLE);
	// xyRenderer.setColor(Color.RED);
	// xyRenderer.setPointStyle(PointStyle.CIRCLE);
	// xyRenderer.setLineWidth(6);
	// xyRenderer.setChartValuesTextSize(22);
	// xyRenderer.setDisplayChartValuesDistance(26);
	//
	// String prompt = "";
	//
	// XYSeries series = new XYSeries("平均溶氧(" + st + "mg/L)" + 12);
	//
	// // 填充数据
	// for (int k = minvalue; k < list1.size(); k++) {
	// // 填充x, y的数值
	// series.add(k, Double.parseDouble(list1.get(k)));
	// }
	// try {
	// // 需要绘制的点放入dataset中
	// dataset.addSeries(series);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// // 3.2设置样式
	// // xyRenderer.setPointStrokeWidth(20);
	// // 3.3, 将要绘制的点添加到坐标绘制中
	// renderer.addSeriesRenderer(xyRenderer);
	// renderer.setChartTitle(name);
	// renderer.setChartTitleTextSize(27);
	// renderer.setXAxisMin(0);
	// renderer.setXAxisMax(12);
	// renderer.setYAxisMin(min - 0.1);
	// renderer.setYAxisMax(max + 0.1);
	// renderer.setYLabelsAlign(Align.RIGHT);
	// renderer.setShowGridX(true);// 是否显示X方向的网格线
	// renderer.setAxisTitleTextSize(40);
	// renderer.setShowGridY(true);// 是否显示Y方向的网格线
	// renderer.setGridColor(Color.GREEN);
	// renderer.setApplyBackgroundColor(true);
	// renderer.setBackgroundColor(Color.BLACK);
	// renderer.setYLabels(8);
	// renderer.setXLabels(6);
	// renderer.setLegendTextSize(25);// 图例字体大小
	// renderer.setLabelsTextSize(22);// 设置标签字体的大小
	// renderer.setAxesColor(Color.WHITE);
	// renderer.setXLabelsColor(Color.WHITE);// x轴数值的字体颜色\\
	// renderer.setMargins(new int[] { 40, 50, 30, 0 });// 设置chart上下左右距离
	// renderer.setDisplayChartValues(true);// 显示点对应值
	// xyRenderer.setFillPoints(true);
	// renderer.setPointSize(6);
	// xyRenderer.setChartValuesFormat(NumberFormat.getNumberInstance());
	// xyRenderer.setChartValuesTextAlign(Align.LEFT);
	//
	// try {
	// view = ChartFactory.getLineChartView(context, dataset, renderer);
	// // 将图表添加到布局中去
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public View getView() {

		return view;

	}

	private XYMultipleSeriesDataset getDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries(" 最大值：" + getMax(list) + " 最小值: "
				+ getMin(list) + " 平均值：" + getAve(list));
		// series.setTitle();

		for (int i = 0; i < arrayList.size(); i++) {

			// series.setTitle(title+ " " + "   最大值: "
			// + "getMax(list.get(i))");
			// for (int k = 0; k < list.size(); k++) {
			series.add(arrayList.get(i), Double.parseDouble(list.get(i)));// 往系列中加入一个随机分布的点
			// }

		}
		dataset.addSeries(series);
		return dataset;
	}

	public double getMax(ArrayList<String> arrayList) {
		double max = Double.parseDouble(arrayList.get(0));
		for (int i = 0; i < arrayList.size(); i++) {
			double parseDouble = Double.parseDouble(arrayList.get(i));

			if (parseDouble > max) {
				max = parseDouble;
			}

		}
		DecimalFormat fnum = new DecimalFormat("##0.00");
		return Double.parseDouble(fnum.format(max));
	}

	public double getMin(ArrayList<String> arrayList) {
		double min = Double.parseDouble(arrayList.get(0));
		for (int i = 0; i < arrayList.size(); i++) {
			double parseDouble = Double.parseDouble(arrayList.get(i));
			if (parseDouble < min) {
				min = parseDouble;
			}

		}
		DecimalFormat fnum = new DecimalFormat("##0.00");

		return Double.parseDouble(fnum.format(min));
	}

	public double getAve(ArrayList<String> arrayList) {
		double sum = 0;
		for (int i = 0; i < arrayList.size(); i++) {
			sum = sum + Double.parseDouble(arrayList.get(i));
		}
		DecimalFormat fnum = new DecimalFormat("##0.00");

		return Double.parseDouble(fnum.format(sum / arrayList.size()));

	}

	public XYMultipleSeriesRenderer getRenderer() {

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(20);// 设置坐标轴标题文本大小
		renderer.setChartTitleTextSize(28); // 设置图表标题文本大小
		renderer.setLabelsTextSize(20); // 设置轴标签文本大小
		renderer.setLegendTextSize(25); // 设置图例文本大小
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setDisplayChartValues(true);// 显示点对应值
		renderer.setApplyBackgroundColor(true);
		renderer.setShowGridX(true);
		renderer.setShowGridY(true);
		renderer.setYLabels(8);
		renderer.setXLabels(6);
		renderer.setChartTitle(title);

		renderer.setExternalZoomEnabled(true);
		renderer.setPointSize(6);
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(23);

		// renderer.setZoomEnabled(true,tr);
		renderer.setPanEnabled(false);
		renderer.setYAxisMax(getDateSingMax(list) + 2);
		renderer.setYAxisMin(getDateSingMin(list) - 1);
		// renderer.setMargins(new int[] { 40, 50, 30, 0 });

		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setChartValuesFormat(NumberFormat.getNumberInstance());
		r.setColor(color);
		r.setDisplayChartValues(true);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setLineWidth(4);
		r.setChartValuesTextSize(22);
		r.setFillPoints(true);
		r.setDisplayChartValuesDistance(25);

		r.setChartValuesSpacing(10);
		r.setChartValuesTextAlign(Align.LEFT);
		renderer.addSeriesRenderer(r);

		return renderer;
	}

	public View getDataview() {
		View view = ChartFactory.getLineChartView(context, getDataset(),
				getRenderer());

		return view;
	}

	public double getDateSingMin(ArrayList<String> list) {
		double min = Double.parseDouble(list.get(0));
		for (int i = 0; i < list.size(); i++) {
			if (Double.parseDouble(list.get(i)) < min) {
				min = Double.parseDouble(list.get(i));

			}

		}
		return min;

	}

	public double getDateSingMax(ArrayList<String> list) {
		double max = Double.parseDouble(list.get(0));
		for (int i = 0; i < list.size(); i++) {
			if (Double.parseDouble(list.get(i)) > max) {
				max = Double.parseDouble(list.get(i));

			}

		}
		return max;

	}

	public double getDatemin(ArrayList<ArrayList<String>> arrayList) {
		double min = 0;
		for (int i = 0; i < arrayList.size(); i++) {
			double arrayListMin = ListMaxMin.ArrayListMin(arrayList.get(i));
			if (arrayListMin < min) {
				min = arrayListMin;
			}
		}

		return min;

	}

	public double getDatemax(ArrayList<ArrayList<String>> arrayList) {
		double max = 0;
		for (int i = 0; i < arrayList.size(); i++) {
			double arrayListMax = ListMaxMin.ArrayListMax(arrayList.get(i));
			if (arrayListMax > max) {
				max = arrayListMax;
			}
		}
		return max;
	}

}
