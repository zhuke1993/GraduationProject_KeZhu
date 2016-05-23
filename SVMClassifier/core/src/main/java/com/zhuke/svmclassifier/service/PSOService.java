package com.zhuke.svmclassifier.service;

import com.zhuke.svmclassifier.entity.SVMParam;
import com.zhuke.svmclassifier.util.ArrayUtil;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 粒子群优化算法
 * Created by ZHUKE on 2016/5/16.
 */
@Service
public class PSOService {

    @Autowired
    private DataSource2SvmProblemService dataSource2SvmProblemService;

    private svm_model model;

    private svm_problem probTrain;
    private svm_problem probTest;

    private int n = 2;//粒子维数
    private double[] gLimitation = {0.000001, 1000};//g参数的位置限制
    private double[] cLimitation = {0.000001, 1000};//c参数的位置限制

    private double weight = 0.8;//惯性权重
    private double c1 = 2;//粒子的个体学习因子
    private double c2 = 2;//粒子的社会学习因子
    private int maxgen = 500;//最大迭代次数
    private int populationSize = 50;//种群规模

    private double[] vLimitation = {-2, 2};//速度限制
    private double[][] position = new double[populationSize][n];//粒子的当前位置
    private double[][] v = new double[populationSize][n];//粒子的当前速度
    private double[] fitness = new double[populationSize];//粒子当前位置的适应值

    private double[][] pbestPositon = new double[populationSize][n];//粒子的个体最优解位置
    private double[] pbestFitness = new double[populationSize];//例子的个体最优解适应值
    private double[] gbestPosition = new double[n];//种群最优解位置
    private double gbestFitness = 0;//种群最优解适应值


    /**
     * 初始化种群离子位置和速度
     */
    private void init() {
        for (int i = 0; i < populationSize; i++) {
            double[] temp_p = new double[n];
            double[] temp_v = new double[n];
            for (int j = 0; j < n; j++) {
                temp_p[j] = Math.random();
                temp_v[j] = Math.random();
            }
            position[i] = temp_p;
            v[i] = temp_v;
        }
    }


    /**
     * 设置更新最优位置
     */
    private void updateBest() throws IOException {
        for (int i = 0; i < populationSize; i++) {
            double adoptedValue = getAdoptedValue(probTrain, probTest, position[i][0], position[i][1]);
            fitness[i] = adoptedValue;

            if (adoptedValue > pbestFitness[i]) {
                pbestFitness[i] = adoptedValue;
                pbestPositon[i] = position[i];
                System.out.println("+++++++++++++++Update pbest" + i + ", pbestFitness=" + adoptedValue + ", pbestPosition=" + position[i][0] + "," + position[i][1]);
            }

            if (adoptedValue > gbestFitness) {
                gbestPosition = position[i];
                gbestFitness = adoptedValue;
                System.out.println("****************Update gbest" + i + ", gbestFitness=" + adoptedValue + ", pbestPosition=" + position[i][0] + "," + position[i][1]);
            }
        }
    }

    /**
     * 开始PSO算法
     */
    public void pso() throws Exception {
        this.probTrain = dataSource2SvmProblemService.readFromDB(1L, 0, 70);
        this.probTest = dataSource2SvmProblemService.readFromDB(1L, 71, 60);
        init();
        for (int j = 0; j < maxgen; j++) {
            updateBest();
            //更新粒子速度
            for (int i = 0; i < populationSize; i++) {
                v[i] = ArrayUtil.plusArray(ArrayUtil.plusArray(ArrayUtil.muiltiArray(weight, v[i]), ArrayUtil.muiltiArray(c1 * Math.random(), ArrayUtil.subArray(pbestPositon[i], position[i]))), ArrayUtil.muiltiArray(c2 * Math.random(), ArrayUtil.subArray(gbestPosition, position[i])));
                position[i] = ArrayUtil.plusArray(position[i], v[i]);
                for (int k = 0; k < n; k++) {
                    if (v[i][k] > vLimitation[1])
                        v[i][k] = vLimitation[1];
                    if (v[i][k] < vLimitation[0])
                        v[i][k] = vLimitation[0];
                    if (position[i][k] > cLimitation[1])
                        position[i][k] = cLimitation[1];
                    if (position[i][k] < cLimitation[0])
                        position[i][k] = cLimitation[0];
                }
            }
        }
        System.out.println("PSO completed , the gbestFitness = " + gbestFitness + ", get the best parameter : c=" + gbestPosition[0] + ", g=" + gbestPosition[1]);

    }


    public double getAdoptedValue(svm_problem probTrain, svm_problem probTest, double c, double g) throws IOException {

        model = svm.svm_train(probTrain, SVMParam.customize(c, g));
        int errorCount = 0;
        for (int i = 0; i < probTest.l; i++) {
            double v = svm.svm_predict(model, probTest.x[i]);
            if (v != probTest.y[i]) {
                errorCount++;
            }
        }

        return new Double((probTrain.l - errorCount)) / probTrain.l;
    }

    public static void main(String[] args) throws IOException {
    }

}

