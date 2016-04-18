package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.entity.ActionRecord;
import com.zhuke.svmclassifier.service.DataSource2SvmProblemService;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * 数据转换为svm_problem
 *
 * @author ZHUKE
 */
@Service
public class DataSource2SvmProblemServiceImpl implements DataSource2SvmProblemService {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public static void main(String[] args) {
        StringTokenizer st = new StringTokenizer("1 1:1 2:2s", " ");
        System.out.println(st.countTokens());
    }

    public double atof(String s) {
        return Double.valueOf(s).doubleValue();
    }


    /**
     * 获取问题描述，从数据库进行获取，数据的存放格式为<lable>,<attr1>:<value1>,<attr2>:<value2>
     *
     * @param param 参数值
     * @return 得到的svm_problem
     * @throws IOException
     */
    public svm_problem readFromDB(svm_parameter param) throws IOException {
        List<ActionRecord> actionRecordList = (List<ActionRecord>) hibernateTemplate.find("from ActionRecord", null);

        svm_problem svmProblem = new svm_problem();
        ArrayList<svm_node[]> X = new ArrayList<svm_node[]>();
        ArrayList<Double> Y = new ArrayList<Double>();

        for (int i = 0; i < actionRecordList.size(); i++) {
            String action = actionRecordList.get(i).getAction();
            StringTokenizer st = new StringTokenizer(action, ",");
            int countTockens = st.countTokens();

            double label = atof(st.nextToken());
            Y.add(i, label);

            svm_node[] svmNodes = new svm_node[countTockens - 1];

            for (int j = 1; j < countTockens; j++) {
                svm_node node = new svm_node();
                String nodeStr = st.nextToken();//<index>:<value>
                node.index = Integer.parseInt(nodeStr.split(":")[0]);
                node.value = Double.parseDouble(nodeStr.split(":")[1]);
                svmNodes[j - 1] = node;
            }
            X.add(i, svmNodes);
        }
        svmProblem.l = Y.size();
        svmProblem.x = new svm_node[Y.size()][];
        svmProblem.y = new double[Y.size()];
        for (int i = 0; i < Y.size(); i++) {
            svmProblem.x[i] = X.get(i);
            svmProblem.y[i] = Y.get(i);
        }

        return svmProblem;

    }


    /**
     * 获取问题描述，从文件中进行获取，文件中数据的存放格式为<lable>空格<attr1>:<value1>空格<attr2>:<value2>
     *
     * @param input_file_name 文件名
     * @param param           参数值
     * @return 得到的svm_problem
     * @throws IOException
     */
    public svm_problem readFromFile(String input_file_name, svm_parameter param) throws IOException {
        BufferedReader fp = new BufferedReader(new FileReader(input_file_name));
        Vector<Double> vy = new Vector<Double>();
        Vector<svm_node[]> vx = new Vector<svm_node[]>();
        int max_index = 0;
        while (true) {
            String line = fp.readLine();
            if (line == null)
                break;
            StringTokenizer st = new StringTokenizer(line, ",:");

            vy.addElement(atof(st.nextToken()));

            int m = st.countTokens() / 2;// 获取该文件中能拼接成的结点的个数

            svm_node[] x = new svm_node[m];
            for (int j = 0; j < m; j++) {
                x[j] = new svm_node();
                x[j].index = Integer.parseInt(st.nextToken());
                x[j].value = atof(st.nextToken());
            }
            if (m > 0)
                max_index = Math.max(max_index, x[m - 1].index);
            vx.addElement(x);
        }
        svm_problem prob = new svm_problem();
        prob.l = vy.size();
        prob.x = new svm_node[prob.l][];
        for (int i = 0; i < prob.l; i++) {
            prob.x[i] = vx.elementAt(i);
        }
        prob.y = new double[prob.l];
        for (int i = 0; i < prob.l; i++) {
            prob.y[i] = vy.elementAt(i);
        }
        if (param.gamma == 0 && max_index > 0)
            param.gamma = 1.0 / max_index;
        if (param.kernel_type == svm_parameter.PRECOMPUTED)
            for (int i = 0; i < prob.l; i++) {
                if (prob.x[i][0].index != 0) {
                    System.err
                            .print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
                    System.exit(1);
                }
                if ((int) prob.x[i][0].value <= 0
                        || (int) prob.x[i][0].value > max_index) {
                    System.err
                            .print("Wrong input format: sample_serial_number out of range\n");
                    System.exit(1);
                }
            }
        fp.close();
        return prob;
    }

}
