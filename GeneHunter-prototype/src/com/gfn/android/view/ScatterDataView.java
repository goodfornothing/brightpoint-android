package com.gfn.android.view;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static java.lang.Double.parseDouble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.ScatterChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class ScatterDataView extends FrameLayout {

	private ScatterChart chart;
	private GraphicalView mainView;

	public ScatterDataView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ScatterDataView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ScatterDataView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private class DataItem implements Cloneable {
		String chromosone;
		int start;
		int end;
		String feature;
		double data;
	}

	{
		try {
			InputStream in = getContext().getAssets().open(
					"Dataset1DEMOdata2.igv");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String type = reader.readLine();
			String headers = reader.readLine();
			String line = null;
			// List<DataItem> items = new
			// LinkedList<ScatterDataView.DataItem>();
			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
			XYSeries series = new XYSeries("Data");

			double minData = Double.MAX_VALUE;
			double maxData = Double.MIN_VALUE;
			double minStart = Double.MAX_VALUE;
			double maxStart = Double.MIN_VALUE;

			int count = 80000;
			while ((line = reader.readLine()) != null && count > 0) {
				count--;
				String[] values = line.split("\t");
				// DataItem item = new DataItem();
				// item.chromosone = values[0];
				// item.start = Integer.getInteger(values[1]);
				// item.end = Integer.getInteger(values[2]);
				// item.feature = values[3];
				// item.data = Double.parseDouble(values[4]);
				// items.add(item);
				double data = parseDouble(values[4]);
				double start = parseDouble(values[1]);
				minData = Math.min(minData, data);
				maxData = Math.max(maxData, data);
				minStart = Math.min(minStart, start);
				maxStart = Math.max(maxStart, start);
				series.add(start, data);
			}
			in.close();

			dataset.addSeries(series);

			// int[] colors = new int[] { Color.BLUE, Color.CYAN, Color.MAGENTA,
			// Color.LTGRAY, Color.GREEN };
			// PointStyle[] styles = new PointStyle[] { PointStyle.X,
			// PointStyle.DIAMOND, PointStyle.TRIANGLE, PointStyle.SQUARE,
			// PointStyle.CIRCLE };

			XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
			renderer.setAxisTitleTextSize(16);
			renderer.setChartTitleTextSize(0);
			renderer.setLabelsTextSize(15);
			renderer.setLegendTextSize(15);
			renderer.setPointSize(5f);
			renderer.setMargins(new int[] { 0, 0, 0, 0 });
			renderer.setPanLimits(new double[] { minStart, maxStart });
			renderer.setAntialiasing(true);
			renderer.setZoomEnabled(false, true);
			renderer.setPanEnabled(true, false);

			// int length = colors.length;
			// for (int i = 0; i < length; i++) {
			// XYSeriesRenderer r = new XYSeriesRenderer();
			// r.setColor(colors[i]);
			// r.setPointStyle(styles[i]);
			// renderer.addSeriesRenderer(r);
			// }

			XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
			seriesRenderer.setColor(Color.BLACK);
			seriesRenderer.setPointStyle(PointStyle.SQUARE);
			seriesRenderer.setFillPoints(true);
			renderer.addSeriesRenderer(seriesRenderer);

			setChartSettings(renderer, "", "X", "Y", minStart, 2000000,
					minData, maxData, Color.GRAY, Color.LTGRAY);
			renderer.setXLabels(10);
			renderer.setYLabels(10);
			// int length = renderer.getSeriesRendererCount();
			// for (int i = 0; i < length; i++) {
			// ((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
			// .setFillPoints(true);
			// }

			chart = new ScatterChart(dataset, renderer);
			LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
			mainView = new GraphicalView(getContext(), chart);
			addView(mainView, params);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

}
