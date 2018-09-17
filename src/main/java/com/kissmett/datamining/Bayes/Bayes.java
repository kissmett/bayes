package com.kissmett.datamining.Bayes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;  
import java.util.Map;  
  
  
  
/** 
 * 贝叶斯主体类 
 * ref: https://www.cnblogs.com/leoo2sk/archive/2010/09/17/naive-bayesian-classifier.html
 */  
public class Bayes {  
	
	
	private ArrayList<ArrayList<String>> _trainDatas = null;
	private Map<String, ArrayList<ArrayList<String>>> _allCatagoryTrainDict = null ;//= this.getCatagoryTrainDict(datas);
	private int[] _selectedIndexes = null;
	private int[] _ignoredIndexes = null;
	
	//所选index存在,则比较,否则默认全选;
	//默认忽略index为空,有则比较,而且优先于所选index
	public boolean isIndexPicked(int index){
		boolean res = true;
		if (null != this._selectedIndexes ){
			//if(!Arrays.asList(this._selectedIndexes).contains(index)){ //index是int,不好使?	
			if( Arrays.binarySearch(this._ignoredIndexes, index)<0 ){
				res = false;
			}
		}
		if (null != this._ignoredIndexes ){			
			if( Arrays.binarySearch(this._ignoredIndexes, index)>=0 ){
				res = false;
			}
		}
		return res;
		
	}

	public void setSelectedIndexes(int[] selectedIndexes) {
		this._selectedIndexes = selectedIndexes;
	}

	public void setIgnoredIndexes(int[] ignoredIndexes) {
		this._ignoredIndexes = ignoredIndexes;
	}

	public Bayes(){		 
	}
	
	//以训练数据而构造
	public Bayes(ArrayList<ArrayList<String>> trainDatas){
		this.readTrainData(trainDatas);  
	}
	
	//读取训练数据,可以构造之后调用;但一定要先于识别前;
	public void readTrainData(ArrayList<ArrayList<String>> trainDatas){
		this._trainDatas = trainDatas;
		this._allCatagoryTrainDict = this.getCatagoryTrainDict(_trainDatas);  
	}
	
	public 
	
