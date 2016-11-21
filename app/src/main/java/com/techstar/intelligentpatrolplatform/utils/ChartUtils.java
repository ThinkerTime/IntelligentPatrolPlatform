package com.techstar.intelligentpatrolplatform.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import com.techstar.intelligentpatrolplatform.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

/**
 * author lrzg on 16/11/11.
 * 描述：折线图
 */

public class ChartUtils {

    private Context context;
    private String title;
    private int xMin,xMax,yMin,yMax,axesColor,labelsColor;
    private int addX = -1;
    private float addY;


    private XYSeries series;
    private XYMultipleSeriesDataset mDataset;
    private XYMultipleSeriesRenderer renderer;
    private GraphicalView mChartView;
    private static final int MAX_POINT = 50;
    int[] xv = new int[MAX_POINT];
    int[] yv = new int[MAX_POINT];
    int i = 0;


    public ChartUtils(Context context,String title, int xMin, int xMax, int yMin, int yMax,int axesColor, int labelsColor) {
        this.context = context;
        this.title = title;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.axesColor = axesColor;
        this.labelsColor = labelsColor;
        initChaert();
    }

    private void initChaert() {

        this.series = new XYSeries(title);
        // 创建一个数据集的实例，这个数据集将被用来创建图表，创建数据层
        this.mDataset = new XYMultipleSeriesDataset();
        // 将点集添加到这个数据集中
        this.mDataset.addSeries(series);
        renderer = buildRenderer();

        // 设置好图表的样式（横轴时间分钟值，纵轴电压毫伏值）
        setChartSettings(renderer,title,"", "", xMin, xMax, yMin, yMax, axesColor, labelsColor);

    }

    /**
     * 设置图表
     * @param renderer
     * @param chartTitle 图表名称
     * @param xTitle 横坐标名称
     * @param yTitle 纵坐标名称
     * @param xMin 设置X轴最小值
     * @param xMax 设置X轴最大值
     * @param yMin 设置Y轴最小值
     * @param yMax 设置Y轴最大值
     * @param axesColor 设置X/Y数轴的颜色
     * @param labelsColor 设置X/Y标签颜色
     */
    private void setChartSettings(XYMultipleSeriesRenderer renderer,String chartTitle, String xTitle, String yTitle, int xMin, int xMax, int yMin, int yMax, int axesColor, int labelsColor) {

        renderer.setChartTitle(chartTitle);// 图表名称
        renderer.setXTitle(xTitle);// 横坐标名称
        renderer.setYTitle(yTitle);// 纵坐标名称
        renderer.setXAxisMin(xMin);//设置X轴最小值
        renderer.setXAxisMax(xMax);//设置X轴最大值
        renderer.setYAxisMin(yMin);//设置Y轴最小值
        renderer.setYAxisMax(yMax); //设置Y轴最大值
        renderer.setAxesColor(context.getResources().getColor(axesColor)); //设置数轴的颜色
        renderer.setLabelsColor(context.getResources().getColor(labelsColor));//设置标签颜色
        renderer.setShowGrid(true); //设置是否需要显示网格


        renderer.setApplyBackgroundColor(true);//必须设置为true，颜色值才生效
        renderer.setBackgroundColor(context.getResources().getColor(R.color.bg_page));//设置表格背景色
        renderer.setMarginsColor(context.getResources().getColor(R.color.bg_page));//设置周边背景色

        renderer.setGridColor(context.getResources().getColor(R.color.wathet));// 网格颜色
        renderer.setXLabels(12);//设置x轴显示10个点,根据setChartSettings的最大值和最小值自动计算点的间隔
        renderer.setYLabels(8);//设置y轴显示10个点,根据setChartSettings的最大值和最小值自动计算点的间隔
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setPointSize(2f);// 设置点的大小
        renderer.setShowLegend(false);//是否显示网格
        renderer.setPanEnabled(true, true);// 设置滑动,这边是横向可以滑动,纵向不可滑动
        renderer.setZoomEnabled(false, false);// 设置缩放，横向可以，纵向不可以
    }

    private XYMultipleSeriesRenderer buildRenderer() {

        //创建你需要的图表最下面的图层 XYMultipleSeriesRenderer
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        //创建你需要在图层上显示的具体内容的图层,设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.BLACK);//点的颜色
        r.setPointStyle(PointStyle.POINT);//折线点的样式
        r.setFillPoints(false); //实心点
        r.setLineWidth((float) 2);// 线粗尺寸
        //添加进去
        renderer.addSeriesRenderer(r);
        renderer.setChartTitleTextSize(50); // 设置图表标题文本大小
        renderer.setAxisTitleTextSize(40.0f);// 设置坐标轴标题文本大小
        renderer.setLabelsTextSize(30.0f); // 设置轴标签文本大小
        renderer.setMargins(new int[]{0, 80, 0, 80}); // 设置4边留白

        return renderer;
    }

    public GraphicalView getChart(){
        //直线
//        mChartView = ChartFactory.getLineChartView(context, mDataset, renderer);
        //曲线
        mChartView = ChartFactory.getCubeLineChartView(context, mDataset, renderer, 0.33f);
        return mChartView;
    }
    // 更新绘图方法
    public void updateChart(float what) {
        // 设置好下一个需要增加的节点
//        addX = (int) (addX + 1);
//        addY = what;
//
//        // 移除数据集中旧的点集
//        mDataset.removeSeries(series);
//        series.add(addX, addY);
//        // 在数据集中添加新的点集
//        mDataset.addSeries(series);
//
//        // 视图更新，没有这一步，曲线不会呈现动态
//        // 如果在非UI主线程中，需要调用postInvalidate()，具体参考api
////        chart.invalidate();
//
//        mChartView.postInvalidate();



        // 设置好下一个需要增加的节点
        addX = 0;
        addY = what;
        // 移除数据集中旧的点集
        mDataset.removeSeries(series);
        // 判断当前点集中到底有多少点，因为屏幕总共只能容纳MAX_POINT个，所以当点数超过MAX_POINT时，长度永远是MAX_POINT
        int length = series.getItemCount();
        if (length > MAX_POINT) {
            length = MAX_POINT;
        }
        // 将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
        for (int i = 0; i < length; i++) {
            xv[i] = (int) series.getX(i) + 1;
            yv[i] = (int) series.getY(i);
        }
        // 点集先清空，为了做成新的点集而准备
        series.clear();
        // 将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
        // 这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点
        series.add(addX, addY);
        for (int k = 0; k < length; k++) {
            series.add(xv[k], yv[k]);
        }

        // 在数据集中添加新的点集
        mDataset.addSeries(series);
        // 视图更新，没有这一步，曲线不会呈现动态
        // 如果在非UI主线程中，需要调用postInvalidate()，具体参考api
        //chart.invalidate();
        mChartView.postInvalidate();
    }

    /**
     * @Title rightUpdateCharts
     * @Description 新生成的点一直在左侧，产生向右平移的效果， 基于X轴坐标从0开始，然后递减的思想处理
     * @param datas
     * @return void
     */
    public void  rightUpdateCharts(ArrayList<Float> datas) {

        for (Float addY : datas) {
            series.add(i, addY);
            i++;
        }

        if (Math.abs(i) < MAX_POINT) {
            renderer.setXAxisMin(0); //设置X轴最小值
            renderer.setXAxisMax(MAX_POINT); //设置X轴最大值
        } else {
            renderer.setXAxisMin(series.getItemCount() - MAX_POINT);
            renderer.setXAxisMax(series.getItemCount());
        }

        mChartView.repaint();
    }

}
