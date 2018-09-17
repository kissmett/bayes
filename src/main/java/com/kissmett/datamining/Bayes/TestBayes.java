package com.kissmett.datamining.Bayes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class TestBayes {
	/**
	 * 读取测试元组
	 * 
	 * @return 一条测试元组
	 * @throws IOException
	 *             uci数据库 UCI数据库是加州大学欧文分校(University of CaliforniaIrvine)提出的用于机器学习的数据库，这个数据库目前共有335个数据集，其数目还在不断增加，
	 *             UCI数据集是一个常用的标准测试数据集。
	 *             http://archive.ics.uci.edu/ml/machine-learning-databases/adult/adult.data 训练
	 *             http://archive.ics.uci.edu/ml/machine-learning-databases/adult/adult.test 测试
	 * 
	 */
	public ArrayList<String> readTestData() throws IOException {
		ArrayList<String> candAttr = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String str = "";
		while (!(str = reader.readLine()).equals("")) {
			StringTokenizer tokenizer = new StringTokenizer(str);
			while (tokenizer.hasMoreTokens()) {
				candAttr.add(tokenizer.nextToken());
			}
		}
		return candAttr;
	}

	/**
	 * 读取训练元组
	 * 
	 * @return 训练元组集合
	 * @throws IOException
	 */
	public ArrayList<ArrayList<String>> readTrainData() throws IOException {
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String str = "";
		while (!(str = reader.readLine()).equals("")) {
			StringTokenizer tokenizer = new StringTokenizer(str);
			ArrayList<String> s = new ArrayList<String>();
			while (tokenizer.hasMoreTokens()) {
				s.add(tokenizer.nextToken());
			}
			datas.add(s);
		}
		return datas;
	}

	public static void main(String[] args) {
		TestBayes tb = new TestBayes();
		ArrayList<ArrayList<String>> datas = null;
		ArrayList<String> testT = null;
		//Bayes bayes = new Bayes();
		try {
			System.out.println("请输入训练数据");
			datas = tb.readTrainData();
			Bayes bayes = new Bayes(datas);
			bayes.setIgnoredIndexes(new int[]{2});
			while (true) {
				System.out.println("请输入测试元组");
				testT = tb.readTestData();
				//String c = bayes.predictCatagory(datas, testT);
				String c = bayes.predictCatagory(testT);
				System.out.println("------归属分类: " + c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