    /** 
     * 将原训练元组按类别划分 
     * @param trainDatas 训练元组 
     * @return Map<类别，属于该类别的训练元组> 
     */  
    Map<String, ArrayList<ArrayList<String>>> getCatagoryTrainDict(ArrayList<ArrayList<String>> trainDatas){            
        //用于存放 某类别catagory 与   该类别对应的训练数据  
        Map<String, ArrayList<ArrayList<String>>> map = new HashMap<String, ArrayList<ArrayList<String>>>();  
  
          
        for(int i = 0 ; i < trainDatas.size() ; i++){  
            //用于表示第i条训练数据  
            ArrayList<String> trainData = trainDatas.get(i);  
            //类别  
            String catagory = null;  
            //第i条数据的类别  
            catagory = trainDatas.get(i).get(trainDatas.get(i).size() - 1);  
            //如果不是第一次遇到catagory类型  
            if(map.containsKey(catagory)){  
                //将整个第i条训练数据放入到catagory类型{Key}对应的{value}中  
                map.get(catagory).add(trainData);  
            }else{//如果是第一次遇到catagory类型  
                ArrayList<ArrayList<String>> catagoryTrainData = new ArrayList<ArrayList<String>>();  
                //将第i条数据放入到type类型对应的训练集typeTrainDatas中  
                catagoryTrainData.add(trainData);  
                //将  类型  与   相应训练集加入到map中  
                map.put(catagory,catagoryTrainData);  
            }  
        }  
          
        return map;  
    }  
    
    
    /** 
     * 在训练数据的基础上预测测试元组的类别 
     * @param datas 训练元组 
     * @param testData 测试元组 
     * @return 测试元组的类别 
     */   
    public String predictCatagory( ArrayList<String> testData) {  
          
    	ArrayList<ArrayList<String>> datas = this._trainDatas; //训练数据;
        //分类训练集  
        //Map<String, ArrayList<ArrayList<String>>> allCatagoryTrainDict = this.getCatagoryTrainDict(datas);  
        Map<String, ArrayList<ArrayList<String>>> allCatagoryTrainDict = this._allCatagoryTrainDict;  
        //分类集  
        Object[] catagories = allCatagoryTrainDict.keySet().toArray();  
        double maxP = 0.00;  //所属分类概率中的最大值;
        int maxPIndex = -1;  //所属分类概率最大的分类索引;
        //Vnb =arg max P( Ci ) Π j P ( aj | Ci )     Class.j 代表第j种分类  
        for(int i = 0 ; i < allCatagoryTrainDict.size() ; i ++ ){//allCatagoryTrainDict.size() 表示类别数  
            //得到第i种分类  
            String catagory = catagories[i].toString();  
            System.out.print("分类["+catagory+"]:");
            //求P( Ci ) --- 该分类出现的概率 --- 该分类 
            //1、求训练集中分类catagory的条数catagoryTrainDict.size()  
            ArrayList<ArrayList<String>> catagoryTrainDict = allCatagoryTrainDict.get(catagory);  
            //2、求训练数据的总条数 //datas.size();  
              
            //3、求出P( Ci )  //pOfC表示P( Ci )  ： 种类为catagory的训练元组的组数/训练元组的组数   Ci表示第i种分类  
            double pOfC = DecimalCalculate.div(catagoryTrainDict.size(), datas.size(), 3);  
            System.out.println("该分类出现的概率:"+pOfC+";");
            //一条测试数据   testT  
            //Π j P ( Ij=aj | C=Ci )  = p(I0=a0|C=Ci)*p(I1=a1|Ci)*p(I2=a2|Ci)*...*p()  
            // P( C=Ci |{I0,I1,I2,...}={a0,a1,a2,...}) 
            // = P( {I0,I1,I2,...}={a0,a1,a2,...} | C=Ci) * P(C=Ci) / P({I0,I1,I2,...}= {a0,a1,a2,...} ), --{I0,I1,I2,...}={a0,a1,a2,...} 为各指标分别取值时的一个联合事件
            //其中,分子上的 P({I0,I1,I2,...}={a0,a1,a2,...}|C=Ci) = Π j  P ( Ij=aj | C=Ci )
            //另,分母上的 P({I0,I1,I2,...}={a0,a1,a2,...}) = Π j P ( Ij=aj ), 这个对于分类C=Ci是一个常数;在计算时可以不计算
            for(int j = 0 ; j < testData.size() ; j++){  
            	if(isIndexPicked(j)){
            		//pOfI_under_C = p(aj|Ci)  //具体分类下,某指标值在该指标内出现的概率;
            		double pOfI_under_C = calcPofIndexUnderCatagory(catagoryTrainDict,testData.get(j),j);  
            		System.out.println("    在此分类下,指标["+j+"]出现概率:"+pOfI_under_C+";");
            		pOfC = DecimalCalculate.mul(pOfC, pOfI_under_C);  
            	}else{
            		System.out.println("    在此分类下,指标["+j+"]被忽略;");
            	}
            }  
            
            System.out.println("在此分类下,综合概率为:"+pOfC);
            //求各Ci中pOfC最大者,确定其i,即,为第i个Catagory
            if(pOfC > maxP){  
                maxP = pOfC;  
                maxPIndex = i;  
            }  
        }  
        System.out.println("分类选择为["+maxPIndex+"]:"+catagories[maxPIndex].toString());
        return catagories[maxPIndex].toString();  
          
    }  
      
    /** 
     * 计算指定属性列上指定值出现的概率 
     * 计算在某一分类下的数据中,某值在某指标(Index)出现的频率,做为概率
     * @param catagoryTrainDict 属于某一类的训练元组 ,包含各种指标在该分类下的样本值集合;
     * @param value 列值 
     * @param index 属性列索引 
     * @return 概率 
     */  
    private double calcPofIndexUnderCatagory(ArrayList<ArrayList<String>> catagoryTrainDict, String value, int index) {  
        double p = 0.0;  
        int count = 0;  
        int total = catagoryTrainDict.size();            
        
        for(int i = 0 ; i < catagoryTrainDict.size(); i++){  
            if(catagoryTrainDict.get(i).get(index).equals(value)){  
                count++;  
            }  
        }  
        //加入Laplace调整:若没与该value对应的记录,补计数1,防止0概率出现;
        if(0==count){count=1;total++;}
        p = DecimalCalculate.div(count, total, 9); //大样本下需要精读足够高,否则,在离散化程度高的情况下,count会在低精度下为0; 
        return p;  
          
    }  
    
    public static void main(String[] 啊如果是){
    	Bayes b = new Bayes();
    	b.setIgnoredIndexes(new int[]{1,2});
    	//System.out.println(b.isIndexPicked(0));
    	//System.out.println(b.isIndexPicked(1));
    	System.out.println(b.isIndexPicked(2));
       	System.out.println(b.isIndexPicked(1));
       	System.out.println(b.isIndexPicked(0));
    	//System.out.println(b.isIndexPicked(3));
  	
    }
}  