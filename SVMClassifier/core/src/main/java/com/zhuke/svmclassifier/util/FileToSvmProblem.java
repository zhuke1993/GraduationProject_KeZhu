package com.zhuke.svmclassifier.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

/**
 * 将file文件中的数据转换为svm_problem
 *
 * @author Administrator
 *
 */
public class FileToSvmProblem {

	// 数字化处理
	private static double atof(String s) {
		double d = Double.valueOf(s).doubleValue();
		if (Double.isNaN(d) || Double.isInfinite(d)) {
			System.err.println("输入值为非数字或者超过上限");
			System.exit(1);
		}
		return (d);
	}

	/**
	 * 获取问题描述，从文件中进行获取，文件中数据的存放格式为<lable>空格<attr1>:<value1>空格<attr2>:<value2>
	 *
	 * @param input_file_name
	 *            文件名
	 * @param param
	 *            参数值
	 * @return 得到的svm_problem
	 * @throws IOException
	 */
	public static svm_problem read_problem(String input_file_name,
										   svm_parameter param) throws IOException {
		BufferedReader fp = new BufferedReader(new FileReader(input_file_name));
		Vector<Double> vy = new Vector<Double>();
		Vector<svm_node[]> vx = new Vector<svm_node[]>();
		int max_index = 0;
		while (true) {
			String line = fp.readLine();
			if (line == null)
				break;
			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

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
